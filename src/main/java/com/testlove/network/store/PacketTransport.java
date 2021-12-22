package com.testlove.network.store;

import org.jnetpcap.packet.PcapPacket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 获取数据包(实时抓取或者文件中读取)并放入阻塞队列
 */
public class PacketTransport {

	private static PacketTransport pkts = new PacketTransport();
	
	private CatchPacket catchPacket = CatchPacket.newInstance();;
	private OfflinePacket offlinePacket;
	private BlockingQueue<PcapPacket> packetQueue = new LinkedBlockingQueue<PcapPacket>();
	private int index=-1;

	private PacketTransport() {
	}

	public static PacketTransport newInstance() {
		return pkts;
	}

	// 开始抓包
	public void startCatchPacket() {
		// 初始化数据包队列
		
		packetQueue.clear();
		catchPacket.setPacketQueue(packetQueue);
		catchPacket.startCatch(index);
	}
	//从文件路径读取pcap文件
	public void startOfflinePacket(String filepath) {
		offlinePacket = OfflinePacket.newInstance();
		packetQueue.clear();
		offlinePacket.setPacketQueue(packetQueue);
		offlinePacket.getofflinePacket(filepath);
	}

	public BlockingQueue<PcapPacket> getPacketQueue() {
		return packetQueue;
	}

	public void setPacketQueue(BlockingQueue<PcapPacket> packetQueue) {
		this.packetQueue = packetQueue;
	}

	public String getDeviceInfo(int index) {

		return catchPacket.getDeviceInfo(index);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	//初始化catchPacket中的pcap
	public void initPcap() {
		if(null !=catchPacket.getPcap()) {
			catchPacket.getPcap().close();
		}
		catchPacket.openDevice(index);
	}

}