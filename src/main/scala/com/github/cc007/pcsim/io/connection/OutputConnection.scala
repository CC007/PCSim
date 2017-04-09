/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.io.connection

import com.github.cc007.pcsim.io.wires.Output

class OutputConnection(output: Output) extends Connection(output) {

  @throws(classOf[UnsupportedOperationException])
  def voltage:Nothing  = throw new UnsupportedOperationException("Only using the setter is supported");
  
  def voltage_=(voltage: Double) {
    output.voltage = (connection, voltage)
  }
}
