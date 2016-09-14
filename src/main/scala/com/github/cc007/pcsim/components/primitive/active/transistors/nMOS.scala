/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.primitive.active.transistors

import com.github.cc007.pcsim.components.Component
import com.github.cc007.pcsim.io.connection.InputConnection
import com.github.cc007.pcsim.io.connection.OutputConnection
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.input.SingleInput
import com.github.cc007.pcsim.io.container.output.SingleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output
import com.github.cc007.pcsim.utils.PrimitiveActive

class nMOS(val wPower: Input, val wIn: Input, val wOut: Output, threshold: Double) extends Component with PrimitiveActive with PowerInput with SingleInput with SingleOutput {
  protected val powerIn = new InputConnection(wPower)
  protected val in = new InputConnection(wIn)
  protected val out = new OutputConnection(wOut)
  
  override def loop() {
    if (in.voltage > threshold) out.voltage = powerIn.voltage
    else out.voltage = 0.0
  }

  override def disconnect() {
    out.disconnect()
  }
}
