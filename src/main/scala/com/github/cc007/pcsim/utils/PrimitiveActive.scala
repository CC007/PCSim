/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.utils

trait PrimitiveActive extends Active {
  private var terminated: Boolean = false

  val t = new Thread() {
    override def run() {
      while (!terminated) {
        loop()
        Thread.`yield`()
      }
    }
  }

  override def start() {
    t.start()
  }

  override def terminate() {
    terminated = true
  }

  override def isTerminated = terminated

  def loop()
}
