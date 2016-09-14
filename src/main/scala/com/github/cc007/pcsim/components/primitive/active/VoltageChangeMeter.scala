/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.primitive.active

import com.github.cc007.pcsim.components.Component
import com.github.cc007.pcsim.io.connection.InputConnection
import com.github.cc007.pcsim.io.container.input.SingleInput
import com.github.cc007.pcsim.utils.PrimitiveActive
import com.github.cc007.pcsim.io.wires.Input
import java.util.Calendar

class VoltageChangeMeter(val wIn: Input, protected val name: String) extends Component with PrimitiveActive with SingleInput {
  protected val in: InputConnection = new InputConnection(wIn)
  protected var prevVoltage = 0.0
  protected var newVoltage = in.voltage

  override def loop() {
    if (prevVoltage != newVoltage) {
      val date = Calendar.getInstance
      println(date.get(Calendar.SECOND) + "." + date.get(Calendar.MILLISECOND) + ": voltage of " + name + " changed from " + prevVoltage + " to " + newVoltage)
      prevVoltage = newVoltage
    }
    newVoltage = in.voltage
  }

  override def disconnect() {}
}
