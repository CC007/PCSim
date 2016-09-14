/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.io.wires

import scala.collection.mutable.ListBuffer

trait Connectible {
  protected val connections: ListBuffer[Double] = ListBuffer()

  def connect(): Int = connect(0.0)

  def connect(voltage: Double): Int = {
    connections += voltage
    connections.size - 1
  }
  
  def disconnect(connection: Int) {
    connections(connection) = 0.0
  }

}
