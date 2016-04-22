package RayTracing;

public class Plane implements Surface {

	public Vector normal;
	public double offset;
	public int material_index;
	public Vector point_on_plane;
	
	public Plane(Vector normal, double offset, int material_index)
	{
		this.normal = new Vector(normal);
		this.normal.normalize();
		this.offset = offset;
		this.material_index = material_index;
		
		if(0 != this.normal.x_cor)
		{
			this.point_on_plane = new Vector(this.offset / this.normal.x_cor, 0, 0);
		}
		else if(0 != this.normal.y_cor)
		{
			this.point_on_plane = new Vector(0, this.offset / this.normal.y_cor, 0);
		}
		else if(0 != this.normal.z_cor)
		{
			this.point_on_plane = new Vector(0, 0, this.offset / this.normal.z_cor);
		}
	}
	
	
	public Vector get_intersection_point_with_surface(Ray r) {
		if (r.direction.dot(this.normal) == 0) {
			// The ray and the plane are parallel
			return null;
		}
		Vector tmp = new Vector(this.point_on_plane);
		tmp.substract(r.start);
		double t = tmp.dot(this.normal) /
				(r.direction.dot(this.normal));
		if (t < 0) {
			return null;
		}
		else {
			Vector intersection_point = new Vector(r.direction);
			intersection_point.multiplyByScalar(t);
			intersection_point.add(r.start);
			return intersection_point;
		}
	}

	@Override
	public Ray get_reflection_ray(Vector intersection_point, Ray incomming_ray) 
	{
		// R = incomming_ray_vector - 2(<incomming_ray_vector,n>)n
		// note - Are you sure? Look at this http://www.3dkingdoms.com/weekly/weekly.php?a=2 our incoming vector is the other way around
		Vector normal = new Vector(this.normal);
		normal.normalize(); // normalize the normal
		normal.multiplyByScalar(2 * (normal.dot(incomming_ray.direction)));
		Vector reflection_direction = new Vector(incomming_ray.direction);
		reflection_direction.substract(normal);
		Ray reflection = new Ray(intersection_point, reflection_direction);
		return reflection;
	}
	
	public int get_material_index() {
		return this.material_index;
	}

	public Vector get_normal_direction(Vector point)
	{
		return this.normal;
	}
	
}
