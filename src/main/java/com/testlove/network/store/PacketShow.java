package com.testlove.network.store;

import com.testlove.network.analyse.ProtocolAnalyse;
import com.testlove.network.bean.FormatPacket;
import com.testlove.network.filter.FilterAfterCatch;
import com.testlove.network.thread.ThreadManager;
import org.jnetpcap.packet.PcapPacket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @author TestLove
 * @version 1.0
 * @date 2021/12/19 21:30
 * @Description: null
 */
public class PacketShow implements Runnable{
    private List<PcapPacket> packets; // 缓存收到的数据包
    private ProtocolAnalyse ptlAna;
    private boolean suspend = false;// 线程暂停标识
    private String control = ""; // 线程锁绑定的对象
    private boolean dofilter = false;
    private String filter_expression = "";
    private BlockingQueue<PcapPacket> packetQueue = null;
    public  PacketShow(BlockingQueue<PcapPacket> packetQueue) {
        this.packetQueue = packetQueue;

    }
    @Override
    public void run() {
        // 进行的操作多是在List后追加元素，或是查找某一元素，故使用ArrayList
        packets = new ArrayList<PcapPacket>();

        PcapPacket packet;

        // 进行数据定时保存
//        openUpdatePacketTimer();

        // ProtocolAnalyse初始化须在Counter之前，因为Counter中需要ProtocolAnalyse中先注册协议
        ptlAna = ProtocolAnalyse.newInstance();

        while (true) {
            try {
                if (suspend ) {
                    synchronized (control) {
                        System.out.println("showPacketThread waiting");
                        control.wait();
                        System.out.println("showPacketThread notify");
                    }
                }
                while (packetQueue.size() > 0) {

                    packet = packetQueue.take();

                    packets.add(packet);

                    // 过滤数据包
                    if (isDofilter()) {
                        int result = FilterAfterCatch.filter(packet, filter_expression);
                        if (result == 0) {
                            continue;
                        } else if (result == -1) {
                            setDofilter(false);
                            System.out.println("过滤器表达式有错误");
                            // 挂起线程
                            setSuspend(true);
                            continue;
                        }
                    }

                    showPacketInConsole(packet);

                }
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("showPacketThread中断");
                ThreadManager.newInstance().printallThreadState();
            }
        }

    }

    private void showPacketInConsole(PcapPacket packet) {
        FormatPacket pkt;

        pkt = new FormatPacket();
        pkt.setInfo(ptlAna.analyse(packet)); // ptlAna.analyse()用于统计和显示，功能强大
        pkt.setNo((int) packet.getFrameNumber());
//        pkt.setTime(FormatTime.formatTime(new Date(packet.getCaptureHeader().timestampInMillis())));
        pkt.setSrc(ptlAna.getSrc());
        pkt.setDest(ptlAna.getDest());
        pkt.setProtocol(ptlAna.getProtocolName());
        pkt.setLength(packet.getPacketWirelen());
        System.out.println(pkt);
    }

    private void setSuspend(boolean b) {
        suspend = b;
    }

    private void setDofilter(boolean b) {
        dofilter = b;
    }

    public boolean isDofilter() {
        return dofilter;
    }
}
