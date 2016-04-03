package RayTracing;

public class Sphere implements Surface{
	public Vector Center;
	public double radius;
	
	public Sphere(double x, double y, double z, double radius)
	{
		this.Center = new Vector(x, y, z);
		this.radius = radius;
	}
	
	/*  implemented as explained in presentation */
	public Vector get_intersection_point_with_surface(Vector v)
	{
		double t_ca = v.dot(this.Center);
		if (0 > t_ca)
		{
			return null;
		}
		
		double d_squared = this.Center.dot(this.Center) - Math.pow(t_ca, 2);
		if(d_squared > Math.pow(this.radius, 2))
		{
			return null;
		}
		
		double t_hc = Math.sqrt(Math.pow(this.radius, 2) - d_squared);
		double t = t_ca - t_hc;
		
		Vector intersection_point = new Vector(v);
		intersection_point.multiplyByScalar(t);
		return intersection_point;
	}

}
