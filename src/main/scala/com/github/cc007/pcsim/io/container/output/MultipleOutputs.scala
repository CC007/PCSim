/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.io.container.output

import com.github.cc007.pcsim.io.wires.Output
import scala.collection.mutable.{HashMap => MutableHashMap}

trait MultipleOutput {
  val outputs: MutableHashMap[String, Output]
}

trait GroupedMultipleOutput {
  val outputGroups: MutableHashMap[String, MutableHashMap[String, Output]]
}
