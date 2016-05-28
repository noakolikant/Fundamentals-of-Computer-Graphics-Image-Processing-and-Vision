package seamCarving;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
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
		int w = img.getWidth();
		int h = img.getHeight();
		int [][] energy_matrix = new int [h][w];
		int sum = 0;
		int neighbors_num = 0;
		
		for(int a = 0; a < img.getHeight(); a++)
		{
			for(int b = 0; b < img.getWidth(); b++)
			{
				sum = 0;
				Pixel p = new Pixel(a, b);
				List<Pixel> neighbors_list = p.get_neighbors(img.getWidth(), img.getHeight());
				neighbors_num = neighbors_list.size();
				
				Color self_color = new Color(img.getRGB(b, a));
				
				while(neighbors_list.size() > 0)
				{
					Pixel neighbor = neighbors_list.remove(0);
					Color color_neighbor = new Color(img.getRGB(neighbor.col_number, neighbor.row_number));
					sum = sum + Math.abs(color_neighbor.getRed() - self_color.getRed()) +
							Math.abs(color_neighbor.getBlue() - self_color.getBlue()) + 
							Math.abs(color_neighbor.getGreen() - self_color.getGreen());
				}
				energy_matrix[a][b] = sum / neighbors_num; // normalize it to number of neighbors.
			}
			//TODO: compute entropy if requested. Maybe not here
		}
		return energy_matrix;
	}
	
	//TODO: Add an option of removed seam or added seam
	public static int [][] update_enegy_mat(int [][]energy_mat, BufferedImage new_img, Seam s)
	{
		return compute_energy_mat_from_image(new_img);
		//TODO: copy all unchanged seam to their new place
		//TODO: re calculate energy for seam's up and down neighbors
		
	}
	
	public static Seam pick_next_seam_dynamic_function(Seam [][] working_table, int [][] energy_mat, Pixel p)
	{
		//if(1 == p.col_number)
		{
		//System.out.println("pick_next_seam_dynamic_function p = (" + p.row_number + ", " + p.col_number + ")\n");
		}
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
			if(null == working_table[neighbor.row_number][neighbor.col_number])
			{
				returned_seam = pick_next_seam_dynamic_function(working_table, energy_mat, neighbor);
				working_table[neighbor.row_number][neighbor.col_number] = returned_seam;
			}
			else
			{
				returned_seam = working_table[neighbor.row_number][neighbor.col_number];
			}
			if((null == winning_seam) || (returned_seam.total_energy < winning_seam.total_energy))
			{
				winning_seam = returned_seam;
			}
		}
		Seam new_winning_seam = new Seam(winning_seam);
		new_winning_seam.insert_pixel(p, energy_mat[p.row_number][p.col_number]);
		working_table[p.row_number][p.col_number] = new_winning_seam;
		
		return new_winning_seam;
	}

	//TODO: change function to be able to return the minimum k seams and not one only (for the enlarging part)
	public static Seam pick_next_seam(int [][] energy_mat)
	{
		int rows = energy_mat.length;
		int cols = energy_mat[0].length;
		
		Seam [][] table = new Seam [rows][cols];
		
		// Initialize all matrix to -1
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
			{
				table[i][j] = null;
			}
		}
		
		//Initialize all most left  seam parts in the dynamic programming's table
		for(int i = 0; i < rows; i++)
		{
			Seam s = new Seam(cols);
			Pixel first_pixel = new Pixel(i, 0);
			s.insert_pixel(first_pixel, energy_mat[i][0]);
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
	
	static void saveImage(String path, BufferedImage input_image)
	{
		try {

			ImageIO.write(input_image, "png", new File(path));

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}
	}
	
	//debug_func
	static void color_seam_on_image(Seam s, BufferedImage input_image, int operation_number) throws IOException
	{
		int r = 255;
		int g = 0;
		int b = 0;
		int col = (r << 16) | (g << 8) | b;
		
		Iterator<Pixel> it = s.pixels_list.iterator();
		
		for(int i = 0; i < s.max_length; i++)
		{
			Pixel p = it.next();
			input_image.setRGB(p.col_number, p.row_number, col);
		}
		
		String outputfile = "C:\\Users\\noa\\Desktop\\red_seam" + operation_number + ".jpg";
		saveImage(outputfile, input_image);
	}

	public static BufferedImage deepCopy(BufferedImage bi)
	{
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	public static BufferedImage remove_seam_from_image(Seam s, BufferedImage img)
	{
		Seam local_seam_copy = new Seam(s);
		
		int type = img.getType();
		int w = img.getWidth();
		int h = img.getHeight();
		
		//The image is -1 less in height
		BufferedImage output_img = new BufferedImage(w, h - 1, type);
		int output_img_col_index = 0;
		
		for(int a = 0; a < w; a++)
		{	
			output_img_col_index = 0;
			Pixel p = local_seam_copy.pixels_list.remove(0);
			for(int b = 0; b < h; b++)
			{
				//copy all col but seam's
				if(p.row_number != b)
				{
					output_img.setRGB(a, output_img_col_index, img.getRGB(a, b));
					output_img_col_index++;
				}
			}
		}
		return output_img;
	}
	
	public static void main(String[] args) {
		
		String image_file = args[0];
		int cols_num_after = Integer.parseInt(args[1]);
		int rows_num_after = Integer.parseInt(args[2]);
		int energy_type = Integer.parseInt(args[3]);
		String output_image_file = args[4];
		// note for Noa's debugging used origin picture has width = 962, height = 445
		
		try
	    {
	      BufferedImage input_image = ImageIO.read(new File(image_file));
	      BufferedImage output_image = deepCopy(input_image);
	      BufferedImage red_seams_image = deepCopy(input_image);
	      
	      int delta_cols = input_image.getWidth() - cols_num_after;
	      int delta_rows = input_image.getHeight() - rows_num_after;

	      //Calculate full energy matrix for all pixels on the first time
	      int [][] energy_mat = compute_energy_mat_from_image(output_image);

    	  //TODO: Decide to if the next operation is for row or col. If a col generate a transposed energy matrix.

	      //Removing delta_rows rows from image
	      if(delta_rows > 0)
	      {
	    	  for(int i = 0; i < delta_rows; i ++)
	    	  {
	    		  Seam lowest_energy_seam= pick_next_seam(energy_mat);

	    		  //For debug
	    		  color_seam_on_image(lowest_energy_seam, red_seams_image, i);

	    		  //TODO: add an if for duplicate or remove seam. right now there is only removing
	    		  output_image = remove_seam_from_image(lowest_energy_seam, output_image);
	    		  energy_mat = update_enegy_mat(energy_mat, output_image, lowest_energy_seam);
	    	  }
	      }
	      
	      saveImage(output_image_file, output_image);
	    } 
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		

	}

}
