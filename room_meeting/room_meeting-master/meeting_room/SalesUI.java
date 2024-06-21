package meeting_room;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultFormatter;

public class SalesUI extends JFrame implements ActionListener {
	
	JTable totaltable;
	JScrollPane pane;
	JLabel start_date_lb = new JLabel("검색 시작일");
	JLabel end_date_lb = new JLabel("검색 종료일");
	JButton ok_btn = new JButton("확인");
	JButton search_btn = new JButton("검색");

	// 현재 날짜를 얻어옴
	Date currentDate = new Date();
	Calendar calendar = Calendar.getInstance();
	
	// 현재 날짜를 기본값으로 설정
	int currentYear = calendar.get(Calendar.YEAR);
	int currentMonth = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH는 0부터 시작하므로 1을 더해줌
	int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
	
	SpinnerNumberModel spinnerNumberModel1 = new SpinnerNumberModel(currentYear, currentYear, 2030, 1);
	JSpinner YearSpinner1 = new JSpinner(spinnerNumberModel1);
	
	SpinnerNumberModel spinnerNumberModel2 = new SpinnerNumberModel(currentYear, currentYear, 2030, 1);
	JSpinner YearSpinner2 = new JSpinner(spinnerNumberModel2);
	
	SpinnerNumberModel spinnerNumberModel3 = new SpinnerNumberModel(currentMonth, 1, 12, 1);
	JSpinner MonthSpinner1 = new JSpinner(spinnerNumberModel3);
	
	SpinnerNumberModel spinnerNumberModel4 = new SpinnerNumberModel(currentMonth, 1, 12, 1);
	JSpinner MonthSpinner2 = new JSpinner(spinnerNumberModel4);
	
	SpinnerNumberModel spinnerNumberModel5 = new SpinnerNumberModel(currentDay, 1, 31, 1);
	JSpinner DaySpinner1 = new JSpinner(spinnerNumberModel5);
	
	SpinnerNumberModel spinnerNumberModel6 = new SpinnerNumberModel(currentDay, 1, 31, 1);
	JSpinner DaySpinner2 = new JSpinner(spinnerNumberModel6);
	
