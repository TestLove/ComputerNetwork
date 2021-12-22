package test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.PcapPktHdr;
import org.jnetpcap.winpcap.WinPcap;
import org.jnetpcap.winpcap.WinPcapSendQueue;

public class Test5 {

	 public static void main(String[] args) {
	
	 //发送数据包，一次发送一个数据包
	
	 List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
	 StringBuilder errbuf = new StringBuilder(); // For any error msgs
	
	 /***************************************************************************
	 * 获取设备列表
	 **************************************************************************/
	 int r = Pcap.findAllDevs(alldevs, errbuf);
	 if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
	 System.err.printf("Can't read list of devices, error is %s",
	 errbuf.toString());
	 return;
	 }
	 PcapIf device = alldevs.get(1); // We know we have atleast 1 device
	
	 /*****************************************
	 * 打开网络接口
	 *****************************************/
	 int snaplen = 64 * 1024; // Capture all packets, no trucation
	 int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
	 int timeout = 10 * 1000; // 10 seconds in millis
	 Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
	
	
	 /*******************************************************
	 * 创建一个未加工的packet
	 *******************************************************/
	 byte[] a = new byte[14];
	 Arrays.fill(a, (byte) 0xff);
	 ByteBuffer b = ByteBuffer.wrap(a);
	 /*******************************************************
	 * 再一个open状态接口发送这个packet
	 *******************************************************/
	 if (pcap.sendPacket(b) != Pcap.OK) {
	 System.err.println(pcap.getErr());
	 }
	
	 /********************************************************
	 * 最后关闭
	 ********************************************************/
	 pcap.close();
	 }



}
