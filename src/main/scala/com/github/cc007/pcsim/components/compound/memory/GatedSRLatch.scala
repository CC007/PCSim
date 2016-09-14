/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.memory

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

class GatedSRLatch(val wPower:Input, wS:Input,wR:Input, wEnable:Input, wQ:Output, wNQ:Output, threshold:Double) extends CompoundComponent with PowerInput with MultipleInput with MultipleOutput {
  val inputs: mutable.HashMap[String, Input] = mutable.HashMap()
  val outputs: mutable.HashMap[String, Output] = mutable.HashMap()
  inputs("wS") = wS
  inputs("wR") = wR
  inputs("wEnable") = wEnable
  outputs("wQ") = wQ
  outputs("wNQ") = wNQ
  protected val wES = new Wire
  protected val wER = new Wire
  this += new ANDGate(wPower, wS, wEnable, wES, threshold)
  this += new ANDGate(wPower, wR, wEnable, wER, threshold)
  this += new SRLatch(wPower, wES, wER, wQ, wNQ, threshold)
}
