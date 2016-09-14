/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.logicgates

import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.io.container.input.DoubleInput
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.output.SingleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output

abstract class DoubleInputGate(val wPower: Input, val wA: Input, val wB: Input, val wOut: Output) extends CompoundComponent with PowerInput with DoubleInput with SingleOutput  {

}
