/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.memory

import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.components.compound.logicgates.NORGate
import com.github.cc007.pcsim.components.primitive.active.Diode
import com.github.cc007.pcsim.io.container.input.MultipleInput
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.output.MultipleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output
import com.github.cc007.pcsim.io.wires.Wire

import scala.collection.mutable
import scala.collection.mutable.HashMap

class SRLatch(val wPower:Input, wS:Input, wR:Input, wQ:Output, wNQ:Output, threshold:Double) extends CompoundComponent with PowerInput with MultipleInput with MultipleOutput {
  val inputs: mutable.HashMap[String, Input] = mutable.HashMap()
  val outputs: mutable.HashMap[String, Output] = mutable.HashMap()
  inputs("wS") = wS
  inputs("wR") = wR
  outputs("wQ") = wQ
  outputs("wNQ") = wNQ
  protected val wQInner = new Wire
  protected val wNQInner = new Wire
  this += new NORGate(wPower, wS, wQInner, wNQInner, threshold)
  this += new NORGate(wPower, wR, wNQInner, wQInner, threshold)
  this += new Diode(wQInner, wQ)
  this += new Diode(wNQInner, wNQ)
}

