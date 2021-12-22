package main;


import frame.*;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;



public class Main{
	
	public static void main(String[] args) {
		//JFrame app = new MainPage("流量监控");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame app = new MainPage("流量监控");
			        app.setSize(700,700); 
					app.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
}
