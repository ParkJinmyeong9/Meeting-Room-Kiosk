package meeting_room;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginUI extends JFrame implements ActionListener, ItemListener, KeyListener {
	public static String ID;
	
	//요소생성
	JLabel id_lb = new JLabel("아이디");	
	JLabel pw_lb = new JLabel("비밀번호");
	JTextField id_tf = new JTextField(20);
	JTextField pw_tf = new JPasswordField(20);
	JButton signup_btn = new JButton("회원가입");
	JButton login_btn = new JButton("로그인");
	JCheckBox check_admin = new JCheckBox("관리자 로그인");
	
	int check_action = 1;
	private Statement stmt;
	private DBConnection pool;

	public LoginUI() {
		setTitle("로그인");
		setSize(800, 630);

		//컨텐츠 패널의 객체 메소드 호출
		Container c = getContentPane();
		c.setLayout(null);  //컨텐츠 패널 초기화
		
		// 로고 추가
		ImageIcon logoIcon = new ImageIcon("meeting_room/logo.jpg");
		JLabel logoLabel = new JLabel(logoIcon);
		logoLabel.setBounds(45, 20, 700, 270);
		c.add(logoLabel);
		
		//요소 위치 지정
		id_lb.setBounds(255,  322,  100,  25);
		pw_lb.setBounds(255,  358,  100,  25);
		id_tf.setBounds(365, 322, 170, 25);
		pw_tf.setBounds(365, 358, 170, 25);
		signup_btn.setBounds(243, 440, 130, 70);
		login_btn.setBounds(401, 440, 130, 70);
		check_admin.setBounds(250, 394, 250, 25);
		
		//Font 지정
		Font font = new Font("Dialog", Font.BOLD, 13); // 폰트굵게
		Font font2 = new Font("Dialog", Font.BOLD, 22);
		
		id_lb.setFont(font2);
		pw_lb.setFont(font2);
		check_admin.setFont(font);
		
		//버튼 이벤트 추가
		signup_btn.addActionListener(this);
		login_btn.addActionListener(this);
		check_admin.addItemListener(this);
		id_tf.addKeyListener(this);
		pw_tf.addKeyListener(this);
		
		//요소 추가
		c.add(id_lb);
		c.add(pw_lb);
		c.add(id_tf);
		c.add(pw_tf);
		c.add(signup_btn);
		c.add(login_btn);
		c.add(check_admin);
		
		//화면 중앙에 오게 설정
		//프로그램 종료할 때 프로세스까지 함께 종료
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		setVisible(true);
		setResizable(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if(obj == signup_btn) {//회원가입 버튼 동작
			SignUpUI suu = new SignUpUI();
		} else if(obj == login_btn ) {
			Login();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//엔터키로 로그인 실행
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			Login();
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void Login() {
		try {
			pool = DBConnection.getInstance();
			Connection con = pool.getConnection();
			stmt = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			String id = id_tf.getText().trim();
			byte[] password = SeedEncoding.encrypt(pw_tf.getText().trim());
			String pw = new String(password);
			ResultSet rs = stmt.executeQuery("SELECT * FROM member WHERE member_id='" + id + "' AND member_pw=('" + pw + "') AND member_level='" + check_action + "'");
			if (rs.next()) {
				ID = id;
				JOptionPane.showMessageDialog(this, "로그인 성공!");
				if (check_action == 1) { //일반 사용자
					MainPageUI mainpage = new MainPageUI();
				} else if (check_action == 2) {
					AdminMainPageUI admin = new AdminMainPageUI();
				}
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "로그인 실패. 다시 시도하세요.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			//체크박스가 선택되면 실행되는 코드
			check_action = 2;
		} else {
			//체크박스가 선택 해제되면 실행되는 코드
			check_action = 1;
		}
	}
	
	public static void main(String[] args) {
		LoginUI login = new LoginUI();
	}
}