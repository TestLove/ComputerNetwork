package com.testlove.network.util;


import java.text.DecimalFormat;

public class Tool {

	public static String transFloatToHundred(float f) {
		DecimalFormat df = new DecimalFormat("0.00%");
		return df.format(f);
	}

	public static String formatByteSize(long l) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (l < 1024) {
			fileSizeString = df.format((double) l) + "B";
		} else if (l < 1048576) {
			fileSizeString = df.format((double) l / 1024) + "K";
		} else if (l < 1073741824) {
			fileSizeString = df.format((double) l / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) l / 1073741824) + "G";
		}
		return fileSizeString;
	}

	// 判断ip地址是否为公网IP
	public static boolean isPublicIp(byte[] addr) {
		final byte b0 = addr[0];
		final byte b1 = addr[1];
		// 10.x.x.x/8
		final byte SECTION_1 = 0x0A;
		// 172.16.x.x/12
		final byte SECTION_2 = (byte) 0xAC;
		final byte SECTION_3 = (byte) 0x10;
		final byte SECTION_4 = (byte) 0x1F;
		// 192.168.x.x/16
		final byte SECTION_5 = (byte) 0xC0;
		final byte SECTION_6 = (byte) 0xA8;
		switch (b0) {
		case SECTION_1:
			return false;
		case SECTION_2:
			if (b1 >= SECTION_3 && b1 <= SECTION_4) {
				return false;
			}
		case SECTION_5:
			switch (b1) {
			case SECTION_6:
				return false;
			}
		default:
			return true;

		}
	}

}
