package RayTracing;

public class Color {
	public byte red;
	public byte green;
	public byte blue;
	
	public Color()
	{
		this.red = 0;
		this.green = 0;
		this.blue = 0;
	}
	
	public Color(byte red, byte blue, byte green)
	{
		this.red = red;
		this.blue = blue;
		this.green = green;
	}
	
	public Color(Color other) {
		this.red = other.red;
		this.green = other.green;
		this.blue = other.blue;
	}
}
