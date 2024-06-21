package meeting_room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

// 음식 데이터를 저장하는 클래스
class Food {
	String type;
	String name;
	String detail;
	String imageUrl;
	int price;
	
	Food(String type, String name, String imageUrl, int price) {  // 상세 정보를 받는 생성자 추가
		this.type = type;
		this.name = name;
		this.imageUrl = imageUrl;
		this.price = price;
	}
	
	String getType() { return type; }
	String getName() { return name; }
	String getImageUrl() { return imageUrl; }
	int getPrice() { return price; }
}

public class FoodOrderUI extends JFrame implements Runnable, ActionListener {
	
	Socket sock;
	BufferedReader in;
	PrintWriter out;
	
	JPanel panel = new JPanel(new BorderLayout());
	JLabel totalLabel = new JLabel("총 주문 금액: 0");
	//상단 카테고리 표시
	JPanel northPanel = new JPanel(new GridLayout(1, 5));
	String[] foodTypes = {"카페", "음료", "면류", "밥류", "과자", "술"};
	//centerPanel을 CardLayout으로 설정
	CardLayout cl = new CardLayout();
	JPanel centerPanel = new JPanel(cl);
	//장바구니 리스트 생성
	DefaultListModel<String> cartList = new DefaultListModel<>();
	JList<String> cart = new JList<>(cartList);
	JScrollPane cartScrollPane = new JScrollPane(cart);
	JButton purchaseButton = new JButton("주문하기");
	//음식 리스트 생성
	List<Food> foods = new ArrayList<>();
	JFrame detailDialog;
	String room;
	String in_time;
	JLabel requestLabel = new JLabel("주문 요청사항: ");
	JTextField requestField = new JTextField(20);
	boolean flag = false;
	
	 public void updateTotalPrice() {
	        int total = 0;
	        for (int i = 0; i < cartList.size(); i++) {
	            String item = cartList.getElementAt(i);
	            String[] split = item.split("\\s+(?=x)");
	            int quantity = split.length == 2 ? Integer.parseInt(split[1].trim().replaceAll("[^0-9]", "")) : 1;
	            String foodName = split[0].trim().replaceAll("\\s+\\d+$", "");
	            int price = 0;
	            for (Food food : foods) {
	                if (food.getName().equals(foodName)) {
	                    price = food.getPrice();
	                    break;
	                }
	            }
	            total += price * quantity;
	        }
	        totalLabel.setText("총 주문 금액: " + total);
	    }


