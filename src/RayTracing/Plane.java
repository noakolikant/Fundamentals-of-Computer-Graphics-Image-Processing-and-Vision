package RayTracing;

public class Plane implements Surface {

	public Vector normal;
	public double offset;
	public int material_index;
	
	public Plane(Vector normal, double offset, int material_index)
	{
		this.normal = new Vector(normal);
		this.normal.normalize();
		this.offset = offset;
		this.material_index = material_index;
	}
	
	public Vector get_intersection_point_with_surface(Ray r) {
		if (r.direction.dot(this.normal) == 0) {
			// The ray and the plane are parallel
			return null;
		}
		double t = - ((r.start.dot((this.normal)) + this.offset)) /
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
		Vector normal = new Vector(this.normal);
		normal.normalize();

		Vector reflection_direction = new Vector(normal);
		reflection_direction.multiplyByScalar(2 * normal.dot(incomming_ray.direction));
		reflection_direction.add(incomming_ray.direction);
		//R = 2 * dot(normal, incomming_ray_vector) + incomming_ray_vector
		Ray reflection = new Ray(intersection_point, reflection_direction);

		return reflection;
	}
}