	public SalesUI() {
		setSize(1100, 622);
		setTitle("매출 현황");
		
		Font font = new Font("Dialog", Font.BOLD, 50);
		
		JLabel title = new JLabel("전체 매출 현황");
		
		viewlist();
		
		Container c = getContentPane();
		c.setLayout(null);
		
		title.setFont(font);
		title.setBounds(290, 15, 600, 45);
		pane.setBounds(55, 170, 986, 385);
		
		start_date_lb.setBounds(60, 70, 150, 50);
		end_date_lb.setBounds(240, 70, 150, 50);

		YearSpinner1.setBounds(60, 110, 50, 30);
		YearSpinner2.setBounds(240, 110, 50, 30);
		MonthSpinner1.setBounds(120, 110, 40, 30);
		MonthSpinner2.setBounds(300, 110, 40, 30);
		DaySpinner1.setBounds(170, 110, 40, 30);
		DaySpinner2.setBounds(350, 110, 40, 30);

		search_btn.setBounds(700, 80, 140, 70);
		ok_btn.setBounds(880, 80, 140, 70);
		
		totaltable.addMouseListener(new MouseAction());
		search_btn.addActionListener(this);
		ok_btn.addActionListener(this);
		
		JFormattedTextField.AbstractFormatter formatter1 = ((JSpinner.DefaultEditor) YearSpinner1.getEditor()).getTextField().getFormatter();

		if (formatter1 instanceof DefaultFormatter) {
			((DefaultFormatter) formatter1).setAllowsInvalid(false);
		}
		
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(YearSpinner1, "####");
		YearSpinner1.setEditor(editor);
				
		JSpinner.NumberEditor editor2 = new JSpinner.NumberEditor(YearSpinner2, "####");
		YearSpinner2.setEditor(editor2);
		
		c.add(title);
		c.add(pane);
		c.add(start_date_lb);
		c.add(end_date_lb);
		c.add(YearSpinner1);
		c.add(YearSpinner2);
		c.add(MonthSpinner1);
		c.add(MonthSpinner2);
		c.add(DaySpinner1);
		c.add(DaySpinner2);
		c.add(ok_btn);
		c.add(search_btn);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setResizable(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if (obj.equals(ok_btn)) {
			dispose();
		} else if (obj.equals(search_btn)) {
			searchlist();
		}
	}
	
	private class MouseAction extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				int row = totaltable.getSelectedRow();
				TableModel model = totaltable.getModel();
				String room = String.valueOf(model.getValueAt(row,  0));
				SalesDetailUI sdui = new SalesDetailUI(room);
			}
			super.mouseClicked(e);
		}
	}
	
	public void viewlist() {
		Vector<TotalBean> vlist;
		MyInfoMgr mgr = new MyInfoMgr();
				
		String header[] = {"방 번호", "음식 매출", "게임 매출", "방 매출","총 매출"};
		String [][] conts = new String[12][header.length];
		String a;
		int food_total = 0, game_total = 0, room_total = 0, total = 0;
		
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startOfDay = now.with(LocalTime.MIN);
		LocalDateTime endOfDay = now.with(LocalTime.MAX);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String startDay = String.valueOf(startOfDay.format(formatter));
		String endDay = String.valueOf(endOfDay.format(formatter));
		
		for (int i = 0; i < 12; i++) {
			food_total = 0;
			game_total = 0;
			room_total = 0;
			total = 0;
			
			if (i < 9) a = "R0" + (i + 1);
			else a = "R" + String.valueOf(i + 1);
			
			vlist = mgr.salesAll(a, startDay, endDay);
			
			for (int j = 0; j < vlist.size(); j++) {
				TotalBean bean = vlist.get(j);
				food_total += bean.getFood_total();
				game_total += bean.getGame_total();
				room_total += bean.getRoom_total();
				total += bean.getTotal();
			}
			conts[i][0] = a;
			conts[i][1] = String.valueOf(food_total);
			conts[i][2] = String.valueOf(game_total);
			conts[i][3] = String.valueOf(room_total);
			conts[i][4] = String.valueOf(total);
		}
		
		DefaultTableModel model = new DefaultTableModel(conts, header) {
			public boolean isCellEditable(int i, int c) {return false;}
		};

		totaltable = new JTable(model);
		pane = new JScrollPane(totaltable);
		
		view();
	}
	
	public void searchlist() {
		String syear = String.valueOf(YearSpinner1.getValue());
		String eyear = String.valueOf(YearSpinner2.getValue());
		String smonth = String.valueOf(MonthSpinner1.getValue());
		String emonth = String.valueOf(MonthSpinner2.getValue());
		String sday = String.valueOf(DaySpinner1.getValue());
		String eday = String.valueOf(DaySpinner2.getValue());
		
		if ("".equals(syear) || "".equals(eyear) || "".equals(smonth) || "".equals(emonth) || "".equals(sday) || "".equals(eday)) {
			errormsg();
			return;
		}
		
		if (Integer.parseInt(syear) == Integer.parseInt(eyear)) {
			if (Integer.parseInt(smonth) == Integer.parseInt(emonth)) {
				if (Integer.parseInt(sday) > Integer.parseInt(eday)) {
					errormsg();
					return;
				}
			} else if (Integer.parseInt(smonth) > Integer.parseInt(emonth)) {
				errormsg();
				return;
			}
		} else if (Integer.parseInt(syear) > Integer.parseInt(eyear)) {
			errormsg();
			return;
		}
		
		if (smonth.length() == 1) smonth = "0" + smonth;
		if (emonth.length() == 1) emonth = "0" + emonth;
		if (sday.length() == 1) sday = "0" + sday;
		if (eday.length() == 1) eday = "0" + eday;
		String startday = syear + "-" + smonth + "-" + sday;
		String endday = eyear + "-" + emonth + "-" + eday;
		Vector<TotalBean> vlist;
		MyInfoMgr mgr = new MyInfoMgr();
				
		String header[] = {"방 번호", "음식 매출", "게임 매출", "방 매출","총 매출"};
		String [][] conts = new String[12][header.length];
		String a;
		int food_total = 0, game_total = 0, room_total = 0, total = 0;
		
		for (int i = 0; i < 12; i++) {
			food_total = 0;
			game_total = 0;
			room_total = 0;
			total = 0;
			
			if (i < 9) a = "R0" + (i + 1);
			else a = "R" + String.valueOf(i + 1);
			
			vlist = mgr.salesAll(a, startday, endday);

			for (int j = 0; j < vlist.size(); j++) {
				TotalBean bean = vlist.get(j);
				food_total += bean.getFood_total();
				game_total += bean.getGame_total();
				room_total += bean.getRoom_total();
				total += bean.getTotal();
			}
			conts[i][0] = a;
			conts[i][1] = String.valueOf(food_total);
			conts[i][2] = String.valueOf(game_total);
			conts[i][3] = String.valueOf(room_total);
			conts[i][4] = String.valueOf(total);
		}
		
		DefaultTableModel model = new DefaultTableModel(conts, header) {
			public boolean isCellEditable(int i, int c) {return false;}
		};
		totaltable.setModel(model);
		
		view();
	}
	
	public void view() {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		Font font = new Font("Dialog",Font.BOLD, 16);
		totaltable.setFont(font);
		
		totaltable.getTableHeader().setResizingAllowed(false);
		totaltable.getTableHeader().setReorderingAllowed(false);
		
		totaltable.getColumnModel().getColumn(0).setPreferredWidth(20);
		totaltable.getColumnModel().getColumn(1).setPreferredWidth(40);
		totaltable.getColumnModel().getColumn(2).setPreferredWidth(40);
		totaltable.getColumnModel().getColumn(3).setPreferredWidth(40);
		totaltable.getColumnModel().getColumn(4).setPreferredWidth(40);
		totaltable.setRowHeight(30);
		
		for (int i = 0; i < totaltable.getColumnCount(); i++) {
			totaltable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
	}
	
	public void errormsg() {
		JOptionPane optionPane;
		JDialog dialog;
		
		optionPane = new JOptionPane("날짜 선택 오류입니다.", JOptionPane.ERROR_MESSAGE);
		dialog = optionPane.createDialog(null, "오류 안내");
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
	
	public static void main(String[] args) {
		
	}
}