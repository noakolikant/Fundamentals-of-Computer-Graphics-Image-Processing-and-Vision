package RayTracing;

import java.util.ArrayList;
import java.util.List;

public class Cube implements Surface {

	public Vector center;
	public List<Plane> sides;
	Vector new_x_axis;
	Vector new_y_axis;
	Vector new_z_axis;
	public double length;
	public int material_index;
	
	public Cube(double x, double y, double z, double length, double x_rotation,
			double y_rotation, double z_rotation, int material_index)
	{
		this.center = new Vector(x, y, z);
		this.length = length;
		this.material_index = material_index;
		this.sides = new ArrayList<Plane>();
		
		List<Vector> sides_vectors = new ArrayList<Vector>();
		List<Double> offsets = new ArrayList<Double>();
		Vector v;
		v = new Vector(0, 0, 1);
		sides_vectors.add(v);
		v = new Vector(0, 0, 1);
		sides_vectors.add(v);
		v = new Vector(1, 0, 0);
		sides_vectors.add(v);
		v = new Vector(1, 0, 0);
		sides_vectors.add(v);
		v = new Vector(0, 1, 0);
		sides_vectors.add(v);
		v = new Vector(0, 1, 0);
		sides_vectors.add(v);
		
		this.new_x_axis = new Vector(1, 0, 0);
		this.new_y_axis = new Vector(0, 1, 0);
		this.new_z_axis = new Vector(0, 0, 1);
		
		this.new_x_axis.rotate_vector('x', x_rotation);
		this.new_x_axis.rotate_vector('y', y_rotation);
		this.new_x_axis.rotate_vector('z', z_rotation);

		this.new_y_axis.rotate_vector('x', x_rotation);
		this.new_y_axis.rotate_vector('y', y_rotation);
		this.new_y_axis.rotate_vector('z', z_rotation);
		
		this.new_z_axis.rotate_vector('x', x_rotation);
		this.new_z_axis.rotate_vector('y', y_rotation);
		this.new_z_axis.rotate_vector('z', z_rotation);
		
		for(int i = 0; i < sides_vectors.size(); i++)
		{
			sides_vectors.get(i).rotate_vector('x', x_rotation);
			sides_vectors.get(i).rotate_vector('y', y_rotation);
			sides_vectors.get(i).rotate_vector('z', z_rotation);
			double offset = sides_vectors.get(i).x_cor * this.center.x_cor+
					sides_vectors.get(i).y_cor * this.center.y_cor +
					sides_vectors.get(i).z_cor * this.center.z_cor;
			offsets.add(offset);
		}
		
		Plane p;
		p = new Plane(sides_vectors.get(0), offsets.get(0) + this.length / 2, this.material_index);
		this.sides.add(p);
		p = new Plane(sides_vectors.get(1), offsets.get(1) - this.length / 2, this.material_index);
		this.sides.add(p);
		
		p = new Plane(sides_vectors.get(2), offsets.get(2) + this.length / 2, this.material_index);
		this.sides.add(p);
		p = new Plane(sides_vectors.get(3), offsets.get(3) - this.length / 2, this.material_index);
		this.sides.add(p);
		
		p = new Plane(sides_vectors.get(4), offsets.get(4) + this.length / 2, this.material_index);
		this.sides.add(p);
		p = new Plane(sides_vectors.get(5), offsets.get(5) - this.length / 2, this.material_index);
		this.sides.add(p);
	}
	
	@Override
	public Vector get_intersection_point_with_surface(Ray r) {
		double min_dest = -1;
		Vector intersection_point = null, potential_intersection_point = null, distance_v, distance_from_center;
		
		for(int i = 0; i < this.sides.size(); i++)
		{
			potential_intersection_point = this.sides.get(i).get_intersection_point_with_surface(r);
			if(potential_intersection_point == null)
			{
				continue;
			}
			distance_from_center = new Vector(this.center);
			distance_from_center.substract(potential_intersection_point);
			//TODO check if on cube's side
			
			double proj_new_x, proj_new_y, proj_new_z;
			proj_new_x = Math.abs(this.new_x_axis.dot(distance_from_center));
			proj_new_y = Math.abs(this.new_y_axis.dot(distance_from_center));
			proj_new_z = Math.abs(this.new_z_axis.dot(distance_from_center));

			if((proj_new_x - 0.001 > this.length / 2) ||
					(proj_new_y - 0.001 > this.length / 2) ||
					(proj_new_z - 0.001 > this.length / 2))
			{
				continue;
			}
			distance_v = new Vector(potential_intersection_point);
			distance_v.substract(r.start);
			if((intersection_point == null) || (min_dest > distance_v.length()))
			{
				min_dest = distance_v.length();
				intersection_point = potential_intersection_point;
			}
		}
		return intersection_point;
	}

	public Ray get_reflection_ray(Vector intersection_point, Ray incomming_ray) {
		Vector normal = this.get_normal_direction(intersection_point);
		normal.normalize();

		Vector reflection_direction = new Vector(normal);
		reflection_direction.multiplyByScalar(2 * normal.dot(incomming_ray.direction));
		reflection_direction.add(incomming_ray.direction);
		//R = 2 * dot(normal, incomming_ray_vector) + incomming_ray_vector
		Ray reflection = new Ray(intersection_point, reflection_direction);

		return reflection;	
	}

	public int get_material_index() {
		return this.material_index;
	}

	public Vector get_normal_direction(Vector point) {
		for(int i = 0; i < this.sides.size(); i++)
		{
			double tmp = this.sides.get(i).normal.x_cor * point.x_cor + 
					this.sides.get(i).normal.y_cor * point.y_cor + 
					this.sides.get(i).normal.z_cor * point.z_cor;
			if(Math.abs(tmp - this.sides.get(i).offset) < 0.001)
			{
				return this.sides.get(0).normal;
			}
		}
		return null;
	}

}
