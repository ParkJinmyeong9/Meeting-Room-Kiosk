package meeting_room;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class OrderRoomUI extends JFrame {
	JTable reservetable;
	JScrollPane pane;
	static JButton jb = new JButton(" ");
	
	Container c = getContentPane();

	String formattedDate; // 포멧 변경 YY-MM-DD
	String formattedDate2; // 포멧 변경 HH:MM:SS
	String EndformattedDate2; // 캘린더에 사용시간을 더하고 포맷 변경하여 출력

	SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd"); 
	SimpleDateFormat newFormat2 = new SimpleDateFormat("HH:mm:ss");
	
	String info;
	
	public OrderRoomUI(String info) {
		this.info = info;
		
		setSize(1000,440);
		setTitle("주문 방 정보");
		
		c.setLayout(null);
		
		prviewlist();
		
		pane.setBounds(0,0,986,440);	
		
		reservetable.addMouseListener(new MouseAction());
		
		c.add(pane);
		c.add(jb);

		//화면 중앙에 오게 설정
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setResizable(false);
	}
	
	private class MouseAction extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				int row = reservetable.getSelectedRow();
				TableModel tm = reservetable.getModel();
				if (check(String.valueOf(tm.getValueAt(row, 5)), Integer.valueOf((String) tm.getValueAt(row,  6))) == true) {
					if (info.equals("음식")) {
						FoodOrderUI fui = new FoodOrderUI(String.valueOf(tm.getValueAt(row, 4)), String.valueOf(tm.getValueAt(row, 5)));
						dispose();
					} else if (info.equals("게임")) {
						GameOrderUI gui = new GameOrderUI(String.valueOf(tm.getValueAt(row, 4)), String.valueOf(tm.getValueAt(row, 5)));
						dispose();
					}
				} else {
					JOptionPane.showMessageDialog(OrderRoomUI.this, "현재 주문 가능한 시간이 아닙니다.\n예약 시간 이후 주문해주세요.");
				}
			}
			super.mouseClicked(e);
		}
	}
	
	public void prviewlist() {		
		Vector<ReserveBean>vlist;
		MyInfoMgr mgr = new MyInfoMgr();
		
		vlist = mgr.reserveAll();

		int a = 0;
		
		for (int i = 0; i < vlist.size(); i++) {
			ReserveBean bean = vlist.get(i);
			if (resvlist(bean.getResvtime(), bean.getResvusetime()) == true) {
				a++;
			}
		}
		
		String header[] = {"번호", "아이디", "이름", "전화번호","방 번호", "예약 날짜", "이용 시간", "이용 인원"};
		String [][] conts = new String[a][header.length];
		
		a = 0;
		for (int i = 0; i < vlist.size(); i++) {
			ReserveBean bean = vlist.get(i);
			if (resvlist(bean.getResvtime(), bean.getResvusetime()) == true) {
				conts[a][0] = String.valueOf(a + 1);
				conts[a][1] = bean.getResvid();
				conts[a][2] = bean.getResvname();
				conts[a][3] = bean.getResvphone();
				conts[a][4] = bean.getResvroom();
				conts[a][5] = bean.getResvtime();
				conts[a][6] = String.valueOf(bean.getResvusetime());
				conts[a][7] = String.valueOf(bean.getResvperson());
				a++;
			}
		}
				
		DefaultTableModel model = new DefaultTableModel(conts, header) {
			public boolean isCellEditable(int i, int c) {return false;}
		};
		
		if (reservetable != null) {
			model = (DefaultTableModel) reservetable.getModel();  // 기존 모델 가져오기
			model.setRowCount(0);  // 모델의 행 초기화
			
			for (int i = 0; i < vlist.size(); i++) {
				model.addRow(conts[i]);  // 새로운 데이터로 모델 업데이트
			}
		}
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		reservetable = new JTable(model);
		pane = new JScrollPane(reservetable);
		
		Font font = new Font("Dialog",Font.BOLD, 16);
		reservetable.setFont(font);
		
		reservetable.getTableHeader().setResizingAllowed(false);
		reservetable.getTableHeader().setReorderingAllowed(false);
		
		reservetable.getColumnModel().getColumn(0).setPreferredWidth(20);
		reservetable.getColumnModel().getColumn(1).setPreferredWidth(50);
		reservetable.getColumnModel().getColumn(2).setPreferredWidth(40);
		reservetable.getColumnModel().getColumn(3).setPreferredWidth(40);
		reservetable.getColumnModel().getColumn(4).setPreferredWidth(10);
		reservetable.getColumnModel().getColumn(5).setPreferredWidth(80);
		reservetable.getColumnModel().getColumn(6).setPreferredWidth(10);
		reservetable.getColumnModel().getColumn(7).setPreferredWidth(10);
		reservetable.setRowHeight(30);
		
		for (int i = 0; i < reservetable.getColumnCount(); i++) {
			reservetable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
	}
	
	public boolean resvlist(String time, int use) {
		boolean flag = false;

		String OriginalResvtime = time;
		int resvtime = use;
			
		try {
			Date date = originalFormat.parse(OriginalResvtime);
			formattedDate = newFormat.format(date); // 포멧 변경 YY-MM-DD
			
			LocalDateTime currentTime = LocalDateTime.now();
			LocalDate todayDate = currentTime.toLocalDate();
			
			LocalDate resv = LocalDate.parse(formattedDate);
			
                  Date currentDate = new Date();
                  Calendar calendar = Calendar.getInstance();
                  calendar.setTime(date); // 캘린더의 데이트를 받아온 시간으로 변경
                  calendar.add(Calendar.HOUR_OF_DAY, resvtime); // 캘린더에 사용시간 더하기

                  String EndformattedDate = originalFormat.format(calendar.getTime()); // 캘린더에 사용시간을 더하고 포맷 변경하여 출력
                  Date endtime = originalFormat.parse(EndformattedDate);
            
			if (todayDate.isEqual(resv) && endtime.after(currentDate)) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return flag;
	}
	
	public boolean check(String time, int use) {
		boolean flag = false;

		String OriginalResvtime = time;
		int resvtime = use;
			
		try {
			Date date = originalFormat.parse(OriginalResvtime);
				
			//getResvusetime()을 시간으로 변환하고 더하기
			Calendar calendar = Calendar.getInstance();
			
			calendar.setTime(date); // 캘린더의 데이트를 받아온 시간으로 변경
			calendar.add(Calendar.HOUR_OF_DAY, resvtime); // 캘린더에 사용시간 더하기
			
			formattedDate = newFormat.format(date); // 포멧 변경 YY-MM-DD
			formattedDate2 = newFormat2.format(date); // 포멧 변경 HH:MM:SS
			EndformattedDate2 = newFormat2.format(calendar.getTime()); // 캘린더에 사용시간을 더하고 포맷 변경하여 출력
			
			//현재 시간 가져오기
			LocalDateTime currentTime = LocalDateTime.now();
			
			//비교할 시간 가져오기
			LocalDateTime resv = LocalDateTime.parse(formattedDate + "T" + formattedDate2);
			LocalDateTime resv2 = LocalDateTime.parse(formattedDate + "T" + EndformattedDate2);
			
			//현재 시간이 특정 시간 범위 내에 있는지 확인
			if (isDateTimeInRange(currentTime, resv, resv2)) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return flag;
	}
	
	// 현재 시간이 특정 시간 범위 내에 있는지 확인하는 메서드
	private static boolean isDateTimeInRange(LocalDateTime dateTime, LocalDateTime startTime, LocalDateTime endTime) {
		return !dateTime.isBefore(startTime) && !dateTime.isAfter(endTime);
	}
}