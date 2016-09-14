/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.io.wires

trait Output extends Connectible {
  
  def voltage: Double
  
  def voltage_=(tup: (Int, Double)) {
    tup match {
      case (connection, voltage) => this.synchronized {
        connections(connection) = voltage
      }
    }
  }
}
