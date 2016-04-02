package RayTracing;

public class Material {
	ColorAttribute diffusive_color;
	ColorAttribute Specular_color;
	double phong_specularity;
	ColorAttribute Reflection_color;
	double transperacy;
	
	public Material(ColorAttribute diffusive_color, ColorAttribute Specular_color,
			double phong_specularity, ColorAttribute Reflection_color, double transperacy)
	{
		this.diffusive_color = diffusive_color;
		this.Specular_color = Specular_color;
		this.phong_specularity = phong_specularity;
		this.Reflection_color = Reflection_color;
		this.transperacy = transperacy;
	}
}
