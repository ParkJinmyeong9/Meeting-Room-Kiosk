package meeting_room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class OrderInfoMgr {
	private DBConnection pool;
	
	public OrderInfoMgr() {
		try {
			pool = DBConnection.getInstance();
			Connection con = pool.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public Vector<OrderInfoBean> orderfood() {
		Connection con = null;
		PreparedStatement pstmt =  null;
		ResultSet rs = null;
		String sql = null;
		Vector<OrderInfoBean> vlist = new Vector<OrderInfoBean>();
		try {
			con = pool.getConnection();
			sql = "select * from food_sales";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderInfoBean bean = new OrderInfoBean();
				bean.setFoodname(rs.getString("foods"));
				bean.setFoodcount(rs.getInt("food_count"));
				bean.setFoodprice(rs.getInt("Sales_amount"));
				bean.setRoom_no(rs.getString("food_room"));
				bean.setFoodstate(rs.getBoolean("food_state"));
				bean.setOrdertime(rs.getString("sales_day"));
				bean.setFoodid(rs.getString("food_id"));
				vlist.addElement(bean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		
		
		return vlist;
		
	}
	public boolean update(OrderInfoBean bean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection();
			sql = "update food_sales set food_state=? where food_room=? and food_id=? and foods=? and sales_day=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setBoolean(1, bean.isFoodstate());
			pstmt.setString(2, bean.getRoom_no());
			pstmt.setString(3, bean.getFoodid());
			pstmt.setString(4, bean.getFoodname());
			pstmt.setString(5,  bean.getOrdertime());
			if(pstmt.executeUpdate()==1) flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;
	}
	
}

