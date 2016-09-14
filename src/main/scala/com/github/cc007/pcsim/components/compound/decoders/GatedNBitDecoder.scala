/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.decoders

import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.components.compound.logicgates.ANDGate
import com.github.cc007.pcsim.components.compound.logicgates.NOTGate
import com.github.cc007.pcsim.io.container.input.GroupedMultipleInput
import com.github.cc007.pcsim.io.container.input.MultipleInput
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.output.MultipleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output
import com.github.cc007.pcsim.io.wires.Wire

import scala.collection.mutable
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

class GatedNBitDecoder(val wPower: Input, wEnable: Input, wIn: List[Input], wOut: List[Output], threshold: Double) extends CompoundComponent with PowerInput with GroupedMultipleInput with MultipleOutput {
  val inputGroups: mutable.HashMap[String, mutable.HashMap[String, Input]] = mutable.HashMap()
  val outputs: mutable.HashMap[String, Output] = mutable.HashMap()
  inputGroups("") = mutable.HashMap()
  inputGroups("")("wEnable") = wEnable
  inputGroups("wIn") = mutable.HashMap()
  if (wOut.size != Math.pow(2, wIn.size).toInt) throw new IllegalArgumentException("The output list requires " + Math.pow(2, wIn.size).toInt + " output wires")
  for (i <- wIn.indices) {
    inputGroups("wIn")(Math.pow(2, i).toInt + "s") = wIn(i)
  }
  protected val wDecoderOut: List[Wire] = {
    val listBuffer: ListBuffer[Wire] = ListBuffer()
    for (i <- 0 until Math.pow(2, wIn.size).toInt) {
      listBuffer += new Wire
    }
    listBuffer.toList
  }
  this += new NBitDecoder(wPower, wIn, wDecoderOut, threshold)
  for (i <- wDecoderOut.indices) {
    outputs(i.toHexString) = wOut(i)
    this += new ANDGate(wPower, wEnable, wDecoderOut(i), wOut(i), threshold)
  }
}
