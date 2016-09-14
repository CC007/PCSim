/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.memory

import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.components.compound.logicgates.NOTGate
import com.github.cc007.pcsim.components.compound.logicgates.ANDGate
import com.github.cc007.pcsim.io.container.input.MultipleInput
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.output.MultipleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output
import com.github.cc007.pcsim.io.wires.Wire

import scala.collection.mutable
import scala.collection.mutable.HashMap

class RAMCell(val wPower:Input, wD:Input, wEnable:Input, wRW: Input, wQ:Output, wNQ:Output, threshold:Double) extends CompoundComponent with PowerInput with MultipleInput with MultipleOutput {
  val inputs: mutable.HashMap[String, Input] = mutable.HashMap()
  val outputs: mutable.HashMap[String, Output] = mutable.HashMap()
  inputs("wD") = wD
  inputs("wEnable") = wEnable
  inputs("wRW") = wRW
  outputs("wQ") = wQ
  outputs("wNQ") = wNQ
  protected val wNotRW = new Wire
  protected val wEnableW = new Wire
  protected val wEnableR = new Wire
  protected val wInnerQ = new Wire
  protected val wInnerNQ = new Wire
  this += new NOTGate(wPower, wRW, wNotRW, threshold)
  this += new ANDGate(wPower, wNotRW, wEnable, wEnableR, threshold)
  this += new ANDGate(wPower, wRW, wEnable, wEnableW, threshold)
  this += new DLatch(wPower, wD, wEnableW, wInnerQ, wInnerNQ, threshold)
  this += new ANDGate(wPower, wInnerQ, wEnableR, wQ, threshold)
  this += new ANDGate(wPower, wInnerNQ, wEnableR, wNQ, threshold)
}
