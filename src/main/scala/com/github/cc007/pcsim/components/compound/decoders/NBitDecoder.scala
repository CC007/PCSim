/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.cc007.pcsim.components.compound.decoders

import com.github.cc007.pcsim.components.compound.CompoundComponent
import com.github.cc007.pcsim.components.compound.logicgates.ANDGate
import com.github.cc007.pcsim.components.compound.logicgates.NOTGate
import com.github.cc007.pcsim.io.container.input.MultipleInput
import com.github.cc007.pcsim.io.container.input.PowerInput
import com.github.cc007.pcsim.io.container.output.MultipleOutput
import com.github.cc007.pcsim.io.wires.Input
import com.github.cc007.pcsim.io.wires.Output
import com.github.cc007.pcsim.io.wires.Wire

import scala.collection.mutable
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

class NBitDecoder(val wPower: Input, wIn: List[Input], wOut: List[Output], threshold: Double) extends CompoundComponent with PowerInput with MultipleInput with MultipleOutput {
  val inputs: mutable.HashMap[String, Input] = mutable.HashMap()
  val outputs: mutable.HashMap[String, Output] = mutable.HashMap()
  protected val wHalfNBitDecoderLower: List[Wire] = {
    val listBuffer: ListBuffer[Wire] = ListBuffer()
    for (i <- 0 until Math.pow(2, wIn.size / 2).toInt) {
      listBuffer += new Wire
    }
    listBuffer.toList
  }
  protected val wHalfNBitDecoderHigher: List[Wire] = {
    val listBuffer: ListBuffer[Wire] = ListBuffer()
    for (i <- 0 until Math.pow(2, wIn.size / 2).toInt) {
      listBuffer += new Wire
    }
    listBuffer.toList
  }
  protected val wNMinusOneBitDecoder: List[Wire] = {
    val listBuffer: ListBuffer[Wire] = ListBuffer()
    for (i <- 0 until Math.pow(2, wIn.size - 1).toInt) {
      listBuffer += new Wire
    }
    listBuffer.toList
  }
  protected val wNotLastIn = new Wire
  if (wIn.size == 3) {
    this += new ThreeBitDecoder(wPower, wIn(0), wIn(1), wIn(2), wOut, threshold)
  } else if (wIn.size == 4) {
    this += new FourBitDecoder(wPower, wIn(0), wIn(1), wIn(2), wIn(3), wOut, threshold)
  } else {
    if (wOut.size != Math.pow(2, wIn.size).toInt) throw new IllegalArgumentException("The output list requires " + Math.pow(2, wIn.size).toInt + " output wires")
    for (i <- wIn.indices) {
      inputs(Math.pow(2, i).toInt + "s") = wIn(i)
    }
    if (wIn.size % 2 == 0) {
      for (i <- wOut.indices) {
        outputs(i.toHexString) = wOut(i)
        this += new ANDGate(wPower, wHalfNBitDecoderHigher(i / Math.sqrt(wOut.size).toInt), wHalfNBitDecoderLower(i % Math.sqrt(wOut.size).toInt), wOut(i), threshold)
      }
      this += new NBitDecoder(wPower, wIn.take(wIn.size / 2), wHalfNBitDecoderLower, threshold)
      this += new NBitDecoder(wPower, wIn.drop(wIn.size / 2), wHalfNBitDecoderHigher, threshold)
    } else {
      for (i <- wOut.indices) {
        outputs(i.toHexString) = wOut(i)
        if (2 * i / wOut.size == 0) {
          this += new ANDGate(wPower, wNMinusOneBitDecoder(i), wNotLastIn, wOut(i), threshold)
        } else {
          this += new ANDGate(wPower, wNMinusOneBitDecoder(i - wOut.size / 2), wIn.last, wOut(i), threshold)
        }
      }
      this += new NBitDecoder(wPower, wIn.take(wIn.size - 1), wNMinusOneBitDecoder, threshold)
      this += new NOTGate(wPower, wIn.last, wNotLastIn, threshold)
    }
  }
}