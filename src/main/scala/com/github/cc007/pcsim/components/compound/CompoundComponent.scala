/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound

import com.github.cc007.pcsim.components.Component
import com.github.cc007.pcsim.utils.Active
import com.github.cc007.pcsim.utils.CompoundActive
import scala.collection.mutable.ListBuffer

abstract class CompoundComponent extends Component with CompoundActive {
  val components:ListBuffer[Component] = ListBuffer()
  
  def +=(component: Component) {
    components += component
  }
  
  override protected def actives = {
    val actives:ListBuffer[Active] = ListBuffer()
    for(component <- components){
      component match{
        case active:Active => actives += active
      }
    }
    actives
  }
  
  def disconnect(){
    for(component <- components){
      component.disconnect()
    }
  }
}
