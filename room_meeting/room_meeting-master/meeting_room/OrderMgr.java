package meeting_room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class OrderMgr {
private DBConnection pool;
	
	public OrderMgr() {
		try {
			pool = DBConnection.getInstance();
			Connection con = pool.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public Vector<OrderBean> order() {
		Connection con = null;
		PreparedStatement pstmt =  null;
		ResultSet rs = null;
		String sql = null;
		Vector<OrderBean> vlist = new Vector<OrderBean>();
		try {
			con = pool.getConnection();
			sql = "select * from `order` where order_time between ? and ?";
			pstmt = con.prepareStatement(sql);
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime startOfDay = now.with(LocalTime.MIN);
			LocalDateTime endOfDay = now.with(LocalTime.MAX);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			pstmt.setString(1,  String.valueOf(startOfDay.format(formatter)));
			pstmt.setString(2,  String.valueOf(endOfDay.format(formatter)));
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
	public boolean update(OrderBean bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "update `order` set order_state = ? where order_id = ? and order_room=? and order_time=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setBoolean(1, bean.isOrder_state());
			pstmt.setString(2, bean.getOrder_id());
			pstmt.setString(3, bean.getOrder_room());
			pstmt.setString(4, bean.getOrder_time());
			if(pstmt.executeUpdate()==1) flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
}
