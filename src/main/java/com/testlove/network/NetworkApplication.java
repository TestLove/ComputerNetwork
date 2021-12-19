package com.testlove.network;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapDLT;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TestLove
 * @version 1.0
 * @date 2021/12/18 20:05
 * @Description:
 * 主要功能:
 * 1. 选择网卡进行流量监测
 * 2. 对各种流量进行分析和展示
 * 3. 可以保存为文件
 * 4. 可以过滤
 */
public class NetworkApplication {
    private static final String DEV_NAME = "\\Device\\NPF_{4F039C40-B2B8-4531-9C75-E6C79FCA3CB9}";

    private static void start() {
        StringBuilder errbuf = new StringBuilder();

        // 获得所有网卡
        List<PcapIf> ifs = new ArrayList<PcapIf>();
        if (Pcap.findAllDevs(ifs, errbuf) != 0) {
            System.err.println("No interfaces found");
            return;
        }

        Pcap pcap = null;
        //查找要监听的网卡进行流量嗅探
        for (PcapIf i : ifs) {
            //System.out.println(i);
            errbuf.setLength(0);
            if ((pcap = Pcap.openLive(i.getName(), 64, Pcap.MODE_NON_PROMISCUOUS,
                    1000, errbuf)) == null) {
                System.err.printf("Capture open %s: %s\n", i.getName(), errbuf
                        .toString());
            }

            // 混杂模式，看源码注释可能被忽略设置.其实我觉得现在也多用不到，实际中用Java写这个流量嗅探还得是混杂模式的应该不多吧
            // 况且说真的，在生活中，交换机现在这么智能，混杂模式不好用呀。
            // 本地环回：NULL，
            // 通用的话如以太网卡什么的推荐EN10MB，
            // 但是可能出现多网卡情况，所以过滤条件写复杂点比如指定ip呀mac地址什么
            // 这只是个demo写的简单了
            // 所以实际开发此处需要注意考虑监听网上配置，或者尝试启用混杂模式
            // 我这与这个代码的时候用的无限网卡，我看下值是30，这里就写这个30写死了，仅供参考
            if (pcap.datalink() == PcapDLT.EN10MB.getValue() && i.getFlags() == 30) {
                System.out.printf("Opened interface\n\t%s\n\t%s\n", i.getName(), i
                        .getDescription());
                System.out.printf("Warnings='%s'\n", errbuf.toString());
                break;
            } else {
                pcap.close();
                pcap = null;
            }
        }
        if (pcap == null) {
            System.err.println("Unable to find interface");
            return;
        }

        pcap.loop(Pcap.LOOP_INFINATE, (PcapPacket packet, String userObject) -> {
            System.out.println(userObject);
            // 打印报文
            System.out.println(packet);
        }, "xuxd");
        System.out.println("start capture...");
    }

    public static void main(String[] args) {
        start();
    }
}
