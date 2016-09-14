/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.adders

import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.io.container.input.GroupedMultipleInput
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.output.GroupedMultipleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output
import com.github.cc007.pcsim.io.wires.Wire

import scala.collection.mutable
import scala.collection.mutable.HashMap

class NBitAdder(val wPower: Input, wA: List[Input], wB: List[Input], wCarryIn: Input, wFullSum: List[Output], wCarryOut: Output, threshold: Double) extends CompoundComponent with PowerInput with GroupedMultipleInput with GroupedMultipleOutput {
  if (wA.size != wFullSum.size || wB.size != wFullSum.size) throw new IllegalArgumentException("The full sum wire list requires the same amount of output wires as the amount of wA wires and as the amount of wB wires.")
  val inputGroups: mutable.HashMap[String, mutable.HashMap[String, Input]] = mutable.HashMap()
  val outputGroups: mutable.HashMap[String, mutable.HashMap[String, Output]] = mutable.HashMap()
  inputGroups("") = mutable.HashMap()
  outputGroups("") = mutable.HashMap()
  inputGroups("")("wCarryIn") = wCarryIn
  outputGroups("")("sCarryOut") = wCarryOut
  
  {
    var wCurrentCarryOut: Wire = null
    var wPreviousCarryOut = wCarryIn
    inputGroups("wA") = mutable.HashMap()
    inputGroups("wB") = mutable.HashMap()
    outputGroups("wFullSum") = mutable.HashMap()
    for (i <- wA.indices) {
      inputGroups("wA")(Math.pow(2, i).toInt + "s") = wA(i)
      inputGroups("wB")(Math.pow(2, i).toInt + "s") = wB(i)
      outputGroups("wFullSum")(Math.pow(2, i).toInt + "s") = wFullSum(i)
      if (i < wA.size - 1) {
        wCurrentCarryOut = new Wire
        this += new FullAdder(wPower, wA(i), wB(i), wPreviousCarryOut, wFullSum(i), wCurrentCarryOut, threshold)
        wPreviousCarryOut = wCurrentCarryOut
      } else {
        this += new FullAdder(wPower, wA(i), wB(i), wPreviousCarryOut, wFullSum(i), wCarryOut, threshold)
      }
    }
  }
}