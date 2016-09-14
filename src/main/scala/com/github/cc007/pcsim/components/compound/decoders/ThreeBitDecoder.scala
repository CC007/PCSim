/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.decoders

import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.components.compound.logicgates.ANDGate
import com.github.cc007.pcsim.components.compound.logicgates.NOTGate
import com.github.cc007.pcsim.io.container.input.MultipleInput
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.output.MultipleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output
import com.github.cc007.pcsim.io.wires.Wire

import scala.collection.mutable
import scala.collection.mutable.HashMap

class ThreeBitDecoder(val wPower: Input, wOnes: Input, wTwos: Input, wFours: Input, wOut: List[Output], threshold: Double) extends CompoundComponent with PowerInput with MultipleInput with MultipleOutput {
  if (wOut.size != 8) throw new IllegalArgumentException("The output list requires 8 output wires")
  val inputs: mutable.HashMap[String, Input] = mutable.HashMap()
  val outputs: mutable.HashMap[String, Output] = mutable.HashMap()
  inputs("wOnes") = wOnes
  inputs("wTwos") = wTwos
  inputs("wFours") = wFours
  outputs("0") = wOut(0)
  outputs("1") = wOut(1)
  outputs("2") = wOut(2)
  outputs("3") = wOut(3)
  outputs("4") = wOut(4)
  outputs("5") = wOut(5)
  outputs("6") = wOut(6)
  outputs("7") = wOut(7)
  protected val wNotFours = new Wire
  protected val wTwoBitDecoder: List[Wire] = List(new Wire, new Wire, new Wire, new Wire)
  this += new TwoBitDecoder(wPower, wOnes, wTwos, wTwoBitDecoder, threshold)
  this += new NOTGate(wPower, wFours, wNotFours, threshold)
  this += new ANDGate(wPower, wTwoBitDecoder(0), wNotFours, wOut(0), threshold)
  this += new ANDGate(wPower, wTwoBitDecoder(1), wNotFours, wOut(1), threshold)
  this += new ANDGate(wPower, wTwoBitDecoder(2), wNotFours, wOut(2), threshold)
  this += new ANDGate(wPower, wTwoBitDecoder(3), wNotFours, wOut(3), threshold)
  this += new ANDGate(wPower, wTwoBitDecoder(0), wFours, wOut(4), threshold)
  this += new ANDGate(wPower, wTwoBitDecoder(1), wFours, wOut(5), threshold)
  this += new ANDGate(wPower, wTwoBitDecoder(2), wFours, wOut(6), threshold)
  this += new ANDGate(wPower, wTwoBitDecoder(3), wFours, wOut(7), threshold)
}