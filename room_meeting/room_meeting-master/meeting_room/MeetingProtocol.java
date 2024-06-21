package meeting_room;

public class MeetingProtocol {

	public static final String MODE = ":";

	//ID, CHAT, CHATALL , MESSAGE, CHATLIST
	
	//(C->S) ID:aaa
	//(S->C) CHATLIST:aaa;bbb;ccc;홍길동;
	public static final String ID = "ID";
	
	//(C->S) CHAT:받는아이디;메세지 ex)CHAT:bbb;밥먹자
	//(S->C) CHAT:보내는아이디;메세지 ex)CHAT:aaa;밥먹자
	public static final String ORDER = "ORDER";
	
	//(C->S) CHATALL:메세지
	//(S->C) CHATALL:[보내는아이디]메세지
	public static final String CHATALL = "CHATALL";
	
	//(C->S) MESSAGE:받는아이디;쪽지내용 ex)MESSAGE:bbb;밥먹자
	//(S->C) MESSAGE:보내는아이디;쪽지내용 ex)MESSAGE:aaa;밥먹자
	public static final String MESSAGE = "MESSAGE";
	
	//(S->C) CHATLIST:aaa;bbb;ccc;홍길동;
	public static final String CHATLIST = "CHATLIST";
	
	
	
}





