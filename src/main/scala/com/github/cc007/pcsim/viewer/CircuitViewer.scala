/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.viewer

import com.github.cc007.pcsim.components.Component
import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.components.compound.logicgates.ANDGate
import com.github.cc007.pcsim.components.compound.logicgates.ANOTBGate
import com.github.cc007.pcsim.components.compound.logicgates.DoubleInputGate
import com.github.cc007.pcsim.components.compound.logicgates.NANDGate
import com.github.cc007.pcsim.components.compound.logicgates.NORGate
import com.github.cc007.pcsim.components.compound.logicgates.NOTGate
import com.github.cc007.pcsim.components.compound.logicgates.ORGate
import com.github.cc007.pcsim.components.compound.logicgates.XNORGate
import com.github.cc007.pcsim.components.compound.logicgates.XORGate
import com.github.cc007.pcsim.components.primitive.active.Switch
import com.github.cc007.pcsim.io.container.input.DoubleInput
import com.github.cc007.pcsim.io.container.input.GroupedMultipleInput
import com.github.cc007.pcsim.io.container.input.MultipleInput
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.input.SingleInput
import com.github.cc007.pcsim.io.container.output.GroupedMultipleOutput
import com.github.cc007.pcsim.io.container.output.MultipleOutput
import com.github.cc007.pcsim.io.container.output.SingleOutput
import com.github.cc007.pcsim.io.wires.Connectible
import com.github.cc007.pcsim.io.wires.Wire
import java.awt.BasicStroke
import java.awt.Color
import java.awt.geom.Path2D
import java.awt.geom.QuadCurve2D
import scala.collection.mutable.{HashMap => MutableHashMap}
import scala.collection.mutable.ListBuffer
import scala.swing._
import scala.swing.event._
import scala.util.Random
import scala.util.control.Breaks._

object CircuitViewer {
  val INPUT = 0
  val OUTPUT = 1
  val POWER_DRAWING_ON = 0
  val POWER_DRAWING_FADED = 1
  val POWER_DRAWING_OFF = 2
}

class CircuitViewer(val powerDrawType: Int) extends MainFrame {
  class CircuitDiagram() extends scala.swing.Component with Publisher {
    var draggedComponent: Component = null
    var draggedWire: Wire = null
    preferredSize = new Dimension(1280, 720)
    listenTo(mouse.clicks)
    listenTo(mouse.moves)
    reactions += {
      case e: MouseClicked if e.clicks >= 2 =>
        for (component <- components.keySet) {
          val (x, y) = components(component)
          if (e.point.x > x - 40 && e.point.x < x + 40 && e.point.y > y - 40 && e.point.y < y + 40) {
            expand(component)
          }
        }
      case e: MousePressed => breakable {
        for (component <- components.keySet) {
          val (x, y) = components(component)
          if (e.point.x > x - 40 && e.point.x < x + 40 && e.point.y > y - 40 && e.point.y < y + 40) {
            draggedComponent = component
            println("Component pressed")
            break
          }
        }
        for (wire <- wires.keySet) {
          val (x, y) = wires(wire)
          if (e.point.x > x - 8 && e.point.x < x + 8 && e.point.y > y - 8 && e.point.y < y + 8) {
            draggedWire = wire
            println("Wire pressed")
            break
          }
        }
      }
      case e: MouseReleased if draggedComponent != null =>
        draggedComponent = null
        println("Component released")
      case e: MouseReleased if draggedWire != null =>
        draggedWire = null
        println("Wire released")
      case e: MouseDragged if draggedComponent != null =>
        println("Component dragged to (" + e.point.x + ", " + e.point.y + ")")
        components(draggedComponent) = (e.point.x, e.point.y)
        repaint
      case e: MouseDragged if draggedWire != null =>
        println("Wire dragged to (" + e.point.x + ", " + e.point.y + ")")
        wires(draggedWire) = (e.point.x, e.point.y)
        repaint
      case _ =>
    }

    override def paintComponent(g: Graphics2D) {
      g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
      g.setColor(Color.WHITE)
      g.fillRect(0, 0, size.width, size.height)
      g.setColor(Color.BLACK)
      for (component <- components.keySet) drawWires(g, component)
      for (component <- components.keySet) draw(g, component)
      for (wire <- wires.keySet) draw(g, wire)
    }

    def drawWires(g: Graphics2D, component: Component) {
      matchWire(component, (wire, groupName, name, connType) => wire match {
        case w: Wire => draw(g, component, w, groupName, name, connType)
      })
    }

