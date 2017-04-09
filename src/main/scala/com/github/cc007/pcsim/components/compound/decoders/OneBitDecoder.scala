/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.decoders

import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.components.compound.logicgates.NOTGate
import com.github.cc007.pcsim.components.primitive.active.Diode
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.input.SingleInput
import com.github.cc007.pcsim.io.container.output.MultipleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output
import com.github.cc007.pcsim.io.wires.Wire

import scala.collection.mutable
import scala.collection.mutable.HashMap

class OneBitDecoder(val wPower: Input, val wIn: Input, wZero: Output, wOne: Output, threshold: Double) extends CompoundComponent with PowerInput with SingleInput with MultipleOutput {
  val outputs: mutable.HashMap[String, Output] = mutable.HashMap()
  outputs("wZero") = wZero
  outputs("wOne") = wOne
  protected val wInnerZero = new Wire
  this += new NOTGate(wPower, wIn, wZero, threshold)
  this += new NOTGate(wPower, wInnerZero, wOne, threshold)
  this += new Diode(wInnerZero, wZero, 0.0)
}
