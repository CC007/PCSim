package com.github.cc007.pcsim

/**
  * Hello world!
  *
  */

import com.github.cc007.pcsim.components.compound.adders.FullAdder
import com.github.cc007.pcsim.components.compound.adders.FullAdder
import com.github.cc007.pcsim.components.compound.adders.NBitAdder
import com.github.cc007.pcsim.components.compound.decoders.NBitDecoder
import com.github.cc007.pcsim.components.compound.logicgates.ANDGate
import com.github.cc007.pcsim.components.compound.logicgates.ANOTBGate
import com.github.cc007.pcsim.components.compound.logicgates.EQGate
import com.github.cc007.pcsim.components.compound.logicgates.NANDGate
import com.github.cc007.pcsim.components.compound.logicgates.NEQGate
import com.github.cc007.pcsim.components.compound.logicgates.NORGate
import com.github.cc007.pcsim.components.compound.logicgates.NOTGate
import com.github.cc007.pcsim.components.compound.logicgates.ORGate
import com.github.cc007.pcsim.components.compound.logicgates.XORGate
import com.github.cc007.pcsim.components.primitive.active.{Diode, Repeater, Switch, VoltageChangeMeter}
import com.github.cc007.pcsim.components.primitive.passive.VoltageMeter
import com.github.cc007.pcsim.components.primitive.passive.VoltageSource
import com.github.cc007.pcsim.io.wires.Wire
import com.github.cc007.pcsim.viewer.CircuitViewer
import java.util.Calendar

import com.github.cc007.pcsim.viewer.drawables.Drawable

import scala.collection.mutable.ListBuffer

object Main extends App {
  val wPower = new Wire
  val source = new VoltageSource(wPower)
  source.voltage = 5.0
  val wA = new Wire
  val sA = new Switch(wPower, wA)
  val wB = new Wire
  val sB = new Switch(wPower, wB)
  val wCarryIn = new Wire
  val sCarryIn = new Switch(wPower, wCarryIn)
  val wDiodeIn = new Wire
  val repeater = new Repeater(wPower, wDiodeIn)
  val wDiodeOut = new Wire
  val diode = new Diode(wDiodeIn, wDiodeOut, 2.0)
  val wOut = new Wire
  val wCarryOut = new Wire
  val adder = new FullAdder(wPower, wA, wB, wCarryIn, wOut, wCarryOut, 3.3)
  val notGate = new NOTGate(wPower, wA, wOut, 3.3)
  val andGate = new ANDGate(wPower, wA, wB, wOut, 3.3)
  val orGate = new ORGate(wPower, wA, wB, wOut, 3.3)
  val nandGate = new NANDGate(wPower, wA, wB, wOut, 3.3)
  val norGate = new NORGate(wPower, wA, wB, wOut, 3.3)
  val eqGate = new EQGate(wPower, wA, wB, wOut, 3.3)
  val neqGate = new NEQGate(wPower, wA, wB, wOut, 3.3)
  val xorGate = new XORGate(wPower, wA, wB, wOut, 3.3)
  val aNotbGate = new ANOTBGate(wPower, wA, wB, wOut, 3.3)
  val vMeterOut = new VoltageMeter(wOut)
  val vMeterCarry = new VoltageMeter(wCarryOut)
  val vMeterDiode = new VoltageMeter(wDiodeOut)
  diode.start
  repeater.start

  val viewer = new CircuitViewer(CircuitViewer.POWER_DRAWING_FADED)
  viewer.addWire(wPower, 150, 360)
  viewer.addWire(wA, 250, 240)
  viewer.addWire(wB, 250, 360)
  viewer.addWire(wCarryIn, 250, 550, false)
  viewer.addWire(wOut, 700, 270)
  viewer.addWire(wCarryOut, 700, 550, false)
  viewer.addWire(wDiodeIn, 325, 650, false)
  viewer.addWire(wDiodeOut, 700, 650, false)
  viewer += Drawable(source, 50, 360)
  viewer += Drawable(sA, 200, 240)
  viewer += Drawable(sB, 200, 360)
  viewer += Drawable(sCarryIn, 200, 550)
  viewer += Drawable(repeater, 150, 650)
  viewer += Drawable(diode, 500, 650)
  viewer += Drawable(notGate, 500, 50)
  viewer += Drawable(andGate, 500, 100)
  viewer += Drawable(orGate, 500, 150)
  viewer += Drawable(nandGate, 500, 200)
  viewer += Drawable(norGate, 500, 250)
  viewer += Drawable(eqGate, 500, 300)
  viewer += Drawable(neqGate, 500, 350)
  viewer += Drawable(xorGate, 500, 400)
  viewer += Drawable(aNotbGate, 500, 450)
  viewer += Drawable(adder, 500, 550)
  viewer += Drawable(vMeterOut, 800, 270)
  viewer += Drawable(vMeterCarry, 800, 550)
  viewer += Drawable(vMeterDiode, 800, 650)
  viewer.visible = true
  println(vMeterDiode.voltage)
}
