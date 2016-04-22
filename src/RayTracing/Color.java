package RayTracing;

public class Color {
	public int red;
	public int green;
	public int blue;
	
	public Color()
	{
		this.red = (int) 255;
		this.green = (int) 255;
		this.blue = (int) 255;
	}
	
	public Color(int red, int blue, int green)
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
