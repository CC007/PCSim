/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.memory

import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.components.compound.decoders.GatedNBitDecoder
import com.github.cc007.pcsim.io.container.input.GroupedMultipleInput
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.output.GroupedMultipleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output
import com.github.cc007.pcsim.io.wires.Wire

import scala.collection.mutable
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

class NxNRAM(val wPower: Input, wAddress: List[Input], wD: List[Input], wEnable: Input, wRW: Input, wQ: List[Output], wNQ: List[Output], threshold: Double) extends CompoundComponent with PowerInput with GroupedMultipleInput with GroupedMultipleOutput {
  if (wD.size != wQ.size || wD.size != wNQ.size) throw new IllegalArgumentException("wD requires the same amount of input wires as the amount of wQ wires and as the amount of wNQ wires.")
  val inputGroups: mutable.HashMap[String, mutable.HashMap[String, Input]] = mutable.HashMap()
  val outputGroups: mutable.HashMap[String, mutable.HashMap[String, Output]] = mutable.HashMap()
  inputGroups("") = mutable.HashMap()
  inputGroups("")("wEnable") = wEnable
  inputGroups("")("wRW") = wRW
  inputGroups("wAddress") = mutable.HashMap()
  inputGroups("wD") = mutable.HashMap()
  outputGroups("wQ") = mutable.HashMap()
  outputGroups("wNQ") = mutable.HashMap()
  protected val wWord: List[Wire] = {
    val listBuffer: ListBuffer[Wire] = ListBuffer()
    for (i <- 0 until Math.pow(2, wAddress.size).toInt) {
      listBuffer += new Wire
    }
    listBuffer.toList
  }
  this += new GatedNBitDecoder(wPower, wEnable, wAddress, wWord, threshold)
  for (i <- wAddress.indices) {
    inputGroups("wAddress")(Math.pow(2, i).toInt + "s") = wAddress(i)
  }
  for (i <- wD.indices) {
    inputGroups("wD")(Math.pow(2, i).toInt + "s") = wD(i)
    outputGroups("wQ")(Math.pow(2, i).toInt + "s") = wQ(i)
    outputGroups("wNQ")(Math.pow(2, i).toInt + "s") = wNQ(i)
    for (j <- wWord.indices) {
      this += new RAMCell(wPower, wD(i), wWord(j), wRW, wQ(i), wNQ(i), threshold)
    }
  }
}
