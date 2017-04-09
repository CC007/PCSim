/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.logicgates

import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.components.primitive.active.transistors.pMOS
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.input.SingleInput
import com.github.cc007.pcsim.io.container.output.SingleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output

class NOTGate(val wPower: Input, val wIn: Input, val wOut: Output, threshold: Double) extends LogicGate with PowerInput with SingleInput with SingleOutput {
  this += new pMOS(wPower, wIn, wOut, threshold)
}