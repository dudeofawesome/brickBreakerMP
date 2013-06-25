/**
 * @author Louis Orleans
 * @date 22 June 2013
 * @homepage http://orleans.pl/brickbreakermp
*/

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.image.BufferedImage;
import org.net.Msg.*;
import org.net.p2p.*;

public class frame extends JFrame implements MouseListener, MouseMotionListener, ActionListener, WindowListener{
	public final static String versionNumber = "0.0";
	private static int FRAMEWIDTH = 800;
	private static int FRAMEHEIGHT = 600;
	private int level = 1;
	private ArrayList<Brick> bricks = new ArrayList<Brick>();
	private ArrayList<Ball> balls = new ArrayList<Ball>();

	public Timer drawTimer = new Timer();
	private final static int DRAWDELAY = 33;
	private TimerTask drawTask = new TimerTask() {
		public void run() {
			// if(gamePaused != true){
			// 	if(gameOver != true){
					// updateBlocks();
					// movePlayer();
					// moveEnemies();
					// if(timeSinceLastPowerup >= NEWPOWERUPMAX){
					// 	timeSinceLastPowerup = 0;
					// 	addPowerup();
					// }
					// else{
					// 	timeSinceLastPowerup++;
					// }
				// }
				// moveParticles();
				repaint();
			// }
			// else{
			// 	repaint();
			// }
		}
	};

	public Timer networkTimer = new Timer();
	private final static int NETWORKDELAY = 33;
	private TimerTask networkTask = new TimerTask() {
		public void run() {

		}
	};

	public static void main(String[] args){
		final frame f = new frame();
		f.addMouseListener(f);
		f.addMouseMotionListener(f);
		f.setSize(FRAMEWIDTH,FRAMEHEIGHT);
		f.init();
		f.setVisible(true);
	}

	frame(){
		super();
	}

	public void init(){
		startGame();
	}

	public void startGame(){
		setupLevel(level * 5 + 20);
		drawTimer.schedule(drawTask, 0, DRAWDELAY);
		networkTimer.schedule(networkTask, 0, NETWORKDELAY);
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
			if(bricks.get(i).x + bricks.get(i).width + _xSpacing + _width > FRAMEWIDTH){
				_yPos += _height + _ySpacing;
				System.out.println((bricks.get(i).x + " " + bricks.get(i).width + " " + _xSpacing) + ">" + FRAMEWIDTH + " " + i);
			}
			else{
				System.out.println((bricks.get(i).x + " " + bricks.get(i).width + " " + _xSpacing) + "<" + FRAMEWIDTH + " " + i);
			}
		}
	}

	@Override
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		BufferedImage image = new BufferedImage(FRAMEWIDTH, FRAMEHEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D ig = image.createGraphics();

		for(int i = 0; i < bricks.size(); i++){
			Brick _brick = bricks.get(i);
			ig.setColor(_brick.color);
			ig.fillRect(_brick.x,_brick.y,_brick.width,_brick.height);
		}
		// Anti-aliasing
		// ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(image, 0, 0, this);
	}

	@Override
	public void mouseMoved(MouseEvent e){
		// System.out.println(e.getPoint());
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
		// System.out.println(e.getPoint());
	}

	@Override
	public void actionPerformed(ActionEvent e){

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
}