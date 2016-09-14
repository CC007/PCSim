/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.io.connection

import com.github.cc007.pcsim.io.wires.Input

class InputConnection(input: Input) extends Connection(input){

  def voltage = input.voltage
}
