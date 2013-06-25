import java.awt.Color;

public class Brick{
	public int x = 0;
	public int y = 0;
	public int width = 80;
	public int height = 30;
	public Color color = Color.WHITE;

	Brick(int x,int y){
		this.x = x;
		this.y = y;
	}
	Brick(int x,int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}