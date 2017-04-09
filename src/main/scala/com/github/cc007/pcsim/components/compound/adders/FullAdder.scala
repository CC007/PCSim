/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.adders

import com.github.cc007.pcsim.components.Named
import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.io.container.input.MultipleInput
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.output.MultipleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output
import com.github.cc007.pcsim.io.wires.Wire

import scala.collection.mutable

class FullAdder(val wPower: Input, wA: Input, wB: Input, wCarryIn: Input, wFullSum: Output, wCarryOut: Output, threshold: Double) extends CompoundComponent with PowerInput with MultipleInput with MultipleOutput with Named{
  val inputs: mutable.HashMap[String, Input] = mutable.HashMap()
  val outputs: mutable.HashMap[String, Output] = mutable.HashMap()

  var name = "Full adder"
  var subName = ""

  inputs("wA") = wA
  inputs("wB") = wB
  inputs("wCarryIn") = wCarryIn
  outputs("wFullSum") = wFullSum
  outputs("sCarryOut") = wCarryOut
  protected val wPartialSum = new Wire
  
  this += new HalfAdder(wPower, wA, wB, wPartialSum, wCarryOut, threshold)
  this += new HalfAdder(wPower, wPartialSum, wCarryIn, wFullSum, wCarryOut, threshold)
}