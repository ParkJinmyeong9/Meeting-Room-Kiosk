package meeting_room;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class MemberInfoUI extends JFrame implements ActionListener {

	JTable membertable;
	JScrollPane pane;
	static JButton jb = new JButton("a");

	//컨텐츠 패널의 객체 메소드 호출
	Container c = getContentPane();
	
	public MemberInfoUI() {
		setSize(549, 436);
		setTitle("회원 정보 관리");

		c.setLayout(null); //컨텐츠 패널 초기

		viewlist();
		
		//요소 위치 지정
		pane.setBounds(0, 0, 536, 400);
		
		//이벤트 추가
		membertable.addMouseListener(new MouseAction());
		jb.addActionListener(this);
		
		//요소 추가
		c.add(pane);
		c.add(jb);
		
		//화면 중앙에 오게 설정
		setLocationRelativeTo(null);
		//프로그램 종료할 때 현재 창만 종료
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setVisible(true);
		setResizable(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//MemUpdateUI를 통해 DB 정보가 수정 되었을 때 이벤트 발생 시키기 위해 생성
		//새로고침으로는 수정된 내용 불러오기를 못해서 창을 껐다 키게 만들어서 목록 새로고침
		if (e.getSource() == jb) {
			dispose();
			MemberInfoUI MIUI = new MemberInfoUI();
		}
	}
	
	private class MouseAction extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			//더블클릭 시 이벤트 발생
			if(e.getClickCount()==2) {
				int row = membertable.getSelectedRow();
				TableModel mmm = membertable.getModel();
				MemUpdateUI MUUI = new MemUpdateUI(String.valueOf(mmm.getValueAt(row, 1)));
			}
		}
	}
	
	public void viewlist() {
		Vector<MyInfoBean> vlist;
		MyInfoMgr mgr;
		
		mgr = new MyInfoMgr();
		vlist = mgr.selectAll();
		
		String header[]= {"번호", "아이디", "이름", "전화번호", "현재잔액"};
		String[][] conts = new String[vlist.size()][header.length];
		
		//순번, 아이디, 이름, 전화번호, 현재잔액 값 conts 배열에 저장
		for(int i = 0; i < vlist.size(); i++) {
			MyInfoBean bean = vlist.get(i);
			conts[i][0] = String.valueOf(i + 1);
			conts[i][1] = bean.getID();
			conts[i][2] = bean.getName();
			conts[i][3] = bean.getPhone();
			conts[i][4] = String.valueOf(bean.getMoney());
		}
		
		//테이블 직접 편집 불가능
		DefaultTableModel model = new DefaultTableModel(conts, header) {
			public boolean isCellEditable(int i, int c){ return false; }
		};
		
		//테이블 가운데 정렬
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            
		//선언
		membertable = new JTable(model);
		pane = new JScrollPane(membertable);
		
		//폰트 설정
		Font font = new Font("Dialog", Font.BOLD, 16);
		membertable.setFont(font);

		//크기 조절 불가능
		membertable.getTableHeader().setResizingAllowed(false);
		membertable.getTableHeader().setReorderingAllowed(false);
		
		//컬럼 사이즈 지정
		membertable.getColumnModel().getColumn(0).setPreferredWidth(25);
		membertable.getColumnModel().getColumn(1).setPreferredWidth(70);
		membertable.getColumnModel().getColumn(2).setPreferredWidth(70);
		membertable.getColumnModel().getColumn(3).setPreferredWidth(160);
		membertable.getColumnModel().getColumn(4).setPreferredWidth(110);
		membertable.setRowHeight(30);

		//header 추가, 가운데 정렬
		for (int i = 0; i < membertable.getColumnCount(); i++) {
			membertable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
	}
	
	public static void main(String[] args) {
		
	}
}