package test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHandler;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;

public class Test1 {

	
	public static void main(String[] args) {

		//获取当前机器的网卡信息
		StringBuilder errbuf = new StringBuilder();

		List<PcapIf> alldevs = new ArrayList<PcapIf>();
		
		int r = Pcap.findAllDevs(alldevs, errbuf);
		if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
		    System.err.printf("Can't read list of devices, error is %s", errbuf
		            .toString());
		    return;
		}
		
		//输出网卡信息
		System.out.println("Network devices found:");

		int i = 0;
		for (PcapIf device : alldevs) {
		    System.out.printf("#%d: %s [%s]\n", i++, device.getName(), device
		            .getDescription());
		}
		
		//选择要监控的网卡
		PcapIf device = alldevs.get(1);
		
		//打开设备
		
        // openlive方法：这个方法打开一个和指定网络设备有关的，活跃的捕获器 

        // 参数：snaplen指定的是可以捕获的最大的byte数，
        // 如果 snaplen的值 比 我们捕获的包的大小要小的话，
        // 那么只有snaplen大小的数据会被捕获并以packet data的形式提供。
        // IP协议用16位来表示IP的数据包长度，所有最大长度是65535的长度
        // 这个长度对于大多数的网络是足够捕获全部的数据包的

        // 参数：flags promisc指定了接口是promisc模式的，也就是混杂模式，
        // 混杂模式是网卡几种工作模式之一，比较于直接模式：
        // 直接模式只接收mac地址是自己的帧，
        // 但是混杂模式是让网卡接收所有的，流过网卡的帧，达到了网络信息监视捕捉的目的

        // 参数：timeout 这个参数使得捕获报后等待一定的时间，来捕获更多的数据包，
        // 然后一次操作读多个包，不过不是所有的平台都支持，不支持的会自动忽略这个参数

        // 参数：errbuf pcap_open_live()失败返回NULL的错误信息，或者成功时候的警告信息     

		int snaplen = 64 * 1024;
		int flags = Pcap.MODE_PROMISCUOUS;//混杂模式  接受所有经过网卡的帧
		int timeout = 1 * 1000;
		Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
		if (pcap == null) {
		    System.err.printf("Error while opening device for capture: "
		            + errbuf.toString());
		    return;
		}
		
		//创建一个数据包处理器
		PcapHandler<String> printSummaryHandler = new PcapHandler<String>() {

		    public void nextPacket(String user, long seconds, int useconds,
		                           int caplen, int len, ByteBuffer buffer) {
		        Date timestamp = new Date(seconds * 1000 + useconds/1000); // In millis

		        System.out.printf("Received packet at %s caplen=%-4d len=%-4d %s\n",
		                timestamp.toString(), // timestamp to 1 ms accuracy
		                caplen, // Length actually captured
		                len,    // Original length of the packet
		                user    // User supplied object
		        );
		    }
		};
		
		//循环监听
		pcap.loop(Integer.MAX_VALUE, printSummaryHandler, "jNetPcap rocks!");
		//监听结束后关闭
		pcap.close();
	}
}
