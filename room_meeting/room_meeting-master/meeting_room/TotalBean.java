package meeting_room;

public class TotalBean {
	private String room;
	private String id;
	private String day;
	private String intime;
	private String outtime;
	private int food_total;
	private int game_total;
	private int room_total;
	private int total;
	
	public int getFood_total() {
		return food_total;
	}
	public void setFood_total(int food_total) {
		this.food_total = food_total;
	}
	public int getGame_total() {
		return game_total;
	}
	public void setGame_total(int game_total) {
		this.game_total = game_total;
	}
	public int getRoom_total() {
		return room_total;
	}
	public void setRoom_total(int room_total) {
		this.room_total = room_total;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getID() {
		return id;
	}
	public void setID(String id) {
		this.id = id;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getIntime() {
		return intime;
	}
	public void setIntime(String intime) {
		this.intime = intime;
	}
	public String getOuttime() {
		return outtime;
	}
	public void setOuttime(String outtime) {
		this.outtime = outtime;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}