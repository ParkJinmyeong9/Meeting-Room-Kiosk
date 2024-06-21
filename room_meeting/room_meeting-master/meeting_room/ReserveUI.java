package meeting_room;

import java.awt.Color;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ReserveUI extends JFrame implements ActionListener{
	private Reserve reserve;
	String id;
	
	JLabel reserve_lb = new JLabel("미팅룸 예약하기");
	JLabel room2_lb = new JLabel("2인");
	JLabel room4_lb = new JLabel("4인");
	JLabel room8_lb = new JLabel("8인");
	
	JLabel memberinfo_lb = new JLabel("회원정보");	
	JLabel name_lb = new JLabel("이름 : ");
	JLabel name_value_lb = new JLabel("집가고싶다");
	JLabel balance_lb = new JLabel("잔액 : ");
	JLabel balance_value_lb = new JLabel("<TEST> 원");
	
	static JButton a = new JButton("");
	
	JButton reserve_btn = new JButton("예약");
	JButton cancel_btn = new JButton("취소");
	
	JLabel chosen_room_lb = new JLabel("선택된 룸 ");
	JLabel chosen_room_value_lb = new JLabel("   ");
	JLabel chosen_room_lb2 = new JLabel("의 예약 정보");
	
	Container c = getContentPane();
	
	
	JTable reserveTable;
    JScrollPane reservePane;
 
	public ReserveUI() {
		setTitle("미팅룸 예약하기");
		setSize(900, 800);
		
		c.setLayout(null);
		
		id = LoginUI.ID;
		
		name_value_lb.setText(MainPageUI.name_value_lb.getText());
		balance_value_lb.setText(MainPageUI.balance_value_lb.getText());
		
		reserve_lb.setBounds(5,2,500,40);
		room2_lb.setBounds(65, 75, 100, 20);
		room4_lb.setBounds(265, 75, 100, 20);
		room8_lb.setBounds(465, 75, 100, 20);
		
		memberinfo_lb.setBounds(640,13,100,40);
		name_lb.setBounds(640,50,100,40);	
		name_value_lb.setBounds(690, 50, 100, 40);
		balance_lb.setBounds(640, 87, 100, 40);
		balance_value_lb.setBounds(690, 87, 150, 40);
		
		reserve_btn.setBounds(655, 150, 160, 85);
		cancel_btn.setBounds(655, 250, 160, 85);
		
		chosen_room_lb.setBounds(450, 400, 100, 40);
		chosen_room_value_lb.setBounds(550, 400, 100, 40);
		chosen_room_value_lb.setForeground(Color.RED);
		chosen_room_lb2.setBounds(600, 400, 110, 40);
		
		reserve = new Reserve(this);
		
		reserve_btn.addActionListener(reserve);
		cancel_btn.addActionListener(reserve);
		a.addActionListener(this);
		
		//Font 지정
		Font font = new Font("Dialog", Font.BOLD, 30); // 폰트굵게
		Font font2 = new Font("Dialog", Font.PLAIN, 23); 
		Font font3 = new Font("Dialog", Font.PLAIN, 18);
		
		reserve_lb.setFont(font);
		room2_lb.setFont(font2);
		room4_lb.setFont(font2);
		room8_lb.setFont(font2);
		memberinfo_lb.setFont(font3);
		name_lb.setFont(font3);
		name_value_lb.setFont(font3);;
		balance_lb.setFont(font3);
		balance_value_lb.setFont(font3);
		reserve_btn.setFont(font2);
		reserve_btn.setEnabled(false); // 예약 버튼은 미팅룸을 선택햐야 활성화 되도록 설정
		cancel_btn.setFont(font2);
		chosen_room_lb.setFont(font3);
		chosen_room_value_lb.setFont(font3);
		chosen_room_lb2.setFont(font3);
		
		
		Vector<RoomBean> vlist;
		MyInfoMgr mgr = new MyInfoMgr();
		vlist = mgr.roomAll();
		
		int startY = 110;

		for(int i = 0; i < vlist.size(); i++) {
			RoomBean bean = vlist.get(i);
			JButton button = new JButton(bean.getRoom());
			button.addActionListener(reserve);
			
			if (i < 4) {
				button.setBounds(5, startY, 160, 85);
				startY += 100;
			} else if (4<= i && i < 10) {
				if (i == 4) startY -= 400;
				button.setBounds(210, startY, 160, 85);
				startY += 100;
			} else {
				if (i == 10) startY -= 600;
				button.setBounds(405, startY, 160, 85);
				startY += 100;
			}
			
			button.setFont(font2);
			c.add(button);
		}
		
		// 선택된 방에 대한 테이블 출력 
		// 데이터 및 컬럼명 배열 정의
		String[] columnNames = {"예약 날짜", "시작 시간", "종료 시간"};
		String[][] data = {{"", "", ""}};
			
		// DefaultTableModel을 사용하여 JTable에 데이터 설정
		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        // 모든 셀을 편집 불가능하도록 설정
		        return false;
		    }
		};
		reserveTable = new JTable(model);
		reservePane = new JScrollPane(reserveTable);

			    
		// 수평 스크롤바 비활성화
		reservePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		// 컬럼 너비 조절
		reserveTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		reserveTable.getColumnModel().getColumn(1).setPreferredWidth(80);
		reserveTable.getColumnModel().getColumn(2).setPreferredWidth(80);

		// 행 높이 조절
		reserveTable.setRowHeight(20);
			    
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < reserveTable.getColumnCount(); i++) {
			reserveTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		
		model.setRowCount(0); // 기존 테이블 내용 지우기
		reservePane.setBounds(440, 450, 280, 250);
		
		
		//요소 추가
		c.add(reserve_lb);
		c.add(room2_lb);
		c.add(room4_lb);
		c.add(room8_lb);
		
		c.add(memberinfo_lb);
		c.add(name_lb);
		c.add(name_value_lb);
		c.add(balance_lb);
		c.add(balance_value_lb);
		
		c.add(reserve_btn);
		c.add(cancel_btn);
		c.add(a);
		
		c.add(chosen_room_lb);
		c.add(chosen_room_value_lb);
		c.add(chosen_room_lb2);
		
		c.add(reservePane);
		
		//화면 중앙에 오게 설정
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
		setVisible(true);
		setResizable(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == a) {
			MainPageUI.as.doClick();
			dispose();
		}
	}

	public static void main(String[] args) {
		
	}
}