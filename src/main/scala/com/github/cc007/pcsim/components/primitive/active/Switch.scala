/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.primitive.active

import com.github.cc007.pcsim.components.Component
import com.github.cc007.pcsim.io.connection.InputConnection
import com.github.cc007.pcsim.io.connection.OutputConnection
import com.github.cc007.pcsim.io.container.input.SingleInput
import com.github.cc007.pcsim.io.container.output.SingleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output
import com.github.cc007.pcsim.utils.PrimitiveActive
  
class Switch(val wIn: Input, val wOut: Output) extends Component with PrimitiveActive with SingleInput with SingleOutput {
  protected val in = new InputConnection(wIn)
  protected val out = new OutputConnection(wOut)
  protected var on = false
  
  override def loop() {
    if (on) out.voltage = in.voltage
    else out.voltage = 0.0
  }

  override def disconnect() {
    out.disconnect()
  }

  def flipSwitch() {
    if (on) on = false
    else on = true
  }

}
