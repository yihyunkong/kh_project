package chat.step1;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TalkClient extends JFrame implements ActionListener {
	// 선언부
	////////////////통신과 관련한 전역변수 추가 시작/// ///////////
	Socket 				socket 	= null; 
	// 소켓 클래스를 사용해서 클라이언트는 서버와 연결, 서버로 데이터 전송할 수 있다.
	// 서버는 데이터를 리딩할 수 있다.
	
	// 객체의 직렬화 - 자바 시스템 내부에서 사용되는 object 또는 data를 외부 자바 시스템에서도 사용할 수 있도록 byte 형태로 데이터를 변환한다. 
	// >> 프로그램이 실행하는 동안 생성된 객체를 stream을 이용하여 지속적인 보관, 전송할 수 있다. (<-> 역직렬화)
	ObjectOutputStream 	oos 	= null; // 직렬화 - 자바에서 객체 안에 저장되어 있는 내용을 파일로 저장하거나, 네트워크를 통하여 다른 곳으로 전달할 수 있게 한다.
	
	ObjectInputStream 	ois		= null;	// 역직렬화
	 
	String 				nickName= null;//닉네임 등록
	////////////////통신과 관련한 전역변수 추가  끝  //////////////
	
	JPanel 				jp_second	  	= new JPanel();
	JPanel				jp_second_south = new JPanel();
	JButton 			jbtn_one	  	= new JButton("1:1");
	JButton 			jbtn_change	  	= new JButton("대화명변경");
	JButton 			jbtn_font	  	= new JButton("글자색");
	JButton 			jbtn_exit	  	= new JButton("나가기");
	
	String 				cols[] 		  	= {"대화명"};
	String 				data[][] 	  	= new String[0][1];
	// 모델과 데이터를 연결해준다.
	// JTable에 defaultTableModel을 연결함
	DefaultTableModel 	dtm 			= new DefaultTableModel(data,cols);
	
	JTable			  	jtb 			= new JTable(dtm);
	JScrollPane       	jsp 			= new JScrollPane(jtb);
	JPanel 				jp_first 		= new JPanel();
	JPanel 				jp_first_south 	= new JPanel();
	JTextField			jtf_msg 		= new JTextField(20);//south속지 center // 한줄의 문자열을 입력받는 창이다. enter를 누르면 action 발생.. 입력 가능한 문자개수나 입력창의 크기를 조절할 수 있다.
	JButton 			jbtn_send  		= new JButton("전송");//south속지 east
	JTextArea 			jta_display 	= null;
	JScrollPane 		jsp_display 	= null;
	//배경 이미지에 사용될 객체 선언-JTextArea에 페인팅
	Image 				back 			= null;
	
	// 생성자
	public TalkClient() {
		jtf_msg.addActionListener(this);
		jbtn_change.addActionListener(this);
		jbtn_exit.addActionListener(this);
	}
	
	// 화면선언부
	public void initDisplay() {
		//사용자의 닉네임 받기
		nickName = JOptionPane.showInputDialog("닉네임을 입력하세요.");
		this.setLayout(new GridLayout(1,2));
		jp_second.setLayout(new BorderLayout());
		jp_second.add("Center",jsp);
		jp_second_south.setLayout(new GridLayout(2,2));
		jp_second_south.add(jbtn_one);
		jp_second_south.add(jbtn_change);
		jp_second_south.add(jbtn_font);
		jp_second_south.add(jbtn_exit);
		jp_second.add("South",jp_second_south);
		jp_first.setLayout(new BorderLayout());
		jp_first_south.setLayout(new BorderLayout());
		jp_first_south.add("Center",jtf_msg);
		jp_first_south.add("East",jbtn_send);
		back = getToolkit().getImage("src\\chat\\step1\\accountmain.png");
		
		jta_display = new JTextArea() {
			private static final long serialVersionUID = 1L;
			public void paint(Graphics g) {
				g.drawImage(back, 0, 0, this);
				Point p = jsp_display.getViewport().getViewPosition();
				g.drawImage(back, p.x, p.y, null);
				paintComponent(g);
			}
		};
		jta_display.setLineWrap(true);
		jta_display.setOpaque(false);
		Font font = new Font("돋움",Font.BOLD,16);
		jta_display.setFont(font);
		jsp_display = new JScrollPane(jta_display);		
		jp_first.add("Center",jsp_display);
		jp_first.add("South",jp_first_south);
		this.add(jp_first);
		this.add(jp_second);
		this.setTitle(nickName);
		this.setSize(800, 550);
		this.setVisible(true);
	}
	
	// 메인메소드
	public static void main(String args[]) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		TalkClient tc = new TalkClient();
		tc.initDisplay();
		tc.init();
	}
	
	//소켓 관련 초기화
	public void init() {
		try {
			//서버측의 ip주소 작성하기
			socket = new Socket("127.0.0.1", 3002); // 서버에 대한 연결을 시작하는 부분 (ip-host, 포트번호)
			oos = new ObjectOutputStream(socket.getOutputStream()); // 소켓으로 연결한 ip주소와 포트번호를 이용하여 objectoutstream의 객체 oos에 주입한다. 
			ois = new ObjectInputStream(socket.getInputStream()); // socket이라는 객체에 저장된 값을 이용하여 해당 경로에 저장된 object(객체)를 읽어오기
			//initDisplay에서 닉네임이 결정된 후 init메소드가 호출되므로
			
			//서버에게 내가 입장한 사실을 알린다.(말하기)
			oos.writeObject(100+"#"+nickName); // 원하는 데이터 (      )를 보여주기 >> 닉네임과 숫자를 불러옴
			
			//서버에 말을 한 후 들을 준비를 한다.
			TalkClientThread tct = new TalkClientThread(this);
			tct.start();
		} catch (Exception e) {
			//예외가 발생했을 때 직접적인 원인되는 클래스명 출력하기
			System.out.println(e.toString());
		}
	}
	 
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object obj = ae.getSource();
		String msg = jtf_msg.getText();
		if(jtf_msg==obj) {
			try {
				oos.writeObject(201
						   +"#"+nickName
						   +"#"+msg);
				jtf_msg.setText("");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		else if(jbtn_exit==obj) {
			try {
				oos.writeObject(500+"#"+this.nickName);
				//자바가상머신과 연결고리 끊기
				System.exit(0);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		else if(jbtn_change == obj) {
			String afterName = JOptionPane.showInputDialog("변경할 대화명을 입력하세요.");
			if(afterName == null || afterName.trim().length()<1) {
				JOptionPane.showMessageDialog(this
				, "변경할 대화명을 입력하세요"
				, "INFO", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			try {
				oos.writeObject(202
						   +"#"+nickName
						   +"#"+afterName
						   +"#"+nickName+"의 대화명이 "+afterName+"으로 변경되었습니다.");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}//////////////////////end of actionPerformed
}

