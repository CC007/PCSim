/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.primitive.passive

import com.github.cc007.pcsim.components.Component
import com.github.cc007.pcsim.io.connection.OutputConnection
import com.github.cc007.pcsim.io.container.output.SingleOutput
import com.github.cc007.pcsim.io.wires.Output

class VoltageSource(val wOut:Output) extends Component with SingleOutput {
  protected val out = new OutputConnection(wOut)
  
  @deprecated("Only using the setter is supported", "2000-01-01")
  def voltage:Double = 0.0
  
  def voltage_=(voltage: Double) = out.voltage_=(voltage)
  
  override def disconnect(){
    out.disconnect()
  }
}
