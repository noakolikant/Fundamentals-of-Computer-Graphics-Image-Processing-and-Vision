package seamCarving;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

public class seamCarving {

	public enum EnergyTypes {
		REGULAR_WITHOUT_ENTROPY, REGULAR_WITH_ENTROPY, FORWARD_ENERGY
	}

	public static int[] trasposeArr(int[] arr, int m, int n)
	{
		int[] trasposedArr = new int[n * m];

		for(int x = 0; x < m; x++)
		{
			for(int y = 0; y < n; y++)
			{
				trasposedArr[n * x + y] = arr[y * m + x];
			}
		}
		return trasposedArr;
	}

	public static double [][]compute_regular_energy(BufferedImage img)
	{
		int w = img.getWidth();
		int h = img.getHeight();
		double [][] energy_matrix = new double [h][w];
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
		}
		return energy_matrix;
	}
	
	public static ForwardEnergyCost [][] compute_forward_energy_mat_from_image(BufferedImage img)
	{
		int w = img.getWidth();
		int h = img.getHeight();
		ForwardEnergyCost [][] energy_matrix = new ForwardEnergyCost [h][w];

		for(int a = 0; a < img.getHeight(); a++)
		{
			for(int b = 1; b < img.getWidth(); b++)
			{
				Pixel p = new Pixel(a, b);
				energy_matrix[a][b] = new ForwardEnergyCost(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
				Color c1, c2;
//				int base = Integer.MAX_VALUE;
				int base = 0;
				if((0 != a) && (h - 1 != a))
				{
					c1 = new Color(img.getRGB(b, a + 1));
					c2 = new Color(img.getRGB(b, a - 1));

					base = Math.abs(c1.getBlue() - c2.getBlue()) + Math.abs(c1.getRed() - c2.getRed()) +
							Math.abs(c1.getGreen() - c2.getGreen());
				}

				//if not on top
				if((0 != a) && (0 != b))
				{
					c1 = new Color(img.getRGB(b - 1, a));
					c2 = new Color(img.getRGB(b, a - 1));

					energy_matrix[a][b].C_U = base + Math.abs(c1.getBlue() - c2.getBlue()) +
							Math.abs(c1.getRed() - c2.getRed()) + Math.abs(c1.getGreen() - c2.getGreen());
				}
				
				//if not on bottom
				if ((h - 1 != a) && (0 != b))
				{
					c1 = new Color(img.getRGB(b - 1, a));
					c2 = new Color(img.getRGB(b, a + 1));

					energy_matrix[a][b].C_D = base + Math.abs(c1.getBlue() - c2.getBlue()) +
							Math.abs(c1.getRed() - c2.getRed()) + Math.abs(c1.getGreen() - c2.getGreen());
				}
				
				//if not most left
				if(0 != b)
				{
					energy_matrix[a][b].C_L = base;
				}
				//note the most left pixels do not choose next pixels
			}
		}
		return energy_matrix;
	}

	public static double get_gray_color_from_rgb(Color c)
	{
		return (c.getBlue() + c.getGreen() + c.getRed()) / 3;
	}

	public static double [][]compute_entropy_energy(BufferedImage img)
	{
		int w = img.getWidth();
		int h = img.getHeight();
		double [][] energy_matrix = new double [h][w];
		double H = 0;

		for(int a = 0; a < img.getHeight(); a++)
		{
			for(int b = 0; b < img.getWidth(); b++)
			{
				H = 0;
				Pixel p = new Pixel(a, b);
				List<Pixel> neighbors_list = p.get_enthropy_members(img.getWidth(), img.getHeight());

				//Calculate the total grey colors
				double sum_grey = 0;
				Iterator<Pixel> it = neighbors_list.iterator();
				while(it.hasNext())
				{
					Pixel p_grey = it.next();
					Color color_neighbor = new Color(img.getRGB(p_grey.col_number, p_grey.row_number));
					sum_grey += get_gray_color_from_rgb(color_neighbor);
				}

				it = neighbors_list.iterator();
				while(it.hasNext())
				{
					Pixel neighbor = it.next();
					Color color_neighbor = new Color(img.getRGB(neighbor.col_number, neighbor.row_number));

					double P_mn = (double)get_gray_color_from_rgb(color_neighbor) / sum_grey;
					if(P_mn > 1)
					{
						System.out.println("P_mn greater than 1");
					}
					if(P_mn < 0)
					{
						System.out.println("P_mn less than 0");
					}
					H -= (P_mn * Math.log(P_mn));
				}
				energy_matrix[a][b] = H;
			}
		}
		return energy_matrix;
	}


	public static double[][] compute_energy_mat_from_image(BufferedImage img, EnergyTypes energy_type)
	{
		int w = img.getWidth();
		int h = img.getHeight();
		double [][] energy_matrix = new double [h][w];

		if((energy_type == EnergyTypes.REGULAR_WITH_ENTROPY) ||
				(energy_type == EnergyTypes.REGULAR_WITHOUT_ENTROPY))
		{
			double [][] regular_enrgy_mat = compute_regular_energy(img);
			if(energy_type == EnergyTypes.REGULAR_WITHOUT_ENTROPY)
			{
				return regular_enrgy_mat;
			}
			if(energy_type == EnergyTypes.REGULAR_WITH_ENTROPY)
			{
				double [][] entropy_energy_mat = compute_entropy_energy(img);
				for(int i = 0; i < h; i ++)
				{
					for(int j = 0; j < w; j++)
					{
						energy_matrix[i][j] = regular_enrgy_mat[i][j] + 3 * entropy_energy_mat[i][j];
					}
				}
			}
		}
		else
		{
			//	return compute_forward_energy(img);
		}

		return energy_matrix;
	}

	public static double [][] update_enegy_mat(double [][]energy_mat, BufferedImage new_img, Seam s, EnergyTypes energy_type)
	{
		return compute_energy_mat_from_image(new_img, energy_type);
		//TODO: copy all unchanged seam to their new place
		//TODO: re calculate energy for seam's up and down neighbors

	}
	
	public static DynamicProgrammingTableEnrty pick_next_seam_dynamic_function
	(DynamicProgrammingTableEnrty [][] working_table, ForwardEnergyCost [][] energy_mat, Pixel p)
	{
		int rows = energy_mat.length;
		int cols = energy_mat[0].length;

		ForwardEnergyCost f_c = energy_mat[p.row_number][p.col_number];
		//Basic case col 1 picks pixels from col 0 with put considering them dynmic programming calc
		if(1 == p.col_number)
		{
			DynamicProgrammingTableEnrty d;
			if((f_c.C_U < f_c.C_D) && (f_c.C_U < f_c.C_L))
			{
				d = new DynamicProgrammingTableEnrty(p.row_number - 1, 0, f_c.C_U);
			}
			else if((f_c.C_D < f_c.C_U) && (f_c.C_D < f_c.C_L))
			{
				d = new DynamicProgrammingTableEnrty(p.row_number + 1, 0, f_c.C_D);
			}
			else
			{
				d = new DynamicProgrammingTableEnrty(p.row_number, 0, f_c.C_L);
			}
			return d;
		}

		DynamicProgrammingTableEnrty returned_up = null, returned_down = null, returned_left = null;
		Pixel winning_pixel = null;

		int left_value = Integer.MAX_VALUE, up_value = Integer.MAX_VALUE, down_value = Integer.MAX_VALUE;
		//Making sure the dynamic programming calculation for all three left neighbors was done.
		if(null == working_table[p.row_number][p.col_number - 1])
		{
			returned_left = pick_next_seam_dynamic_function(working_table, energy_mat,
					new Pixel(p.row_number, p.col_number - 1));
			working_table[p.row_number][p.col_number - 1] = returned_left;
		}
		left_value = (int) (working_table[p.row_number][p.col_number - 1].total_energy + f_c.C_L);

		if(1 != p.row_number)
		{
			if(null == working_table[p.row_number - 1][p.col_number - 1])
			{
				returned_up = pick_next_seam_dynamic_function(working_table, energy_mat,
						new Pixel(p.row_number - 1, p.col_number - 1));
				working_table[p.row_number - 1][p.col_number - 1] = returned_up;
			}
			up_value = (int) (working_table[p.row_number - 1][p.col_number - 1].total_energy + f_c.C_U);
		}
		if((rows - 2 != p.row_number) && (null == working_table[p.row_number + 1][p.col_number - 1]))
		{
			returned_down = pick_next_seam_dynamic_function(working_table, energy_mat,
					new Pixel(p.row_number + 1, p.col_number - 1));
			working_table[p.row_number + 1][p.col_number - 1] = returned_down;
			down_value = (int) (working_table[p.row_number + 1][p.col_number - 1].total_energy + f_c.C_D);
		}

		DynamicProgrammingTableEnrty d;
		//Basic case col 1 picks pixels from col 0 with put considering them dynamic programming calc
		if((up_value < down_value) && (up_value < left_value))
		{
			d = new DynamicProgrammingTableEnrty(p.row_number - 1, p.col_number - 1, up_value);
		}
		else if((down_value < up_value) && (down_value < left_value))
		{
			d = new DynamicProgrammingTableEnrty(p.row_number + 1, p.col_number - 1, down_value);
		}
		else
		{
			d = new DynamicProgrammingTableEnrty(p.row_number, p.col_number - 1, left_value);
		}
		working_table[p.row_number][p.col_number] = d;
		return d;
	}
	
	
	public static DynamicProgrammingTableEnrty pick_next_seam_dynamic_function
	(DynamicProgrammingTableEnrty [][] working_table, double [][] energy_mat, Pixel p)
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
			DynamicProgrammingTableEnrty s = new DynamicProgrammingTableEnrty(p, energy_mat[p.row_number][p.col_number]);
			return s;
		}

		List<Pixel> left_neighbors_list = p.get_left_neighbors(cols, rows);
		Iterator<Pixel> it = left_neighbors_list.iterator();

		DynamicProgrammingTableEnrty winning_entry = null, returned_enrty = null;
		Pixel winning_pixel = null;

		//Making sure the dynamic programming calculation for all three left neighbors was done.
		while(it.hasNext())
		{
			Pixel neighbor = it.next();
			if(null == working_table[neighbor.row_number][neighbor.col_number])
			{
				returned_enrty = pick_next_seam_dynamic_function(working_table, energy_mat, neighbor);
				working_table[neighbor.row_number][neighbor.col_number] = returned_enrty;
			}
			else
			{
				returned_enrty = working_table[neighbor.row_number][neighbor.col_number];
			}
			if((null == winning_entry) || (returned_enrty.total_energy < winning_entry.total_energy))
			{
				winning_entry = returned_enrty;
				winning_pixel = neighbor;
			}
		}
		DynamicProgrammingTableEnrty current_new_entry = new
				DynamicProgrammingTableEnrty(winning_pixel, winning_entry.total_energy + energy_mat[p.row_number][p.col_number]);
		working_table[p.row_number][p.col_number] = current_new_entry;

		return current_new_entry;
	}

	public static Seam pick_next_seam(double[][] energy_mat)
	{
		int rows = energy_mat.length;
		int cols = energy_mat[0].length;

		DynamicProgrammingTableEnrty [][] table = new DynamicProgrammingTableEnrty [rows][cols];

		// Initialize all matrix to -1
		for(int i = 0; i < rows; i++)
		{
			for(int j = 1; j < cols; j++)
			{
				table[i][j] = null;
			}
		}

		//Initialize all most left  seam parts in the dynamic programming's table
		for(int i = 0; i < rows; i++)
		{
			DynamicProgrammingTableEnrty dpte = new DynamicProgrammingTableEnrty(i, 0, energy_mat[i][0]);
			table[i][0] = dpte;
		}

		DynamicProgrammingTableEnrty winning_entry = null, returned_entry = null;
		Pixel first_pixel_in_seam = null;
		//Calculate and pick table entry
		for(int i = 0; i < rows; i++)
		{
			Pixel p = new Pixel(i, cols - 1); 
			returned_entry = pick_next_seam_dynamic_function(table, energy_mat, p);
			if((null == winning_entry)||(returned_entry.total_energy < winning_entry.total_energy))
			{
				winning_entry = returned_entry;
				first_pixel_in_seam = new Pixel(i, cols - 1);
			}
		}

		//create the chosen seam
		Seam result_seam = new Seam(cols);
		Pixel pixel_it = first_pixel_in_seam;
		DynamicProgrammingTableEnrty entry_iterator = winning_entry;
		result_seam.insert_pixel(pixel_it, energy_mat[pixel_it.row_number][pixel_it.col_number]);
		for(int i = cols - 2; i >= 0; i --)
		{
			pixel_it = entry_iterator.next_pixel;
			entry_iterator = table[pixel_it.row_number][pixel_it.col_number];
			result_seam.insert_pixel(pixel_it, energy_mat[pixel_it.row_number][pixel_it.col_number]);
		}

		return result_seam;
	}
	
	public static List<Seam> pick_next_k_seams(int k, double[][] energy_mat) {
		List<Seam> k_seams = new ArrayList<Seam>();
		Seam lowest_energy_seam = null, temp_seam = null;
		// Copy energy mat
		double [][] last_energy_matrix = new double[energy_mat.length][energy_mat[0].length];
		for (int i = 0; i < energy_mat.length; i++) {
			for (int j = 0; j < energy_mat[0].length; j++) {
				last_energy_matrix[i][j] = energy_mat[i][j];
			}
		}
		for (int i = 0; i < k; i++) {
			// we remove only rows
			double [][] temp_energy_matrix = new double [last_energy_matrix.length - 1][last_energy_matrix[0].length];
			lowest_energy_seam = pick_next_seam(last_energy_matrix);
			temp_seam = new Seam(lowest_energy_seam);

			Iterator<Pixel> it = temp_seam.pixels_list.iterator();
			for(int b = 0; b < temp_energy_matrix[0].length; b++)
			{	
				int output_img_row_index = 0;
				Pixel p = it.next();
					
				for(int a = 0; a < temp_energy_matrix.length; a++)
				{
					if(p.row_number != a)
					{
						temp_energy_matrix[output_img_row_index][b] = last_energy_matrix[a][b]; 
						output_img_row_index++;
					}
				}
			}
			last_energy_matrix = temp_energy_matrix;			
			
			k_seams.add(lowest_energy_seam);
			
		}
		return k_seams;
	}

	public static Seam pick_next_seam(ForwardEnergyCost[][] energy_mat)
	{
		int rows = energy_mat.length;
		int cols = energy_mat[0].length;

		DynamicProgrammingTableEnrty [][] table = new DynamicProgrammingTableEnrty [rows][cols];

		// Initialize all matrix to -1
		for(int i = 0; i < rows; i++)
		{
			table[i][0] = new DynamicProgrammingTableEnrty(i,  0, 0);
			for(int j = 1; j < cols; j++)
			{
				table[i][j] = null;
			}
		}

		DynamicProgrammingTableEnrty winning_entry = null, returned_entry = null;
		Pixel first_pixel_in_seam = null;
		//Calculate and pick table entry
		for(int i = 1; i < rows - 1; i++)
		{
			Pixel p = new Pixel(i, cols - 1); 
			returned_entry = pick_next_seam_dynamic_function(table, energy_mat, p);
			if((null == winning_entry)||(returned_entry.total_energy < winning_entry.total_energy))
			{
				winning_entry = returned_entry;
				first_pixel_in_seam = new Pixel(i, cols - 1);
			}
		}

		//create the chosen seam
		Seam result_seam = new Seam(cols);
		Pixel pixel_it = first_pixel_in_seam;
		DynamicProgrammingTableEnrty entry_iterator = winning_entry;
		result_seam.insert_pixel(pixel_it, table[pixel_it.row_number][pixel_it.col_number].total_energy);
		int i;
		for(i = cols - 2; i >= 0; i --)
		{
			pixel_it = entry_iterator.next_pixel;
			entry_iterator = table[pixel_it.row_number][pixel_it.col_number];
			result_seam.insert_pixel(pixel_it, table[pixel_it.row_number][pixel_it.col_number].total_energy);
		}

		return result_seam;
	}
	
	public static List<Seam> pick_next_k_seams(int k, ForwardEnergyCost[][] energy_mat) {
		List<Seam> k_seams = new ArrayList<Seam>();
		Seam lowest_energy_seam = null, temp_seam = null;
		// Copy energy mat
		ForwardEnergyCost [][] last_energy_matrix = new ForwardEnergyCost[energy_mat.length][energy_mat[0].length];
		for (int i = 0; i < energy_mat.length; i++) {
			for (int j = 0; j < energy_mat[0].length; j++) {
				last_energy_matrix[i][j] = energy_mat[i][j];
			}
		}
		for (int i = 0; i < k; i++) {
			// we remove only cols
			ForwardEnergyCost [][] temp_energy_matrix = new ForwardEnergyCost [last_energy_matrix.length - 1][last_energy_matrix[0].length];
			lowest_energy_seam = pick_next_seam(last_energy_matrix);
			temp_seam = new Seam(lowest_energy_seam);
			
			Iterator<Pixel> it = temp_seam.pixels_list.iterator();
			for(int b = 0; b < temp_energy_matrix[0].length; b++)
			{	
				int output_img_row_index = 0;
				Pixel p = it.next();
				for(int a = 0; a < temp_energy_matrix.length; a++)
				{
					if(p.row_number != a)
					{
						temp_energy_matrix[output_img_row_index][b] = last_energy_matrix[a][b]; 
						output_img_row_index++;
					}
				}
			}
			last_energy_matrix = temp_energy_matrix;			
			
			k_seams.add(lowest_energy_seam);
			
		}
		return k_seams;
	}	

	static void saveImage(String path, BufferedImage input_image)
	{
		try {

			ImageIO.write(input_image, "png", new File(path));

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}
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

		for(int a = w -1; a >= 0; a--)
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
	
	public static BufferedImage add_seam_to_image(Seam s, BufferedImage img)
	{
		Seam local_seam_copy = new Seam(s);

		int type = img.getType();
		int w = img.getWidth();
		int h = img.getHeight();

		//The image is +1 less in height
		BufferedImage output_img = new BufferedImage(w, h + 1, type);
		int output_img_col_index = 0;

		for(int a = w -1; a >= 0; a--)
		{	
			output_img_col_index = 0;
			Pixel p = local_seam_copy.pixels_list.remove(0);
			for(int b = 0; b < h; b++)
			{
				output_img.setRGB(a, output_img_col_index, img.getRGB(a, b));
				output_img_col_index++;
				// duplicate the seam using the avg. value of its neighboors
				if(p.row_number == b) {
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
		int energy_type_integer = Integer.parseInt(args[3]);
		EnergyTypes energy_type = EnergyTypes.values()[energy_type_integer];
		String output_image_file = args[4];

		try
		{
			BufferedImage input_image = ImageIO.read(new File(image_file));
			BufferedImage output_image = deepCopy(input_image);

			int delta_cols = input_image.getWidth() - cols_num_after;
			int delta_rows = input_image.getHeight() - rows_num_after;

			double [][] energy_mat = null;
			ForwardEnergyCost [][] forward_energy_mat = null;
			
			//Calculate full energy matrix for all pixels on the first time
			if(EnergyTypes.FORWARD_ENERGY != energy_type)
			{
				energy_mat = compute_energy_mat_from_image(output_image, energy_type);
			}
			else
			{
				forward_energy_mat = compute_forward_energy_mat_from_image(output_image);
			}
			//TODO: Decide to if the next operation is for row or col. If a col generate a transposed energy matrix.

			//Removing or Adding delta_rows rows from image
			if(Math.abs(delta_rows) > 0)
			{
				List<Seam> k_seams = null;
				if(EnergyTypes.FORWARD_ENERGY != energy_type)
				{
					k_seams = pick_next_k_seams(Math.abs(delta_rows), energy_mat);
				}
				else {
					k_seams = pick_next_k_seams(Math.abs(delta_rows), forward_energy_mat);
				}
				if (delta_rows > 0) {
					// remove seams
					for(int i = 0; i < delta_rows; i ++)
					{
						Seam lowest_energy_seam = k_seams.get(i);
						
						output_image = remove_seam_from_image(lowest_energy_seam, output_image);
					}
				}
				else {
					// fix the seams to the oringal image coordinates
					for (int i = 0; i < Math.abs(delta_rows); i++) {
						Seam ith_seam = k_seams.get(i);
						for (int j = i+1; j< Math.abs(delta_rows); j++) {
							// the j-th seam was selected after the lowest energy
							// seam was deleted from the original image
							k_seams.get(j).add_one_to_row(ith_seam);
						}
					}
					for (int i = 0; i < Math.abs(delta_rows); i++) {
						Seam lowest_energy_seam = k_seams.get(i);
						output_image = add_seam_to_image(lowest_energy_seam, output_image);
						// fix coordinates of all the seams according to the new row
						for (int j = i + 1; j < Math.abs(delta_rows); j++) {
							k_seams.get(j).add_one_to_row(lowest_energy_seam);
						}
					}
				}
				if(EnergyTypes.FORWARD_ENERGY != energy_type)
				{
					energy_mat = compute_energy_mat_from_image(output_image, energy_type);
				}
				else
				{
					forward_energy_mat = compute_forward_energy_mat_from_image(output_image);
				}	
			}

			if(Math.abs(delta_cols) > 0)
			{
				int w_before_transpose = output_image.getWidth();
				int h_before_transpose = output_image.getHeight(); 

				int[] rgb_arr = new int [h_before_transpose * w_before_transpose]; 
				output_image.getRGB(0, 0, w_before_transpose, h_before_transpose, rgb_arr, 0, w_before_transpose);
				rgb_arr = trasposeArr(rgb_arr, w_before_transpose, h_before_transpose);
				output_image = new BufferedImage(h_before_transpose, w_before_transpose,
						output_image.getType());
				output_image.setRGB(0, 0, h_before_transpose, w_before_transpose, rgb_arr, 0, h_before_transpose);	    	  

				if(EnergyTypes.FORWARD_ENERGY != energy_type)
				{
					energy_mat = compute_energy_mat_from_image(output_image, energy_type);
				}
				else
				{
					forward_energy_mat = compute_forward_energy_mat_from_image(output_image);
				}	    	  

				List<Seam> k_seams = null;
				
				if(EnergyTypes.FORWARD_ENERGY != energy_type)
				{
					k_seams = pick_next_k_seams(Math.abs(delta_cols), energy_mat);
				}
				else {
					k_seams = pick_next_k_seams(Math.abs(delta_cols), forward_energy_mat);
				}
				
				if (delta_cols > 0) {
					// remove seams
					for(int i = 0; i < delta_cols; i ++)
					{
						Seam lowest_energy_seam = k_seams.get(i);
						
						output_image = remove_seam_from_image(lowest_energy_seam, output_image);
					}
				}
				
				else {
					// fix the seams to the original image coordinates
					for (int i = 0; i < Math.abs(delta_cols); i++) {
						Seam ith_seam = k_seams.get(i);
						for (int j = i+1; j< Math.abs(delta_cols); j++) {
							// the j-th seam was selected after the lowest energy
							// seam was deleted from the original image
							k_seams.get(j).add_one_to_row(ith_seam);
						}
					}
					for (int i = 0; i < Math.abs(delta_cols); i++) {
						Seam lowest_energy_seam = k_seams.get(i);
						output_image = add_seam_to_image(lowest_energy_seam, output_image);
						// fix coordinates of all the seams according to the new row
						for (int j = i + 1; j < Math.abs(delta_cols); j++) {
							k_seams.get(j).add_one_to_row(lowest_energy_seam);
						}
					}
				}
				
				//transpose again
				h_before_transpose = output_image.getHeight();
				w_before_transpose = output_image.getWidth();
				rgb_arr = new int [h_before_transpose * w_before_transpose]; 
				output_image.getRGB(0, 0, w_before_transpose, h_before_transpose, rgb_arr, 0, w_before_transpose);
				rgb_arr = trasposeArr(rgb_arr, w_before_transpose, h_before_transpose);
				output_image = new BufferedImage(h_before_transpose, w_before_transpose,
						output_image.getType());
				output_image.setRGB(0, 0, h_before_transpose, w_before_transpose, rgb_arr, 0, h_before_transpose);
				
				if(EnergyTypes.FORWARD_ENERGY != energy_type)
				{
					energy_mat = compute_energy_mat_from_image(output_image, energy_type);
				}
				else
				{
					forward_energy_mat = compute_forward_energy_mat_from_image(output_image);
				}	
			}

			output_image = output_image.getSubimage(0, 0, cols_num_after, rows_num_after);
			saveImage(output_image_file, output_image);
		} 
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
}
