package meeting_room;

public class OrderBean {
	String order_id ;
	String order_room;
	String order_time;
	int order_total;
	String order_request;
	boolean order_state;
	
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOrder_room() {
		return order_room;
	}
	public void setOrder_room(String order_room) {
		this.order_room = order_room;
	}
	public String getOrder_time() {
		return order_time;
	}
	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}
	public int getOrder_total() {
		return order_total;
	}
	public void setOrder_total(int order_total) {
		this.order_total = order_total;
	}
	public String getOrder_request() {
		return order_request;
	}
	public void setOrder_request(String order_request) {
		this.order_request = order_request;
	}
	public boolean isOrder_state() {
		return order_state;
	}
	public void setOrder_state(boolean order_state) {
		this.order_state = order_state;
	}
}
