package com.github.cc007.pcsim

/**
 * Hello world!
 *
 */
import com.github.cc007.pcsim.components.compound.adders.FullAdder
import com.github.cc007.pcsim.components.compound.adders.FullAdder
import com.github.cc007.pcsim.components.compound.adders.NBitAdder
import com.github.cc007.pcsim.components.compound.decoders.NBitDecoder
import com.github.cc007.pcsim.components.compound.logicgates.ANDGate
import com.github.cc007.pcsim.components.compound.logicgates.ANOTBGate
import com.github.cc007.pcsim.components.compound.logicgates.EQGate
import com.github.cc007.pcsim.components.compound.logicgates.NANDGate
import com.github.cc007.pcsim.components.compound.logicgates.NEQGate
import com.github.cc007.pcsim.components.compound.logicgates.NORGate
import com.github.cc007.pcsim.components.compound.logicgates.NOTGate
import com.github.cc007.pcsim.components.compound.logicgates.ORGate
import com.github.cc007.pcsim.components.compound.logicgates.XORGate
import com.github.cc007.pcsim.components.primitive.active.Diode
import com.github.cc007.pcsim.components.primitive.active.Switch
import com.github.cc007.pcsim.components.primitive.active.VoltageChangeMeter
import com.github.cc007.pcsim.components.primitive.passive.VoltageMeter
import com.github.cc007.pcsim.components.primitive.passive.VoltageSource
import com.github.cc007.pcsim.io.wires.Wire
import com.github.cc007.pcsim.viewer.CircuitViewer
import java.util.Calendar
import scala.collection.mutable.ListBuffer

