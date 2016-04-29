package RayTracing;

public class Cylinder implements Surface {

	/* note - Cylinder is represented as two disks and pivot to make it easier to get
	 *  intersection point with rays later
	 */
	public Plane top_disk_plane;
	public Plane bottom_disk_plane;
	public Vector bottom_disk_center;
	public Vector top_disk_center;
	public Vector Center;
	public double length;
	public Vector pivot;
	public double radius;
	public int material_index;
	public double max_length_from_body_to_center;

	public Cylinder (double x, double y, double z, double length, double radius, double x_rotation,
			double y_rotation, double z_rotation, int material_index)
	{
		this.radius = radius;
		this.material_index = material_index;

		// Determining Cylinder pivot direction
		this.pivot = new Vector(0, 0, 1);
		this.pivot.rotate_vector('x', x_rotation);
		this.pivot.rotate_vector('y', y_rotation);
		this.pivot.rotate_vector('z', z_rotation);

		Vector Center = new Vector(x, y, z);
		this.Center = Center;
		this.length = length;

		// Determining top and bottom disks' centers
		Vector vector_delta = new Vector(this.pivot);
		vector_delta.multiplyByScalar(length / 2);
		this.bottom_disk_center = new Vector(Center);
		this.bottom_disk_center.substract(vector_delta);
		this.top_disk_center = new Vector(Center);
		this.top_disk_center.add(vector_delta);
		
		this.max_length_from_body_to_center = Math.pow(Math.pow(this.length/2, 2) + Math.pow(this.radius, 2),  0.5);
		
		double offset = (this.pivot.x_cor * this.top_disk_center.x_cor +
				this.pivot.y_cor * this.top_disk_center.y_cor +
				this.pivot.z_cor * this.top_disk_center.z_cor);
		this.top_disk_plane = new Plane(this.pivot, offset, this.material_index);
		
		Vector normal_vector = new Vector(this.pivot);
		normal_vector.multiplyByScalar(-1);
		
		offset = (normal_vector.x_cor * this.bottom_disk_center.x_cor +
				normal_vector.y_cor * this.bottom_disk_center.y_cor +
				normal_vector.z_cor * this.bottom_disk_center.z_cor);

		this.bottom_disk_plane = new Plane(normal_vector, offset, this.material_index);
	}

	private Vector get_intersection_point_with_bottom_disk(Ray r)
	{
		Vector bottom_disk_plane_intersection_point = this.bottom_disk_plane.get_intersection_point_with_surface(r);
		if(null != bottom_disk_plane_intersection_point)
		{
			Vector delata_intersection_point_disk_center = new Vector(bottom_disk_plane_intersection_point);
			delata_intersection_point_disk_center.substract(this.bottom_disk_center);
			if(delata_intersection_point_disk_center.length() <= this.radius)
			{
				return bottom_disk_plane_intersection_point;
			}
		}
		return null;
	}

	private Vector get_intersection_point_with_top_disk(Ray r)
	{
		Vector top_disk_plabe_intersection_point = this.top_disk_plane.get_intersection_point_with_surface(r);
		if(null != top_disk_plabe_intersection_point)
		{
			Vector delata_intersection_point_disk_center = new Vector(top_disk_plabe_intersection_point);
			delata_intersection_point_disk_center.substract(this.top_disk_center);
			if(delata_intersection_point_disk_center.length() <= this.radius)
			{
				return top_disk_plabe_intersection_point;
			}
		}
		return null;
	}

	private Vector get_intersection_point_with_body(Ray r)
	{
		if(r.direction == this.pivot) // parallel to cylinder
		{
			return null;
		}

		//First, get intersection point with infinite cylinder
		/* for a ray p0 + vt, and a cylinder (q - pa - (va*(q – pa) ) va)^2 - r^2 = 0, where q is a point
		 *  (x,y,z) on the cylinder, Va the cylinders direction vector, pa bottom disk center
		 *   we solve:
		 *  (p0 - pa + vt - (va * (p - pa + vt))va)^2 - r^2 = 0
		 *  and get At^2 + Bt + C where
		 *  A = (v -(v * va )va)^2
		 *  B = 2((v - (v * va)va) * (delta_p -(delta_p * va)va))
		 *  C = (delta_p -(delta_p * va)va)^2 - r^2
		 *  and delta_p = p0 -pa 
		 *  Math taken from http://mrl.nyu.edu/~dzorin/cg05/lecture12.pdf*/

		Vector delta_p = new Vector(r.start);
		delta_p.substract(this.bottom_disk_center);  //delta_p = p0- pa 

		double A, B, C;
		Vector v1; // v1 = v -(v * va )va
		Vector v2; // v2 = delta_p -(delta_p * va)*va
		Vector va = new Vector(this.pivot);
		va.normalize();
		Vector v = new Vector(r.direction);
		v.normalize();
		
		v1 = new Vector(v);
		va.multiplyByScalar(v.dot(va));
		v1.substract(va);

		va = new Vector(this.pivot);
		v2 = new Vector(delta_p);
		va.multiplyByScalar(delta_p.dot(va));
		v2.substract(va);

		A = v1.lengthSquared();
		B = 2 * v1.dot(v2);
		C = v2.lengthSquared() - Math.pow(this.radius, 2);

		double Discriminant = Math.pow(B, 2) - 4 * A * C;
		if((0 > Discriminant) || (0 == A))
		{
			return null;
		}

		double chosen_t = 0;
		double t1 = (- B + Math.pow(Discriminant, 0.5)) / 2 / A;
		double t2 = (- B - Math.pow(Discriminant, 0.5)) / 2 / A;
		if((t1 < 0) && (t2 < 0))
		{
			return null;
		}
		else if((t1 < 0) && (t2 > 0))
		{
			chosen_t = t2;
		}
		else if((t1 > 0) && (t2 < 0))
		{
			chosen_t = t1;
		}
		else if((t1 > 0) && (t2 > 0))
		{
			chosen_t = Math.min(t1, t2);
		}
		
		Vector potential_intersection_point = new Vector(r.direction);
		potential_intersection_point.multiplyByScalar(chosen_t);
		potential_intersection_point.add(r.start);
		
		Vector tmp = new Vector(this.Center);
		tmp.substract(potential_intersection_point);
		if(tmp.length() > this.max_length_from_body_to_center)
		{
			return null;
		}
		
		return potential_intersection_point;
	}

