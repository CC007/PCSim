/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.utils

import scala.collection.mutable.ListBuffer

trait CompoundActive extends Active {
  protected def actives: ListBuffer[Active]

  override def start() {
    for (active <- actives) {
      active.start()
    }
  }

  override def terminate() {
    for (active <- actives) {
      active.terminate()
    }
  }

  override def isTerminated = {
    for (active <- actives) {
      if(!active.isTerminated) false
    }
    true
  }
}
