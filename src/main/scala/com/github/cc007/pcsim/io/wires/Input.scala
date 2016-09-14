/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.io.wires

trait Input extends Connectible{
  def voltage = if (connections.nonEmpty) {
    connections.max
  } else {
    0.0
  }
}
