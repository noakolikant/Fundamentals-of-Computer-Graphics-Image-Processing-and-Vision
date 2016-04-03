package RayTracing;

public class Sphere implements Surface{
	public Vector Center;
	public double radius;
	public int material_index;
	
	public Sphere(double x, double y, double z, double radius, int material_index)
	{
		this.Center = new Vector(x, y, z);
		this.radius = radius;
		this.material_index = material_index;
	}
	
	/*  implemented as explained in presentation */
	public Vector get_intersection_point_with_surface(Vector v)
	{
		// I think that this is correct only if the ray starts from the origin
		// otherwise, v should be a Ray class (start point and a direction)
		// and all the calculations should take it another consideration.
		// Another solution is to move everything to be like the start point of
		// the ray is the origin, and in this case it means to start the function
		// with this.Center.neg(start) and add the function with this.Center.add(start).
		// What do you think ?
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
