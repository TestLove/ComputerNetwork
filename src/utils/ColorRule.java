package utils;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.jnetpcap.packet.PcapPacket;

import bean.FormatFilter;
import deal.FilterDeal;

public class ColorRule{

	public final static Color defaultBgColor = Color.WHITE;
	public final static Color[] colors = {
			new Color(0x12272E),	//tcp bad,hsrp change,stp change,ospf change,icmp error,checksum error
			new Color(0xFCE0FF),	//icmp,arp
			new Color(0xE4FFC7),	//http
			new Color(0xA40000),	//tcp rst
			new Color(0xA0A0A0),	//tcp syn/fin
			new Color(0xE7E6FF),	//tcp	
			new Color(0xDAEEFF),	//udp
			new Color(0xBFCDDB),	//broadcast
			new Color(0xFFFFFF)		//default	
			};
		
	//加载着色规则
	public Color[] loadColorRule(){
		String filepath = Properties.colorRulepath;
		FilterDeal filterDeal = FilterDeal.newInstance(filepath);
		List<FormatFilter> filters = filterDeal.getFilters();
		Color[] colors = new Color[filters.size()];
		for (int i=0;i<colors.length;i++) {
			colors[i] = filters.get(i).getColor();
		}
		return colors;
	}
	

	


}
