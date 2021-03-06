/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.adders

import com.github.cc007.pcsim.components.Named
import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.components.compound.logicgates.ANDGate
import com.github.cc007.pcsim.components.compound.logicgates.XORGate
import com.github.cc007.pcsim.io.container.input.DoubleInput
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.output.MultipleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output

import scala.collection.mutable

class HalfAdder(val wPower:Input, val wA:Input, val wB:Input, wSum:Output, wCarry:Output, threshold:Double) extends CompoundComponent with PowerInput with DoubleInput with MultipleOutput with Named {
  val inputs: mutable.HashMap[String, Input] = mutable.HashMap()
  val outputs:mutable.HashMap[String, Output] = mutable.HashMap()

  var name = "Half adder"
  var subName = ""

  inputs("wA") = wA
  inputs("wB") = wB
  outputs("wSum") = wSum
  outputs("sCarry") = wCarry
  this += new XORGate(wPower, wA, wB, wSum, threshold)
  this += new ANDGate(wPower, wA, wB, wCarry, threshold)
}
