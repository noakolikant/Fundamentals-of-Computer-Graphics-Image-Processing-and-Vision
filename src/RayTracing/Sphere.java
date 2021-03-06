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
	public Vector get_intersection_point_with_surface(Ray r)
	{
		// if the ray starts in the sphere, it shouldn't intersect with it again
		Vector start_length = new Vector(r.start);
		start_length.substract(this.Center);
		if (start_length.length() < this.radius - (this.radius / 100)) {
			return null;
		}
		Vector L_vector = new Vector(this.Center);
		L_vector.substract(r.start); // L = O - P0
		double t_ca = r.direction.dot(L_vector); //t_ca = dot(L, V)
		if (0 > t_ca)
		{
			return null;
		}
		
		double d_squared = L_vector.dot(L_vector) - Math.pow(t_ca, 2); // d^2 = dot(L, L) - t_ca^2
		if(d_squared > Math.pow(this.radius, 2))
		{
			return null;
		}
		
		double t_hc = Math.sqrt(Math.pow(this.radius, 2) - d_squared); // (r^2 - d^2)^0.5
		double t = t_ca - t_hc;
		
		// P = P0 + tV
		Vector intersection_point = new Vector(r.direction);
		intersection_point.multiplyByScalar(t);
		intersection_point.add(r.start);
		
		return intersection_point;
	}

	public Ray get_reflection_ray(Vector intersection_point, Ray incomming_ray)
	{
		// normal to the sphere at intersection point
		Vector normal = this.get_normal_direction(intersection_point);
		normal.normalize();
		
		Vector reflection_direction = new Vector(normal);
		reflection_direction.multiplyByScalar(2 * normal.dot(incomming_ray.direction));
		reflection_direction.substract(incomming_ray.direction);
		reflection_direction.multiplyByScalar(-1);
		//R = 2 * dot(normal, incomming_ray_vector) - incomming_ray_vector
		Ray reflection = new Ray(intersection_point, reflection_direction);
		
		return reflection;
	}
	
	public int get_material_index() {
		return this.material_index;
	}

	public Vector get_normal_direction(Vector point) 
	{
		Vector normal = new Vector(point);
		normal.substract(this.Center);
		normal.normalize();
		return normal;
	}
}
