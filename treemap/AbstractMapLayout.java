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
 * Abstract class holding utility routines that several
 * implementations of MapLayout use.
 *
 */
public abstract class AbstractMapLayout implements MapLayout
{
    // Flags for type of rectangle division
    // and sort orders.
    public static final int VERTICAL=0, HORIZONTAL=1;
    public static final int ASCENDING=0, DESCENDING=1;
    
    /** Subclasses implement this method themselves. */
    public abstract void layout(Mappable[] items, Rect bounds);
    
    public void layout(MapModel model, Rect bounds)
    {
        layout(model.getItems(), bounds);
    }

    public void layout(MapModel model, double x, double y, double w, double h)
    {
        layout(model.getItems(), new Rect(x, y, w, h));
    }
    
    public static double totalSize(Mappable[] items)
    {
        return totalSize(items,0,items.length-1);
    }

    public static double totalSize(Mappable[] items, int start, int end)
    {
        double sum=0;
        for (int i=start; i<=end; i++)
            sum+=items[i].getSize();
        return sum;
    }
    
    // For a production system, use a quicksort...
    public Mappable[] sortDescending(Mappable[] items)
    {
        Mappable[] s=new Mappable[items.length];
        System.arraycopy(items,0,s,0,items.length);
        int n=s.length;
        boolean outOfOrder=true;
        while (outOfOrder)
        {
            outOfOrder=false;
            for (int i=0; i<n-1; i++)
            {
                boolean wrong=(s[i].getSize()<s[i+1].getSize());
                if (wrong)
                {
                    Mappable temp=s[i];
                    s[i]=s[i+1];
                    s[i+1]=temp;
                    outOfOrder=true;
                }
            }
        }
        return s;
    }

    public static void sliceLayout(Mappable[] items, int start, int end, Rect bounds, int orientation)
    {
        sliceLayout(items,start,end,bounds,orientation,ASCENDING);
    }
    
    public static void sliceLayout(Mappable[] items, int start, int end, Rect bounds, int orientation, int order)
    {
        double total=totalSize(items, start, end);
        double a=0;
        boolean vertical=orientation==VERTICAL;
        
        for (int i=start; i<=end; i++)
        {
            Rect r=new Rect();
            double b=items[i].getSize()/total;
            if (vertical)
            {
                r.x=bounds.x;
                r.w=bounds.w;
                if (order==ASCENDING)
                    r.y=bounds.y+bounds.h*a;
                else
                    r.y=bounds.y+bounds.h*(1-a-b);
                r.h=bounds.h*b;
            }
            else
            {
                if (order==ASCENDING)
                    r.x=bounds.x+bounds.w*a;
                else
                    r.x=bounds.x+bounds.w*(1-a-b);
                r.w=bounds.w*b;
                r.y=bounds.y;
                r.h=bounds.h;
            }
            items[i].setBounds(r);
            a+=b;
        }
    }
}
