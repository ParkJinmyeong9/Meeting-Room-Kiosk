package meeting_room;

import java.awt.Container;
import java.awt.Dialog;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class OrderInfo extends JFrame implements ActionListener{
	static JTable orderTable;
	JScrollPane pane;
	static JButton jb = new JButton(" ");
	String [][] conts;
	String [][] cont;
	Container c = getContentPane();
	
	public OrderInfo() {
		setSize(1000,440);
		setTitle("주문 현황");
		
		c.setLayout(null);
		
		orderview();
		
		pane.setBounds(0, 0 , 986,440);
		
		orderTable.addMouseListener(new MouseAction());
		jb.addActionListener(this);
		
		
		c.add(pane);
		c.add(jb);
		
		
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jb) {
			dispose();
			OrderInfo orderinfo = new OrderInfo();
		}
	
	}
	private	class MouseAction extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() == 2) {
				int row = orderTable.getSelectedRow();
				 if (row != -1) { // 선택된 행이 있는지 확인
			            TableModel tm = orderTable.getModel();
			            String no = tm.getValueAt(row,1).toString();
			            OrderInfoDetail OID = new OrderInfoDetail(no, conts[row][2],conts[row][3], row, cont[row][0]);
				 } 
			}
			super.mouseClicked(e);
		}
		
	}
	
	public void orderview() {
		Vector<OrderBean> vlist ;
		OrderMgr mgr = new OrderMgr();
		vlist = mgr.order();
			
		String header[] = {"순서"/**/, "주문한 방","주문한 사람"/*room_num*/, "주문한 시간"/*ordertime*/,"총 금액"/*use_total*/, "주문 상태"/**/};
		conts = new String [vlist.size()][header.length];
		cont = new String[vlist.size()][1];
		
//		id = new String [vlist.size()][header.length];	

			for (int i = 0; i < vlist.size(); i++) {
				OrderBean  bean = vlist.get(i);
				conts[i][0] = String.valueOf(i+1);
				conts[i][1] = bean.getOrder_room();
				conts[i][2] = bean.getOrder_id();
				conts[i][3] = bean.getOrder_time();
				conts[i][4] = String.valueOf(bean.getOrder_total());
				conts[i][5] = String.valueOf(bean.isOrder_state()? "완료" : "대기중...");
				cont[i][0] = bean.getOrder_request();
			
			}
		DefaultTableModel model = new DefaultTableModel(conts, header) {
			public boolean isCellEditable(int i, int c) {return false;}
		};
		orderTable = new JTable(model);
		pane = new JScrollPane(orderTable);
		
		Font fnt = new Font("Dialog", Font.BOLD, 15);
		orderTable.setFont(fnt);
		orderTable.setRowHeight(30);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		orderTable.getTableHeader().setResizingAllowed(false);
		orderTable.getTableHeader().setReorderingAllowed(false);
		
		for (int i = 0; i < orderTable.getColumnCount(); i++) {
			orderTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
	}
	public static void main(String[] args) {
		
	}
}