object App {
  def main(args: Array[String]): Unit = {
    /*
    val wPower = new Wire
    val powerSource = new VoltageSource(wPower)
    val meterPower = new VoltageMeter(wPower)

    val wOut = new Wire
    val meterOut = new VoltageMeter(wOut)

    println("*************")
    println(" Diode test *")
    println("*************")
    val diode = new Diode(wPower, wOut)
    println(meterOut.voltage)
    diode.start
    Thread.sleep(1)
    println(meterOut.voltage)
    powerSource.voltage = 4.4
    println(meterOut.voltage)
    Thread.sleep(1)
    println(meterOut.voltage)
    println(diode.isTerminated)
    diode.terminate
    println(diode.isTerminated)
    println(meterOut.voltage)
    diode.disconnect
    println(meterOut.voltage)
    println

    //- gate tests -//
    val wA = new Wire
    val meterA = new VoltageMeter(wA)
    val sA = new Switch(wPower, wA)
    sA.start

    val wB = new Wire
    val meterB = new VoltageMeter(wB)
    val sB = new Switch(wPower, wB)
    sB.start

    println("**************")
    println("AND gate test*")
    println("**************")
    val andGate = new ANDGate(wPower, wA, wB, wOut, 3.0)
    val meterAndGate = andGate.meterW
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("AND gate: " + meterAndGate.voltage)
    println("Out: " + meterOut.voltage)
    println
    andGate.start
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("AND gate: " + meterAndGate.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("AND gate: " + meterAndGate.voltage)
    println("Out: " + meterOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("AND gate: " + meterAndGate.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("AND gate: " + meterAndGate.voltage)
    println("Out: " + meterOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    andGate.terminate
    andGate.disconnect

    println("**************")
    println(" OR gate test*")
    println("**************")
    val orGate = new ORGate(wPower, wA, wB, wOut, 3.0)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    orGate.start
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    orGate.terminate
    orGate.disconnect

    println("**************")
    println("NOT gate test*")
    println("**************")
    val notGate = new NOTGate(wPower, wA, wOut, 3.0)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("Out: " + meterOut.voltage)
    println
    notGate.start
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("Out: " + meterOut.voltage)
    println
    notGate.terminate
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("Out: " + meterOut.voltage)
    println
    notGate.disconnect
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("Out: " + meterOut.voltage)
    println

    println("**************")
    println("NOR gate test*")
    println("**************")
    val norGate = new NORGate(wPower, wA, wB, wOut, 3.0)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    norGate.start
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    norGate.terminate
    norGate.disconnect

    println("***************")
    println("NAND gate test*")
    println("***************")
    // or gate test
    val nandGate = new NANDGate(wPower, wA, wB, wOut, 3.0)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    nandGate.start
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    nandGate.terminate
    nandGate.disconnect

    println("****************")
    println("ANOTB gate test*")
    println("****************")
    // or gate test
    val aNotbGate = new ANOTBGate(wPower, wA, wB, wOut, 3.0)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    aNotbGate.start
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    aNotbGate.terminate
    aNotbGate.disconnect

    println("**************")
    println(" EQ gate test*")
    println("**************")
    val eqGate = new EQGate(wPower, wA, wB, wOut, 3.0)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    eqGate.start
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    eqGate.terminate
    eqGate.disconnect

    println("**************")
    println("NEQ gate test*")
    println("**************")
    val neqGate = new NEQGate(wPower, wA, wB, wOut, 3.0)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    neqGate.start
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    neqGate.terminate
    neqGate.disconnect

    println("**************")
    println("XOR gate test*")
    println("**************")
    val xorGate = new XORGate(wPower, wA, wB, wOut, 3.0)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    xorGate.start
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Out: " + meterOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    xorGate.terminate
    xorGate.disconnect

    //- adder tests -//
    val wCarryIn = new Wire
    val meterCarryIn = new VoltageMeter(wCarryIn)
    val sCarryIn = new Switch(wPower, wCarryIn)
    sCarryIn.start

    val wCarryOut = new Wire
    val meterCarryOut = new VoltageMeter(wCarryOut)

    println("****************")
    println("Full adder test*")
    println("****************")
    val fullAdder = new FullAdder(wPower, wA, wB, wCarryIn, wOut, wCarryOut, 3.0)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Carry in: " + meterCarryIn.voltage)
    println("Sum: " + meterOut.voltage)
    println("Carry out: " + meterCarryOut.voltage)
    println
    fullAdder.start
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Carry in: " + meterCarryIn.voltage)
    println("Sum: " + meterOut.voltage)
    println("Carry out: " + meterCarryOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Carry in: " + meterCarryIn.voltage)
    println("Sum: " + meterOut.voltage)
    println("Carry out: " + meterCarryOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Carry in: " + meterCarryIn.voltage)
    println("Sum: " + meterOut.voltage)
    println("Carry out: " + meterCarryOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Carry in: " + meterCarryIn.voltage)
    println("Sum: " + meterOut.voltage)
    println("Carry out: " + meterCarryOut.voltage)
    println
    sB.flipSwitch
    sCarryIn.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Carry in: " + meterCarryIn.voltage)
    println("Sum: " + meterOut.voltage)
    println("Carry out: " + meterCarryOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Carry in: " + meterCarryIn.voltage)
    println("Sum: " + meterOut.voltage)
    println("Carry out: " + meterCarryOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Carry in: " + meterCarryIn.voltage)
    println("Sum: " + meterOut.voltage)
    println("Carry out: " + meterCarryOut.voltage)
    println
    sA.flipSwitch
    Thread.sleep(1)
    println("Power: " + meterPower.voltage)
    println("A: " + meterA.voltage)
    println("B: " + meterB.voltage)
    println("Carry in: " + meterCarryIn.voltage)
    println("Sum: " + meterOut.voltage)
    println("Carry out: " + meterCarryOut.voltage)
    println
    sB.flipSwitch
    Thread.sleep(1)
    fullAdder.terminate
    fullAdder.disconnect

    // clean up
    sA.terminate
    sB.terminate

    //- Decoder tests -//
    def printVoltage(in: List[VoltageMeter], out: List[VoltageMeter]) {
      for (i <- 0 until in.size) println("Input " + i + ": " + in(i).voltage)
      for (i <- 0 until out.size / 8) {
        print("Output " + 8 * i + " to " + (8 * i + 7) + ":  \t")
        for (j <- 0 until 8) print(out(i * 8 + j).voltage + " ")
        println
      }
    }

    val wInList: List[Wire] = {
      val listBuffer: ListBuffer[Wire] = ListBuffer()
      for (i <- 0 until 7) {
        listBuffer += new Wire
      }
      listBuffer.toList
    }
    val vcmInList: List[VoltageChangeMeter] = {
      val listBuffer: ListBuffer[VoltageChangeMeter] = ListBuffer()
      for (i <- 0 until 7) {
        val vcm = new VoltageChangeMeter(wInList(i), Math.pow(2, i).toInt + "s")
        vcm.start
        listBuffer += vcm
      }
      listBuffer.toList
    }
    val sList: List[Switch] = {
      val listBuffer: ListBuffer[Switch] = ListBuffer()
      for (i <- 0 until 7) {
        val switch = new Switch(wPower, wInList(i))
        switch.start
        listBuffer += switch
      }
      listBuffer.toList
    }

    val wOutList: List[Wire] = {
      val listBuffer: ListBuffer[Wire] = ListBuffer()
      for (i <- 0 until 128) {
        listBuffer += new Wire
      }
      listBuffer.toList
    }
    val meterOutList: List[VoltageMeter] = {
      val listBuffer: ListBuffer[VoltageMeter] = ListBuffer()
      for (i <- 0 until 128) {
        listBuffer += new VoltageMeter(wOutList(i))
      }
      listBuffer.toList
    }
    val vcmOutList: List[VoltageChangeMeter] = {
      val listBuffer: ListBuffer[VoltageChangeMeter] = ListBuffer()
      for (i <- 0 until 128) {
        val vcm = new VoltageChangeMeter(wOutList(i), i.toHexString)
        vcm.start
        listBuffer += vcm
      }
      listBuffer.toList
    }
    println("*************")
    println("Decoder test*")
    println("*************")
    val decoder: NBitDecoder = new NBitDecoder(wPower, wInList, wOutList, 3.3)
    val dateBefore = Calendar.getInstance
    decoder.start
    Thread.sleep(50)
    sList(2).flipSwitch
    Thread.sleep(50)
    sList(2).flipSwitch
    Thread.sleep(50)
    sList(3).flipSwitch
    Thread.sleep(50)
    sList(2).flipSwitch
    Thread.sleep(50)
    sList(2).flipSwitch
    Thread.sleep(50)
    sList(3).flipSwitch
    Thread.sleep(50)
    sList(4).flipSwitch
    Thread.sleep(50)
    sList(4).flipSwitch
    Thread.sleep(50)
    sList(5).flipSwitch
    Thread.sleep(50)
    sList(5).flipSwitch
    Thread.sleep(50)
    sList(6).flipSwitch
    Thread.sleep(50)
    sList(0).flipSwitch
    sList(1).flipSwitch
    sList(2).flipSwitch
    sList(3).flipSwitch
    sList(4).flipSwitch
    sList(5).flipSwitch
    Thread.sleep(50)
    sList(0).flipSwitch
    sList(1).flipSwitch
    sList(2).flipSwitch
    sList(3).flipSwitch
    sList(4).flipSwitch
    sList(5).flipSwitch
    sList(6).flipSwitch
    //printVoltage(meterInList, meterOutList)
    Thread.sleep(1000)
    decoder.terminate
    decoder.disconnect
    Thread.sleep(1000)
    println(dateBefore.get(Calendar.SECOND) + "." + dateBefore.get(Calendar.MILLISECOND) + ": before starting decoder")

    
    
    val nBitAdder = new NBitAdder(wPower, wInList.take(3), wInList.drop(3).take(3), wCarryIn, wOutList.take(3), wCarryOut, 3.0)
    
    
    //TODO add nbit adder tests
    
    
    Thread.sleep(1000)
    
    
    sCarryIn.terminate
    for (switch <- sList) switch.terminate
    for (vcm <- vcmOutList) vcm.terminate
    for (vcm <- vcmInList) vcm.terminate
    */
    val wPower = new Wire
    val source = new VoltageSource(wPower)
    source.voltage = 5.0
    val wA = new Wire
    val sA = new Switch(wPower, wA)
    val wB = new Wire
    val sB = new Switch(wPower, wB)
    val wCarryIn = new Wire
    val sCarryIn = new Switch(wPower, wCarryIn)
    val wOut = new Wire
    val wCarryOut = new Wire
    val adder = new FullAdder(wPower, wA, wB, wCarryIn, wOut, wCarryOut, 3.3)
    val notGate = new NOTGate(wPower, wA, wOut, 3.3)
    val andGate = new ANDGate(wPower, wA, wB, wOut, 3.3)
    val orGate = new ORGate(wPower, wA, wB, wOut, 3.3)
    val nandGate = new NANDGate(wPower, wA, wB, wOut, 3.3)
    val norGate = new NORGate(wPower, wA, wB, wOut, 3.3)
    val eqGate = new EQGate(wPower, wA, wB, wOut, 3.3)
    val neqGate = new NEQGate(wPower, wA, wB, wOut, 3.3)
    val xorGate = new XORGate(wPower, wA, wB, wOut, 3.3)
    val aNotbGate = new ANOTBGate(wPower, wA, wB, wOut, 3.3)
    val viewer = new CircuitViewer()
    viewer += adder
    viewer += source
    viewer += sA
    viewer += sB
    viewer += sCarryIn
    viewer += notGate
    viewer += andGate
    viewer += orGate
    viewer += nandGate
    viewer += norGate
    viewer += eqGate
    viewer += neqGate
    viewer += xorGate
    viewer += aNotbGate
    viewer.visible = true
  }

}
