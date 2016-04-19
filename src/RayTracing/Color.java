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
	
	public void add(Color other)
	{
		this.red += other.red;
		this.blue += other.blue;
		this.green += other.green;
	}
	
	public void multiply_with_colorAttribute(ColorAttribute color_attribute)
	{
		this.red *= color_attribute.red;
		this.blue *= color_attribute.blue;
		this.green *= color_attribute.green;
	}
	
	public void multiply_with_scalar(double scalar)
	{
		this.red *= scalar;
		this.blue *= scalar;
		this.green *= scalar;
	}
}
