/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package treemap;

/**
 * The original slice-and-dice layout for treemaps.
 */
public class SliceLayout extends AbstractMapLayout
{
    public static final int BEST=2, ALTERNATE=3;
    private int orientation;
    
    public SliceLayout()
    {
        this(ALTERNATE);
    }
    
    public SliceLayout(int orientation)
    {
        this.orientation=orientation;
    }
    
    public void layout(Mappable[] items, Rect bounds)
    {
        if (items.length==0) return;
        int o=orientation;
        if (o==BEST)
            layoutBest(items, 0, items.length-1, bounds);
        else if (o==ALTERNATE)
            layout(items,bounds,items[0].getDepth()%2);
        else
            layout(items,bounds,o);
    }

    public static void layoutBest(Mappable[] items, int start, int end, Rect bounds)
    {
        sliceLayout(items,start,end,bounds,
                    bounds.w>bounds.h ? HORIZONTAL : VERTICAL, ASCENDING);
    }

    public static void layoutBest(Mappable[] items, int start, int end, Rect bounds, int order)
    {
        sliceLayout(items,start,end,bounds,
                    bounds.w>bounds.h ? HORIZONTAL : VERTICAL, order);
    }

    public static void layout(Mappable[] items, Rect bounds, int orientation)
    {
        sliceLayout(items,0,items.length-1,bounds,orientation);
    }

    public String getName()
    {
        return "Slice-and-dice";
    }

    public String getDescription()
    {
        return "This is the original treemap algorithm, "+
               "which has excellent stability properies "+
               "but leads to high aspect ratios.";
    }
}