	@Override
	public Vector get_intersection_point_with_surface(Ray r)
	{
		Vector top_disk_intersection_point = this.get_intersection_point_with_top_disk(r);
		Vector bottom_disk_intersection_point = this.get_intersection_point_with_bottom_disk(r);
		Vector body_intersection_point = this.get_intersection_point_with_body(r);

		if((null == top_disk_intersection_point) && (null == bottom_disk_intersection_point) &&
				(null == body_intersection_point))
		{
			return null;
		}

		double top_disk_point_dest = 0, bottom_disk_point_dest = 0, body_point_dest = 0;
		if(null != top_disk_intersection_point)
		{
			top_disk_point_dest = top_disk_intersection_point.destinstion_from_point(r.start);
		}
		if(null != bottom_disk_intersection_point)
		{
			bottom_disk_point_dest = bottom_disk_intersection_point.destinstion_from_point(r.start);
		}
		if(null != body_intersection_point)
		{
			body_point_dest = body_intersection_point.destinstion_from_point(r.start);
		}

		double max_dest = Math.max(Math.max(top_disk_point_dest, bottom_disk_point_dest), body_point_dest);
		if(null == top_disk_intersection_point)
		{
			top_disk_point_dest = max_dest + 1;
		}
		if(null == bottom_disk_intersection_point)
		{
			bottom_disk_point_dest = max_dest + 1;
		}
		if(null == body_intersection_point)
		{
			body_point_dest = max_dest + 1;
		}

		double min_dest = Math.min(Math.min(top_disk_point_dest, bottom_disk_point_dest), body_point_dest);
		if(min_dest == top_disk_point_dest)
		{
			return top_disk_intersection_point;
		}
		if(min_dest == bottom_disk_point_dest)
		{
			return bottom_disk_intersection_point;
		}
		return body_intersection_point;
	}

	@Override
	public Ray get_reflection_ray(Vector intersection_point, Ray incomming_ray)
	{
		Vector normal = this.get_normal_direction(intersection_point);
		normal.normalize();
		
		Vector reflection_direction = new Vector(normal);
		reflection_direction.multiplyByScalar(2 * normal.dot(incomming_ray.direction));
		reflection_direction.substract(incomming_ray.direction);
		reflection_direction.multiplyByScalar(-1);
		
		Ray reflection = new Ray(intersection_point, reflection_direction);
		return reflection;	
	}
	
	public int get_material_index() {
		return this.material_index;
	}

	public Vector get_normal_direction(Vector point) {
		//from_center_to_intersection_point - points from the cylinder center to the intersection point
		Vector from_center_to_intersection_point = new Vector(point);
		from_center_to_intersection_point.substract(this.Center);

		Vector normal;

		if(Math.abs(this.bottom_disk_plane.normal.x_cor * point.x_cor +
				this.bottom_disk_plane.normal.y_cor * point.y_cor +
				this.bottom_disk_plane.normal.z_cor * point.z_cor - this.bottom_disk_plane.offset) < 0.0001)
		{
			normal = new Vector(this.bottom_disk_plane.normal);
		}
		else if(Math.abs(this.top_disk_plane.normal.x_cor * point.x_cor +
				this.top_disk_plane.normal.y_cor * point.y_cor +
				this.top_disk_plane.normal.z_cor * point.z_cor - this.top_disk_plane.offset) < 0.0001)
		{
			normal = new Vector(this.top_disk_plane.normal);
		}
		
		
/*
		Vector height_v = new Vector(this.pivot);
		height_v.normalize();
		height_v.multiplyByScalar(this.length/2);
		
		double dist_from_center = height_v.dot(from_center_to_intersection_point);
		if(Math.abs(dist_from_center - this.length / 2) < 0.0001) //intersection point is on top disk
		{
			normal = new Vector(this.pivot);
		}
		else if(Math.abs(dist_from_center + this.length / 2) < 0.0001) //intersection point is on bottom disk
		{
			normal = new Vector(this.pivot);
			normal.multiplyByScalar(-1);
		}*/
		else //intersection point is on the cylinder body
		{
			normal = new Vector(point);
			normal.substract(this.Center);
			
			Vector pivot_hegith = new Vector(this.pivot);
			pivot_hegith.normalize();
			pivot_hegith.multiplyByScalar(this.length/2);
			double proj_on_pivot = normal.dot(pivot_hegith);
			
			Vector pivot_direction_vector_to_substract = new Vector(this.pivot);
			pivot_direction_vector_to_substract.multiplyByScalar(proj_on_pivot);
			
			normal.substract(pivot_direction_vector_to_substract);
		}

		normal.normalize();		
		return normal;
	}
}
