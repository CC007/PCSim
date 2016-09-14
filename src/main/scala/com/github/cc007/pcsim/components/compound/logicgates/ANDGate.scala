/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.logicgates

import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.components.primitive.active.transistors.nMOS
import com.github.cc007.pcsim.components.primitive.passive.VoltageMeter
import com.github.cc007.pcsim.io.container.input.DoubleInput
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.output.SingleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output
import com.github.cc007.pcsim.io.wires.Wire

class ANDGate(wPower: Input, wA: Input, wB: Input, wOut: Output, threshold: Double) extends DoubleInputGate(wPower, wA, wB, wOut) {
  protected val w = new Wire
  this += new nMOS(wPower, wA, w, threshold)
  this += new nMOS(w, wB, wOut, threshold)

  val meterW = new VoltageMeter(w)
}
