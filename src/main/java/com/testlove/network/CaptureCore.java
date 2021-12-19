package com.testlove.network;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapDLT;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;


/**
 * @author TestLove
 * @version 1.0
 * @date 2021/12/19 18:37
 * @Description: null
 */
public class CaptureCore {
    /**
     * 获取机器上的网卡列表
     * @return
     * @param pcapIf
     */
   public static void capture(PcapIf pcapIf){
       Pcap pcap = null;
       StringBuilder errbuf = new StringBuilder();;
       if ((pcap = Pcap.openLive(pcapIf.getName(), 64, Pcap.MODE_NON_PROMISCUOUS,
               1000, errbuf)) == null) {
           System.err.printf("Capture open %s: %s\n", pcapIf.getName(), errbuf
                   .toString());
       }
       pcap.loop(Pcap.LOOP_INFINITE, (PcapPacket packet, String userObject) -> {
           System.out.println(userObject);
           // 打印报文
           System.out.println(packet.toString());
       }, "==========================================下一个数据包=============================================");

   }

}
