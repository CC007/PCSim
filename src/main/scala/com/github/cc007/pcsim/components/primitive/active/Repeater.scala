package com.github.cc007.pcsim.components.primitive.active

import com.github.cc007.pcsim.io.connection.{InputConnection, OutputConnection}
import com.github.cc007.pcsim.io.wires.{Input, Output}

/**
  * Created by Rik on 9-4-2017.
  */
class Repeater(wIn: Input with Output, wOut: Input with Output) extends Diode(wIn, wOut){
  protected val inReverse = new OutputConnection(wIn)
  protected val outReverse = new InputConnection(wOut)
  override def loop(): Unit = {
    super.loop()
    inReverse.voltage = outReverse.voltage
  }
}