	public FoodOrderUI(String room, String in_time) {
		this.room = room;
		this.in_time = in_time;
		
		setTitle("음식 메뉴");
		setSize(1000, 800);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		cart.setCellRenderer(new CartItemRenderer());
		
		cart.setFixedCellHeight(50); // Adjust the value as needed

		cart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedIndex = cart.locationToIndex(e.getPoint());
                if (selectedIndex != -1) {
                    Rectangle bounds = cart.getCellBounds(selectedIndex, selectedIndex);
                    if (bounds != null && e.getX() >= bounds.width - 80) { // Adjust the value as needed
                        cartList.remove(selectedIndex);
                        updateTotalPrice();
                    }
                }
            }
        });
		//창을 X 를 눌러서 닫을 때
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//상세정보(담기) 프레임이 열려있을 때 함께 닫기
				if (detailDialog != null && detailDialog.isVisible() == true) detailDialog.dispose();
			}
		});
		
		cart.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        if (e.getClickCount() == 2) {
		            int index = cart.locationToIndex(e.getPoint());
		            if (index >= 0) {
		                String item = cartList.getElementAt(index);
		                String[] split = item.split("x");
		                int oldQuantity = 1;
		                if (split.length == 2) {
		                    try {
		                        oldQuantity = Integer.parseInt(split[1].trim().replaceAll("[^0-9]", ""));
		                    } catch (NumberFormatException ex) {
		                        // Handle the exception
		                    }
		                }

		                SpinnerNumberModel model = new SpinnerNumberModel(oldQuantity, 1, 10, 1);
		                JSpinner spinner = new JSpinner(model);
		                JOptionPane.showMessageDialog(null, spinner, "수량을 입력하세요", JOptionPane.QUESTION_MESSAGE);
		                try {
		                    int newQuantity = (Integer) spinner.getValue();
		                    cartList.setElementAt(split[0].trim() + " x" + newQuantity, index);
		                    
		                    // Update the total order amount
		                    updateTotalPrice();
		                } catch (NumberFormatException ex) {
		                    JOptionPane.showMessageDialog(null, "유효한 수량을 입력하세요", "오류", JOptionPane.ERROR_MESSAGE);
		                }
		            }
		        }
		    }
		});
		
		
		//음식 데이터 생성
		Vector<FoodBean> vlist;
		MyInfoMgr mgr = new MyInfoMgr();
		vlist = mgr.foodAll();
		
		for (int i = 0; i < vlist.size(); i++) {
			FoodBean bean = vlist.get(i);
			String name = bean.getFname().trim();
			String img = "./Food/" + name + ".jpg";
			int price = bean.getFprice();
			//String img = "./Food/\" + bean.getFname().trim() + \".jpg";
			foods.add(new Food(bean.getFtype(), name, img, price));
		}
		showFood();
		
		//음식 카테고리 버튼에 ActionListener 추가
		for (String foodType : foodTypes) {
			JButton foodTypeButton = new JButton(foodType);
			foodTypeButton.setPreferredSize(new Dimension(100, 30));
			foodTypeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//모든 버튼의 배경색을 초기화
					for (Component comp : northPanel.getComponents()) {
						if (comp instanceof JButton) {
							comp.setBackground(null);
						}
					}
					//현재 누른 버튼의 배경색을 변경
					foodTypeButton.setBackground(Color.YELLOW);
					//해당 음식 유형의 패널로 변경
					cl.show(centerPanel, foodType);
				}
			});
			northPanel.add(foodTypeButton);
			
		}
		
		JButton homeButton = new JButton("돌아가기");
		//돌아가기 버튼을 눌렀을 때 새로운 메인 UI가 열리지 않고 현재 UI를 닫도록 수정
		homeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (detailDialog != null && detailDialog.isVisible() == true) detailDialog.dispose();
				dispose();
			}
		});
		northPanel.add(homeButton);
		panel.add(northPanel, BorderLayout.NORTH);
		
		cl.show(centerPanel, "카페"); //초기에 보여줄 패널 설정
		panel.add(centerPanel, BorderLayout.CENTER);
		
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(cartScrollPane, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
		
		// 전체 취소 버튼의 이벤트 핸들러
		JButton cancelButton = new JButton("전체 취소");
		cancelButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // 장바구니 내 모든 항목 삭제
		        cartList.clear(); 
		        // 총 주문 금액 업데이트
		        totalLabel.setText("총 주문 금액: 0");
		    }
		});
		buttonPanel.add(cancelButton);

		
		//총 주문 금액 표시
		JPanel totalPanel = new JPanel(new BorderLayout());
		totalPanel.add(totalLabel, BorderLayout.NORTH);
		
		//주문 요청사항 입력 패널
		JPanel requestPanel = new JPanel();

		requestField.setPreferredSize(new Dimension(200, 40));
		requestPanel.add(requestLabel);
		requestPanel.add(requestField);
		totalPanel.add(requestPanel, BorderLayout.CENTER);
		
		rightPanel.add(totalPanel, BorderLayout.NORTH);
		rightPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		panel.add(rightPanel, BorderLayout.EAST);
		
		purchaseButton.addActionListener(this);

		buttonPanel.add(purchaseButton);
		add(panel);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == purchaseButton) {
			try {
				new Thread(this).start();
				if (flag == false) {
					JOptionPane.showMessageDialog(null, "현재 영업 시간이 아닙니다.", "경고", JOptionPane.WARNING_MESSAGE);
					return;
				} else if (flag == true) {
			if (cartList.isEmpty()) {
				JOptionPane.showMessageDialog(null, "장바구니가 비어있습니다.", "경고", JOptionPane.WARNING_MESSAGE);
			} else {
				MyInfoMgr mgr = new MyInfoMgr();
				MyInfoBean bean = mgr.select(LoginUI.ID);
				int money = bean.getMoney();
				int total = Integer.parseInt(totalLabel.getText().split(": ")[1]);
				if (money < total) {
					JOptionPane.showMessageDialog(null, "잔액이 부족합니다.", "경고", JOptionPane.ERROR_MESSAGE);
					return;
				}
				for (int i = 0; i < cartList.size(); i++) {
					OrderInfoBean beans = new OrderInfoBean();
					FoodBean bean2 = mgr.food(cartList.getElementAt(i).split(" ")[0]);
					beans.setRoom_no(room);
					beans.setFoodname(bean2.getFood());
					beans.setFoodid(LoginUI.ID);
					beans.setFoodprice(bean2.getFprice() * Integer.parseInt(cartList.getElementAt(i).split("x")[1]));
					beans.setFoodcount(Integer.parseInt(cartList.getElementAt(i).split("x")[1]));
					mgr.foodsales(beans);
				}
				OrderBean bean4 = new OrderBean();
		                bean4.setOrder_id(LoginUI.ID);
		                bean4.setOrder_room(room);
		                bean4.setOrder_total(total);
						if (!"".equals(requestField.getText()))
							bean4.setOrder_request(requestField.getText());
		                LocalDateTime currentTime = LocalDateTime.now();
		                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		                String day = currentTime.format(formatter);
		                TotalBean bean5 = mgr.useAll(room, LoginUI.ID, day);
		                bean5.setFood_total(bean5.getFood_total() + total);
		                bean5.setTotal(bean5.getTotal() + total);
		                bean5.setIntime(in_time);
		                bean.setMoney(money - total);
				if (mgr.charge(bean) && mgr.order(bean4) && mgr.totalprice(bean5)) {
					JOptionPane.showMessageDialog(null, "구매가 완료되었습니다.");
					sendMessage(MeetingProtocol.ID + MeetingProtocol.MODE + LoginUI.ID);
					sendMessage(MeetingProtocol.ORDER + MeetingProtocol.MODE + room);
					cartList.clear(); // 장바구니 비우기
					ReserveUI.a.doClick();
					dispose();
					return;
				}
			}
			totalLabel.setText("총 주문 금액: 0");
				}
		        } catch (Exception ex) {
			            ex.printStackTrace();
			        }
		}
	}
	
	class CartItemRenderer extends JPanel implements ListCellRenderer<Object> {
	    private static final long serialVersionUID = 1L;

	    private JLabel label;
	    private JButton cancelButton;

	    public CartItemRenderer() {
	        setLayout(new BorderLayout());
	        setOpaque(true);

	        label = new JLabel();
	        label.setPreferredSize(new Dimension(200, 30));

	        cancelButton = new JButton("취소");
	        

	        JPanel contentPanel = new JPanel(new BorderLayout());
	        contentPanel.add(label, BorderLayout.CENTER);
	        contentPanel.add(cancelButton, BorderLayout.EAST);

	        add(contentPanel, BorderLayout.CENTER);
	    }

	    @Override
	    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
	            boolean cellHasFocus) {
	        label.setText(value.toString());

	        if (isSelected) {
	            setBackground(list.getSelectionBackground());
	            setForeground(list.getSelectionForeground());
	        } else {
	            setBackground(list.getBackground());
	            setForeground(list.getForeground());
	        }

	        return this;
	    }
	}
	
	public void showFood() {
		//음식 카테고리별로 JPanel 생성
		for (String foodType : foodTypes) {
		JPanel foodPanel = new JPanel(new GridLayout(0, 5)); // 세로 크기는 무한대로, 가로 크기는 5로 설정하여 고정 크기의 그리드를 생성합니다.
		int count = 0; // 음식 버튼을 추가한 수를 저장합니다.
		
			for (final Food food : foods) {
				if (food.getType().equals(foodType)) {
					ImageIcon icon = new ImageIcon(food.getImageUrl());
					Image originalImage = icon.getImage();
					Image resizedImage = originalImage.getScaledInstance(150, 140, Image.SCALE_SMOOTH);
					ImageIcon resizedIcon = new ImageIcon(resizedImage);
					
					JButton foodButton = new JButton("<html><center>" + food.getName() + "<br>" + food.getPrice() + "원</center></html>");
					foodButton.setIcon(resizedIcon);
					
					foodButton.setVerticalTextPosition(SwingConstants.BOTTOM);
					foodButton.setHorizontalTextPosition(SwingConstants.CENTER);
					
					foodButton.setPreferredSize(new Dimension(100, 100));  //생성된 버튼의 크기 고정
					foodButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							//팝업 창을 생성하고 보여줌
							if (detailDialog != null && detailDialog.isVisible() == true) detailDialog.dispose();
							detailDialog = new JFrame("음식 상세 정보");
							detailDialog.setSize(250, 150);
							detailDialog.setLocationRelativeTo(null);
							
							//팝업 창에 음식 정보를 표시
							JPanel detailPanel = new JPanel();
							detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
							
							JLabel nameLabel = new JLabel("이름: " + food.getName());
							JLabel priceLabel = new JLabel("가격: " + food.getPrice());
							
							SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 10, 1);  // 초기 값 1, 최소 값 1, 최대 값 10, 단계 1
							JSpinner quantitySpinner = new JSpinner(model);
							JLabel quantityLabel = new JLabel("수량: ");
							quantitySpinner.setPreferredSize(new Dimension(50, 25));
		
							JPanel quantityPanel = new JPanel();
							quantityPanel.add(quantityLabel);
							quantityPanel.add(quantitySpinner);
							quantityLabel.setLabelFor(quantitySpinner);
							
							detailPanel.add(nameLabel);
							detailPanel.add(priceLabel);
							detailPanel.add(quantityPanel);
							
							//담기 버튼 추가
							JButton addButton = new JButton("담기");
							addButton.addActionListener(new ActionListener() {
							    @Override
							    public void actionPerformed(ActionEvent e) {
							        //주문 내역에 음식 추가
							        String foodNamePrice = food.getName() + " " + food.getPrice();

							        //스피너에서 선택된 수량을 가져옴
							        int quantity = (Integer) quantitySpinner.getValue();

							        //주문 내역에 이미 같은 음식이 있으면
							        int currentIndex = -1;
							        for (int i = 0; i < cartList.size(); i++) {
							            if (cartList.get(i).startsWith(foodNamePrice)) {
							                currentIndex = i;
							                break;
							            }
							        }

							        //이미 리스트에 있다면
							        if (currentIndex != -1) {
							            String oldEntry = cartList.getElementAt(currentIndex);
							            String[] split = oldEntry.split("x");
							            int oldCount = split.length == 2 ? Integer.parseInt(split[1].trim().replaceAll("[^0-9]", "")) : 1;
							            cartList.setElementAt(foodNamePrice + " x" + (oldCount + quantity), currentIndex);  // 수량 증가
							        } else {  //리스트에 없다면
							            cartList.addElement(foodNamePrice + " x" + quantity);  // 수량만큼 새로 추가
							        }

							        // 총 주문 금액 업데이트
							        int total = Integer.parseInt(totalLabel.getText().split(": ")[1].replaceAll("[^0-9]", "")) + food.getPrice() * quantity;
							        totalLabel.setText("총 주문 금액: " + total);

							        // 총 주문 금액 업데이트
							        updateTotalPrice();

							        detailDialog.dispose();
							    }
							});
							
							
							detailPanel.add(addButton);
							detailDialog.add(detailPanel, BorderLayout.NORTH);
							detailDialog.setVisible(true);

						}
					});
					foodPanel.add(foodButton);
					count++;
				}
			}
			
			
			//총 20개의 칸 중에서 게임 버튼으로 채우지 못한 나머지 칸을 빈 패널로 채우기
			for (int i = count; i < 20; i++) {
				JPanel emptyPanel = new JPanel();
				foodPanel.add(emptyPanel);
			}
			
			JScrollPane scrollPane = new JScrollPane(foodPanel);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);// 항상 세로 스크롤바 표시
			centerPanel.add(scrollPane, foodType);
		}
	}
	
	public void run() {
		try {
			String host = "127.0.0.1";
			int port = MeetingServer.PORT;
			connect(host, port);
			flag = true;
			while(true) {
				String line = in.readLine();
				if(line==null)
					break;
				else
					routine(line);
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
	}
	
	public void routine(String line) {
		int idx = line.indexOf(MeetingProtocol.MODE);
		String cmd = line.substring(0, idx);
		String data = line.substring(idx + 1);
		
		if (cmd.equals(MeetingProtocol.MESSAGE)) {
			idx = data.indexOf(';');
			cmd = data.substring(0, idx);
			data = data.substring(idx + 1);
		}
	}
	
	public void connect(String host, int port) {
		try {
			sock = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(),true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String msg) {
		if (out != null)
			out.println(msg);
	}
	
	public static void main(String[] args) {
		
	}
}