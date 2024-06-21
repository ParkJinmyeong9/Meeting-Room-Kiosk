package meeting_room;

public class OrderInfoBean {
	//주문 현황
	String room_no;
	String ordertime;
	int totalprice;
	//주문 현황 디테일
	String foodid;
	String gameid;
	String foodname;
	int foodcount;
	int foodprice;
	String foodrequest;
	boolean foodstate;
	
	String gamename;
	int gamecount;
	int gameprice;
	String gametype;
	boolean gamestate;
	
	

	public String getRoom_no() {
		return room_no;
	}
	public void setRoom_no(String room_no) {
		this.room_no = room_no;
	}
	
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	
	public int getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(int totalprice) {
		this.totalprice = totalprice;
	}
	
	public String getFoodname() {
		return foodname;
	}
	public void setFoodname(String foodname) {
		this.foodname = foodname;
	}
	
	public int getFoodcount() {
		return foodcount;
	}
	public void setFoodcount(int foodcount) {
		this.foodcount = foodcount;
	}
	
	public int getFoodprice() {
		return foodprice;
	}
	public void setFoodprice(int foodprice) {
		this.foodprice = foodprice;
	}
	
	public String getFoodrequest() {
		return foodrequest;
	}
	public void setFoodrequest(String foodrequest) {
		this.foodrequest = foodrequest;
	}
	
	public boolean isFoodstate() {
		return foodstate;
	}
	public void setFoodstate(boolean foodstate) {
		this.foodstate = foodstate;
	}
	
	public String getGamename() {
		return gamename;
	}
	public void setGamename(String gamename) {
		this.gamename = gamename;
	}
	
	public int getGameprice() {
		return gameprice;
	}
	public void setGameprice(int gameprice) {
		this.gameprice = gameprice;
	}
	
	public int getGamecount() {
		return gamecount;
	}
	public void setGamecount(int gamecount) {
		this.gamecount = gamecount;
	}
	
	public String getGametype() {
		return gametype;
	}
	public void setGametype(String gametype) {
		this.gametype = gametype;
	}
	
	public boolean isGamestate() {
		return gamestate;
	}
	public void setGamestate(boolean gamestate) {
		this.gamestate = gamestate;
	}

	public String getGameid() {
		return gameid;
	}
	public void setGameid(String gameid) {
		this.gameid = gameid;
	}
	public String getFoodid() {
		return foodid;
	}
	public void setFoodid(String foodid) {
		this.foodid = foodid;
	}
}
