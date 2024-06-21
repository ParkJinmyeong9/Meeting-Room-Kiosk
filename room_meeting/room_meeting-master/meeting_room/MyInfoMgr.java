package meeting_room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class MyInfoMgr {

	private DBConnection pool;

	public MyInfoMgr() {
		try {
			pool = DBConnection.getInstance();
			Connection con = pool.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//관리자 제외 모든 사용자
	public Vector<MyInfoBean> selectAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<MyInfoBean> vlist = new Vector<MyInfoBean>();
		try {
			//Connection 객체를 pool 에서 가져옴
			con = pool.getConnection();
			sql = "select * from member where member_level=1";
			pstmt = con.prepareStatement(sql);
			//rs: select 실행 결과 객체
			rs = pstmt.executeQuery(); //실제 SQL문 실행
			
			while (rs.next()) { //커서를 밑으로 내림
				MyInfoBean bean = new MyInfoBean();
				bean.setID(rs.getString("member_id"));
				bean.setPW(rs.getString("member_pw"));
				bean.setName(rs.getString("member_name"));
				bean.setPhone(rs.getString("member_tel"));
				bean.setMoney(rs.getInt("member_money"));
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//con 반납, pstmt, rs 는 close
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}
	
	//예약 리스트
	public Vector<ReserveBean> reserveAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<ReserveBean> vlist = new Vector<ReserveBean>();
		try {
			con = pool.getConnection();
			sql = "select * from reserve";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				ReserveBean bean = new ReserveBean();
				bean.setResvid(rs.getString("resv_id"));
				bean.setResvname(rs.getString("resv_name"));
				bean.setResvphone(rs.getString("resv_tel"));
				bean.setResvroom(rs.getString("resv_room"));
				bean.setResvtime(rs.getString("resv_time"));
				bean.setResvusetime(rs.getInt("resv_usetime"));
				bean.setResvperson(rs.getInt("resv_person"));
				bean.setResvtotal(rs.getInt("resv_total"));
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}
	
	//사용자 예약 리스트
	public Vector<ReserveBean> reserveUser(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<ReserveBean> vlist = new Vector<ReserveBean>();
		try {
			con = pool.getConnection();
			sql = "select * from reserve where resv_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id); //첫번째 ?에 매개변수 id 값 세팅
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReserveBean bean = new ReserveBean();
				bean.setResvid(rs.getString("resv_id"));
				bean.setResvname(rs.getString("resv_name"));
				bean.setResvphone(rs.getString("resv_tel"));
				bean.setResvroom(rs.getString("resv_room"));
				bean.setResvtime(rs.getString("resv_time"));
				bean.setResvusetime(rs.getInt("resv_usetime"));
				bean.setResvperson(rs.getInt("resv_person"));
				bean.setResvtotal(rs.getInt("resv_total"));
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}
	
	//사용자 예약 리스트 날짜별
	public Vector<ReserveBean> reserveUserDetail(String id, String start, String end) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<ReserveBean> vlist = new Vector<ReserveBean>();
		try {
			con = pool.getConnection();
			sql = "select * from reserve where resv_id=? and resv_time BETWEEN ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, start);
			pstmt.setString(3, end);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReserveBean bean = new ReserveBean();
				bean.setResvid(rs.getString("resv_id"));
				bean.setResvname(rs.getString("resv_name"));
				bean.setResvphone(rs.getString("resv_tel"));
				bean.setResvroom(rs.getString("resv_room"));
				bean.setResvtime(rs.getString("resv_time"));
				bean.setResvusetime(rs.getInt("resv_usetime"));
				bean.setResvperson(rs.getInt("resv_person"));
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}
	
	//사용자 예약 리스트 날짜별
	public Vector<OrderInfoBean> salesFoodDetail(String room, String day, String name, String start, String end) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<OrderInfoBean> vlist = new Vector<OrderInfoBean>();
		try {
			con = pool.getConnection();
			sql = "select * from food_sales where food_room=? and sales_day=? and foods=? and sales_day BETWEEN ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, room);
			pstmt.setString(2, day);
			pstmt.setString(3, name);
			pstmt.setString(4, start);
			pstmt.setString(5, end);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderInfoBean bean = new OrderInfoBean();
				bean.setFoodcount(rs.getInt("food_count"));
				bean.setFoodprice(rs.getInt("sales_amount"));
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}

	public Vector<OrderInfoBean> userfoodsales(String room, String id, String day) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<OrderInfoBean> vlist = new Vector<OrderInfoBean>();
		try {
			con = pool.getConnection();
			sql = "select * from food_sales where food_room=? and food_id=? and sales_day=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, room);
			pstmt.setString(2, id);
			pstmt.setString(3, day);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderInfoBean bean = new OrderInfoBean();
				bean.setFoodname(rs.getString("foods"));
				bean.setOrdertime(rs.getString("sales_day"));
				bean.setFoodcount(rs.getInt("food_count"));
				bean.setFoodprice(rs.getInt("sales_amount"));
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}
	
	public Vector<OrderInfoBean> usergamesales(String room, String id, String day) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<OrderInfoBean> vlist = new Vector<OrderInfoBean>();
		try {
			con = pool.getConnection();
			sql = "select * from game_sales where game_room=? and game_id=? and sales_day=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, room);
			pstmt.setString(2, id);
			pstmt.setString(3, day);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderInfoBean bean = new OrderInfoBean();
				bean.setGamename(rs.getString("games"));
				bean.setOrdertime(rs.getString("sales_day"));
				bean.setGamecount(rs.getInt("game_count"));
				bean.setGameprice(rs.getInt("sales_amount"));
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}
	
	//사용자 예약 리스트 날짜별
	public Vector<OrderInfoBean> salesGameDetail(String room, String day, String name, String start, String end) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<OrderInfoBean> vlist = new Vector<OrderInfoBean>();
		try {
			con = pool.getConnection();
			sql = "select * from game_sales where game_room=? and sales_day=? and games=? and sales_day BETWEEN ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, room);
			pstmt.setString(2, day);
			pstmt.setString(3, name);
			pstmt.setString(4, start);
			pstmt.setString(5, end);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderInfoBean bean = new OrderInfoBean();
				bean.setGamecount(rs.getInt("game_count"));
				bean.setGameprice(rs.getInt("sales_amount"));
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}
	
	//음식 리스트
	public Vector<FoodBean> foodAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<FoodBean> vlist = new Vector<FoodBean>();
		try {
			con = pool.getConnection();
			sql = "select * from food";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				FoodBean bean = new FoodBean();
				bean.setFood(rs.getString("food"));
				bean.setFname(rs.getString("food_name"));
				bean.setFprice(rs.getInt("food_price"));
				bean.setFtype(rs.getString("food_type"));
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}
	
	//보드게임 리스트
	public Vector<GameBean> gameAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<GameBean> vlist = new Vector<GameBean>();
		try {
			con = pool.getConnection();
			sql = "select * from game";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				GameBean bean = new GameBean();
				bean.setGame(rs.getString("game"));
				bean.setGname(rs.getString("game_name"));
				bean.setGperson(rs.getInt("game_person"));
				bean.setGprice(rs.getInt("game_price"));
				bean.setGstate(rs.getBoolean("game_state"));
				bean.setGtype(rs.getString("game_type"));
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}
	
	//방 리스트
	public Vector<RoomBean> roomAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<RoomBean> vlist = new Vector<RoomBean>();
		try {
			con = pool.getConnection();
			sql = "select * from room";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				RoomBean bean = new RoomBean();
				bean.setRoom(rs.getString("room"));
				bean.setRperson(rs.getInt("room_person"));
				bean.setRprice(rs.getInt("room_price"));
				bean.setRadd(rs.getInt("room_add"));
				bean.setRstate(rs.getBoolean("room_state"));
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}
	
	//매출
	public Vector<TotalBean> salesAll(String room, String start, String end) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<TotalBean> vlist = new Vector<TotalBean>();
		try {
			con = pool.getConnection();
			sql = "select * from `use` where room_num=? and use_day BETWEEN ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, room); //첫번째 ?에 매개변수 id 값 세팅
			pstmt.setString(2, start);
			pstmt.setString(3, end);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TotalBean bean = new TotalBean();
				bean.setRoom(rs.getString("room_num"));
				bean.setDay(rs.getString("use_day"));
				bean.setFood_total(rs.getInt("food_total"));
				bean.setGame_total(rs.getInt("game_total"));
				bean.setRoom_total(rs.getInt("room_total"));
				bean.setTotal(rs.getInt("use_total"));
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}
	
	//
	public TotalBean useAll(String room, String id, String day) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		TotalBean bean = new TotalBean();
		try {
			con = pool.getConnection();
			sql = "select * from `use` where room_num=? and use_id=? and use_day=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, room);
			pstmt.setString(2, id);
			pstmt.setString(3, day);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				bean.setID(id);
				bean.setRoom(room);
				bean.setDay(rs.getString("use_day"));
				bean.setIntime(rs.getString("in_time"));
				bean.setOuttime(rs.getString("out_time"));
				bean.setFood_total(rs.getInt("food_total"));
				bean.setGame_total(rs.getInt("game_total"));
				bean.setRoom_total(rs.getInt("room_total"));
				bean.setTotal(rs.getInt("use_total"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return bean;
	}
	
	public TotalBean getuse(String room, String id, String time) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		TotalBean bean = new TotalBean();
		try {
			con = pool.getConnection();
			sql = "select * from `use` where room_num=? and use_id=? and in_time=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, room);
			pstmt.setString(2, id);
			pstmt.setString(3, time);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				bean.setID(id);
				bean.setRoom(room);
				bean.setDay(rs.getString("use_day"));
				bean.setIntime(rs.getString("in_time"));
				bean.setOuttime(rs.getString("out_time"));
				bean.setFood_total(rs.getInt("food_total"));
				bean.setGame_total(rs.getInt("game_total"));
				bean.setRoom_total(rs.getInt("room_total"));
				bean.setTotal(rs.getInt("use_total"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return bean;
	}
	
	public Vector<OrderBean> order(String id, String room, String start, String end) {
		Connection con = null;
		PreparedStatement pstmt =  null;
		ResultSet rs = null;
		String sql = null;
		Vector<OrderBean> vlist = new Vector<OrderBean>();
		try {
			con = pool.getConnection();
			sql = "select * from `order` where order_id=? and order_room=? and order_time between ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, room);
			pstmt.setString(3, start);
			pstmt.setString(4, end);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderBean bean = new OrderBean();
				bean.setOrder_id(rs.getString("order_id"));
				bean.setOrder_room(rs.getString("order_room"));
				bean.setOrder_time(rs.getString("order_time"));
				bean.setOrder_total(rs.getInt("order_total"));
				bean.setOrder_request(rs.getString("order_request"));
				bean.setOrder_state(rs.getBoolean("order_state"));
				vlist.addElement(bean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		
		return vlist;
		
	}
	
	//한개의 레코드: select
	public MyInfoBean select(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		MyInfoBean bean = new MyInfoBean();
		try {
			con = pool.getConnection();
			sql = "select * from member where member_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id); //첫번째 ?에 매개변수 id 값 세팅
			rs = pstmt.executeQuery();
			if (rs.next()) {
				bean.setID(rs.getString(1));
				bean.setPW(rs.getString(2));
				bean.setName(rs.getString(3));
				bean.setPhone(rs.getString(4));
				bean.setMoney(rs.getInt(5));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return bean;
	}
	
	//방 select
	public RoomBean room(String rid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		RoomBean bean = new RoomBean();
		try {
			con = pool.getConnection();
			sql = "select * from room where room = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, rid); //첫번째 ?에 매개변수 id 값 세팅
			rs = pstmt.executeQuery();
			if (rs.next()) {
				bean.setRoom(rs.getString(1));
				bean.setRperson(rs.getInt(2));
				bean.setRprice(rs.getInt(3));
				bean.setRadd(rs.getInt(4));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return bean;
	}
	
	//정보 수정
	public boolean update(MyInfoBean bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "update member set member_pw=?, member_name=?, member_tel=? where member_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,  bean.getPW());
			pstmt.setString(2,  bean.getName());
			pstmt.setString(3,  bean.getPhone());
			pstmt.setString(4, bean.getID());
			if (pstmt.executeUpdate() == 1) flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
	
	//금액충전
	public boolean charge(MyInfoBean bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "update member set member_money=? where member_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1,  bean.getMoney());
			pstmt.setString(2, bean.getID());
			if (pstmt.executeUpdate() == 1) flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
	
	//예약하기
	public boolean reserve(ReserveBean bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "insert into reserve values (NULL, ?, ?, ?, ?, ?, ?, ?, ?, FALSE)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,  bean.getResvid());
			pstmt.setString(2,  bean.getResvname());
			pstmt.setString(3,  bean.getResvphone());
			pstmt.setString(4,  bean.getResvroom());
			pstmt.setString(5,  bean.getResvtime());
			pstmt.setInt(6,  bean.getResvusetime());
			pstmt.setInt(7,  bean.getResvperson());
			pstmt.setInt(8,  bean.getResvtotal());
			
			if (pstmt.executeUpdate() == 1) flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
	
	//음식주문
	public boolean foodsales(OrderInfoBean bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "insert into food_sales values (NULL, ?, ?, now(), ?, ?, ?, FALSE)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,  bean.getRoom_no());
			pstmt.setString(2, bean.getFoodid());
			pstmt.setString(3, bean.getFoodname());
			pstmt.setInt(4, bean.getFoodcount());
			pstmt.setInt(5,  bean.getFoodprice());
			
			if (pstmt.executeUpdate() == 1) flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
	
	//음식 select
	public FoodBean food(String fid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		FoodBean bean = new FoodBean();
		try {
			con = pool.getConnection();
			sql = "select * from food where food_name=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, fid); //첫번째 ?에 매개변수 id 값 세팅
			rs = pstmt.executeQuery();
			if (rs.next()) {
				bean.setFood(rs.getString(1));
				bean.setFname(rs.getString(2));
				bean.setFprice(rs.getInt(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return bean;
	}
	
	//게임주문
	public boolean gamesales(OrderInfoBean bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "insert into game_sales values (NULL, ?, ?, now(), ?, ?, ?, FALSE)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,  bean.getRoom_no());
			pstmt.setString(2,  bean.getGameid());
			pstmt.setString(3, bean.getGamename());
			pstmt.setInt(4,  bean.getGamecount());
			pstmt.setInt(5, bean.getGameprice());
			
			if (pstmt.executeUpdate() == 1) flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
	
	//게임 select
	public GameBean game(String gid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		GameBean bean = new GameBean();
		try {
			con = pool.getConnection();
			sql = "select * from game where game_name=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, gid); //첫번째 ?에 매개변수 id 값 세팅
			rs = pstmt.executeQuery();
			if (rs.next()) {
				bean.setGame(rs.getString(1));
				bean.setGname(rs.getString(2));
				bean.setGprice(rs.getInt(4));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return bean;
	}
	
	//회원가입 등록
	public boolean signup(MyInfoBean bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "insert into member values (?, ?, ?, ?, NULL, 1)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,  bean.getID());
			pstmt.setString(2, bean.getPW());
			pstmt.setString(3, bean.getName());
			pstmt.setString(4,  bean.getPhone());
			
			if (pstmt.executeUpdate() == 1) flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
	
	//토탈
	public boolean total(TotalBean bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "insert into `use` values (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,  bean.getRoom());
			pstmt.setString(2,  bean.getID());
			pstmt.setString(3, bean.getDay());
			pstmt.setString(4, bean.getIntime());
			pstmt.setString(5,  bean.getOuttime());
			pstmt.setInt(6,  bean.getFood_total());
			pstmt.setInt(7,  bean.getGame_total());
			pstmt.setInt(8,  bean.getRoom_total());
			pstmt.setInt(9,  bean.getTotal());
			
			if (pstmt.executeUpdate() == 1) flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
	
	//토탈
	public boolean order(OrderBean bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "insert into `order` values (NULL, ?, ?, now(), ?, ?, FALSE)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, bean.getOrder_id());
			pstmt.setString(2,  bean.getOrder_room());
			pstmt.setInt(3, bean.getOrder_total());
			pstmt.setString(4, bean.getOrder_request());
			if (pstmt.executeUpdate() == 1) flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
	
	//게임 select
	public TotalBean selecttotal(String tid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		TotalBean bean = new TotalBean();
		try {
			con = pool.getConnection();
			sql = "select * from `use` where use_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, tid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				bean.setRoom(rs.getString(2));
				bean.setID(rs.getString(3));
				bean.setTotal(rs.getInt(10));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return bean;
	}
	
	//토탈금액
	public boolean totalprice(TotalBean bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "update `use` set food_total=?, game_total=?, room_total=?, use_total=? where room_num=? and use_id=? and in_time=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, bean.getFood_total());
			pstmt.setInt(2, bean.getGame_total());
			pstmt.setInt(3, bean.getRoom_total());
			pstmt.setInt(4, bean.getTotal());
			pstmt.setString(5, bean.getRoom());
			pstmt.setString(6, bean.getID());
			pstmt.setString(7, bean.getIntime());
			if (pstmt.executeUpdate() == 1) flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
	
	//예약 취소
	public boolean cancelresv(String id, String room, String time) {
		Connection con = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		String sql = null;
		String sql2 = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "delete from reserve where resv_id=? and resv_room=? and resv_time=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, room);
			pstmt.setString(3,  time);
			
			sql = "delete from `use` where room_num=? and use_id=? and in_time=?";
			pstmt2 = con.prepareStatement(sql);
			pstmt2.setString(1, room);
			pstmt2.setString(2, id);
			pstmt2.setString(3,  time);
			
			if (pstmt.executeUpdate() == 1 && pstmt2.executeUpdate() == 1) flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
	
	//음식 select
	public Vector<OrderInfoBean> orderfood(String room, String food, int a) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<OrderInfoBean> vlist = new Vector<OrderInfoBean>();
		try {
			con = pool.getConnection();
			if (a == 1) {
				sql = "select * from food_sales where food_room=? and foods=? and sales_day BETWEEN ? and ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, room);
				pstmt.setString(2, food);
				LocalDateTime now = LocalDateTime.now();
				LocalDateTime startOfDay = now.with(LocalTime.MIN);
				LocalDateTime endOfDay = now.with(LocalTime.MAX);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				pstmt.setString(3,  String.valueOf(startOfDay.format(formatter)));
				pstmt.setString(4,  String.valueOf(endOfDay.format(formatter)));
			} else if (a == 0) {
				sql = "select * from food_sales where food_room=? and foods=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, room);
				pstmt.setString(2, food);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderInfoBean bean = new OrderInfoBean();
				bean.setFoodcount(rs.getInt("food_count"));
				bean.setFoodprice(rs.getInt("sales_amount"));
				bean.setOrdertime(rs.getString("sales_day"));
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}
	
	//게임 select
	public Vector<OrderInfoBean> ordergame(String room, String game, int a) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<OrderInfoBean> vlist = new Vector<OrderInfoBean>();
		try {
			con = pool.getConnection();
			if (a == 1) {
				sql = "select * from game_sales where game_room=? and games=? and sales_day BETWEEN ? and ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, room);
				pstmt.setString(2, game);
				LocalDateTime now = LocalDateTime.now();
				LocalDateTime startOfDay = now.with(LocalTime.MIN);
				LocalDateTime endOfDay = now.with(LocalTime.MAX);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				pstmt.setString(3,  String.valueOf(startOfDay.format(formatter)));
				pstmt.setString(4,  String.valueOf(endOfDay.format(formatter)));
			} else if (a == 0) {
				sql = "select * from game_sales where game_room=? and games=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, room);
				pstmt.setString(2, game);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderInfoBean bean = new OrderInfoBean();
				bean.setGamecount(rs.getInt("game_count"));
				bean.setGameprice(rs.getInt("sales_amount"));
				bean.setOrdertime(rs.getString("sales_day"));
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vlist;
	}
}

