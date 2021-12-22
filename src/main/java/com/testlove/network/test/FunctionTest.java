package com.testlove.network.test;

import com.testlove.network.store.CatchPacket;
import com.testlove.network.store.PacketShow;
import com.testlove.network.store.PacketTransport;
import com.testlove.network.thread.ThreadManager;
import org.jnetpcap.packet.PcapPacket;

import java.util.concurrent.BlockingQueue;

/**
 * @author TestLove
 * @version 1.0
 * @date 2021/12/19 18:39
 * @Description: null
 */
public class FunctionTest {
    public static void main(String[] args) {
        PacketTransport transport =PacketTransport.newInstance();
        CatchPacket catchPacket = CatchPacket.newInstance();
        catchPacket.getAllDevice();
        catchPacket.openDevice(5);
        transport.setIndex(5);
        transport.initPcap();
        transport.startCatchPacket();
        BlockingQueue<PcapPacket> packetQueue = transport.getPacketQueue();
        Thread showPacketThread = new Thread(new PacketShow(packetQueue));
        showPacketThread.setName("showPacketThread");
        ThreadManager.newInstance().addThread("showPacketThread", showPacketThread);
        showPacketThread.start();

        CatchPacket.newInstance().getPcap().dumpOpen("E:\\");


    }
}
