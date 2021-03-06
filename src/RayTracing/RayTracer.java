package RayTracing;

import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {

	public int imageWidth;
	public int imageHeight;
	public Camera camera;
	public Color background_color;
	public int shadow_rays_num;
	public int max_recursion_level;
	List<Surface> surfaces_list;
	List<Material> materials_list;
	List<LightSource> light_sources_list;
	
	public RayTracer()
	{
		this.surfaces_list = new ArrayList<Surface>();
		this.materials_list = new ArrayList<Material>();
		this.light_sources_list = new ArrayList<LightSource>();
	}

	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size as input.
	 */
	public static void main(String[] args) {

		try {

			RayTracer tracer = new RayTracer();
            // Default values:
			tracer.imageWidth = 500;
			tracer.imageHeight = 500;

			if (args.length < 2)
				throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

			String sceneFileName = args[0];
			String outputFileName = args[1];

			if (args.length > 3)
			{
				tracer.imageWidth = Integer.parseInt(args[2]);
				tracer.imageHeight = Integer.parseInt(args[3]);
			}


			// Parse scene file:
			tracer.parseScene(sceneFileName);
			// Render scene:
			tracer.renderScene(outputFileName);

//		} catch (IOException e) {
//			System.out.println(e.getMessage());
		} catch (RayTracerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}


	}

	/**
	 * Parses the scene file and creates the scene. Change this function so it generates the required objects.
	 */
	public void parseScene(String sceneFileName) throws IOException, RayTracerException
	{
		FileReader fr = new FileReader(sceneFileName);

		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		System.out.println("Started parsing scene file " + sceneFileName);
				
		while ((line = r.readLine()) != null)
		{
			line = line.trim();
			++lineNum;

			if (line.isEmpty() || (line.charAt(0) == '#'))
			{  // This line in the scene file is a comment
				continue;
			}
			else
			{
				String code = line.substring(0, 3).toLowerCase();
				// Split according to white space characters:
				String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

				if (code.equals("cam"))
				{
					Vector position = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					Vector look_at_point = new Vector(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]));
					Vector up_vector = new Vector(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
							Double.parseDouble(params[8]));
					
					this.camera = new Camera(position, look_at_point, up_vector, Double.parseDouble(params[9]),
							Double.parseDouble(params[10]));
					
					System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
				}
				else if (code.equals("set"))
				{
					//this.background_color = new Color((int)(255 * Double.parseDouble(params[0])), 
					//		(int)(255 * Double.parseDouble(params[1])), (int)(255 *Double.parseDouble(params[2])));
					ColorAttribute background_color_attribute = new ColorAttribute(Double.parseDouble(params[0]), 
							Double.parseDouble(params[1]), Double.parseDouble(params[2]));
					this.background_color = new Color();
					this.background_color.multiply_with_colorAttribute(background_color_attribute);
					this.shadow_rays_num = Integer.parseInt(params[3]);
					this.max_recursion_level = Integer.parseInt(params[4]);

					System.out.println(String.format("Parsed general settings (line %d)", lineNum));
				}
				else if (code.equals("mtl"))
				{
					ColorAttribute diffusive_color = new ColorAttribute(Double.parseDouble(params[0]), 
							Double.parseDouble(params[1]), Double.parseDouble(params[2]));
					ColorAttribute Specular_color = new ColorAttribute(Double.parseDouble(params[3]), 
							Double.parseDouble(params[4]), Double.parseDouble(params[5]));
					ColorAttribute Reflection_color = new ColorAttribute(Double.parseDouble(params[6]), 
							Double.parseDouble(params[7]), Double.parseDouble(params[8]));
					
					Material material = new Material(diffusive_color, Specular_color, Double.parseDouble(params[9]),
							Reflection_color, Double.parseDouble(params[10]));
					
					this.materials_list.add(material);

					System.out.println(String.format("Parsed material (line %d)", lineNum));
				}
				else if (code.equals("sph"))
				{
					Sphere sph = new Sphere(Double.parseDouble(params[0]), 
							Double.parseDouble(params[1]), Double.parseDouble(params[2]),
							Double.parseDouble(params[3]), Integer.parseInt(params[4]));
					this.surfaces_list.add(sph);

					System.out.println(String.format("Parsed sphere (line %d)", lineNum));
				}
				else if (code.equals("cub"))
				{	
					Cube cube = new Cube(Double.parseDouble(params[0]),
							Double.parseDouble(params[1]), Double.parseDouble(params[2]),
							Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]), Double.parseDouble(params[6]),
							Integer.parseInt(params[7]));
					this.surfaces_list.add(cube);
							
					System.out.println(String.format("Parsed cube (line %d)", lineNum));
				}
				else if (code.equals("pln"))
				{
					Plane plane = new Plane(new Vector(Double.parseDouble(params[0]),
							Double.parseDouble(params[1]), Double.parseDouble(params[2])),
							Double.parseDouble(params[3]), Integer.parseInt(params[4]));
					this.surfaces_list.add(plane);
					System.out.println(String.format("Parsed plane (line %d)", lineNum));
				}
				else if (code.equals("cyl"))
				{
					Cylinder cylinder = new Cylinder(Double.parseDouble(params[0]),
							Double.parseDouble(params[1]), Double.parseDouble(params[2]),
							Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]), Double.parseDouble(params[6]),
							Double.parseDouble(params[7]), Integer.parseInt(params[8])
							);
					this.surfaces_list.add(cylinder);
							
					System.out.println(String.format("Parsed cylinder (line %d)", lineNum));
				}
				else if (code.equals("lgt"))
				{
					Vector position = new Vector(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					ColorAttribute color = new ColorAttribute(Double.parseDouble(params[3]), 
							Double.parseDouble(params[4]), Double.parseDouble(params[5]));
					
					LightSource light_source = new LightSource(position, color, Double.parseDouble(params[6]),
							Double.parseDouble(params[7]), Double.parseDouble(params[8]));
					
					this.light_sources_list.add(light_source);
							
					System.out.println(String.format("Parsed light (line %d)", lineNum));
				}
				else
				{
					System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
				}
			}
		}

                // It is recommended that you check here that the scene is valid,
                // for example camera settings and all necessary materials were defined.

		System.out.println("Finished parsing scene file " + sceneFileName);

	}
	
	private Ray ConstructRayThroughPixel(Camera camera, int i, int j)
	{
		Vector pixel_location = camera.get_center_of_screen();
		
		double pixel_width = camera.screen_width / this.imageWidth;
		double delta_x = -((double)this.imageWidth / 2 - i) * pixel_width;
		double delta_y = ((double)this.imageHeight / 2 - j) * pixel_width;
		
		Vector delta_y_vector = new Vector(camera.up_vector);
		delta_y_vector.normalize();
		delta_y_vector.multiplyByScalar(delta_y);
		pixel_location.add(delta_y_vector);
		
		Vector delta_x_vector = new Vector(camera.direction);
		delta_x_vector.cross(camera.up_vector);
		delta_x_vector.normalize();
		delta_x_vector.multiplyByScalar(-delta_x);
		pixel_location.add(delta_x_vector);
		
		Vector pixel_direction = new Vector(pixel_location);
		pixel_direction.substract(camera.position);
		
		Ray result = new Ray(pixel_location, pixel_direction);
		return result;
	}
	
	private Surface isLineOfSight(Surface surface, Vector start_point, Vector end_point) {
		Vector direction = new Vector(end_point);
		Vector start_point_copy = new Vector(start_point);
		direction.substract(start_point_copy);
		Vector epsilon = new Vector(direction);
		epsilon.normalize();
		epsilon.multiplyByScalar(0.001);
		start_point_copy.add(epsilon);
		Ray ray = new Ray(start_point_copy, direction);
		SurfaceIntersection surface_intersection = this.find_closest_intersection_with_surface(ray, surface);
		
		direction = new Vector(end_point);
		direction.substract(start_point_copy);
		double distance = direction.length(); 
		
		if (null != surface_intersection) {
			double dist_closer_intersection_from_light;
			if(surface_intersection.distance < distance)
			{
				return surface_intersection.surface;
			}
		}
		return null;
	}
	
	private double get_accumaltive_transperncy(Surface surface, Vector start_point, Vector end_point) {
		double transperncy = 1;
		Vector direction = new Vector(end_point);
		Vector start_point_copy = new Vector(start_point);
		direction.substract(start_point_copy);
		Vector epsilon = new Vector(direction);
		epsilon.normalize();
		epsilon.multiplyByScalar(0.001);
		start_point_copy.add(epsilon);
		Ray ray = new Ray(start_point_copy, new Vector(direction));
		
		Vector potential_intersection_point = null;
		/* Iterating all surfaces to find closest intersection point*/
		for(int i = 0; i < this.surfaces_list.size(); i++)
		{
			if (surface == this.surfaces_list.get(i)) {
				continue;
			}
			potential_intersection_point = this.surfaces_list.get(i).get_intersection_point_with_surface(ray);
			if (null != potential_intersection_point) {
				transperncy *= this.materials_list.get(this.surfaces_list.get(i).get_material_index() - 1).transperacy;
			}
		}
		return transperncy;		
	}
	
	private LightSourceIntersection find_closest_intersection_with_light_source(Ray ray) {
		double min_dest_from_light_source = 0;
		LightSource intersection_light_source = null;
		Vector potential_intersection_point_with_light_source = null, 
				intersection_point_with_light_source = null;
		/* Iterating all light sources to find closest intersection point*/
		for (int i = 0; i < this.light_sources_list.size(); i++) {
			if (this.light_sources_list.get(i).has_intersection_point_with_ray(ray)) {
				potential_intersection_point_with_light_source = new Vector(this.light_sources_list.get(i).position);
				potential_intersection_point_with_light_source.substract(ray.start);
				if ((null == intersection_point_with_light_source) 
						|| ((null != intersection_point_with_light_source) &&
								(min_dest_from_light_source > potential_intersection_point_with_light_source.length()))) {
					if (potential_intersection_point_with_light_source.length() == 0) {
						continue;
					}
					min_dest_from_light_source = potential_intersection_point_with_light_source.length();
					potential_intersection_point_with_light_source = this.light_sources_list.get(i).position;
					intersection_light_source = this.light_sources_list.get(i);
				}
			}
		}
		if (null == intersection_light_source) {
			return null;
		}
		return new LightSourceIntersection(intersection_light_source, intersection_point_with_light_source, min_dest_from_light_source);
	}
	
	private double get_light_hits(SurfaceIntersection surface_intersection, Vector direction, LightSource light_source) {
		Random rnd = new Random();
		// handle soft shadows
		double hits = 0;
		Vector u = new Vector(this.camera.up_vector);
		u.cross(direction);
		u.normalize();
		Vector w = new Vector(direction);
		w.cross(u);
		w.normalize();
		assert w.dot(u) == w.dot(direction) && w.dot(u) == u.dot(direction) && w.dot(u) == 0;
		u.multiplyByScalar(light_source.light_radius);
		w.multiplyByScalar(light_source.light_radius);
		Vector u_step = new Vector(u);
		u_step.multiplyByScalar(1 / (double) this.shadow_rays_num);
		Vector w_step = new Vector(w);
		w_step.multiplyByScalar(1 / (double) this.shadow_rays_num);
		Vector light_source_position = light_source.position;
		Vector top_left_corner = new Vector(light_source_position);
		u.multiplyByScalar(0.5);
		top_left_corner.substract(u);
		u.multiplyByScalar(2);
		w.multiplyByScalar(0.5);
		top_left_corner.substract(w);
		w.multiplyByScalar(2);
		for (int j = 0; j < this.shadow_rays_num; j++) {
			for (int k = 0; k < this.shadow_rays_num; k++) {
				Vector random_u_step = new Vector(u_step);
				Vector random_w_step = new Vector(w_step);
				random_u_step.multiplyByScalar(rnd.nextDouble());
				random_w_step.multiplyByScalar(rnd.nextDouble());
				top_left_corner.add(random_u_step);
				top_left_corner.add(random_w_step);
				Surface closer_surface = this.isLineOfSight(null, surface_intersection.intersection, top_left_corner); 
				if (null == closer_surface) {
					hits++;
				}
				else {
					double transperacy = get_accumaltive_transperncy(null, surface_intersection.intersection, top_left_corner);
					hits += (transperacy);
				}
				top_left_corner.substract(random_u_step);
				top_left_corner.substract(random_w_step);
				top_left_corner.add(w_step);
			}
			top_left_corner.add(u_step);
			top_left_corner.substract(w);
		}
		double fraction = (double)hits / (this.shadow_rays_num * this.shadow_rays_num);
		hits = (fraction * light_source.shadow_intensity) + (1-light_source.shadow_intensity);
		return hits;
	}
	
	private Color get_diffuse_color(SurfaceIntersection surface_intersection) {
		Color diffuse_color = new Color(0, 0, 0);
		Material material = this.materials_list.get(surface_intersection.surface.get_material_index() - 1);
		for (int i = 0; i < this.light_sources_list.size(); i++) {
			Color light = new Color(255, 255, 255);
			light.multiply_with_colorAttribute(this.light_sources_list.get(i).color);
			light.multiply_with_colorAttribute(material.diffusive_color);
			Vector direction = new Vector(this.light_sources_list.get(i).position);
			direction.substract(surface_intersection.intersection);
			direction.normalize();
			light.multiply_with_scalar(Math.abs(surface_intersection.surface.get_normal_direction(surface_intersection.intersection).dot(direction)));
			double hits = this.get_light_hits(surface_intersection, direction,  this.light_sources_list.get(i));
			light.multiply_with_scalar(hits);
			diffuse_color.add(light);
			
		}
		diffuse_color.normalize_color();
		return diffuse_color;
	}
	
	private Color get_specular_color(SurfaceIntersection surface_intersection, Vector eye_direction) {
		Color specular_color = new Color(0, 0, 0);
		Material material = this.materials_list.get(surface_intersection.surface.get_material_index() - 1);
		 // TODO: specularity in SciFi
		for (int i = 0; i < this.light_sources_list.size(); i++) {
			Color light = new Color(255, 255, 255);
			light.multiply_with_colorAttribute(this.light_sources_list.get(i).color);
			light.multiply_with_colorAttribute(material.specular_color);
			light.multiply_with_scalar(this.light_sources_list.get(i).specular_intensity);
			Vector direction = new Vector(this.light_sources_list.get(i).position);
			direction.substract(surface_intersection.intersection);
			direction.normalize();
			Ray r = surface_intersection.surface.get_reflection_ray(surface_intersection.intersection, 
					new Ray(this.light_sources_list.get(i).position, direction));
			r.direction.normalize();
			if (surface_intersection.surface.getClass() == Plane.class) {
				// TODO: change it
				r.direction.multiplyByScalar(-1);
			}
			Vector l = new Vector(eye_direction);
			l.normalize();
			double angle = r.direction.dot(l);
			if (angle < 0) {
				// there isn't LOS
				light.multiply_with_scalar(0);
			}
			light.multiply_with_scalar(Math.pow(Math.abs(angle), material.phong_specularity));
			double hits = this.get_light_hits(surface_intersection, direction,  this.light_sources_list.get(i));
			light.multiply_with_scalar(hits);
			specular_color.add(light);
		}
		specular_color.normalize_color();
		return specular_color;
	}
	
	private SurfaceIntersection find_closest_intersection_with_surface(Ray ray, Surface origin_surface) {
		double min_dest_from_surface = 0;
		Surface intersection_surface = null;
		Vector potential_intersection_point = null, intersection_point_with_surface = null;
		/* Iterating all surfaces to find closest intersection point*/
		for(int i = 0; i < this.surfaces_list.size(); i++)
		{
			if (origin_surface == this.surfaces_list.get(i)) {
				continue;
			}
			potential_intersection_point = this.surfaces_list.get(i).get_intersection_point_with_surface(ray);
			if((null != potential_intersection_point) &&
					((null == intersection_point_with_surface)
					|| ((null != intersection_point_with_surface) &&
							(min_dest_from_surface > ray.get_dest_from_point(potential_intersection_point)))))
					{
				intersection_point_with_surface = potential_intersection_point;
				min_dest_from_surface = ray.get_dest_from_point(intersection_point_with_surface);
				intersection_surface = this.surfaces_list.get(i);
					}
		}
		if (null == intersection_surface) {
			return null;
		}
		return new SurfaceIntersection(intersection_surface, intersection_point_with_surface, min_dest_from_surface);
		
	}
	
	//note: this one is suppose to be the recursive function in the future
	private Color calcPixelColor(Ray ray, int recusion_level, Surface origin_surface)
	{
		if(0 == recusion_level)
		{
			Color color = new Color(0, 0, 0);
			return color; 
		}
		
		double min_dest_from_surface = 0;
		Vector intersection_point_with_surface = null;
		SurfaceIntersection surface_intersection = this.find_closest_intersection_with_surface(ray, origin_surface);
		LightSourceIntersection light_source_intersection = this.find_closest_intersection_with_light_source(ray);
		double min_dest_from_light_source = 0;
		Vector intersection_point_with_light_source = null;
		
		if (null != surface_intersection) {
			min_dest_from_surface = surface_intersection.distance;
			intersection_point_with_surface = surface_intersection.intersection;
		}

		/* Finding Closest intersection point*/
		if (null != light_source_intersection) {
			min_dest_from_light_source = light_source_intersection.distance;
			intersection_point_with_light_source = light_source_intersection.intersection;
		}
		
		
		Boolean intersect_with_light = (null != intersection_point_with_light_source) && 
				(null == intersection_point_with_surface || 
				(min_dest_from_light_source < min_dest_from_surface));
		Boolean intersect_with_surface = (null != intersection_point_with_surface) && 
				(null == intersection_point_with_light_source || 
				(min_dest_from_surface < min_dest_from_light_source));
		assert intersect_with_light ? !intersect_with_surface : true; // NAND
		
		
		/* If the ray intersected with something (That is not a light source) create next rays */
		if(intersect_with_surface)
		{
			double material_transperncy = this.materials_list.get(surface_intersection.surface.get_material_index() - 1).transperacy;
			ColorAttribute material_reflection_color = this.materials_list.get(surface_intersection.surface.get_material_index() - 1).Reflection_color;

			Color diffusive_color = this.get_diffuse_color(surface_intersection);
			Color specular_color = this.get_specular_color(surface_intersection, ray.direction);
			
			Color non_recursive_color = new Color(diffusive_color);
			non_recursive_color.add(specular_color);
			non_recursive_color.multiply_with_scalar(1 - material_transperncy);
			Color result = new Color(non_recursive_color);
			
			if (!material_reflection_color.all_zeros()) {
				Ray reflection_ray = surface_intersection.surface.get_reflection_ray(surface_intersection.intersection, ray);
				Color reflection_color = this.calcPixelColor(reflection_ray, recusion_level-1, surface_intersection.surface);
				reflection_color.multiply_with_colorAttribute(material_reflection_color);
				result.add(reflection_color);
			}
			
			if (material_transperncy != 0) {
				Ray transparent_ray = new Ray(intersection_point_with_surface, ray.direction);
				Color transparent_color = this.calcPixelColor(transparent_ray, recusion_level-1, surface_intersection.surface);
				transparent_color.multiply_with_scalar(material_transperncy);
				result.add(transparent_color);
			}
			result.normalize_color();
			return result;
		}
		
		else
		{
			if (intersect_with_light) {
				System.out.println("intersect with light");
				// TODO ?
			}
			else {
				Color color = new Color(this.background_color);
				return color;
			}
		}
		return null;
	}
	
	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(String outputFileName)
	{
		long startTime = System.currentTimeMillis();
		
		Ray initial_ray;
		Color pixel_color;
		
		// Create a byte array to hold the pixel data:
		byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];
		
		for(int i = 0; i < this.imageWidth; i++)
		//for(int i = 360; i < 380; i++)
		{
			for(int j = 0; j < this.imageHeight; j++)
		//	for(int j = 220; j < 240; j++)
			{
				 initial_ray = ConstructRayThroughPixel(camera, i, j); 
				 pixel_color = calcPixelColor(initial_ray, this.max_recursion_level, null);
				 rgbData[(j * this.imageWidth + i) * 3] = (byte)pixel_color.red;
				 rgbData[(j * this.imageWidth + i) * 3 + 1] = (byte)pixel_color.green;
				 rgbData[(j * this.imageWidth + i) * 3 + 2] = (byte)pixel_color.blue;
			//	 System.out.println("i = " + i +", j = " + j);
			}
		}

		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;

                // The time is measured for your own conveniece, rendering speed will not affect your score
                // unless it is exceptionally slow (more than a couple of minutes)
		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

                // This is already implemented, and should work without adding any code.
		saveImage(this.imageWidth, rgbData, outputFileName);

		System.out.println("Saved file " + outputFileName);

	}




	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

	/*
	 * Saves RGB data as an image in png format to the specified location.
	 */
	public static void saveImage(int width, byte[] rgbData, String fileName)
	{
		try {

			BufferedImage image = bytes2RGB(width, rgbData);
			ImageIO.write(image, "png", new File(fileName));

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}

	}

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
	 */
	public static BufferedImage bytes2RGB(int width, byte[] buffer) {
	    int height = buffer.length / width / 3;
	    ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	    ColorModel cm = new ComponentColorModel(cs, false, false,
	            Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    SampleModel sm = cm.createCompatibleSampleModel(width, height);
	    DataBufferByte db = new DataBufferByte(buffer, width * height);
	    WritableRaster raster = Raster.createWritableRaster(sm, db, null);
	    BufferedImage result = new BufferedImage(cm, raster, false, null);

	    return result;
	}

	public static class RayTracerException extends Exception {
		public RayTracerException(String msg) {  super(msg); }
	}


}
