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
	
	int timePrice = 0; // �ð� �ݾ� ���̺��� ǥ��
	int personPrice = 0; // �ο� �ݾ� ���̺��� ǥ��
	int totalPrice = 0 ; // �� �ݾ� ���̺��� ǥ��
	int personInfo_int = 0; // �߰� �ο�
	
	public ReserveDetail(ReserveDetailUI rdUI) {
		this.rdUI = rdUI;
		// time_tf�� DocumentListener �߰�
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
	
		// select_add_person �޺��ڽ��� ActionListener �߰�
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
			// ���� �̺�Ʈ ������
			reserve();
		}
	}
	
	public void errorMsg() {
		JOptionPane optionPane;
		JDialog dialog;
		
		optionPane = new JOptionPane("���� �ð� ���� �����Դϴ�.", JOptionPane.ERROR_MESSAGE);
		dialog = optionPane.createDialog(rdUI, "���� �ȳ�");
		dialog.setLocationRelativeTo(rdUI);
		dialog.setVisible(true);
	}
	
	// �ð� �ߺ� ���� �Ǻ� �޼���
	public boolean isTimeOverlap(String ReserveTime, int useTime) {
		Vector<ReserveBean> reservelist;
		MyInfoMgr mgr = new MyInfoMgr();
		reservelist = mgr.reserveAll();
		
		// Spinner�� Value�� �޾ƿ� ������ �ð� �����Ϳ� ���ð��� �޾ƿ� 
		int year = (int) rdUI.yearSpinner.getValue();
		Month month = Month.of((int) rdUI.monthSpinner.getValue());
		int dayOfMonth = (int) rdUI.daySpinner.getValue();
		int hour = (int) rdUI.hourSpinner.getValue();
        int minute = (int) rdUI.minSpinner.getValue();
        
        int selectedUseTime = Integer.parseInt(rdUI.time_tf.getText());
		
        // String ���� �ð� ����Ʈ�� LocalDateTime ������ ���ĺ����� �ϱ� ���� ���� ���� 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		// DB�� ����� �ٸ� ���������� ���� �ð�
		LocalDateTime otherStartTime = LocalDateTime.parse(ReserveTime, formatter);
		
		// DB�� ����� �ٸ� ���������� ���� �ð�, useŸ���� ����
		LocalDateTime otherEndTime = otherStartTime.plusHours(useTime);
		
		// ������ �ð��� LocalDateTime ������ ���� ���� 
		LocalDateTime selectedStartDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
		
		// ������ �ð��� ���ð��� ���� ���� �ð��� LocalDateTime ������ ���� �ð� ���� 
		LocalDateTime selectedEndDateTime = selectedStartDateTime.plusHours(selectedUseTime);
		
		// �ð��� ��ġ���� Ȯ�� 
		// ������ ���� �ð��� �ٸ� ������ ���� �ð����� �����̶�� �ߺ�, ������ ���� �ð��� �ٸ� ���� ���� �ð����� ���Ķ�� �ߺ�
	    if (selectedStartDateTime.isBefore(otherEndTime) && selectedEndDateTime.isAfter(otherStartTime)) {
	        return true;
	    } else {
	        return false;
	    }
	    
	}
	
	public void reserve() {
		int currentYear = rdUI.currentYear;
		int currentMonth = rdUI.currentMonth; // Calendar.MONTH�� 0���� �����ϹǷ� 1�� ������
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
		
		// �̿� �ð��� �Է����� �ʾ��� ��쿡 ���� ó�� 
		if ("".equals(rdUI.time_tf.getText())) {
			optionPane = new JOptionPane("�̿��� �ð��� �Է����ּ���.", JOptionPane.ERROR_MESSAGE);
			dialog = optionPane.createDialog(rdUI, "���� �ȳ�");
			dialog.setLocationRelativeTo(rdUI);
			dialog.setVisible(true);
			return;
		}
		
		// ���� �ð� ���� ���� �ð��� ���� �ϴ� ��쿡 ���� ���� ó�� 
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
		
		
		// ������ �뿡 ������ �� ������ �ð��� �̹� ������ �� �ִ� ��쿡 ���� ���� ó�� 
		Vector<ReserveBean> reservelist;
		MyInfoMgr mgr = new MyInfoMgr();
		reservelist = mgr.reserveAll();
		
		for(int i = 0; i < reservelist.size(); i++ ) {
			ReserveBean bean3 = reservelist.get(i);
			// �� ���� ��
			if(bean3.getResvroom().equals(roomInfo)) {
				// �ð� �ߺ� ���� �Ǻ� 
				if (isTimeOverlap(bean3.getResvtime(), bean3.getResvusetime())) {
					// ����ó��
					optionPane = new JOptionPane("������ ��¥�� �ð��� �̹� ������ �����մϴ�.", JOptionPane.ERROR_MESSAGE);
					dialog = optionPane.createDialog(rdUI, "���� �ߺ�");
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
		int personInfo_int = 0; // �߰� �ο� 

		// �߰� �ο� int �� ��ȯ 
		if(personInfo.equals("����")) {
			personInfo_int = 0;
		} else if(personInfo.equals("1��")) {
			personInfo_int = 1;
		} else if(personInfo.equals("2��")) {
			personInfo_int = 2;
		}
		
		int money = bean2.getMoney();
		if (money < totalPrice) {
			optionPane = new JOptionPane("�ܾ��� �����մϴ�.", JOptionPane.ERROR_MESSAGE);
			dialog = optionPane.createDialog(rdUI, "���� �ȳ�");
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
		
		// DB���� �޾ƿ� DATEŸ���� ���̺��� ������Ʈ �ϱ� ���� ������ �����ϱ� ���� ����
		SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		SimpleDateFormat newFormat2 = new SimpleDateFormat("HH:mm:ss");
		String OriginalResvtime = str;
		String timeText = rdUI.time_tf.getText();
		int resvtime = Integer.parseInt(timeText); // ��� �ð��� �޾ƿ�
		TotalBean bean4 = new TotalBean();
		try {
			Date date = originalFormat.parse(str + ":00");
			
			// getResvusetime()�� �ð����� ��ȯ�ϰ� ���ϱ�
	            Calendar calendar = Calendar.getInstance();
	            calendar.setTime(date); // Ķ������ ����Ʈ�� �޾ƿ� �ð����� ����
	            calendar.add(Calendar.HOUR_OF_DAY, resvtime); // Ķ������ ���ð� ���ϱ�
			
			String formattedDate = newFormat.format(date); // ���� ���� YY-MM-DD
			String formattedDate2 = newFormat2.format(date); // ���� ���� HH:MM:SS
			String EndformattedDate2 = newFormat2.format(calendar.getTime()); // Ķ������ ���ð��� ���ϰ� ���� �����Ͽ� ���
			
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
				optionPane = new JOptionPane("���� �ð��� 09�ú��� 23�ñ��� �Դϴ�. \n���� �ð��� Ȯ�����ּ���.", JOptionPane.ERROR_MESSAGE);
				dialog = optionPane.createDialog(rdUI, "���� �ð� ���� �ȳ�");
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
					optionPane = new JOptionPane("������ �Ϸ�Ǿ����ϴ�.", JOptionPane.INFORMATION_MESSAGE);
					dialog = optionPane.createDialog(rdUI, "���� �ȳ�");
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
		// �ؽ�Ʈ �ʵ��� ���� �����ͼ� ó��
		String timeText = rdUI.time_tf.getText();
		String roomInfo = rdUI.chosen_room_info_lb.getText();
		String personInfo = (String) rdUI.select_add_person.getSelectedItem();
		MyInfoMgr mgr = new MyInfoMgr();
		RoomBean bean = mgr.room(roomInfo);
		
		// �߰� �ο� int �� ��ȯ 
		if(personInfo.equals("����")) {
			personInfo_int = 0;
		} else if(personInfo.equals("1��")) {
			personInfo_int = 1;
		} else if(personInfo.equals("2��")) {
			personInfo_int = 2;
		}
	
		try {
			// �Է°��� �����̰ų� ������� ���� ó��
			// ���ڿ� �齺���̽� Ű�� �Է��� �� �ֵ��� �� 
			if (timeText.matches("\\d*")) {
				// ���� ó���� ���� ���ڷ� ��ȯ
				int hours = timeText.isEmpty() ? 0 : Integer.parseInt(timeText);
				 
				timePrice = hours * bean.getRperson() * 10000;
				personPrice = personInfo_int * bean.getRadd();
				totalPrice = timePrice + personPrice;
				
				// �� ������Ʈ
				rdUI.person_price_value_lb.setText(personPrice + " ��");
				rdUI.time_price_value_lb.setText(timePrice + " ��");
				rdUI.total_price_value_lb.setText(totalPrice + " ��");
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "���ڸ� �Է� �����մϴ�.");
		}
	}
}