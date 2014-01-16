/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package treemap;

/**
 * Layout using the pivot-by-size algorithm.
 * 
 * This is essentially a wrapper class for the OrderedTreemap class.
 */
public class PivotBySize implements MapLayout
{
    OrderedTreemap orderedTreemap;
    
    public PivotBySize()
    {
        orderedTreemap=new OrderedTreemap();
        orderedTreemap.setPivotType(OrderedTreemap.PIVOT_BY_BIGGEST);
    }
    
    public void layout(MapModel model, Rect bounds)
    {
        orderedTreemap.layout(model, bounds);
    }
    
    public void layout(MapModel model, double x, double y, double w, double h)
    {
        orderedTreemap.layout(model, new Rect(x, y, w, h));
    }
    
    public String getName() {return "Pivot by Size / Ben B.";}
    public String getDescription() {return "Pivot by Size, with stopping conditions "+
        "added by Ben Bederson.";}
}
