package com.github.cc007.pcsim.viewer.drawables

import java.awt.geom.{Path2D, QuadCurve2D}
import java.awt._

import com.github.cc007.pcsim.components.{Component, Named}
import com.github.cc007.pcsim.components.compound.logicgates._
import com.github.cc007.pcsim.components.primitive.active.{Diode, Repeater, Switch}

import scala.util.Random

/**
  * Created by Rik on 8-2-2017.
  */
object Drawable {
  val font = new Font("Helvetica", Font.PLAIN, 10)
  val subFont = new Font("Helvetica", Font.PLAIN, 25)

  def apply(component: Component, x: Int, y: Int): Drawable = component match {
    case ng: NOTGate => new NOTGateDrawable(ng, x, y)
    case ag: ANDGate => new ANDGateDrawable(ag, x, y)
    case og: ORGate => new ORGateDrawable(og, x, y)
    case nag: NANDGate => new NANDGateDrawable(nag, x, y)
    case nog: NORGate => new NORGateDrawable(nog, x, y)
    case xog: XORGate => new XORGateDrawable(xog, x, y)
    case xnog: XNORGate => new XNORGateDrawable(xnog, x, y)
    case anbg: ANOTBGate => new ANOTBGateDrawable(anbg, x, y)
    case s: Switch => new SwitchDrawable(s, x, y)
    case r: Repeater => new RepeaterDrawable(r, x, y)
    case d: Diode => new DiodeDrawable(d, x, y)
    case n: Named => new DefaultDrawable(component, x, y)
    case _ => new DefaultDrawable(component, x, y)
  }

  def apply(component: Component): Drawable = {
    val r = new Random(System.nanoTime)
    Drawable(component, r.nextInt(1260) + 10, r.nextInt(700) + 10)
  }
}

abstract class Drawable(val component: Component, var x: Int, var y: Int, val width: Int, val height: Int) {

  def this(component: Component, x: Int, y: Int, radius: Int) {
    this(component, x, y, radius * 2, radius * 2)
  }

  def draw(g: Graphics2D, x: Int, y: Int)
}

class DefaultDrawable(component: Component, x: Int, y: Int) extends Drawable(component, x, y, 40) {
  override def draw(g: Graphics2D, x: Int, y: Int) {
    g.setColor(Color.WHITE)
    g.fillRect(x - width / 2, y - height / 2, width, height)
    g.setColor(Color.BLACK)
    g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
    g.drawRect(x - width / 2, y - height / 2, width, height)

    component match {
      case n: Named => {
        val nameMetrics = g.getFontMetrics(Drawable.font);
        val nameWidth = nameMetrics.stringWidth(n.name)
        val subNameMetrics = g.getFontMetrics(Drawable.subFont);
        val subNameWidth = subNameMetrics.stringWidth(n.subName)
        val subNameHight = subNameMetrics.getHeight
        g.setFont(Drawable.font)
        g.drawString(n.name, x - nameWidth / 2, y - 5)
        g.setFont(Drawable.subFont)
        g.drawString(n.subName, x - subNameWidth / 2, y + subNameHight/2 + 5)
      }
      case _ =>
    }
  }
}

abstract class GateDrawable(component: Component, x: Int, y: Int) extends Drawable(component, x, y, 24)

class NOTGateDrawable(component: NOTGate, x: Int, y: Int) extends GateDrawable(component, x, y) {
  override def draw(g: Graphics2D, x: Int, y: Int) {
    g.setColor(Color.WHITE)
    val path = new Path2D.Double()
    path.moveTo(x + width / 2, y)
    path.lineTo(x - width / 2, y - height / 2)
    path.lineTo(x - width / 2, y + height / 2)
    path.lineTo(x + width / 2, y)
    g.fill(path)
    g.setColor(Color.BLACK)
    g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
    g.draw(path)
    g.setColor(Color.WHITE)
    g.fillOval(x + width / 2 - 5, y - 5, 10, 10)
    g.setColor(Color.BLACK)
    g.drawOval(x + width / 2 - 5, y - 5, 10, 10)
  }
}

class ANDGateDrawable(component: ANDGate, x: Int, y: Int) extends GateDrawable(component, x, y) {
  override def draw(g: Graphics2D, x: Int, y: Int) {
    g.setColor(Color.WHITE)
    g.fillRect(x - 24, y - 24, 24, 48)
    g.fillArc(x - 24, y - 24, 48, 48, -90, 180)
    g.setColor(Color.BLACK)
    g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
    g.drawLine(x - 24, y - 24, x - 24, y + 24)
    g.drawLine(x - 24, y - 24, x, y - 24)
    g.drawLine(x - 24, y + 24, x, y + 24)
    g.drawArc(x - 24, y - 24, 48, 48, -90, 180)
  }
}

class ORGateDrawable(component: ORGate, x: Int, y: Int) extends GateDrawable(component, x, y) {
  override def draw(g: Graphics2D, x: Int, y: Int) {
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
    g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
    g.draw(path)
  }
}

