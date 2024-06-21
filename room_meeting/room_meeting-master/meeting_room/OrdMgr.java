package meeting_room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class OrdMgr {
	private DBConnection pool;
		
	public OrdMgr() {
			try {
				pool = DBConnection.getInstance();
				Connection con = pool.getConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	public Vector<OrderInfoBean>ordergame(){
		Connection con = null;
		PreparedStatement pstmt =  null;
		ResultSet rs = null;
		String sql = null;
		Vector<OrderInfoBean> vlist = new Vector<OrderInfoBean>();
	
		try{
			con = pool.getConnection();
			sql = "select * from game_sales";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderInfoBean bean = new OrderInfoBean();
				bean.setGamename(rs.getString("games"));
				bean.setGameprice(rs.getInt("sales_amount"));
				bean.setOrdertime(rs.getString("sales_day"));
				bean.setRoom_no(rs.getString("game_room"));
				bean.setGamestate(rs.getBoolean("game_state"));
				bean.setGamecount(rs.getInt("game_count"));
				vlist.addElement(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally {
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
			sql = "update game_sales set game_state=? where game_room=? and game_id=? and games=? and sales_day=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setBoolean(1, bean.isGamestate());
			pstmt.setString(2, bean.getRoom_no());
			pstmt.setString(3, bean.getGameid());
			pstmt.setString(4, bean.getGamename());
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

