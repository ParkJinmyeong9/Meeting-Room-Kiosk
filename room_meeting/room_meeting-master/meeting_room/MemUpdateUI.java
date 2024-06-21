package meeting_room;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class MemUpdateUI extends JFrame implements ActionListener {
	
	MyInfoMgr mgr;
	MyInfoBean bean = new MyInfoBean();
	
	String id;
	String pw;
	String name;
	String phone;
	
	JLabel id_lb = new JLabel("아이디");
	JLabel name_lb = new JLabel("이름");
	JLabel phone_lb = new JLabel("전화번호");
	JTextField id_tf = new JTextField("DefaultID", 10);
	JTextField name_tf = new JTextField("UserName", 10);
	JTextField phone_tf = new JTextField("PhoneNum", 10);
	JButton modify_info_btn = new JButton("정보 수정");
	JButton ok_btn = new JButton("확인");
	
	public MemUpdateUI(String id) {
		this.id = id;

		setTitle("회원 정보 수정");
		setSize(400, 350);

		//정보 저장 id, pw, name, phone(tel)
		mgr = new MyInfoMgr();
		bean = mgr.select(this.id);
		pw = bean.getPW();
		name = bean.getName();
		phone = bean.getPhone();
		
		id_tf.setText(id);
		name_tf.setText(name);
		phone_tf.setText(phone);
		
		//컨텐츠 패널의 객체 메소드 호출
		Container c = getContentPane();
		c.setLayout(null); //컨텐츠 패널 초기
		
		//요소 위치 지정
		id_lb.setBounds(85,  30,  100,  40);
		name_lb.setBounds(85,  80,  100,  40);
		phone_lb.setBounds(85,  130,  100,  40);
		id_tf.setBounds(180, 30, 100, 40);
		name_tf.setBounds(180, 80, 100, 40);
		phone_tf.setBounds(180, 130, 100, 40);
		modify_info_btn.setBounds(85, 200, 90, 70);
		ok_btn.setBounds(195, 200, 90, 70);
		
		id_tf.setEnabled(false);
		
		//폰트 설정
		Font font = new Font("Dialog", Font.BOLD, 18);
		id_lb.setFont(font);
		name_lb.setFont(font);
		phone_lb.setFont(font);
		
		//이벤트 추가
		modify_info_btn.addActionListener(this);
		ok_btn.addActionListener(this);
		
		//요소 추가
		c.add(id_lb);
		c.add(name_lb);
		c.add(phone_lb);
		c.add(id_tf);
		c.add(name_tf);
		c.add(phone_tf);
		c.add(modify_info_btn);
		c.add(ok_btn);		
		
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
			if (!id_tf.getText().equals(id)) {
				JOptionPane.showMessageDialog(this, "아이디는 변경할 수 없습니다.");
			} else if (name_tf.getText().equals(name) && phone_tf.getText().equals(phone)) {
				JOptionPane.showMessageDialog(this, "변경할 정보가 없습니다.");
			} else if ("".equals(name_tf.getText()) || "".equals(phone_tf.getText())) {
				JOptionPane.showMessageDialog(this, "공백으로 정보를 변경할 수 없습니다.");
			} else {
				change();
			}
		} else if (obj == ok_btn) {
			dispose();
		}
	}
	
	public void change() {
		String name = name_tf.getText().trim();
		String phone = phone_tf.getText().trim();
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
			name = bean.getName();
			phone = bean.getPhone();
			JOptionPane.showMessageDialog(this, "정보가 변경되었습니다.");
			dispose();
			MemberInfoUI.jb.doClick();
		}
	}
	
	//전화번호 입력 값을 000-0000-0000 형식으로 변경
	public String phone_format(String number) {
	      String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
	      return number.replaceAll(regEx, "$1-$2-$3");
	}
}