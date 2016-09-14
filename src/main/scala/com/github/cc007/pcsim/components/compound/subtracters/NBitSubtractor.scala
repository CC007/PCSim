/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.subtracters

import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.io.container.input.GroupedMultipleInput
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.output.GroupedMultipleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output
import com.github.cc007.pcsim.io.wires.Wire

import scala.collection.mutable
import scala.collection.mutable.HashMap

class NBitSubtractor(val wPower: Input, wA: List[Input], wB: List[Input], wBorrowIn: Input, wFullDifference: List[Output], wBorrowOut: Output, threshold: Double) extends CompoundComponent with PowerInput with GroupedMultipleInput with GroupedMultipleOutput {
  if (wA.size != wFullDifference.size || wB.size != wFullDifference.size) throw new IllegalArgumentException("The full sum wire list requires the same amount of output wires as the amount of wA wires and as the amount of wB wires.")
  val inputGroups: mutable.HashMap[String, mutable.HashMap[String, Input]] = mutable.HashMap()
  val outputGroups: mutable.HashMap[String, mutable.HashMap[String, Output]] = mutable.HashMap()
  inputGroups("") = mutable.HashMap()
  outputGroups("") = mutable.HashMap()
  inputGroups("")("wBorrowIn") = wBorrowIn
  outputGroups("")("wBorrowOut") = wBorrowOut
  
  {
    var wCurrentBorrowOut: Wire = null
    var wPreviousBorrowOut = wBorrowIn
    inputGroups("wA") = mutable.HashMap()
    inputGroups("wB") = mutable.HashMap()
    outputGroups("wFullSum") = mutable.HashMap()
    for (i <- wA.indices) {
      inputGroups("wA")(Math.pow(2, i).toInt + "s") = wA(i)
      inputGroups("wB")(Math.pow(2, i).toInt + "s") = wB(i)
      outputGroups("wFullDifference")(Math.pow(2, i).toInt + "s") = wFullDifference(i)
      if (i < wA.size - 1) {
        wCurrentBorrowOut = new Wire
        this += new FullSubtractor(wPower, wA(i), wB(i), wPreviousBorrowOut, wFullDifference(i), wCurrentBorrowOut, threshold)
        wPreviousBorrowOut = wCurrentBorrowOut
      } else {
        this += new FullSubtractor(wPower, wA(i), wB(i), wPreviousBorrowOut, wFullDifference(i), wBorrowOut, threshold)
      }
    }
  }
}