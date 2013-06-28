import java.awt.Color;

public class Ball{
	public int x = 0;
	public int y = 0;
	public int radius = 10;
	public double angle = 0;
	public double velocity = 5;
	public Color color = Color.RED;

	Ball(int x,int y){
		this.x = x;
		this.y = y;
	}
	
	public void move(){
		this.x += Math.cos(angle) * velocity;
		this.y += Math.sin(angle) * velocity;
	}
}