    def draw(g: Graphics2D, component: Component) {
      val (x, y) = components(component)
      component match {
        case _: NOTGate =>
          val squareSize = 48
          g.setColor(Color.WHITE)
          val path = new Path2D.Double()
          path.moveTo(x + squareSize / 2, y)
          path.lineTo(x - squareSize / 2, y - squareSize / 2)
          path.lineTo(x - squareSize / 2, y + squareSize / 2)
          path.lineTo(x + squareSize / 2, y)
          g.fill(path)
          g.setColor(Color.BLACK)
          g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
          g.draw(path)
          g.setColor(Color.WHITE)
          g.fillOval(x + squareSize / 2 - 5, y - 5, 10, 10)
          g.setColor(Color.BLACK)
          g.drawOval(x + squareSize / 2 - 5, y - 5, 10, 10)
        case _: ANDGate =>
          g.setColor(Color.WHITE)
          g.fillRect(x - 24, y - 24, 24, 48)
          g.fillArc(x - 24, y - 24, 48, 48, -90, 180)
          g.setColor(Color.BLACK)
          g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
          g.drawLine(x - 24, y - 24, x - 24, y + 24)
          g.drawLine(x - 24, y - 24, x, y - 24)
          g.drawLine(x - 24, y + 24, x, y + 24)
          g.drawArc(x - 24, y - 24, 48, 48, -90, 180)
        case _: ORGate =>
          val squareSize = 48
          g.setColor(Color.WHITE)
          val path = new Path2D.Double()
          path.moveTo(x - squareSize / 2 + squareSize * 2 / 5, y - squareSize / 2)
          path.quadTo(x - squareSize / 2 + squareSize * 3.5 / 5, y - squareSize / 2, x + squareSize / 2, y)
          path.quadTo(x - squareSize / 2 + squareSize * 3.5 / 5, y + squareSize / 2, x - squareSize / 2 + squareSize * 2 / 5, y + squareSize / 2)
          path.lineTo(x - squareSize / 2, y + squareSize / 2)
          path.quadTo(x - squareSize / 4, y, x - squareSize / 2, y - squareSize / 2)
          path.lineTo(x - squareSize / 2 + squareSize * 2 / 5, y - squareSize / 2)
          g.fill(path)
          g.setColor(Color.BLACK)
          g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
          g.draw(path)
        case _: NANDGate =>
          g.setColor(Color.WHITE)
          g.fillRect(x - 24, y - 24, 24, 48)
          g.fillArc(x - 24, y - 24, 48, 48, -90, 180)
          g.setColor(Color.BLACK)
          g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
          g.drawLine(x - 24, y - 24, x - 24, y + 24)
          g.drawLine(x - 24, y - 24, x, y - 24)
          g.drawLine(x - 24, y + 24, x, y + 24)
          g.drawArc(x - 24, y - 24, 48, 48, -90, 180)
          g.setColor(Color.WHITE)
          g.fillOval(x + 24 - 5, y - 5, 10, 10)
          g.setColor(Color.BLACK)
          g.drawOval(x + 24 - 5, y - 5, 10, 10)
        case _: NORGate =>
          val squareSize = 48
          g.setColor(Color.WHITE)
          val path = new Path2D.Double()
          path.moveTo(x - squareSize / 2 + squareSize * 2 / 5, y - squareSize / 2)
          path.quadTo(x - squareSize / 2 + squareSize * 3.5 / 5, y - squareSize / 2, x + squareSize / 2, y)
          path.quadTo(x - squareSize / 2 + squareSize * 3.5 / 5, y + squareSize / 2, x - squareSize / 2 + squareSize * 2 / 5, y + squareSize / 2)
          path.lineTo(x - squareSize / 2, y + squareSize / 2)
          path.quadTo(x - squareSize / 4, y, x - squareSize / 2, y - squareSize / 2)
          path.lineTo(x - squareSize / 2 + squareSize * 2 / 5, y - squareSize / 2)
          g.fill(path)
          g.setColor(Color.BLACK)
          g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
          g.draw(path)
          g.setColor(Color.WHITE)
          g.fillOval(x + squareSize / 2 - 5, y - 5, 10, 10)
          g.setColor(Color.BLACK)
          g.drawOval(x + squareSize / 2 - 5, y - 5, 10, 10)
        case _: XORGate =>
          val squareSize = 48
          g.setColor(Color.WHITE)
          val path = new Path2D.Double()
          path.moveTo(x - squareSize / 2 + squareSize * 2 / 5, y - squareSize / 2)
          path.quadTo(x - squareSize / 2 + squareSize * 3.5 / 5, y - squareSize / 2, x + squareSize / 2, y)
          path.quadTo(x - squareSize / 2 + squareSize * 3.5 / 5, y + squareSize / 2, x - squareSize / 2 + squareSize * 2 / 5, y + squareSize / 2)
          path.lineTo(x - squareSize / 2, y + squareSize / 2)
          path.quadTo(x - squareSize / 4, y, x - squareSize / 2, y - squareSize / 2)
          path.lineTo(x - squareSize / 2 + squareSize * 2 / 5, y - squareSize / 2)
          g.fill(path)
          g.setColor(Color.BLACK)
          g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
          g.draw(path)
          g.draw(new QuadCurve2D.Double(x - squareSize / 2 - 5, y + squareSize / 2, x - squareSize / 4 - 5, y, x - squareSize / 2 - 5, y - squareSize / 2))
        case _: XNORGate =>
          val squareSize = 48
          g.setColor(Color.WHITE)
          val path = new Path2D.Double()
          path.moveTo(x - squareSize / 2 + squareSize * 2 / 5, y - squareSize / 2)
          path.quadTo(x - squareSize / 2 + squareSize * 3.5 / 5, y - squareSize / 2, x + squareSize / 2, y)
          path.quadTo(x - squareSize / 2 + squareSize * 3.5 / 5, y + squareSize / 2, x - squareSize / 2 + squareSize * 2 / 5, y + squareSize / 2)
          path.lineTo(x - squareSize / 2, y + squareSize / 2)
          path.quadTo(x - squareSize / 4, y, x - squareSize / 2, y - squareSize / 2)
          path.lineTo(x - squareSize / 2 + squareSize * 2 / 5, y - squareSize / 2)
          g.fill(path)
          g.setColor(Color.BLACK)
          g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
          g.draw(path)
          g.draw(new QuadCurve2D.Double(x - squareSize / 2 - 5, y + squareSize / 2, x - squareSize / 4 - 5, y, x - squareSize / 2 - 5, y - squareSize / 2))
          g.setColor(Color.WHITE)
          g.fillOval(x + squareSize / 2 - 5, y - 5, 10, 10)
          g.setColor(Color.BLACK)
          g.drawOval(x + squareSize / 2 - 5, y - 5, 10, 10)
        case _: ANOTBGate =>
          g.setColor(Color.WHITE)
          g.fillRect(x - 24, y - 24, 24, 48)
          g.fillArc(x - 24, y - 24, 48, 48, -90, 180)
          g.setColor(Color.BLACK)
          g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
          g.drawLine(x - 24, y - 24, x - 24, y + 24)
          g.drawLine(x - 24, y - 24, x, y - 24)
          g.drawLine(x - 24, y + 24, x, y + 24)
          g.drawArc(x - 24, y - 24, 48, 48, -90, 180)
          g.setColor(Color.WHITE)
          g.fillOval(x - 24 - 5, y + 12 - 5, 10, 10)
          g.setColor(Color.BLACK)
          g.drawOval(x - 24 - 5, y + 12 - 5, 10, 10)
        case s: Switch =>
          g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
          g.setColor(Color.BLACK)
          g.drawLine(x - 16, y, x + 16, y - 10)
          g.setColor(Color.WHITE)
          g.fillOval(x - 16 - 5, y - 5, 10, 10)
          g.fillOval(x + 16 - 5, y - 5, 10, 10)
          g.setColor(Color.BLACK)
          g.drawOval(x - 16 - 5, y - 5, 10, 10)
          g.drawOval(x + 16 - 5, y - 5, 10, 10)
        case _ =>
          g.setColor(Color.WHITE)
          g.fillRect(x - 40, y - 40, 80, 80)
          g.setColor(Color.BLACK)
          g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
          g.drawRect(x - 40, y - 40, 80, 80)
      }
    }

