/**
 * @author Louis Orleans
 * @date 22 June 2013
 * @homepage http://orleans.pl/brickbreakermp
*/

import javax.swing.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.image.BufferedImage;

import org.net.Msg.*;
import org.net.p2p.Msg;
import org.net.p2p.*;

import java.net.*;
import java.io.*;

public class frame extends JFrame implements MouseListener, MouseMotionListener, ActionListener, WindowListener, KeyListener{
	public final static String versionNumber = "0.0";
	public static int FRAMEWIDTH = 1100;
	public static int FRAMEHEIGHT = 600;
	private final static int PORTNUMBER = 27425;
	
	private int level = 1;
	private ArrayList<Brick> bricks = new ArrayList<Brick>();
	private ArrayList<Ball> balls = new ArrayList<Ball>();
	private ArrayList<Paddle> paddles = new ArrayList<Paddle>();
	private enum GameMode {CONNECT,SETTINGUP,PLAYING,SCORE};
	private GameMode currentGameMode = GameMode.CONNECT;
	private String myLocalIP = "unknown";
	private String myExternalIP = "unknown";
	private jnmp2p jnm;
	Connection conn;
	private int playerID;

	private TextField txtIP = new TextField(20);
	private Button btnConnect = new Button("Connect");
	private Button btnPlay = new Button("Single Player");

	public Timer timer = new Timer();
	private final static int DRAWDELAY = 16;
	private int networkTimer = 0;
	private final int NETWORKFREQUENCY = 6;
	private TimerTask drawTask = new TimerTask() {
		public void run() {
//			 if(gamePaused != true){
//			 	if(gameOver != true){
//					 updateBlocks();
//					 movePlayer();
//					 moveEnemies();
//					 if(timeSinceLastPowerup >= NEWPOWERUPMAX){
//					 	timeSinceLastPowerup = 0;
//					 	addPowerup();
//					 }
//					 else{
//					 	timeSinceLastPowerup++;
//					 }
//				 }
//				 moveParticles();
//			 }
//			 else{
//			 	repaint();
//			 }
			
			if(currentGameMode == GameMode.PLAYING)
				for(Ball _ball : balls)
					_ball.move(bricks);
			
			repaint();
			if (networkTimer >= NETWORKFREQUENCY) {
//				send network data

				networkTimer = 0;
			}
			else
				networkTimer++;
		}
	};

	public static void main(String[] args){
		final frame f = new frame();
		f.addMouseListener(f);
		f.addMouseMotionListener(f);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(FRAMEWIDTH,FRAMEHEIGHT);
		f.setResizable(false);
		f.setLayout(null);
		Protocol protocol = new Protocol(f);
		protocol.addMsgHandler("receiveData");
		f.jnm = new jnmp2p(protocol, PORTNUMBER);
		f.init();
		f.setVisible(true);
	}

