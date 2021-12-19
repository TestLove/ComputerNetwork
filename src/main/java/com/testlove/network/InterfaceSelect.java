package com.testlove.network;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TestLove
 * @version 1.0
 * @date 2021/12/19 19:57
 * @Description: 用于网卡选择
 */
public class InterfaceSelect {
    public static List<PcapIf> devs = new ArrayList();
    public static List<PcapIf> getNetInterfaces(){
        StringBuilder errsb = new StringBuilder();
        int r = Pcap.findAllDevs(devs, errsb);
        if (r == Pcap.NOT_OK || devs.isEmpty()) {
            System.out.println("=========="+"获取接口出错"+"==========");
            return null;
        } else {
            return devs;
        }
    }
    //控制台格式化打印网络接口,务必先调用获取接口
    public static void showNetInterfaces(){
        System.out.println("以下显示网络接口,请输入要监听的序号");
       for(int i = 0; i <devs.size(); i++){
           System.out.println("No."+i+":"+FormatInterface.getShow(devs.get(i)));
       }

    }
    static class FormatInterface{
        public static String getShow(PcapIf p){
            String str = "接口名:"+p.getName()+", 描述:"+p.getDescription();
            return str;

        }
    }
}
