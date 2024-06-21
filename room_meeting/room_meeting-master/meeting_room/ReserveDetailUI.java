package meeting_room;

import java.awt.Color;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AbstractDocument;

public class ReserveDetailUI extends JFrame implements ActionListener {
	private ReserveDetail rsvdetail;

	JLabel reserve_lb = new JLabel("미팅룸 예약하기 상세정보");
	JLabel chosen_room_lb = new JLabel("선택된 룸  ");
	JLabel chosen_room_info_lb;
	JLabel time_lb = new JLabel("사용 시간  ");
	JLabel add_person_lb = new JLabel("추가 인원 ");
	JLabel start_date_lb = new JLabel("예약 날짜 ");
	JLabel start_time_lb = new JLabel("시작 시간 ");
	JLabel start_hour_lb = new JLabel("시");
	JLabel start_min_lb = new JLabel("분");
	JLabel time_price_lb = new JLabel("시간 금액");
	JLabel person_price_lb = new JLabel("인원 금액");
	JLabel time_price_value_lb = new JLabel("0 원");
	JLabel person_price_value_lb = new JLabel("0 원");
	JLabel total_price_lb = new JLabel("총 금액");
	JLabel total_price_value_lb = new JLabel("0 원");
	
	JTextField time_tf = new JTextField(1); // 시간 입력받을 텍스트 필드
	// JLabel selected_add_person;  추가 인원 선택 레이블 
	
	String items[] = {"없음", "1명", "2명"}; // 콤보박스에 들어갈 항목
	JComboBox<String> select_add_person = new JComboBox<>(items);
	
	JButton payment_btn = new JButton("결제");
	JButton cancel_btn = new JButton("취소");
	
	JLabel hour_lb = new JLabel("시간");
	
	
	// Calendar 객체 생성 및 현재 시간 설정
	Calendar cal = Calendar.getInstance();
	int currentHour = cal.get(Calendar.HOUR_OF_DAY);
	int currentMinute = cal.get(Calendar.MINUTE);
	
	// SpinnerNumberModel을 사용하여 JSpinner 초기화 (9부터 시작, 24까지) 시를 조정
	// 현재 시간의 '시' 를 디폴트로 
	SpinnerNumberModel spinnerNumberModel1 = new SpinnerNumberModel(currentHour, 9, 24, 1);
	JSpinner hourSpinner = new JSpinner(spinnerNumberModel1);
	
	// SpinnerNumberModel을 사용하여 JSpinner 초기화 (0부터 시작, 60까지) 분을 조정
	SpinnerNumberModel spinnerNumberModel2 = new SpinnerNumberModel((currentMinute / 10 * 10) + 10, 0, 60, 10);
	JSpinner minSpinner = new JSpinner(spinnerNumberModel2);
	
	// 현재 날짜를 기본값으로 설정
	int currentYear = cal.get(Calendar.YEAR);
	int currentMonth = cal.get(Calendar.MONTH) + 1; // Calendar.MONTH는 0부터 시작하므로 1을 더해줌
	int currentDay = cal.get(Calendar.DAY_OF_MONTH);
	
	SpinnerNumberModel spinnerNumberModel3 = new SpinnerNumberModel(currentYear, currentYear, 2030, 1);
	JSpinner yearSpinner = new JSpinner(spinnerNumberModel3);
	
	SpinnerNumberModel spinnerNumberModel4 = new SpinnerNumberModel(currentMonth, 1, 13, 1);
	JSpinner monthSpinner = new JSpinner(spinnerNumberModel4);
	
	SpinnerNumberModel spinnerNumberModel5 = new SpinnerNumberModel(currentDay, 1, 32, 1);
	JSpinner daySpinner = new JSpinner(spinnerNumberModel5);
	
	public ReserveDetailUI(String selectedRoomInfo) {
		setTitle("미팅룸 예약하기 상세정보");
		setSize(400, 600);
		
		//컨텐츠 패널의 객체 메소드 호출
		Container c = getContentPane();
		c.setLayout(null); //컨텐츠 패널 초기
		
		reserve_lb.setBounds(35,  10,  500,  40);
		chosen_room_lb.setBounds(80,  70,  90,  25);
		chosen_room_info_lb = new JLabel(selectedRoomInfo);
		chosen_room_info_lb.setBounds(180, 70, 90, 25);
		time_lb.setBounds(80, 110, 90, 25);
		time_tf.setBounds(180, 110, 30, 25);
		add_person_lb.setBounds(80, 150, 90, 25);
		select_add_person.setBounds(180, 150, 90, 25);
		start_time_lb.setBounds(80, 190, 90, 25);
		start_date_lb.setBounds(80, 230, 90, 25);
		
		hour_lb.setBounds(220, 110, 50, 25);
		start_hour_lb.setBounds(235, 190, 50, 25);
		start_min_lb.setBounds(315, 190, 50, 25);
		
		if ((int)minSpinner.getValue() == 60) {
			hourSpinner.setValue((int)hourSpinner.getValue() + 1);
			minSpinner.setValue(0);
		}
		
		// DocumentFilter를 사용하여 텍스트 필드의 입력을 제한
		((AbstractDocument) time_tf.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
			throws BadLocationException {
				// 입력값이 숫자이거나 백스페이스 키일 때만 처리, 최대 입력 자릿수를 1자리로 제한 
				if ((text == null || text.matches("\\d")) && fb.getDocument().getLength() + (text == null ? 0 : text.length()) - length <= 1) {
					super.replace(fb, offset, length, text, attrs);
					// 여기서 금액 계산 등 추가 작업을 할 수 있습니다.
					updatePrice(); // 예시로 추가된 메서드 호출
				}
			}
		});
		
