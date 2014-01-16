/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package treemap;

/** 
 *
 * Object that can measure to the amount of
 * structural change between two layouts.
 * The layouts must have identical tree structures.
 * <p>
 * To measure a difference, first do a "recordLayout" on a MapModel,
 * then call "averageDistance" on the changed model.
 *
 */
public class LayoutDifference
{
    private Rect[] old;
    
    public void recordLayout(MapModel model)
    {
        recordLayout(model.getItems());
    }
    
    public void recordLayout(Mappable[] m)
    {
        old=null;
        if (m==null) return;
        old=new Rect[m.length];
        for (int i=0; i<m.length; i++)
            old[i]=m[i].getBounds().copy();
    }
    
    public double averageDistance(MapModel model)
    {
        return averageDistance(model.getItems());
    }
    
    public double averageDistance(Mappable[] m)
    {
        double d=0;
        int n=m.length;
        if (m==null || old==null || n!=old.length) 
        {
            System.out.println("Can't compare models.");
            return 0;
        }
        for (int i=0; i<n; i++)
            d+=old[i].distance(m[i].getBounds());
        
        return d/n;
    }
}
