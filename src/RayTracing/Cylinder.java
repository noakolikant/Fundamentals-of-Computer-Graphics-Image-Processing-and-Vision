package RayTracing;

public class Cylinder implements Surface {
	
	/* note - Cylinder is represented as two disks and pivot to make it easier to get
	 *  intersection point with rays later
	 */
	public Vector bottom_disk_center;
	public Vector top_disk_center;
	public Vector pivot;
	public double radius;
	
	public Cylinder (double x, double y, double z, double length, double radius, double x_rotation,
			double y_rotation, double z_rotation)
	{
		this.radius = radius;
		
		// Determining Cylinder pivot direction
		this.pivot = new Vector(0, 0, 1);
		this.pivot.rotate_vector('x', x_rotation);
		this.pivot.rotate_vector('y', y_rotation);
		this.pivot.rotate_vector('z', z_rotation);
		
		Vector Center = new Vector(x, y, z);

		// Determining top and bottom disks' centers
		Vector vector_delta = new Vector(this.pivot);
		vector_delta.multiplyByScalar(length / 2);
		this.bottom_disk_center = new Vector(Center);
		this.bottom_disk_center.substract(vector_delta);
		this.top_disk_center = new Vector(Center);
		this.top_disk_center.add(vector_delta);
	}
	
	@Override
	public Vector get_intersection_point_with_surface(Ray r) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ray get_mirrror_ray(Vector intersection_point, Ray incomming_ray) {
		// TODO Auto-generated method stub
		return null;
	}

}