	frame(){
		super();

//		Socket s = new Socket("www", 80);
//		InetAddress current_addr = s.getLocalAddress();
//		s.close();
		
//		if (current_addr instanceof Inet4Address)
//			myLocalIP = current_addr.getHostAddress();
//		else if (current_addr instanceof Inet6Address)
//			myLocalIP = current_addr.getHostAddress();

		try{
			URL _whatismyip = new URL("http://checkip.amazonaws.com/");
			BufferedReader _in;
			_in = new BufferedReader(new InputStreamReader(_whatismyip.openStream()));
			myExternalIP = _in.readLine();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	public void init(){
//		have user input IP address of 2nd client
		txtIP.setBounds(315,15,100,20);
		btnConnect.setBounds(420,15,65,21);
		btnPlay.setBounds(400,40,85,21);
		btnConnect.addActionListener(this);
		btnPlay.addActionListener(this);
		add(txtIP);
		add(btnConnect);
		add(btnPlay);
	}

	public void connected(Connection conn, Msg msg){
		startGame(2);
	}

	public void startGame(int _numOfPlayers){
		this.remove(btnConnect);
		this.remove(btnPlay);
		this.remove(txtIP);
		
		if(level > 30)
			level = 30;
		setupLevel(level * 5 + 20);
		addPlayers(_numOfPlayers);
		addBalls(_numOfPlayers);
		timer.schedule(drawTask, 0, DRAWDELAY);
		currentGameMode = GameMode.PLAYING;
//		show cursor
//		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//		hide cursor
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image _image = toolkit.getImage("cursor.gif");
		Cursor _cursor = toolkit.createCustomCursor(_image , new Point(0,0), "img");
		setCursor(_cursor);
	}

	private void addBalls(int _numOfPlayers) {
		if(_numOfPlayers == 1){
			playerID = 0;
			balls.add(new Ball(FRAMEWIDTH / 2 - 50,FRAMEHEIGHT - 56));
		}
		else if(_numOfPlayers == 2){
			playerID = 0;
			balls.add(new Ball(FRAMEWIDTH / 4 - 50,FRAMEHEIGHT - 56));
			balls.add(new Ball(FRAMEWIDTH * 3 / 4 - 50,FRAMEHEIGHT - 56));
		}
	}

	private void addPlayers(int _numOfPlayers) {
		if(_numOfPlayers == 1){
			playerID = 0;
			paddles.add(new Paddle(FRAMEWIDTH / 2 - 50,FRAMEHEIGHT - 40));
		}
		else if(_numOfPlayers == 2){
			playerID = 0;
			paddles.add(new Paddle(FRAMEWIDTH / 4 - 50,FRAMEHEIGHT - 40));
			paddles.add(new Paddle(FRAMEWIDTH * 3 / 4 - 50,FRAMEHEIGHT - 40));
		}
	}

	private void setupLevel(int _numOfBricks){
		int _yPos = 40;
		int _xPos = 20;
		int _xSpacing = 2;
		int _ySpacing = 2;
		int _width = 80;
		int _height = 30;

		bricks.add(new Brick(_xPos,_yPos));
		for(int i = 1; i < _numOfBricks; i++){
			if(_yPos == bricks.get(i - 1).y)
				bricks.add(new Brick(bricks.get(i - 1).x + bricks.get(i - 1).width + _xSpacing,_yPos));
			else
				bricks.add(new Brick(_xPos,_yPos));
			if(bricks.get(i).x + bricks.get(i).width + _xSpacing + _width > FRAMEWIDTH)
				_yPos += _height + _ySpacing;
		}
	}

	public void receiveData(Connection conn, Msg msg){
		
	}

	@Override
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		BufferedImage image = new BufferedImage(FRAMEWIDTH, FRAMEHEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D ig = image.createGraphics();

		if(currentGameMode == GameMode.CONNECT){
			ig.setFont(new Font("Arial", Font.PLAIN, 25));
			ig.drawString("Other player's IP address: ",30,60);
			ig.setFont(new Font("Arial", Font.PLAIN, 10));
			ig.drawString("Your local IP (use this one if in the same building): " + myLocalIP,30,80);
			ig.drawString("Your external IP (use if not in the same building): " + myExternalIP,30,95);
		}
		if(currentGameMode == GameMode.SETTINGUP){
			ig.setFont(new Font("Arial", Font.PLAIN, 30));
			ig.drawString("Connecting",30,60);
		}
		else if(currentGameMode == GameMode.PLAYING){
//			draw bricks
			for(int i = 0; i < bricks.size(); i++){
				Brick _brick = bricks.get(i);
				ig.setColor(_brick.color);
				ig.fillRect(_brick.x,_brick.y,_brick.width,_brick.height);
			}
//			draw paddles
			for(int i = 0; i < paddles.size(); i++){
				Paddle _paddle = paddles.get(i);
				ig.setColor(_paddle.color);
				ig.fillRect(_paddle.x,_paddle.y,_paddle.width,_paddle.height);
			}
//			turn on anti-aliasing
			ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//			draw balls
			for(int i = 0; i < balls.size(); i++){
				Ball _ball = balls.get(i);
				ig.setColor(_ball.color);
				ig.fillOval(_ball.x,_ball.y,_ball.radius * 2,_ball.radius * 2);
			}
//			turn off anti-aliasing
			ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
		else if(currentGameMode == GameMode.SCORE){

		}
		g2d.drawImage(image, 0, 0, this);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnConnect){
			String _IP = txtIP.getText().replaceAll("\\s","");
			System.out.println("ip:" + _IP);
			if(!_IP.equals("")){
				currentGameMode = GameMode.SETTINGUP;
				conn = jnm.connect(_IP);
//				startRead();
				startGame(2);
			}
			else{
				txtIP.setText("");
				txtIP.requestFocus();
			}
		}
		else if(e.getSource() == btnPlay){
			startGame(1);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e){
//		 System.out.println(e.getPoint());
		if(currentGameMode == GameMode.PLAYING)
			paddles.get(playerID).x = e.getX() - 50;
		
	}

	@Override
	public void mouseDragged(MouseEvent e){

	}

	@Override
	public void mouseExited(MouseEvent e){

	}

	@Override
	public void mouseEntered(MouseEvent e){

	}

	@Override
	public void mouseReleased(MouseEvent e){

	}

	@Override
	public void mousePressed(MouseEvent e){

	}

	@Override
	public void mouseClicked(MouseEvent e){
//		 System.out.println(e.getPoint());
	}

	@Override
	public void windowDeactivated(WindowEvent e){

	}

	@Override
	public void windowActivated(WindowEvent e){

	}

	@Override
	public void windowDeiconified(WindowEvent e){

	}

	@Override
	public void windowIconified(WindowEvent e){

	}

	@Override
	public void windowClosed(WindowEvent e){

	}

	@Override
	public void windowClosing(WindowEvent e){

	}

	@Override
	public void windowOpened(WindowEvent e){

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}