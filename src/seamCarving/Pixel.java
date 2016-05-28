package seamCarving;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Pixel {
	int row_number;
	int col_number;
	
	public Pixel(int row_number, int col_number)
	{
		this.row_number = row_number; 
		this.col_number = col_number;
	}
	
	public Pixel(Pixel p)
	{
		this.row_number = p.row_number;
		this.col_number = p.col_number;
	}
	
	private static void remove_unreal_pixels(List<Pixel> pixels_list, int w, int h)
	{
		Iterator<Pixel> it = pixels_list.iterator();
		
		List<Pixel> pixels_to_remove = new ArrayList<Pixel>();
		
		//First collect all nodes to remove
		while(it.hasNext())
		{
			Pixel p = it.next();
			if((p.row_number < 0) || (p.col_number < 0) || (p.row_number > h - 1) || (p.col_number > w - 1))
			{
				pixels_to_remove.add(p);
			}
		}
		
		//Then Remove it from neighbors_list
		it = pixels_to_remove.iterator();
		while(it.hasNext())
		{
			Pixel p = it.next();
			pixels_list.remove(p);
		}		
	}
	
	public List<Pixel> get_neighbors(int w, int h)
	{
		List<Pixel> neighbors_list = new ArrayList<Pixel>();
		Pixel p1 = new Pixel(this.row_number - 1, this.col_number - 1);
		Pixel p2 = new Pixel(this.row_number - 1, this.col_number);
		Pixel p3 = new Pixel(this.row_number - 1, this.col_number + 1);
		Pixel p4 = new Pixel(this.row_number, this.col_number -1);
		Pixel p5 = new Pixel(this.row_number, this.col_number + 1);
		Pixel p6 = new Pixel(this.row_number + 1, this.col_number - 1);
		Pixel p7 = new Pixel(this.row_number + 1, this.col_number);
		Pixel p8 = new Pixel(this.row_number + 1, this.col_number + 1);
		
		neighbors_list.add(p1);
		neighbors_list.add(p2);
		neighbors_list.add(p3);
		neighbors_list.add(p4);
		neighbors_list.add(p5);
		neighbors_list.add(p6);
		neighbors_list.add(p7);
		neighbors_list.add(p8);
		
		remove_unreal_pixels(neighbors_list, w, h);
		return neighbors_list;
	}
	
	public List<Pixel> get_left_neighbors(int w, int h)
	{
		List<Pixel> neighbors_list = get_neighbors(w, h);
		Iterator<Pixel> it = neighbors_list.iterator();
		
		List<Pixel> pixels_to_remove = new ArrayList<Pixel>();

		while(it.hasNext())
		{
			Pixel p = it.next();
			if(this.col_number - 1 != p.col_number)
			{
				pixels_to_remove.add(p);
			}
		}
		
		it = pixels_to_remove.iterator();
		while(it.hasNext())
		{
			Pixel p = it.next();
			neighbors_list.remove(p);
		}		
		return neighbors_list;
	}
	
	public List<Pixel> get_enthropy_members(int w, int h)
	{
		List<Pixel> neighbors_list = new ArrayList<Pixel>();
		for(int i = this.row_number - 4; i < this.row_number + 5; i++)
		{
			for(int j =  this.col_number - 4; j < this.col_number + 5; j++)
			{
				Pixel p = new Pixel(i, j);
				neighbors_list.add(p);
			}
		}
		remove_unreal_pixels(neighbors_list, w, h);
		return neighbors_list;
	}
}