    def draw(g: Graphics2D, wire: Wire) {
      g.setColor(Color.BLACK)
      g.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
      val (x, y) = wires(wire)
      g.drawLine(x, y, x, y)
    }

    def draw(g: Graphics2D, component: Component, wire: Wire, groupName: String, name: String, connectionType: Int) {
      val (xW, yW) = wires(wire)
      val (xC, yC) = components(component)
      g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
      g.setColor(Color.BLACK)
      if (name.equals("wPower")) {
        if (powerDrawType == CircuitViewer.POWER_DRAWING_OFF) return
        if (powerDrawType == CircuitViewer.POWER_DRAWING_FADED) g.setColor(Color.LIGHT_GRAY)
        component match {
          case _: NOTGate =>
            if (name.equals("wIn")) {
              g.drawLine(xW, yW, xC - 24, yC)
            } else {
              g.drawLine(xW, yW, xC + 24, yC)
            }
          case _: DoubleInputGate =>
            g.drawLine(xW, yW, xC - 24, yC - 24)
          case _ =>
            g.drawLine(xW, yW, xC, yC)
        }
        return
      }

      component match {
        case _: NOTGate =>
          if (name.equals("wIn")) {
            g.drawLine(xW, yW, xC - 24, yC)
          } else {
            g.drawLine(xW, yW, xC + 24, yC)
          }
        case _: ORGate | _: NORGate =>
          if (name.equals("wA")) {
            g.drawLine(xW, yW, xC - 18, yC - 12)
          } else if (name.equals("wB")) {
            g.drawLine(xW, yW, xC - 18, yC + 12)
          } else {
            g.drawLine(xW, yW, xC + 24, yC)
          }
        case _: XORGate | _: XNORGate =>
          if (name.equals("wA")) {
            g.drawLine(xW, yW, xC - 25, yC - 12)
          } else if (name.equals("wB")) {
            g.drawLine(xW, yW, xC - 25, yC + 12)
          } else {
            g.drawLine(xW, yW, xC + 24, yC)
          }
        case _: DoubleInputGate =>
          if (name.equals("wA")) {
            g.drawLine(xW, yW, xC - 24, yC - 12)
          } else if (name.equals("wB")) {
            g.drawLine(xW, yW, xC - 24, yC + 12)
          } else {
            g.drawLine(xW, yW, xC + 24, yC)
          }
        case s: Switch =>
          if (name.equals("wIn")) {
            g.drawLine(xW, yW, xC - 16, yC)
          } else {
            g.drawLine(xW, yW, xC + 16, yC)
          }
        case _ =>
          g.drawLine(xW, yW, xC, yC)
      }
    }
  }

