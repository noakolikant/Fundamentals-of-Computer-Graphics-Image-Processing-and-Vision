package seamCarving;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import RayTracing.RayTracer.RayTracerException;
public class seamCarving {

	public enum EnergyTypes {
	    REGULAR_WITHOUT_ENTROPY, REGULAR_WITH_ENTROPY, FORWARD_ENERGY
	}
	
	public static int[][] compute_energy_mat_from_image(BufferedImage img)
	{
		int [][] energy_matrix = new int [img.getWidth()][img.getHeight()];
		int sum = 0;
		int neighbors_num = 0;
		
		for(int a = 0; a < img.getWidth(); a++)
		{
			for(int b = 0; b < img.getHeight(); b++)
			{
				Pixel p = new Pixel(a, b);
				List<Pixel> neighbors_list = p.get_neighbors(img.getWidth(), img.getHeight());
				neighbors_num = neighbors_list.size();
				int [] rgb_self = img.getRGB(a, b, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
				while(neighbors_list.size() > 0)
				{
					Pixel neighbor = neighbors_list.remove(0);
					int [] rgb_neighbor = img.getRGB(neighbor.x, neighbor.y, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
					sum = sum + Math.abs(rgb_neighbor[0]-rgb_self[0]) + Math.abs(rgb_neighbor[1]-rgb_self[1]) + Math.abs(rgb_neighbor[2]-rgb_self[2]);
				}
				energy_matrix[a][b] = sum / neighbors_num; // normalize it to number of neighbors.
			}
		}
		//TODO: compute the normalized energy for each pixel.
		return energy_matrix;
	}
	
	public static Seam pick_next_seam(int [][] energy_mat)
	{
		//TODO: implement dynamic programming seam picking
		return null;
	}
	
	public static void main(String[] args) {
		
		String image_file = args[0];
		int cols_num_after = Integer.parseInt(args[1]);
		int rows_num_after = Integer.parseInt(args[2]);
		int energy_type = Integer.parseInt(args[3]);
		String output_image_file = args[4];
		
		try
	    {
	      BufferedImage input_image = ImageIO.read(new File(image_file));
	      int delta_cols = input_image.getHeight() - cols_num_after;
	      int delta_rows = input_image.getWidth() - rows_num_after;
	      
	      int [][] energy_mat = compute_energy_mat_from_image(input_image);
	      
	      //TODO: Decide to remove row or col. If a col generate a transposed energy matrix.
	      
	      Seam lowest_energy_seam= pick_next_seam(energy_mat);
	      
	      //TODO: Remove / Duplicate the chosen seam.
	      
	    } 
	    catch (IOException e)
	    {
			System.out.println(e.getMessage());
	    }
		

	}

}
