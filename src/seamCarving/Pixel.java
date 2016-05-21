package seamCarving;

public class Pixel {
	int x;
	int y;
	
	public Pixel(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Pixel(Pixel p)
	{
		this.x = p.x;
		this.y = p.y;
	}
}
