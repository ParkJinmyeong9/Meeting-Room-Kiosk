package meeting_room;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Encoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MyInfoUI extends JFrame implements ActionListener {
	
	MyInfoMgr mgr;
	MyInfoBean bean = new MyInfoBean();
	
	String id;
	String pw;
	String name;
	String phone;
	int amount;
	
	JLabel id_lb = new JLabel("아이디");
	JLabel pw_lb = new JLabel("비밀번호");
	JLabel name_lb = new JLabel("이름");
	JLabel phone_lb = new JLabel("전화번호");
	
	JLabel pwcheck_lb = new JLabel("비밀번호 확인");
	JLabel amount_lb = new JLabel("잔액");
	
	JTextField id_tf = new JTextField("DefaultID", 10);
	JTextField pw_tf = new JPasswordField(10);
	JTextField name_tf = new JTextField("UserName", 10);
	JTextField phone_tf = new JTextField("PhoneNum", 10);
	
	JTextField pwcheck_tf = new JPasswordField(10);
	JTextField amount_tf = new JTextField(10);
	
	JButton my_usage_btn = new JButton("이용 내역");
	JButton modify_info_btn = new JButton("정보 수정");
	JButton ok_btn = new JButton("확인");
	
	public String getID(MyInfoBean bean) {
		return bean.getID();
	}
	
	public MyInfoUI() {		
		setTitle("내 정보");
		setSize(380, 580);

		id = LoginUI.ID;
		mgr = new MyInfoMgr();
		bean = mgr.select(id);
		pw = bean.getPW();
		name = bean.getName();
		phone = bean.getPhone();
		amount = bean.getMoney();
		
		id_tf.setText(id);
		name_tf.setText(name);
		phone_tf.setText(phone);
		amount_tf.setText(String.valueOf(amount));
		
		id_tf.setEnabled(false);
		amount_tf.setEnabled(false);
		
		//컨텐츠 패널의 객체 메소드 호출
		Container c = getContentPane();
		c.setLayout(null); //컨텐츠 패널 초기
		
		id_lb.setBounds(85,  50,  100,  40);
		pw_lb.setBounds(85,  100,  100,  40);
		pwcheck_lb.setBounds(85, 150, 100, 40);
		name_lb.setBounds(85,  200,  100,  40);
		phone_lb.setBounds(85,  250,  100,  40);
		amount_lb.setBounds(85, 300, 100, 40);
		
		id_tf.setForeground(Color.RED);
		amount_tf.setForeground(Color.RED);
		
		id_tf.setBounds(180, 50, 100, 40);
		pw_tf.setBounds(180, 100, 100, 40);
		pwcheck_tf.setBounds(180, 150, 100, 40);
		name_tf.setBounds(180, 200, 100, 40);
		phone_tf.setBounds(180, 250, 100, 40);
		amount_tf.setBounds(180, 300, 100, 40);
		
		my_usage_btn.setBounds(85, 360, 200, 50);
		modify_info_btn.setBounds(85, 430, 90, 50);
		ok_btn.setBounds(195, 430, 90, 50);
		
		Font font = new Font("Dialog", Font.BOLD, 18);
		Font font2 = new Font("Dialog", Font.BOLD, 14);
		
		id_lb.setFont(font);
		pw_lb.setFont(font);
		pwcheck_lb.setFont(font2);
		name_lb.setFont(font);
		phone_lb.setFont(font);
		amount_lb.setFont(font);
		
		id_tf.setFont(font2);
		amount_tf.setFont(font2);
		
		my_usage_btn.addActionListener(this);
		modify_info_btn.addActionListener(this);
		ok_btn.addActionListener(this);
		
		c.add(id_lb);
		c.add(pw_lb);
		c.add(name_lb);
		c.add(phone_lb);
		
		c.add(id_tf);
		c.add(pw_tf);
		c.add(name_tf);
		c.add(phone_tf);
		
		c.add(my_usage_btn);
		c.add(modify_info_btn);
		c.add(ok_btn);	
		
		c.add(pwcheck_lb);
		c.add(pwcheck_tf);
		c.add(amount_lb);
		c.add(amount_tf);
		
		//화면 중앙에 오게 설정
		setLocationRelativeTo(null);
		//프로그램 종료할 때 현재 창만 종료
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
								
		setVisible(true);
		setResizable(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if (obj == modify_info_btn) {
			byte[] password = SeedEncoding.encrypt(pw_tf.getText());
			if (!id_tf.getText().equals(id)) {
				JOptionPane.showMessageDialog(this, "아이디는 변경할 수 없습니다.");
			} else if (pw_tf.getText().equals("")){
				JOptionPane.showMessageDialog(this, "비밀번호를 입력하셔야 정보를 변경할 수 있습니다.");
			} else if (!(pw_tf.getText().equals(pwcheck_tf.getText()))){
				JOptionPane.showMessageDialog(this, "비밀번호와 비밀번호 확인에 입력한 비밀번호가 서로 다릅니다.");
			} else if (name_tf.getText().equals(name) && phone_tf.getText().equals(phone) && new String(password).equals(pw)) {
				JOptionPane.showMessageDialog(this, "변경할 정보가 없습니다.");
			} else if ("".equals(name_tf.getText()) || "".equals(phone_tf.getText()) || "".equals(pw_tf.getText()) || "".equals(pwcheck_tf.getText())) {
				JOptionPane.showMessageDialog(this, "공백으로 정보를 변경할 수 없습니다.");
			} else {
				change();
			}
		} else if (obj == ok_btn) {
			dispose();
		} else if (obj == my_usage_btn) {
			UsageHistoryUI UHUI = new UsageHistoryUI();
		}
	}
	
	public void change() {
		String pw = pw_tf.getText().trim();
		String name = name_tf.getText().trim();
		String phone = phone_tf.getText().trim();
		
		byte[] password = SeedEncoding.encrypt(pw);
		bean.setPW(new String(password));
		
		phone = phone_format(phone);
		if ("".equals(name) || "".equals(phone)) {
			JOptionPane.showMessageDialog(this, "공백인 칸을 입력해주세요.");
			return;
		} else if (phone.length() != 13) {
			JOptionPane.showMessageDialog(this, "11자리 휴대전화 번호를 제대로 입력해주세요.");
			return;
		}
		bean.setName(name);
		bean.setPhone(phone);
		if(mgr.update(bean)) {
			pw = bean.getPW();
			name = bean.getName();
			phone = bean.getPhone();
			JOptionPane.showMessageDialog(this, "정보가 변경되었습니다.");
			MainPageUI.name_value_lb.setText(name);
			dispose();
		}
	}
	
	//전화번호 입력 값을 000-0000-0000 형식으로 변경
	public String phone_format(String number) {
	      String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
	      return number.replaceAll(regEx, "$1-$2-$3");
	}

	public static void main(String[] args) {
		
	}
}