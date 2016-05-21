package seamCarving;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Pixel {
	int x;
	int y;
	
	public Pixel(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Pixel(Pixel p)
	{
		this.x = p.x;
		this.y = p.y;
	}
	
	public List<Pixel> get_neighbors(int w, int h)
	{
		List<Pixel> neighbors_list = new ArrayList<Pixel>();
		Pixel p1 = new Pixel(this.x - 1, this.y - 1);
		Pixel p2 = new Pixel(this.x - 1, this.y);
		Pixel p3 = new Pixel(this.x - 1, this.y);
		Pixel p4 = new Pixel(this.x, this.y -1);
		Pixel p5 = new Pixel(this.x, this.y + 1);
		Pixel p6 = new Pixel(this.x + 1, this.y - 1);
		Pixel p7 = new Pixel(this.x + 1, this.y);
		Pixel p8 = new Pixel(this.x + 1, this.y);
		
		neighbors_list.add(p1);
		neighbors_list.add(p2);
		neighbors_list.add(p3);
		neighbors_list.add(p4);
		neighbors_list.add(p5);
		neighbors_list.add(p6);
		neighbors_list.add(p7);
		neighbors_list.add(p8);
		
		Iterator<Pixel> it = neighbors_list.iterator();
		while(it.hasNext())
		{
			Pixel p = it.next();
			if((p.x < 0) || (p.y < 0) || (x > h - 1) || (y > w - 1))
			{
				neighbors_list.remove(p);
			}
		}
		return neighbors_list;
	}
}
