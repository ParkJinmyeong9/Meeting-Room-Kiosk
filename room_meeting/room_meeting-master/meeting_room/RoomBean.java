package meeting_room;

public class RoomBean {
	
	private String room;
	private int rperson;
	private int rprice;
	private int radd;
	private boolean rstate = false;
	
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public int getRperson() {
		return rperson;
	}
	public void setRperson(int person) {
		this.rperson = person;
	}
	public int getRprice() {
		return rprice;
	}
	public void setRprice(int price) {
		this.rprice = price;
	}
	public int getRadd() {
		return radd;
	}
	public void setRadd(int add) {
		this.radd = add;
	}
	public boolean getRstate() {
		return rstate;
	}
	public void setRstate(boolean state) {
		this.rstate = state;
	}
	public void reserveRoom() {
        this.rstate = true;
    }
	
}