package meeting_room;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ReserveDetail implements ActionListener {
	private ReserveDetailUI rdUI;
	
	int timePrice = 0; // 시간 금액 레이블에 표시
	int personPrice = 0; // 인원 금액 레이블에 표시
	int totalPrice = 0 ; // 총 금액 레이블에 표시
	int personInfo_int = 0; // 추가 인원
	
	public ReserveDetail(ReserveDetailUI rdUI) {
		this.rdUI = rdUI;
		// time_tf에 DocumentListener 추가
		rdUI.time_tf.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			@Override
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				updatePrice();
			}
			
			@Override
			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				updatePrice();
			}
			
			@Override
			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				updatePrice();
			}
		});
	
		// select_add_person 콤보박스에 ActionListener 추가
		rdUI.select_add_person.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updatePrice();
			}
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton clickedButton = (JButton) e.getSource();
		
		if (clickedButton == rdUI.cancel_btn) {
			rdUI.dispose();
		} else if (clickedButton == rdUI.payment_btn) {
			// 결제 이벤트 구현부
			reserve();
		}
	}
	
	public void errorMsg() {
		JOptionPane optionPane;
		JDialog dialog;
		
		optionPane = new JOptionPane("예약 시간 선택 오류입니다.", JOptionPane.ERROR_MESSAGE);
		dialog = optionPane.createDialog(rdUI, "예약 안내");
		dialog.setLocationRelativeTo(rdUI);
		dialog.setVisible(true);
	}
	
	// 시간 중복 여부 판별 메서드
	public boolean isTimeOverlap(String ReserveTime, int useTime) {
		Vector<ReserveBean> reservelist;
		MyInfoMgr mgr = new MyInfoMgr();
		reservelist = mgr.reserveAll();
		
		// Spinner의 Value를 받아와 선택한 시간 데이터와 사용시간을 받아옴 
		int year = (int) rdUI.yearSpinner.getValue();
		Month month = Month.of((int) rdUI.monthSpinner.getValue());
		int dayOfMonth = (int) rdUI.daySpinner.getValue();
		int hour = (int) rdUI.hourSpinner.getValue();
        int minute = (int) rdUI.minSpinner.getValue();
        
        int selectedUseTime = Integer.parseInt(rdUI.time_tf.getText());
		
        // String 형의 시간 데이트를 LocalDateTime 형으로 형식변경을 하기 위한 포맷 설정 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		// DB에 저장된 다른 예약정보의 시작 시간
		LocalDateTime otherStartTime = LocalDateTime.parse(ReserveTime, formatter);
		
		// DB에 저장된 다른 예약정보의 종료 시간, use타임을 더함
		LocalDateTime otherEndTime = otherStartTime.plusHours(useTime);
		
		// 선택한 시간을 LocalDateTime 형으로 형식 변경 
		LocalDateTime selectedStartDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
		
		// 선택한 시간에 사용시간을 더해 종료 시간을 LocalDateTime 형으로 종료 시간 설정 
		LocalDateTime selectedEndDateTime = selectedStartDateTime.plusHours(selectedUseTime);
		
		// 시간이 겹치는지 확인 
		// 선택한 시작 시간이 다른 예약의 종료 시간보다 이전이라면 중복, 선택한 종료 시간이 다른 예약 시작 시간보다 이후라면 중복
	    if (selectedStartDateTime.isBefore(otherEndTime) && selectedEndDateTime.isAfter(otherStartTime)) {
	        return true;
	    } else {
	        return false;
	    }
	    
	}
	
	public void reserve() {
		int currentYear = rdUI.currentYear;
		int currentMonth = rdUI.currentMonth; // Calendar.MONTH는 0부터 시작하므로 1을 더해줌
		int currentDay = rdUI.currentDay;
		int currentHour = rdUI.currentHour;
		int currentMin = rdUI.currentMinute;
		
		Object selectYear = rdUI.yearSpinner.getValue();
		Object selectMonth = rdUI.monthSpinner.getValue();
		Object selectDay = rdUI.daySpinner.getValue();
		Object selectHour = rdUI.hourSpinner.getValue();
		Object selectMin = rdUI.minSpinner.getValue();
		
		JOptionPane optionPane;
		JDialog dialog;
		String roomInfo = rdUI.chosen_room_info_lb.getText();
		
		// 이용 시간을 입력하지 않았을 경우에 예외 처리 
		if ("".equals(rdUI.time_tf.getText())) {
			optionPane = new JOptionPane("이용할 시간을 입력해주세요.", JOptionPane.ERROR_MESSAGE);
			dialog = optionPane.createDialog(rdUI, "예약 안내");
			dialog.setLocationRelativeTo(rdUI);
			dialog.setVisible(true);
			return;
		}
		
		// 현재 시간 보다 이전 시간에 예약 하는 경우에 대한 예외 처리 
		if (currentYear == ((Integer)selectYear).intValue()) {
			if (currentMonth == ((Integer)selectMonth).intValue()) {
				if (currentDay > ((Integer)selectDay).intValue()) {
					errorMsg();
					return;
				} else if (currentDay == ((Integer)selectDay).intValue()) {
					if (currentHour == ((Integer)selectHour).intValue()) {
						if (currentMin > ((Integer)selectMin).intValue() || currentMin == ((Integer)selectMin).intValue()) {
							errorMsg();
							return;
						}
					} else if (currentHour > ((Integer)selectHour).intValue()) {
						errorMsg();
						return;
					}
				}
			} else if (currentMonth > ((Integer)selectMonth).intValue()) {
				errorMsg();
				return;
			}
		}
		
		
		// 선택한 룸에 예약할 때 선택한 시간에 이미 예약이 차 있는 경우에 대한 예외 처리 
		Vector<ReserveBean> reservelist;
		MyInfoMgr mgr = new MyInfoMgr();
		reservelist = mgr.reserveAll();
		
		for(int i = 0; i < reservelist.size(); i++ ) {
			ReserveBean bean3 = reservelist.get(i);
			// 방 정보 비교
			if(bean3.getResvroom().equals(roomInfo)) {
				// 시간 중복 여부 판별 
				if (isTimeOverlap(bean3.getResvtime(), bean3.getResvusetime())) {
					// 예외처리
					optionPane = new JOptionPane("선택한 날짜와 시간에 이미 예약이 존재합니다.", JOptionPane.ERROR_MESSAGE);
					dialog = optionPane.createDialog(rdUI, "예약 중복");
					dialog.setLocationRelativeTo(rdUI);
					dialog.setVisible(true);
					return;
					
		        }
			}
		}
		
		ReserveBean bean = new ReserveBean();
		mgr = new MyInfoMgr();
		
		String id = LoginUI.ID;
		String day = selectYear.toString() + "-" + selectMonth.toString() + "-" + selectDay.toString();
		String str = selectYear.toString() + "-" + selectMonth.toString() + "-" + selectDay.toString() + " " + selectHour.toString() + ":" + selectMin.toString();
		
		MyInfoBean bean2 = mgr.select(id);
		String personInfo = (String) rdUI.select_add_person.getSelectedItem();
		int personInfo_int = 0; // 추가 인원 

		// 추가 인원 int 형 변환 
		if(personInfo.equals("없음")) {
			personInfo_int = 0;
		} else if(personInfo.equals("1명")) {
			personInfo_int = 1;
		} else if(personInfo.equals("2명")) {
			personInfo_int = 2;
		}
		
		int money = bean2.getMoney();
		if (money < totalPrice) {
			optionPane = new JOptionPane("잔액이 부족합니다.", JOptionPane.ERROR_MESSAGE);
			dialog = optionPane.createDialog(rdUI, "예약 안내");
			dialog.setLocationRelativeTo(rdUI);
			dialog.setVisible(true);
			return;
		}
		
		RoomBean bean3 = mgr.room(roomInfo);
		
		bean.setResvid(bean2.getID());
		bean.setResvname(bean2.getName());
		bean.setResvphone(bean2.getPhone());
		bean.setResvroom(roomInfo);
		bean.setResvtime(str);
		bean.setResvusetime(Integer.parseInt(rdUI.time_tf.getText()));
		bean.setResvperson(bean3.getRperson() + personInfo_int);
		bean.setResvtotal(totalPrice);
		
		// DB에서 받아온 DATE타입을 테이블에 업데이트 하기 위해 포맷을 변경하기 위한 선언
		SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		SimpleDateFormat newFormat2 = new SimpleDateFormat("HH:mm:ss");
		String OriginalResvtime = str;
		String timeText = rdUI.time_tf.getText();
		int resvtime = Integer.parseInt(timeText); // 사용 시간을 받아옴
		TotalBean bean4 = new TotalBean();
		try {
			Date date = originalFormat.parse(str + ":00");
			
			// getResvusetime()을 시간으로 변환하고 더하기
	            Calendar calendar = Calendar.getInstance();
	            calendar.setTime(date); // 캘린더의 데이트를 받아온 시간으로 변경
	            calendar.add(Calendar.HOUR_OF_DAY, resvtime); // 캘린더에 사용시간 더하기
			
			String formattedDate = newFormat.format(date); // 포멧 변경 YY-MM-DD
			String formattedDate2 = newFormat2.format(date); // 포멧 변경 HH:MM:SS
			String EndformattedDate2 = newFormat2.format(calendar.getTime()); // 캘린더에 사용시간을 더하고 포맷 변경하여 출력
			
			Date aTime = parseTime(formattedDate2);
			Date bTime = parseTime(EndformattedDate2);
			Date startTime = parseTime("09:00:00");
			Date endTime = parseTime("23:00:01");
			
			
			if (isTimeInRange(aTime, startTime, endTime) && isTimeInRange(bTime, startTime, endTime)) {
				bean4.setRoom(roomInfo);
				bean4.setID(id);
				bean4.setDay(day);
				bean4.setIntime(formattedDate + " " + formattedDate2);
				bean4.setOuttime(formattedDate + " " + EndformattedDate2);
				bean4.setRoom_total(totalPrice);
				bean4.setTotal(totalPrice);
			} else {
				optionPane = new JOptionPane("영업 시간은 09시부터 23시까지 입니다. \n예약 시간을 확인해주세요.", JOptionPane.ERROR_MESSAGE);
				dialog = optionPane.createDialog(rdUI, "예약 시간 오류 안내");
				dialog.setLocationRelativeTo(rdUI);
				dialog.setVisible(true);
				return;
			}
		} catch (Exception e) {
			return;
		}
		
		
		
		if(mgr.reserve(bean)) {
			if (mgr.total(bean4)) {
				bean2.setMoney(money - totalPrice);
				if (mgr.charge(bean2)) {
					optionPane = new JOptionPane("예약이 완료되었습니다.", JOptionPane.INFORMATION_MESSAGE);
					dialog = optionPane.createDialog(rdUI, "예약 안내");
					dialog.setLocationRelativeTo(rdUI);
					dialog.setVisible(true);
					rdUI.dispose();
					ReserveUI.a.doClick();
					return;
				}
			}
		}
	}
	
	private static Date parseTime(String timeStr) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			return dateFormat.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static boolean isTimeInRange(Date time, Date startTime, Date endTime) {
		return time.compareTo(startTime) >= 0 && time.compareTo(endTime) <= 0;
	}
	
	private void updatePrice() {   
		// 텍스트 필드의 값을 가져와서 처리
		String timeText = rdUI.time_tf.getText();
		String roomInfo = rdUI.chosen_room_info_lb.getText();
		String personInfo = (String) rdUI.select_add_person.getSelectedItem();
		MyInfoMgr mgr = new MyInfoMgr();
		RoomBean bean = mgr.room(roomInfo);
		
		// 추가 인원 int 형 변환 
		if(personInfo.equals("없음")) {
			personInfo_int = 0;
		} else if(personInfo.equals("1명")) {
			personInfo_int = 1;
		} else if(personInfo.equals("2명")) {
			personInfo_int = 2;
		}
	
		try {
			// 입력값이 숫자이거나 비어있을 때만 처리
			// 숫자와 백스페이스 키만 입력할 수 있도록 함 
			if (timeText.matches("\\d*")) {
				// 예외 처리를 통해 숫자로 변환
				int hours = timeText.isEmpty() ? 0 : Integer.parseInt(timeText);
				 
				timePrice = hours * bean.getRperson() * 10000;
				personPrice = personInfo_int * bean.getRadd();
				totalPrice = timePrice + personPrice;
				
				// 값 업데이트
				rdUI.person_price_value_lb.setText(personPrice + " 원");
				rdUI.time_price_value_lb.setText(timePrice + " 원");
				rdUI.total_price_value_lb.setText(totalPrice + " 원");
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "숫자만 입력 가능합니다.");
		}
	}
}
