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

class FourBitDecoder(val wPower: Input, wOnes: Input, wTwos: Input, wFours: Input, wEights: Input, wOut: List[Output], threshold: Double) extends CompoundComponent with PowerInput with MultipleInput with MultipleOutput {
  if (wOut.size != 16) throw new IllegalArgumentException("The output list requires 16 output wires")
  val inputs: mutable.HashMap[String, Input] = mutable.HashMap()
  val outputs: mutable.HashMap[String, Output] = mutable.HashMap()
  protected val wTwoBitDecoderLower: List[Wire] = List(new Wire, new Wire, new Wire, new Wire)
  protected val wTwoBitDecoderHigher: List[Wire] = List(new Wire, new Wire, new Wire, new Wire)
  inputs("wOnes") = wOnes
  inputs("wTwos") = wTwos
  inputs("wFours") = wFours
  inputs("wEights") = wEights
  for (i <- 0 until 16) {
    outputs(i.toHexString) = wOut(i)
    this += new ANDGate(wPower, wTwoBitDecoderHigher(i / 4), wTwoBitDecoderLower(i % 4), wOut(i), threshold)
  }
  this += new TwoBitDecoder(wPower, wOnes, wTwos, wTwoBitDecoderLower, threshold)
  this += new TwoBitDecoder(wPower, wFours, wEights, wTwoBitDecoderHigher, threshold)
}

class FourBitDecoderAlt(val wPower: Input, wOnes: Input, wTwos: Input, wFours: Input, wEights: Input, wOut: List[Output], threshold: Double) extends CompoundComponent with PowerInput with MultipleInput with MultipleOutput {
  if (wOut.size != 16) throw new IllegalArgumentException("The output list requires 16 output wires")
  val inputs: mutable.HashMap[String, Input] = mutable.HashMap()
  val outputs: mutable.HashMap[String, Output] = mutable.HashMap()
  protected val wNotEights = new Wire
  protected val wThreeBitDecoder: List[Wire] = List(new Wire, new Wire, new Wire, new Wire, new Wire, new Wire, new Wire, new Wire)
  inputs("wOnes") = wOnes
  inputs("wTwos") = wTwos
  inputs("wFours") = wFours
  inputs("wEights") = wEights
  for (i <- 0 until 16) {
    outputs(i.toHexString) = wOut(i)
    if (i / 8 == 0) {
      this += new ANDGate(wPower, wThreeBitDecoder(i), wNotEights, wOut(i), threshold)
    }else{
      this += new ANDGate(wPower, wThreeBitDecoder(i - 8), wEights, wOut(i), threshold)
    }
  }
  this += new ThreeBitDecoder(wPower, wOnes, wTwos, wFours, wThreeBitDecoder, threshold)
  this += new NOTGate(wPower, wEights, wNotEights, threshold)
}