  val wires: MutableHashMap[Wire, (Int, Int)] = MutableHashMap()
  val components: MutableHashMap[Component, (Int, Int)] = MutableHashMap()

  title = "Circuit"
  val diagram = new CircuitDiagram()
  contents = diagram

  def this() {
    this(CircuitViewer.POWER_DRAWING_OFF)
  }

  def this(componentList: ListBuffer[Component]) {
    this()
    for (component <- componentList) {
      this += component
    }
  }

  def +=(component: Component) {
    val r = new Random(System.nanoTime())
    components(component) = (r.nextInt(1180) + 10, r.nextInt(620) + 10)
    matchWire(component, (w, _, _, _) => this += w)
  }

  def expand(component: Component) {
    if (components.contains(component)) {
      component match {
        case cc: CompoundComponent =>
          components.remove(component)
          for (c <- cc.components) this += c
        case _ =>
      }
    }
    repaint
  }

  def +=(connectable: Connectible) {
    val r = new Random(System.nanoTime())
    connectable match {
      case w: Wire if !wires.contains(w) => wires(w) = (r.nextInt(1260) + 10, r.nextInt(700) + 10)
      case _ =>
    }
  }

  def matchWire(component: Component, callback: (Connectible, String, String, Int) => Unit) {
    component match {
      case pi: PowerInput => callback(pi.wPower, "", "wPower", CircuitViewer.INPUT)
      case _ =>
    }
    component match {
      case si: SingleInput => callback(si.wIn, "", "wIn", CircuitViewer.INPUT)
      case _ =>
    }
    component match {
      case di: DoubleInput =>
        callback(di.wA, "", "wA", CircuitViewer.INPUT)
        callback(di.wB, "", "wB", CircuitViewer.INPUT)
      case _ =>
    }
    component match {
      case mi: MultipleInput => for ((name, i) <- mi.inputs) {
        callback(i, "", name, CircuitViewer.INPUT)
      }
      case _ =>
    }
    component match {
      case gmi: GroupedMultipleInput => for ((groupName, iGroup) <- gmi.inputGroups; (name, i) <- iGroup) {
        callback(i, groupName, name, CircuitViewer.INPUT)
      }
      case _ =>
    }
    component match {
      case so: SingleOutput => callback(so.wOut, "", "wOut", CircuitViewer.OUTPUT)
      case _ =>
    }
    component match {
      case mo: MultipleOutput => for ((name, o) <- mo.outputs) {
        callback(o, "", name, CircuitViewer.OUTPUT)
      }
      case _ =>
    }
    component match {
      case gmo: GroupedMultipleOutput => for ((groupName, oGroup) <- gmo.outputGroups; (name, o) <- oGroup) {
        callback(o, groupName, name, CircuitViewer.OUTPUT)
      }
      case _ =>
    }
  }
}