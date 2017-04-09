/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.viewer

import com.github.cc007.pcsim.components.Component
import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.components.compound.logicgates._
import com.github.cc007.pcsim.components.primitive.active.{Diode, Repeater, Switch}
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

import com.github.cc007.pcsim.viewer.drawables.Drawable

import scala.collection.mutable.{HashMap => MutableHashMap}
import scala.collection.mutable.{HashSet => MutableHashSet}
import scala.collection.mutable.ListBuffer
import scala.swing._
import scala.swing.{Component => SwingComponent}
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
  val wires: MutableHashMap[Wire, (Int, Int, Boolean)] = MutableHashMap()
  val drawables: MutableHashSet[Drawable] = MutableHashSet()

  title = "Circuit"
  val diagram = new CircuitDiagram()
  contents = diagram

  def this() {
    this(CircuitViewer.POWER_DRAWING_OFF)
  }

  def this(drawableList: ListBuffer[Drawable]) {
    this()
    for (drawable <- drawableList) {
      this += drawable
    }
  }

  def +=(drawable: Drawable) {
    drawables += drawable
    matchWire(drawable, (w, _, _, _) => this += w)
  }

  def expand(drawable: Drawable):MutableHashSet[Drawable] = {
    val newDrawables: MutableHashSet[Drawable] = MutableHashSet()
    if (drawables.contains(drawable)) {
      drawable.component match {
        case cc: CompoundComponent =>
          drawables.remove(drawable)
          var i = 0
          for (c <- cc.components) {
            newDrawables += Drawable(c, drawable.x, drawable.y + i * 10)
            i += 1
          }
        case _ =>
      }
    }
    repaint
    newDrawables
  }

  def +=(connectible: Connectible) {
    val r = new Random(System.nanoTime)
    addConnectible(connectible, r.nextInt(1260) + 10, r.nextInt(700) + 10)
  }

  def addWire(w: Wire, x: Int, y: Int) = if (!wires.contains(w)) {
    wires(w) = (x, y, true)
  }

  def addWire(w: Wire, x: Int, y: Int, visible: Boolean) = if (!wires.contains(w)) {
    wires(w) = (x, y, visible)
  }

  private def addConnectible(connectible: Connectible, x: Int, y: Int) {
    connectible match {
      case w: Wire => addWire(w, x, y)
      case _ =>
    }
  }

  def matchWire(drawable: Drawable, callback: (Connectible, String, String, Int) => Unit) {
    drawable.component match {
      case pi: PowerInput => callback(pi.wPower, "", "wPower", CircuitViewer.INPUT)
      case _ =>
    }
    drawable.component match {
      case si: SingleInput => callback(si.wIn, "", "wIn", CircuitViewer.INPUT)
      case _ =>
    }
    drawable.component match {
      case di: DoubleInput =>
        callback(di.wA, "", "wA", CircuitViewer.INPUT)
        callback(di.wB, "", "wB", CircuitViewer.INPUT)
      case _ =>
    }
    drawable.component match {
      case mi: MultipleInput => for ((name, i) <- mi.inputs) {
        callback(i, "", name, CircuitViewer.INPUT)
      }
      case _ =>
    }
    drawable.component match {
      case gmi: GroupedMultipleInput => for ((groupName, iGroup) <- gmi.inputGroups; (name, i) <- iGroup) {
        callback(i, groupName, name, CircuitViewer.INPUT)
      }
      case _ =>
    }
    drawable.component match {
      case so: SingleOutput => callback(so.wOut, "", "wOut", CircuitViewer.OUTPUT)
      case _ =>
    }
    drawable.component match {
      case mo: MultipleOutput => for ((name, o) <- mo.outputs) {
        callback(o, "", name, CircuitViewer.OUTPUT)
      }
      case _ =>
    }
    drawable.component match {
      case gmo: GroupedMultipleOutput => for ((groupName, oGroup) <- gmo.outputGroups; (name, o) <- oGroup) {
        callback(o, groupName, name, CircuitViewer.OUTPUT)
      }
      case _ =>
    }
  }

  class CircuitDiagram() extends SwingComponent with Publisher {
    var draggedDrawable: Drawable = null
    var draggedWire: Wire = null
    preferredSize = new Dimension(1280, 720)
    listenTo(mouse.clicks)
    listenTo(mouse.moves)
    reactions += {
      case e: MouseClicked if e.clicks >= 2 =>
        val newDrawables: MutableHashSet[Drawable] = MutableHashSet()
        for (drawable <- drawables) {
          val x = drawable.x
          val y = drawable.y
          val w = drawable.width
          val h = drawable.height
          if (e.point.x > x - w / 2 && e.point.x < x + w / 2 && e.point.y > y - h / 2 && e.point.y < y + h / 2) {
            newDrawables ++= expand(drawable)
          }
        }
        for(newDrawable <- newDrawables) {
          CircuitViewer.this += newDrawable
        }

      case e: MousePressed => breakable {
        for (drawable <- drawables) {
          val x = drawable.x
          val y = drawable.y
          val w = drawable.width
          val h = drawable.height
          if (e.point.x > x - w / 2 && e.point.x < x + w / 2 && e.point.y > y - h / 2 && e.point.y < y + h / 2) {
            draggedDrawable = drawable
            println("Component pressed")
            break
          }
        }
        for (wire <- wires.keySet) {
          val (x, y, _) = wires(wire)
          if (e.point.x > x - 8 && e.point.x < x + 8 && e.point.y > y - 8 && e.point.y < y + 8) {
            draggedWire = wire
            println("Wire pressed")
            break
          }
        }
      }
      case e: MouseReleased if draggedDrawable != null =>
        draggedDrawable = null
        println("Component released")
      case e: MouseReleased if draggedWire != null =>
        draggedWire = null
        println("Wire released")
      case e: MouseDragged if draggedDrawable != null =>
        println("Component dragged to (" + e.point.x + ", " + e.point.y + ")")
        draggedDrawable.x = e.point.x
        draggedDrawable.y = e.point.y
        repaint
      case e: MouseDragged if draggedWire != null =>
        println("Wire dragged to (" + e.point.x + ", " + e.point.y + ")")
        val (_, _, visible) = wires(draggedWire)
        wires(draggedWire) = (e.point.x, e.point.y, visible)
        repaint
      case _ =>
    }

    override def paintComponent(g: Graphics2D) {
      g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON)
      g.setColor(Color.WHITE)
      g.fillRect(0, 0, size.width, size.height)
      g.setColor(Color.BLACK)
      for (drawable <- drawables) drawWires(g, drawable)
      for (drawable <- drawables) draw(g, drawable)
      for (wire <- wires.keySet) draw(g, wire)
    }

    def drawWires(g: Graphics2D, drawable: Drawable) {
      matchWire(drawable, (wire, groupName, name, connType) => wire match {
        case w: Wire => draw(g, drawable, w, groupName, name, connType)
      })
    }

    def draw(g: Graphics2D, drawable: Drawable) {
      val x = drawable.x
      val y = drawable.y
      drawable.draw(g, x, y)
    }

    def draw(g: Graphics2D, wire: Wire) {
      g.setColor(Color.BLACK)
      g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
      val (x, y, visible) = wires(wire)
      if (visible) {
        g.drawLine(x, y, x, y)
      }
    }

    def draw(g: Graphics2D, drawable: Drawable, wire: Wire, groupName: String, name: String, connectionType: Int) {
      val (xW, yW, _) = wires(wire)
      val xC = drawable.x
      val yC = drawable.y
      g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
      g.setColor(Color.BLACK)
      if (name.equals("wPower")) {
        if (powerDrawType == CircuitViewer.POWER_DRAWING_OFF) return
        if (powerDrawType == CircuitViewer.POWER_DRAWING_FADED) g.setColor(Color.LIGHT_GRAY)
        drawable.component match {
          case _: LogicGate =>
            g.drawLine(xW, yW, xC - 24, yC - 24)
          case _ =>
            g.drawLine(xW, yW, xC, yC)
        }
        return
      }

      drawable.component match {
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
        case r: Repeater =>
          g.drawLine(xW, yW, xC, yC)
        case s: Diode =>
          if (name.equals("wIn")) {
            g.drawLine(xW, yW, xC - 8, yC)
          } else {
            g.drawLine(xW, yW, xC + 8, yC)
          }
        case _ =>
          g.drawLine(xW, yW, xC, yC)
      }
    }
  }

}