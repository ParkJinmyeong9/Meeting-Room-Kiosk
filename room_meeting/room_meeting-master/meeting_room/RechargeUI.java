package meeting_room;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class RechargeUI extends JFrame implements ActionListener, DocumentListener {
	ImageIcon icon = new ImageIcon("meeting_room/img.png"); // 로고에 사용할 이미지 아이콘
	JLabel logo_lb = new JLabel(icon); // 로고 레이블
	
	JButton charge_1000_btn = new JButton("1000원");
	JButton charge_5000_btn = new JButton("5000원");
	JButton charge_10000_btn = new JButton("10000원");
	JButton charge_30000_btn = new JButton("30000원");
	JButton charge_50000_btn = new JButton("50000원");
	
	JLabel recharge_amount_lb = new JLabel("충전금액");
	static JTextField recharge_value_tf = new JTextField("0", 30);
	JLabel won_lb = new JLabel("원");
	
	JButton recharge_btn = new JButton("충전하기");
	static JButton cancel_btn = new JButton("취소");
	
	public RechargeUI(){
		setTitle("충전");
		setSize(800, 800);
		
		//컨텐츠 패널의 객체 메소드 호출
		Container c = getContentPane();
		c.setLayout(null); //컨텐츠 패널 초기
		
		// 로고 추가
        ImageIcon logoIcon = new ImageIcon("meeting_room/logo.jpg");
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(45, 50, 700, 270);
        c.add(logoLabel);
		
        recharge_value_tf.setText("0");
        
		Font font = new Font("Dialog", Font.PLAIN, 18);
		
		charge_1000_btn.setBounds(30, 410, 120, 70);
		charge_5000_btn.setBounds(180, 410, 120, 70);
		charge_10000_btn.setBounds(330, 410, 120, 70);
		charge_30000_btn.setBounds(480, 410, 120, 70);
		charge_50000_btn.setBounds(630, 410, 120, 70);

		won_lb.setBounds(485, 530, 50, 35);
		recharge_amount_lb.setBounds(250, 530, 120, 35);
		recharge_value_tf.setBounds(350, 530, 120, 35);
		recharge_btn.setBounds(250, 600, 120, 70);
		cancel_btn.setBounds(400, 600, 120, 70);
		
		// 텍스트 필드를 우측으로 정렬
		recharge_value_tf.setHorizontalAlignment(SwingConstants.RIGHT);
		recharge_amount_lb.setFont(font);
		won_lb.setFont(font);
		
		recharge_btn.setEnabled(false);
		recharge_value_tf.getDocument().addDocumentListener(this);
		
		c.add(logo_lb);
		c.add(charge_1000_btn);
		c.add(charge_5000_btn);
		c.add(charge_10000_btn);
		c.add(charge_30000_btn);
		c.add(charge_50000_btn);
		c.add(recharge_amount_lb);
		c.add(recharge_value_tf);
		c.add(won_lb);
		c.add(recharge_btn);
		c.add(cancel_btn);
		
		charge_1000_btn.addActionListener(this);
		charge_5000_btn.addActionListener(this);
		charge_10000_btn.addActionListener(this);
		charge_30000_btn.addActionListener(this);
		charge_50000_btn.addActionListener(this);	
		cancel_btn.addActionListener(this);
		recharge_btn.addActionListener(this);
		
		
		recharge_value_tf.addKeyListener(new KeyAdapter() {
		    @Override
		    // 텍스트 필드에 숫자가 아닌 다른 입력을 하는 경우 무시 
		    public void keyTyped(KeyEvent e) {
		        char inputChar = e.getKeyChar();
		        if (!Character.isDigit(inputChar)) {
		            e.consume(); // 입력된 값이 숫자가 아니면 무시
		        }
		        //updateRechargeAmountOnTextChange();
		    }
		    
//		    @Override
//		    // 텍스트 필드가 0으로 시작하는 숫자열일때 앞자리의 0 들을 제거 
//		    public void keyReleased(KeyEvent e) {
//		        updateRechargeAmountOnTextChange();
//		    }
//		    
		    
		});

		//화면 중앙에 오게 설정
		setLocationRelativeTo(null);
		//현재 창만 종료
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setVisible(true);
		setResizable(false);
	}
	
	
	
	// 텍스트 필드의 값이 바뀔때마다 충전하기 버튼을 활성화 할지 체크 
	// DocumentListener 메서드 구현
	@Override
	public void changedUpdate(DocumentEvent e) {
		checkAndEnableButton();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
	    checkAndEnableButton();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
	    checkAndEnableButton();
	}

	private void checkAndEnableButton() {
		// 텍스트 필드의 값이 숫자이면서 0이 아닌지, 또는 공백이 아닌지 체크하여 버튼 활성화
		String textFieldValue = recharge_value_tf.getText().trim();
		
		recharge_btn.setEnabled(isNumeric(textFieldValue) && !textFieldValue.equals("0") && !textFieldValue.isEmpty());
		}

//	// 텍스트 필드에 직접 입력할 때 호출되는 메소드
//	private void updateRechargeAmountOnTextChange() {
//	    String currentText = recharge_value_tf.getText().trim();
//
//	    // 텍스트 필드 값이 0으로 시작하는 경우 앞자리 0 제거
//        if (currentText.startsWith("0")) {
//            currentText = currentText.replaceFirst("^0+(?!$)", "");
//        }
//	    
//	    if (currentText.equals("")) {
//	        recharge_btn.setEnabled(false);
//	    } else {
//	        recharge_btn.setEnabled(true);
//	    }
//	}
	
	// 숫자 여부 체크 메서드 추가
	private boolean isNumeric(String str) {
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if (obj == charge_1000_btn) {
			updateRechargeAmount(1000);
		} else if (obj == charge_5000_btn) {
			updateRechargeAmount(5000);
		} else if (obj == charge_10000_btn) {
			updateRechargeAmount(10000);
		} else if (obj == charge_30000_btn) {
			updateRechargeAmount(30000);
		} else if (obj == charge_50000_btn) {
			updateRechargeAmount(50000);
		} else if (obj == cancel_btn) {
			dispose();
		} else if (obj == recharge_btn) {
			if(Integer.parseInt(recharge_value_tf.getText()) == 0) {
				JOptionPane.showMessageDialog(this, "충전 금액이 0원입니다. 충전 금액을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
		        return;
			}
			RechargeDetailUI rcdui = new RechargeDetailUI();
		}
	}
	
	
	// 공백으로 만들경우 0으로 만들지 않고 다른 문자열을 출력하는 오류 해결 불가
	
	// recharge_value_tf 필드가 공백일 경우에도 버튼을 클릭하면 해당 버튼의 금액만큼 tf를 변경하는 코드로 대체 
	private void updateRechargeAmount(int money) {
		if(recharge_value_tf.getText().equals("")) {
			recharge_value_tf.setText(String.valueOf(money));
		}
		
		else {
			int currentAmount = Integer.parseInt(recharge_value_tf.getText());
			currentAmount += money;
			recharge_value_tf.setText(Integer.toString(currentAmount));
		}
	}
	
	public static void main(String[] args) {
		
	}
}