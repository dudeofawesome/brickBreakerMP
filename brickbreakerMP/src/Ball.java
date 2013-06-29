import java.awt.Color;
import java.util.ArrayList;

public class Ball{
	public int x = 0;
	public int y = 0;
	public int radius = 8;
	public double angle = - Math.PI / 3;
	public double velocity = 5;
	public Color color = Color.BLUE;

	Ball(int x,int y){
		this.x = x;
		this.y = y;
	}
	
	public void move(ArrayList<Brick> _bricks){
		this.x += Math.cos(angle) * velocity;
		this.y += Math.sin(angle) * velocity;
		if(this.x < 0){
			this.angle += Math.PI;
			this.angle *= -1;
		}
		else if(this.x > frame.FRAMEWIDTH){
			this.angle += Math.PI;
			this.angle *= -1;
		}
		else if(this.y < 0){
			this.angle *= -1;
		}
		else if(this.y > frame.FRAMEHEIGHT){
			this.angle *= -1;
		}
	}
}