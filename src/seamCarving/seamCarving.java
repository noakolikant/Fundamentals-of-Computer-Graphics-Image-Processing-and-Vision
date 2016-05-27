package seamCarving;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

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
					int [] rgb_neighbor = img.getRGB(neighbor.row_number, neighbor.col_number, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
					sum = sum + Math.abs(rgb_neighbor[0]-rgb_self[0]) + Math.abs(rgb_neighbor[1]-rgb_self[1]) + Math.abs(rgb_neighbor[2]-rgb_self[2]);
				}
				energy_matrix[a][b] = sum / neighbors_num; // normalize it to number of neighbors.
			}
			//TODO: compute entropy if requested. Maybe not here
		}
		return energy_matrix;
	}
	
	public static Seam pick_next_seam_dynamic_function(int [][] working_table, int [][] energy_mat, Pixel p)
	{
		int rows = energy_mat.length;
		int cols = energy_mat[0].length;

		//Basic case initializes a seam with the first pixel
		if(0 == p.col_number)
		{
			Seam s = new Seam (cols);
			s.insert_pixel(p, energy_mat[p.row_number][p.col_number]);
			return s;
		}
		
		List<Pixel> left_neighbors_list = p.get_left_neighbors(cols, rows);
		Iterator<Pixel> it = left_neighbors_list.iterator();
		
		Seam winning_seam = null, returned_seam = null;

		//Making sure the dynamic programming calculation for all three left neighbors was done.
		while(it.hasNext())
		{
			Pixel neighbor = it.next();
			if(-1 == working_table[neighbor.row_number][neighbor.col_number])
			{
				returned_seam = pick_next_seam_dynamic_function(working_table, energy_mat, neighbor);
			}
			if((null == winning_seam) || (returned_seam.total_energy < winning_seam.total_energy))
			{
				winning_seam = returned_seam;
			}
		}
		winning_seam.insert_pixel(p, energy_mat[p.row_number][p.col_number]);
		
		return winning_seam;
	}
	
	//TODO: change function to be able to return the minimum k seams and not one only (for the enlarging part)
	public static Seam pick_next_seam(int [][] energy_mat)
	{
		int rows = energy_mat.length;
		int cols = energy_mat[0].length;
		
		int [][] table = new int [rows][cols];
		
		// Initialize all matrix to -1
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
			{
				table[i][j] = -1;
			}
		}
		
		//Initialize all most left  seam parts in the dynamic programming's table
		for(int i =0; i < rows; i++)
		{
			table[i][0] = energy_mat[i][0];
		}
		
		Seam winning_seam = null, returned_seam = null;
		
		//Calculate and pick minimum seam
		for(int i = 0; i < rows; i++)
		{
			Pixel p = new Pixel(i, cols - 1);
			returned_seam = pick_next_seam_dynamic_function(table, energy_mat, p);
			if(null == winning_seam)
			{
				winning_seam = returned_seam;
			}
			if(returned_seam.total_energy < winning_seam.total_energy)
			{
				winning_seam = returned_seam;
			}
		}		
		return winning_seam;
	}
	
	//debug_func
	static void color_seam_on_image(Seam s, BufferedImage input_image) throws IOException
	{
		int r = 255;
		int g = 0;
		int b = 0;
		int col = (r << 16) | (g << 8) | b;
		
		Iterator<Pixel> it = s.pixels_list.iterator();
		
		for(int i = 0; i < s.max_length; i++)
		{
			Pixel p = it.next();
			input_image.setRGB(p.row_number, p.col_number, col);
		}
		File outputfile = new File("Desktop/read_seam.jpg");
		ImageIO.write(input_image, "jpg", outputfile);
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
	      int delta_cols = input_image.getWidth() - cols_num_after;
	      int delta_rows = input_image.getHeight() - rows_num_after;
	      
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