		// hourSpinner 값 변경 이벤트 리스너 추가
		hourSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int newValue = (int) hourSpinner.getValue();
				int newValue2 = (int) minSpinner.getValue();
				if ((newValue == 23 && newValue2 > 0)) {
					hourSpinner.setValue(9);
					minSpinner.setValue(0);
				} else if (newValue > 23) {
					hourSpinner.setValue(9); // 24를 초과하면 9로 변경
				}
			}
		});
			
		// minSpinner 값 변경 이벤트 리스너 추가
		minSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int newValue = (int) minSpinner.getValue();
				int newValue2 = (int) hourSpinner.getValue();
				if (newValue >= 60) {
					if (newValue2 == 22) {
						hourSpinner.setValue(9);
						minSpinner.setValue(0); // 60 이상이면 0으로 변경
					} else {
						minSpinner.setValue(0); // 60 이상이면 0으로 변경
						hourSpinner.setValue(newValue2 + 1);
					}
				}
			}
		});
		
		// monthSpinner 값 변경 이벤트 리스너 추가
		monthSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int newValue = (int) monthSpinner.getValue();
				if (newValue > 12) {
					monthSpinner.setValue(1); // 12를 초과하면 1로 변경
				}
			}
		});
		
		// daySpinner 값 변경 이벤트 리스너 추가
		daySpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int newValue = (int) daySpinner.getValue();
				if (newValue > 31) {
					daySpinner.setValue(1); // 31을 초과하면 1로 변경
				}
			}
		});
		
		// 텍스트 필드의 값이 바뀔 때 마다 time_price_value_lb 값 갱신 
		time_tf.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
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
		
		// 콤보 박스의 값이 변경될 때 마다 
		select_add_person.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updatePrice(); // 콤보박스 값이 변경될 때마다 updatePrice 메서드 호출
			}
		});
	
		hourSpinner.setBounds(180, 190, 50, 30);
		minSpinner.setBounds(260, 190, 50, 30);
		
		yearSpinner.setBounds(180, 230, 50, 30);
		monthSpinner.setBounds(240, 230, 50, 30);
		daySpinner.setBounds(300, 230, 50, 30);
		
		time_price_lb.setBounds(80, 320, 90, 25);
		time_price_value_lb.setBounds(180, 320, 90, 25);
		person_price_lb.setBounds(80, 360, 90, 25);
		person_price_value_lb.setBounds(180, 360, 90, 25);
		total_price_lb.setBounds(80, 400, 90, 25);
		total_price_value_lb.setBounds(180, 400, 90, 25);
		
		payment_btn.setBounds(40, 460, 140, 60);
		cancel_btn.setBounds(200, 460, 140, 60);
		
		rsvdetail = new ReserveDetail(this);
		
		payment_btn.addActionListener(rsvdetail);
		cancel_btn.addActionListener(rsvdetail);
		
		//Font 지정
		Font font = new Font("Dialog", Font.BOLD, 25); // 폰트굵게
		Font font2 = new Font("Dialog", Font.PLAIN, 17);
		
		reserve_lb.setFont(font);
		chosen_room_lb.setFont(font2);
		chosen_room_info_lb.setFont(font2);
		chosen_room_info_lb.setForeground(Color.RED);
		time_lb.setFont(font2);
		add_person_lb.setFont(font2);
		select_add_person.setFont(font2);
		start_time_lb.setFont(font2);
		start_date_lb.setFont(font2);
		
		time_price_lb.setFont(font2);
		time_price_value_lb.setFont(font2);
		person_price_lb.setFont(font2);
		person_price_value_lb.setFont(font2);
		total_price_lb.setFont(font2);
		total_price_value_lb.setFont(font2);
		
		payment_btn.setFont(font2);
		cancel_btn.setFont(font2);
		
		hour_lb.setFont(font2);
		start_hour_lb.setFont(font2);
		start_min_lb.setFont(font2);
		
		c.add(reserve_lb);
		c.add(chosen_room_lb);
		c.add(chosen_room_info_lb);
		c.add(time_lb);
		c.add(time_tf);
		c.add(add_person_lb);
		c.add(select_add_person);
		c.add(start_time_lb);
		c.add(start_date_lb);
		
		c.add(time_price_lb);
		c.add(time_price_value_lb);
		c.add(person_price_lb);
		c.add(person_price_value_lb);
		c.add(total_price_lb);
		c.add(total_price_value_lb);
		c.add(payment_btn);
		c.add(cancel_btn);
		c.add(hour_lb);
		c.add(start_hour_lb);
		c.add(start_min_lb);
		c.add(hourSpinner);
		c.add(minSpinner);
		c.add(yearSpinner);
		c.add(monthSpinner);
		c.add(daySpinner);
		
		//화면 중앙에 오게 설정
		setLocationRelativeTo(null);
		//프로그램 종료할 때 프로세스까지 함께 종료
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setVisible(true);
		setResizable(false);
		
		// JSpinner에 사용되는 DefaultFormatter 가져오기
		JFormattedTextField.AbstractFormatter formatter = ((JSpinner.DefaultEditor) yearSpinner.getEditor()).getTextField().getFormatter();
	
		// AllowsInvalid를 false로 설정하여 유효하지 않은 값을 허용하지 않도록 함
		if (formatter instanceof DefaultFormatter) {
			((DefaultFormatter) formatter).setAllowsInvalid(false);
		}
		
		// JSpinner의 에디터를 변경하여 쉼표가 없는 형식으로 표시되도록 함
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(yearSpinner, "####");
		yearSpinner.setEditor(editor);
	}
	
	private void updatePrice() {} // 시간을 입력할때 업데이트할 메서드 
	
	@Override
	public void actionPerformed(ActionEvent e) {
	}
	
	public static void main(String[] args) {
		
	}
}