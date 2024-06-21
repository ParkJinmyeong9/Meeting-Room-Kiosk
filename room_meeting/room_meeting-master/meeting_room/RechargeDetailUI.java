package meeting_room;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class RechargeDetailUI extends JFrame implements ActionListener {
	
	JLabel recharge_check = new JLabel("충전 확인");
	JLabel amount = new JLabel("충전 금액 : ");
	JLabel amount_value = new JLabel("value");
	JLabel pw_check = new JLabel("비밀번호 확인 ");
	JTextField pw_check_tf = new JPasswordField(20);
	JLabel recharge_check_1 = new JLabel("정말 충전하시겠습니까?");
	JLabel won_lb = new JLabel("원");
	
	JButton recharge = new JButton("충전");
	JButton cancel = new JButton("취소");
	
	MyInfoMgr mgr;
	int money;
	String id;
	
	public RechargeDetailUI() {
		setTitle("충전 상세");
		setSize(400, 500);
		
		// RechargeUI.recharge_value_tf.getText()의 값에서 0으로 시작하는 숫자열의 앞자리 0 제거
	    String rechargeValue = RechargeUI.recharge_value_tf.getText().replaceFirst("^0+(?!$)", "");
	    amount_value.setText(rechargeValue);
		
		//컨텐츠 패널의 객체 메소드 호출
		Container c = getContentPane();
		c.setLayout(null); //컨텐츠 패널 초기
		
		Font font = new Font("Dialog", Font.PLAIN, 18);
		Font font2 = new Font("Dialog", Font.PLAIN, 28);
		
		recharge_check.setFont(font2);
		recharge_check_1.setFont(font2);
		amount.setFont(font);
		amount_value.setFont(font);
		pw_check.setFont(font);
		recharge.setFont(font);
		cancel.setFont(font);
		won_lb.setFont(font);
		
		recharge_check.setForeground(Color.RED);
		recharge_check_1.setForeground(Color.RED);
		amount_value.setForeground(Color.MAGENTA);
		
		recharge_check.setBounds(120, 30, 200, 30);
		amount.setBounds(100, 80, 100, 25);
		amount_value.setBounds(200, 80, 100, 25);
		won_lb.setBounds(260, 80, 100, 25);
		pw_check.setBounds(65, 120, 150, 25);
		pw_check_tf.setBounds(200, 120, 100, 25);
		recharge_check_1.setBounds(35, 200, 350, 30);
		recharge.setBounds(50, 300, 130, 100);
		cancel.setBounds(210, 300, 130, 100);
		

		c.add(recharge_check);
		c.add(amount);
		c.add(amount_value);
		c.add(pw_check);
		c.add(pw_check_tf);
		c.add(recharge_check_1);
		c.add(recharge);
		c.add(cancel);
		c.add(won_lb);
		
		recharge.addActionListener(this);
		cancel.addActionListener(this);
		
		//화면 중앙에 오게 설정
		setLocationRelativeTo(null);
		//현재 창만 종료
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setVisible(true);
		setResizable(false);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == recharge) {
			id = LoginUI.ID;
			mgr = new MyInfoMgr();
			MyInfoBean bean = mgr.select(id);
			money = bean.getMoney();
			
			byte[] password = SeedEncoding.encrypt(pw_check_tf.getText().trim());
			String pw = new String(password);
			if (!pw.equals(bean.getPW())) {
				JOptionPane.showMessageDialog(null, "비밀번호가 틀렸습니다. 다시 한 번 확인해주세요.");
				return;
			}
			
			bean.setID(id);
			bean.setMoney(Integer.parseInt(amount_value.getText()) + money);
			if(mgr.charge(bean)) {
				JOptionPane.showMessageDialog(null, "충전되었습니다.");
				mgr.select(bean.getID());
				MainPageUI.balance_value_lb.setText(String.valueOf(bean.getMoney()) + "원");
				RechargeUI.recharge_value_tf.setText("0");
				RechargeUI.cancel_btn.doClick();
				dispose();
			}
		} else if (e.getSource() == cancel) {
			dispose();
		}
	}
	
	public static void main(String[] args) {
		
	}
}
