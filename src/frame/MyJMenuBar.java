package frame;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import deal.CatchPacket;
import deal.PacketTransport;
import deal.TimeUpdatePacket;

public class MyJMenuBar extends JMenuBar {

    private MainPage parent;
    private PacketTransport pktTrans;

    private JMenu[] m = {new JMenu("文件(F)"), new JMenu("捕获(C)"), new JMenu("统计(S)")};



private JMenuItem[][] mI = {{new JMenuItem("打开(O)"), new JMenuItem("保存(S)")},
        {new JMenuItem("开始(S)"), new JMenuItem("停止(T)"), new JMenuItem("重新开始(R)")},
        { new JMenuItem("分层")}
};


    public MyJMenuBar(Frame parent) {
        this.parent = (MainPage) parent;
        for (int i = 0; i < m.length; i++) {
            add(m[i]);
            // m[i].setMnemonic(mC[0][i]); // 设置助记符
            for (int j = 0; j < mI[i].length; j++) {
                m[i].add(mI[i][j]);
            }
        }

//		initJMenuItems();

        addOnClickListener();
    }



    private void addOnClickListener() {

        // --------------------------------------------
        // -------文件
        // --------------------------------------------
        // 打开文件
        mI[0][0].addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FileOperateDialog fDialog = new FileOperateDialog(parent.getPackets());
                String filepath = fDialog.doOpen();
                // 传输数据包到主界面
                startOfflinePacket(filepath);
            }
        });

        // 保存文件
        mI[0][1].addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FileOperateDialog fDialog = new FileOperateDialog(parent.getPackets());
                fDialog.doSave();
            }
        });


        // --------------------------------------------
        // -------捕获
        // --------------------------------------------
        // 开始捕获
        mI[1][0].addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                startCatch();
                mI[1][0].setEnabled(false);
            }
        });

        // 停止捕获
        mI[1][1].addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                stopCatch();
                mI[1][0].setEnabled(true);
            }

        });

        // 重新开始捕获
        mI[1][2].addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 清空表格
                stopCatch();
                parent.clear();
                startCatch();
                // 删除临时文件
                TimeUpdatePacket.deleteTempFile();
            }

        });


		// --------------------------------------------
		// -------统计
		// --------------------------------------------

		// 分层
		mI[2][0].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new ProtocolStatisticsFrame();
			}
		});
//
//		// HTTP
//		mI[5][1].addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				new HTTPStatisticsFrame();
//			}
//		});
//
//		// 内网IP流量
//		mI[5][2].addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				new IntranetIPStatisticsFrame();
//			}
//		});
//
//		// 已知IP-Mac映射
//		mI[5][3].addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				new IpMacMappingFrame();
//			}
//		});
//
//		// --------------------------------------------
//		// -------模式
//		// --------------------------------------------
//
//		// 实时抓包模式
//		mI[6][0].addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				mI[6][0].setEnabled(false);
//				mI[6][1].setEnabled(true);
//
//				// 文件功能和捕获功能可用，对应序号为0，2
//				for (int i = 0; i < mI[0].length; i++) {
//					mI[0][i].setEnabled(true);
//				}
//				for (int i = 0; i < mI[2].length; i++) {
//					mI[2][i].setEnabled(true);
//				}
//
//				ltmf.setVisible(false);
//				ltmf.dispose();
//				// 恢复抓包模式
//				parent.setCatchMode(MainPage.CATCHMODE);
//				parent.setSuspend(false);
//				CatchPacket.newInstance().setMode(CatchPacket.CATCHMODE);
//			}
//		});
//
//		// 长期监控模式
//		mI[6][1].addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				mI[6][0].setEnabled(true);
//				mI[6][1].setEnabled(false);
//
//				// 文件功能和捕获功能不可用，对应序号为0，2
//				for (int i = 0; i < mI[0].length; i++) {
//					mI[0][i].setEnabled(false);
//				}
//				for (int i = 0; i < mI[2].length; i++) {
//					mI[2][i].setEnabled(false);
//				}
//
//				// 只抓包，不显示，不保存
//				parent.setCatchMode(MainPage.MONITORMODE);
//				parent.setSuspend(true);
//				parent.clear();
//				CatchPacket.newInstance().setMode(CatchPacket.MONITORMODE);
//				ltmf = new LongTimeMonitorFrame(parent);
//			}
//		});

    }

    private void stopCatch() {
        // TODO 自动生成的方法存根
        // 中止抓包线程和界面显示数据包线程
        // threadManager = ThreadManager.newInstance();
        // threadManager.stopThread(ThreadManager.catchPacketThread);
        // threadManager.stopThread(ThreadManager.showPacketThread);
        CatchPacket.newInstance().setSuspend(true);
        parent.setSuspend(true);
    }

    private void startCatch() {
        // CatchPacket.newInstance().notify();
        // parent.notify();
        parent.setCatchMode(MainPage.CATCHMODE);
        parent.setShowMode(MainPage.NOFILTER);
        CatchPacket.newInstance().setSuspend(false);
        parent.setSuspend(false);
    }

    // 传输离线文本中的数据包到主界面
    private void startOfflinePacket(String filepath) {
        CatchPacket deal = CatchPacket.newInstance();
        deal.setSuspend(true);
        pktTrans = PacketTransport.newInstance();
        parent.clear();
        parent.setCatchMode(MainPage.OFFLINEMODE);
        parent.setShowMode(MainPage.NOFILTER);
        // TODO catchPacketThread会延迟一段时间才调wait(),可能会导致显示有一个实时包
        pktTrans.startOfflinePacket(filepath);

    }
}
