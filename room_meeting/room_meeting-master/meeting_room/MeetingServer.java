package meeting_room;

import java.awt.BorderLayout;
import java.awt.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MeetingServer extends JFrame {
	
	public static final int PORT = 8123;
	ServerSocket server;
	Vector<ClientThread2> vc;
	
	List slist = new List();
	
	public MeetingServer() {
		setTitle("주문 리스트");
		setSize(300, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		slist.add("** 주문 리스트 **");
		p2.add(BorderLayout.CENTER, slist);
		add(p2);
		
		try {
			server = new ServerSocket(PORT);
			vc = new Vector<ClientThread2>();
		} catch (Exception e) {
			System.err.println("Error in Server");
			System.exit(1);
		}
		System.out.println("***********************");
		System.out.println("서버가 구동되었습니다.");
		System.out.println("***********************");
		try {
			while (true) {
				Socket sock = server.accept();
				ClientThread2 ct = new ClientThread2(sock);
				ct.start();
				vc.addElement(ct);
			}
		} catch (Exception e) {
			System.err.println("Error in Socket");
		}
	}
	
	public void sendAllMessage(String msg) {
		for (int i = 0; i < vc.size(); i++) {
			ClientThread2 ct = vc.get(i);
			ct.sendMessage(msg);
		}
	}
	
	public void removeClient(ClientThread2 ct) {
		vc.remove(ct);
	}
	
	class ClientThread2 extends Thread{
		
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		String id;
		String room;
		
		public ClientThread2(Socket sock) {
			try {
				this.sock = sock;
				in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				out = new PrintWriter(sock.getOutputStream(),true);
				System.out.println(sock.toString() + " 접속됨...");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				while(true) {
					String line = in.readLine();
					if(line==null)
						break;
					else
						routine(line);
				}
			} catch (Exception e) {
				removeClient(this);
				System.err.println(sock+"["+ id +"]끊어짐...");
				for (int i = 0; i < slist.getItemCount(); i++) {
					if (slist.getItem(i).equals(id)) {
						slist.remove(i);
						break;
					}
				}
			}
		}
		
		public void routine(String line) {
			int idx = line.indexOf(MeetingProtocol.MODE);
			String cmd = line.substring(0, idx);
			String data = line.substring(idx + 1);
			
			if (cmd.equals(MeetingProtocol.ID)) {
				id = data;
			} else if (cmd.equals(MeetingProtocol.ORDER)) {
				room = data;
				JOptionPane.showMessageDialog(MeetingServer.this, room + " 에서 새로운 주문이 있습니다.");
				LocalDateTime currentTime = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS");
				String time = currentTime.format(formatter);
				slist.add("[" + time + "] " + room + " 주문");
			}
		}
		
		public void sendMessage(String msg) {
			out.println(msg);
		}
		
	}//--ClientThread2
	
	public static void main(String[] args) {
		new MeetingServer();
	}
}