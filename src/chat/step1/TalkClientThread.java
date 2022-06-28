package chat.step1;

import java.util.StringTokenizer;
import java.util.Vector;

public class TalkClientThread extends Thread {
	// 선언부
	TalkClient tc = null;
	
	// 생성자(파라미터값 받기)
	public TalkClientThread(TalkClient tc) {
		this.tc = tc;
	}
	
	/*
	 * 서버에서 말한 내용을 들어봅시다.
	 */
	public void run() {
		boolean isStop = false;
		
		while(!isStop) {
			try {
				String msg = "";//100#apple
				msg = (String)tc.ois.readObject(); // 이게뭐지 talk 클라이언트에서 object input stream를 읽어서 string 타입으로 받겠다는거? 
				StringTokenizer st = null;  // stringtokenizer는 문자열을 분리한다.
				int protocol = 0;//100|200|201|202|500 // 정해놓은 프로토콜 메세지 int 타입
				
				if(msg !=null) { // 메세지 값이 없다면 ? 
					st = new StringTokenizer(msg,"#"); // stringtokenizer 생성하기 (문자열, 구분자) >> 구분자를 기준으로 문자열을 분리한다.
					protocol = Integer.parseInt(st.nextToken());//100 // stringtokenizer에 저장된 문자열을 띄어쓰기 단위로(nexttoken) protocol에 저장한다.
				}
				
				switch(protocol) { // 프로토콜 값이 
					case 100:{//100#apple // 100이면 ?
						String nickName = st.nextToken(); // 닉네임이라는 변수에 stringTokenizer의 띄어쓰기 단위로 니누기/??
						tc.jta_display.append(nickName+"님이 입장하였습니다.\n");
						Vector<String> v = new Vector<>(); // arraylist와 동일한 구조. 배열의 크기가에 따라 자동으로 크기 조절된다. 순서 정렬.. 스레드에서 주로 사용한다.
						v.add(nickName); // vector에 nickname 저장하기..
						tc.dtm.addRow(v); // talkClient 클래스의 defaultTableModel에 로우값 v를 넣는다.
					}break;
					
					case 200:{
						
					}break;
					
					case 201:{ // 201이면??
						String nickName = st.nextToken(); 
						String message = st.nextToken();
						tc.jta_display.append("["+nickName+"]"+message+"\n");
						tc.jta_display.setCaretPosition
						(tc.jta_display.getDocument().getLength());					
					}break;
					case 202:{
						String nickName = st.nextToken();
						String afterName = st.nextToken();
						String message = st.nextToken();
						//테이블에 대화명 변경하기
						for(int i=0;i<tc.dtm.getRowCount();i++) {
							String imsi = (String)tc.dtm.getValueAt(i, 0);
							if(nickName.equals(imsi)) {
								tc.dtm.setValueAt(afterName, i, 0);
								break;
							}
						}
						//채팅창에 타이틀바에도 대화명을 변경처리 한다.
						if(nickName.equals(tc.nickName)) {
							tc.setTitle(afterName+"님의 대화창");
							tc.nickName = afterName;
						}
						tc.jta_display.append(message+"\n");
					}break;
					case 500:{
						String nickName = st.nextToken();
						tc.jta_display.append(nickName+"님이 퇴장 하였습니다.\n");
						tc.jta_display.setCaretPosition
						(tc.jta_display.getDocument().getLength());
						for(int i=0;i<tc.dtm.getRowCount();i++) {
							String n =(String)tc.dtm.getValueAt(i, 0);
							if(n.equals(nickName)) {
								tc.dtm.removeRow(i);
							}
						}
					}break;
				}////////////end of switch
			} catch (Exception e) {
				// TODO: handle exception
			}
		}////////////////////end of while
	}////////////////////////end of run
}


