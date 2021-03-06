/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.subtracters

import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.components.compound.logicgates.ORGate
import com.github.cc007.pcsim.io.container.input.MultipleInput
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.output.MultipleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output
import com.github.cc007.pcsim.io.wires.Wire

import scala.collection.mutable
import scala.collection.mutable.HashMap

class FullSubtractor(val wPower: Input, wA: Input, wB: Input, wBorrowIn: Input, wFullDifference: Output, wBorrowOut: Output, threshold: Double) extends CompoundComponent with PowerInput with MultipleInput with MultipleOutput {
  val inputs: mutable.HashMap[String, Input] = mutable.HashMap()
  val outputs: mutable.HashMap[String, Output] = mutable.HashMap()
  inputs("wA") = wA
  inputs("wB") = wB
  inputs("wBorrowIn") = wBorrowIn
  outputs("wFullDifference") = wFullDifference
  outputs("wBorrowOut") = wBorrowOut
  protected val wPartialDifference = new Wire
  
  this += new HalfSubtractor(wPower, wA, wB, wPartialDifference, wBorrowOut, threshold)
  this += new HalfSubtractor(wPower, wPartialDifference, wBorrowIn, wFullDifference, wBorrowOut, threshold)
}