package frame;

import bean.FormatFilter;
import deal.CatchPacket;
import deal.FilterDeal;
import deal.PacketTransport;
import filter.ExpressionCheck;
import filter.FilterPreCatch;
import thread.ThreadManager;
import utils.Properties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

public class FilterPreCatchDialog extends JDialog {

	private JTable filtersTable;
	private DefaultTableModel ftableModel;
	private static final String[] header = { "名称", "过滤器" };
	private FilterDeal fdeal;
	private List<FormatFilter> filters;
	private JScrollPane filterpane;
	private JButton btn_delete;
	private JButton btn_add;
	private JButton btn_save;
	private JButton btn_begin; // 开始过滤
	private JButton btn_nofilter;
	private FormatFilter choosedFilter = null;
	private MainPage parent;
	private CatchPacket deal;
	private PacketTransport pktTrans;

	public FilterPreCatchDialog(MainPage parent) {
		deal = CatchPacket.newInstance();
		pktTrans = PacketTransport.newInstance();
		choosedFilter = null;
		this.parent = parent;
		setTitle("捕获过滤器");
		setModal(true);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		loadFilters();

		initFiltersTable();

		btn_delete = new JButton("-");
		btn_add = new JButton("+");
		btn_save = new JButton("保存");
		btn_nofilter = new JButton("直接抓包");
		btn_begin = new JButton("开始过滤");
		btn_delete.setEnabled(false);
		btn_begin.setEnabled(false);

		JPanel operatepanel = new JPanel();
		operatepanel.add(btn_add);
		operatepanel.add(btn_delete);
		operatepanel.add(btn_save);
		operatepanel.add(btn_nofilter);
		operatepanel.add(btn_begin);
		add(operatepanel);

		setOnClickListener();

		setSize(600, 600);
		setVisible(true);

	}

	private void initFiltersTable() {
		ftableModel = new DefaultTableModel(new Object[0][0], header);
		filtersTable = new JTable(ftableModel);
		filtersTable.setRowHeight(20);
		filtersTable.setBorder(BorderFactory.createRaisedBevelBorder());
		filterpane = new JScrollPane(filtersTable);
		for (int i = 0; i < filters.size(); i++) {
			ftableModel.addRow(new Object[] { filters.get(i).getName(), filters.get(i).getGrammar() });
		}
		add(filterpane);

		filtersTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				btn_delete.setEnabled(true);
				btn_begin.setEnabled(true);
				int row = filtersTable.getSelectedRow();
				choosedFilter = filters.get(row);

			}

		});
	}

	private void setOnClickListener() {
		btn_delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int row = filtersTable.getSelectedRow();
				ftableModel.removeRow(row);
				filters.remove(row);
			}
		});

		btn_add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FormatFilter newFilter = new FormatFilter();
				newFilter.setName("请输入过滤器名称");
				newFilter.setGrammar("请输入过滤器语法");
				ftableModel.addRow(new Object[] { newFilter.getName(), newFilter.getGrammar() });
				filters.add(newFilter);
			}
		});

		btn_save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveFilters();
			}
		});

		// 开始过滤
		btn_begin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				int row;
				if ((row = filtersTable.getSelectedRow())!=-1) {
					
					//String expression = filters.get(row).getGrammar();
					String expression = (String) filtersTable.getValueAt(row, 1);
					int result = ExpressionCheck.checkFilterExpression(expression);
					if (result == -1) {
						showMessage("过滤器表达式有错误");
						return;
					} else if (result == -2) {
						showMessage("未知错误");
						return;
					}
					saveFilters();
					setVisible(false);
					dispose();
					FilterPreCatch.filter(deal.getPcap(), expression);
					if(ThreadManager.newInstance().hasThread()) {
						parent.setSuspend(false);	
						pktTrans.startCatchPacket();
						return ;
					}
					startCatch();
				}
			}
		});

		// 直接显示所有数据包
		btn_nofilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFilters();
				setVisible(false);
				dispose();
				startCatch();

			}


		});

		// 关闭Dialog时将filter信息存回文件
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				saveFilters();
				setVisible(false);
				dispose();
			}

		});
	}

	private void loadFilters() {
		fdeal = FilterDeal.newInstance(Properties.filterpath);
		filters = fdeal.loadFilters();
	}

	private void saveFilters() {
		filters = new LinkedList<FormatFilter>();
		for (int i = 0; i < filtersTable.getRowCount(); i++) {
			filters.add(
					new FormatFilter((String) filtersTable.getValueAt(i, 0), (String) filtersTable.getValueAt(i, 1)));
		}

		// 停止编辑，这样JTable才能保存编辑内容，否则存回的是原内容
		if (filtersTable.getCellEditor() != null) {
			filtersTable.getCellEditor().stopCellEditing();
		}
		fdeal.setFilters(filters);
		fdeal.saveFilters(filters);
	}

	private void startCatch() {
		pktTrans.startCatchPacket();
		parent.startShowPackets(pktTrans.getDeviceInfo(pktTrans.getIndex()), pktTrans.getPacketQueue());
	}
	
	private void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}
}
