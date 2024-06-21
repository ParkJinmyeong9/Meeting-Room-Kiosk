package meeting_room;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Reserve implements ActionListener {
	private ReserveUI reserveUI;
	private JButton previousSelectedButton;
	
	String formattedDate;
	String formattedDate2;
	String EndformattedDate;
	String EndformattedDate2;
	Date endtime;
	Date reservationDate;
	Calendar calendar;
 
	SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd"); 
	SimpleDateFormat newFormat2 = new SimpleDateFormat("HH:mm:ss");
	
	public Reserve(ReserveUI reserveUI) {
		this.reserveUI = reserveUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton clickedButton = (JButton) e.getSource();
		
		// 방을 클릭할때 테이블을 출력하고 선택된 방 레이블을 변경하고 선택한 방에 대한 예약 테이블 출력
		if(e.getSource() == clickedButton) {
			String roomName = clickedButton.getText();
			if(roomName != "예약") { // 예약 버튼을 눌렀을 때는 레이블을 변경하지 않음 
				reserveUI.chosen_room_value_lb.setText(roomName); // 레이블 변경
				
				Vector<ReserveBean> reservelist;
				MyInfoMgr mgr = new MyInfoMgr();
				reservelist = mgr.reserveAll();
				
				// 테이블 업데이트
				DefaultTableModel model = (DefaultTableModel) reserveUI.reserveTable.getModel();
	            model.setRowCount(0); // 기존 테이블 내용 지우기
	            
	            // reservelist를 날짜에 따라 정렬
	            Collections.sort(reservelist, new Comparator<ReserveBean>() {
	                @Override
	                public int compare(ReserveBean bean1, ReserveBean bean2) {
	                    Date date1 = null;
	                    Date date2 = null;

	                    try {
	                        date1 = originalFormat.parse(bean1.getResvtime());
	                        date2 = originalFormat.parse(bean2.getResvtime());
	                    } catch (ParseException e) {
	                        e.printStackTrace();
	                    }

	                    return date1.compareTo(date2);
	                }
	            });
	            
				
				for(int i = 0; i < reservelist.size(); i++ ) {
					ReserveBean bean = reservelist.get(i);
					try {
						// 선택된 방과 reserve 테이블의 방을 비교하고 종료시간이 현재 시간 이후인 경우에만 테이블에 추가
			                  Date reservationDate = originalFormat.parse(bean.getResvtime());
			                  Date currentDate = new Date();
			                  Calendar calendar = Calendar.getInstance();
			                  calendar.setTime(reservationDate); // 캘린더의 데이트를 받아온 시간으로 변경
			                  calendar.add(Calendar.HOUR_OF_DAY, bean.getResvusetime()); // 캘린더에 사용시간 더하기
			                  
			                  String EndformattedDate = originalFormat.format(calendar.getTime()); // 캘린더에 사용시간을 더하고 포맷 변경하여 출력
			                  String EndformattedDate2 = newFormat2.format(calendar.getTime());
			                  Date endtime = originalFormat.parse(EndformattedDate);
		                  
			                  
						if(bean.getResvroom().equals(roomName) && endtime.after(currentDate)) {
							String OriginalResvtime = bean.getResvtime();
							int resvtime = bean.getResvusetime(); //사용 시간을 받아옴
							
							try {
								Date date = originalFormat.parse(OriginalResvtime);
								
								
								String formattedDate = newFormat.format(date); //포멧 변경 YY-MM-DD
								String formattedDate2 = newFormat2.format(date); //포멧 변경 HH:MM:SS
								
								String[] rowData = { // 추가할 행의 데이터 
										formattedDate,
										formattedDate2,
										EndformattedDate2
								};
								model.addRow(rowData);
							} catch (Exception e2) {
								e2.printStackTrace();
							}
						}
					} catch (ParseException e3) {
						e3.printStackTrace();
					}
				}
			}
		}
		
		if (clickedButton != reserveUI.reserve_btn && clickedButton != reserveUI.cancel_btn) {
		
			// 이전에 선택된 버튼이 있다면 초기화
			if (previousSelectedButton != null) {
				previousSelectedButton.setBackground(null);
			}
		
			// 현재 선택된 버튼 업데이트
			clickedButton.setBackground(Color.CYAN);
			reserveUI.reserve_btn.setEnabled(true);
			previousSelectedButton = clickedButton;
		}
		
		// 예약하기 버튼 클릭시
		else if (clickedButton == reserveUI.reserve_btn) {
			// 예약된 방의 상태를 변경하기 위한 새로운 버튼 객체 생성 
			
			String selectedRoomInfo = previousSelectedButton.getText();
			// 이전에 클릭한 방 버튼의 텍스트
			
			ReserveDetailUI rdUI = new ReserveDetailUI(selectedRoomInfo);
			rdUI.setVisible(true);
		}
		
		// 취소 버튼 클릭시
		else if (clickedButton == reserveUI.cancel_btn) {
			reserveUI.dispose();
		}
	}
}