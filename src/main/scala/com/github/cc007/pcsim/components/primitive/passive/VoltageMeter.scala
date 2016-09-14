/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.primitive.passive

import com.github.cc007.pcsim.components.Component
import com.github.cc007.pcsim.io.connection.InputConnection
import com.github.cc007.pcsim.io.container.input.SingleInput
import com.github.cc007.pcsim.io.wires.Input

class VoltageMeter(val wIn:Input) extends Component with SingleInput {
  protected val in = new InputConnection(wIn)
  
  def voltage = in.voltage
  
  override def disconnect(){}
}
