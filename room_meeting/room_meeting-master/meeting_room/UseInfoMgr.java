package meeting_room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;



public class UseInfoMgr {
	
	private DBConnection pool;
	
	public UseInfoMgr() {
		try {
			pool = DBConnection.getInstance();
			Connection con = pool.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Vector<UseInfoBean>	useAll() {
		Connection con = null;
		PreparedStatement pstmt =  null;
		ResultSet rs = null;
		String sql = null;
		Vector<UseInfoBean> vlist = new Vector<UseInfoBean>();
		try {
			con = pool.getConnection();
			sql = "select * from `use`";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				UseInfoBean bean = new UseInfoBean();
				bean.setRoom_num(rs.getString("room_num"));
				bean.setUse_id(rs.getString("use_id"));
				bean.setUse_day(rs.getString("use_day"));
				bean.setIn_time(rs.getString("in_time"));
				bean.setOut_time(rs.getString("out_time"));
				bean.setUse_total(rs.getString("use_total"));
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