class NANDGateDrawable(component: NANDGate, x: Int, y: Int) extends GateDrawable(component, x, y) {
  override def draw(g: Graphics2D, x: Int, y: Int) {
    g.setColor(Color.WHITE)
    g.fillRect(x - 24, y - 24, 24, 48)
    g.fillArc(x - 24, y - 24, 48, 48, -90, 180)
    g.setColor(Color.BLACK)
    g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
    g.drawLine(x - 24, y - 24, x - 24, y + 24)
    g.drawLine(x - 24, y - 24, x, y - 24)
    g.drawLine(x - 24, y + 24, x, y + 24)
    g.drawArc(x - 24, y - 24, 48, 48, -90, 180)
    g.setColor(Color.WHITE)
    g.fillOval(x + 24 - 5, y - 5, 10, 10)
    g.setColor(Color.BLACK)
    g.drawOval(x + 24 - 5, y - 5, 10, 10)
  }
}

class NORGateDrawable(component: NORGate, x: Int, y: Int) extends GateDrawable(component, x, y) {
  override def draw(g: Graphics2D, x: Int, y: Int) {
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
    g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
    g.draw(path)
    g.setColor(Color.WHITE)
    g.fillOval(x + squareSize / 2 - 5, y - 5, 10, 10)
    g.setColor(Color.BLACK)
    g.drawOval(x + squareSize / 2 - 5, y - 5, 10, 10)
  }
}

class XORGateDrawable(component: XORGate, x: Int, y: Int) extends GateDrawable(component, x, y) {
  override def draw(g: Graphics2D, x: Int, y: Int) {
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
    g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
    g.draw(path)
    g.draw(new QuadCurve2D.Double(x - squareSize / 2 - 5, y + squareSize / 2, x - squareSize / 4 - 5, y, x - squareSize / 2 - 5, y - squareSize / 2))
  }
}

class XNORGateDrawable(component: XNORGate, x: Int, y: Int) extends GateDrawable(component, x, y) {
  override def draw(g: Graphics2D, x: Int, y: Int) {
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
    g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
    g.draw(path)
    g.draw(new QuadCurve2D.Double(x - squareSize / 2 - 5, y + squareSize / 2, x - squareSize / 4 - 5, y, x - squareSize / 2 - 5, y - squareSize / 2))
    g.setColor(Color.WHITE)
    g.fillOval(x + squareSize / 2 - 5, y - 5, 10, 10)
    g.setColor(Color.BLACK)
    g.drawOval(x + squareSize / 2 - 5, y - 5, 10, 10)
  }
}


class ANOTBGateDrawable(component: ANOTBGate, x: Int, y: Int) extends GateDrawable(component, x, y) {
  override def draw(g: Graphics2D, x: Int, y: Int) {
    g.setColor(Color.WHITE)
    g.fillRect(x - 24, y - 24, 24, 48)
    g.fillArc(x - 24, y - 24, 48, 48, -90, 180)
    g.setColor(Color.BLACK)
    g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
    g.drawLine(x - 24, y - 24, x - 24, y + 24)
    g.drawLine(x - 24, y - 24, x, y - 24)
    g.drawLine(x - 24, y + 24, x, y + 24)
    g.drawArc(x - 24, y - 24, 48, 48, -90, 180)
    g.setColor(Color.WHITE)
    g.fillOval(x - 24 - 5, y + 12 - 5, 10, 10)
    g.setColor(Color.BLACK)
    g.drawOval(x - 24 - 5, y + 12 - 5, 10, 10)
  }
}

class SwitchDrawable(component: Switch, x: Int, y: Int) extends Drawable(component, x, y, 32, 20) {
  override def draw(g: Graphics2D, x: Int, y: Int) {
    g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
    g.setColor(Color.BLACK)
    g.drawLine(x - 16, y, x + 16, y - 10)
    g.setColor(Color.WHITE)
    g.fillOval(x - 16 - 5, y - 5, 10, 10)
    g.fillOval(x + 16 - 5, y - 5, 10, 10)
    g.setColor(Color.BLACK)
    g.drawOval(x - 16 - 5, y - 5, 10, 10)
    g.drawOval(x + 16 - 5, y - 5, 10, 10)
  }
}

class DiodeDrawable(component: Diode, x: Int, y: Int) extends Drawable(component, x, y, 8) {
  override def draw(g: Graphics2D, x: Int, y: Int) {
    val squareSize = 16
    g.setColor(Color.WHITE)
    val path = new Path2D.Double()
    path.moveTo(x + squareSize / 2, y)
    path.lineTo(x - squareSize / 2, y - squareSize / 2)
    path.lineTo(x - squareSize / 2, y + squareSize / 2)
    path.lineTo(x + squareSize / 2, y)
    g.fill(path)
    g.setColor(Color.BLACK)
    g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER))
    g.draw(path)
    g.drawLine(x + squareSize / 2, y + squareSize / 2, x + squareSize / 2, y - squareSize / 2)
  }
}
class RepeaterDrawable(component: Diode, x: Int, y: Int) extends Drawable(component, x, y, 5) {
  override def draw(g: Graphics2D, x: Int, y: Int) {}
}