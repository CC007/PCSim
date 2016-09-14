/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.memory

import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.components.compound.logicgates.NOTGate
import com.github.cc007.pcsim.io.container.input.MultipleInput
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.output.MultipleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output
import com.github.cc007.pcsim.io.wires.Wire

import scala.collection.mutable
import scala.collection.mutable.HashMap

class DLatch(val wPower:Input, wD:Input, wEnable:Input, wQ:Output, wNQ:Output, threshold:Double) extends CompoundComponent with PowerInput with MultipleInput with MultipleOutput {
  val inputs: mutable.HashMap[String, Input] = mutable.HashMap()
  val outputs: mutable.HashMap[String, Output] = mutable.HashMap()
  inputs("wD") = wD
  inputs("wEnable") = wEnable
  outputs("wQ") = wQ
  outputs("wNQ") = wNQ
  protected val wNotD = new Wire
  this += new NOTGate(wPower, wD, wNotD, threshold)
  this += new GatedSRLatch(wPower, wD, wNotD, wEnable, wQ, wNQ, threshold)
}

