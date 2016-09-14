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

class TwoBitDecoder(val wPower: Input, wOnes: Input, wTwos: Input, wOut:List[Output], threshold: Double) extends CompoundComponent with PowerInput with MultipleInput with MultipleOutput {
  if (wOut.size != 4) throw new IllegalArgumentException("The output list requires 4 output wires")
  val inputs: mutable.HashMap[String, Input] = mutable.HashMap()
  val outputs: mutable.HashMap[String, Output] = mutable.HashMap()
  inputs("wOnes") = wOnes
  inputs("wTwos") = wTwos
  outputs("0") = wOut(0)
  outputs("1") = wOut(1)
  outputs("2") = wOut(2)
  outputs("3") = wOut(3)
  protected val wNotOnes = new Wire
  protected val wNotTwos = new Wire
  this += new NOTGate(wPower, wOnes, wNotOnes, threshold)
  this += new NOTGate(wPower, wTwos, wNotTwos, threshold)
  this += new ANDGate(wPower, wNotOnes, wNotTwos, wOut(0), threshold)
  this += new ANDGate(wPower, wOnes, wNotTwos, wOut(1), threshold)
  this += new ANDGate(wPower, wNotOnes, wTwos, wOut(2), threshold)
  this += new ANDGate(wPower, wOnes, wTwos, wOut(3), threshold)
}