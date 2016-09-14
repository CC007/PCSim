/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.io.connection

import com.github.cc007.pcsim.io.wires.Connectible

abstract class Connection(protected val connectable: Connectible) {
  protected val connection = connectable.connect()
  
  def disconnect() {
    connectable.disconnect(connection)
  }
}
