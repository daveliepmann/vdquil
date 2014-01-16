/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package treemap;

/** 
 * This layout uses a static binary tree for
 * dividing map items. It is not great with regard to
 * aspect ratios or ordering, but it has excellent
 * stability properties.
 */
public class BinaryTreeLayout extends AbstractMapLayout
{
    public void layout(Mappable[] items, Rect bounds)
    {
        layout(items,0,items.length-1,bounds);
    }
    
    public void layout(Mappable[] items, int start, int end, Rect bounds)
    {
        layout(items, start, end, bounds, true);
    }
    
    public void layout(Mappable[] items, int start, int end, Rect bounds, boolean vertical)
    {
        if (start>end) return;
            //throw new IllegalArgumentException("start, end= "+start+", "+end);
            
        if (start==end)
        {
            items[start].setBounds(bounds);
            return;
        }
        
        int mid=(start+end)/2;
        
        double total=sum(items,start,end);
        double first=sum(items,start,mid);

        double a=first/total;
        double x=bounds.x, y=bounds.y, w=bounds.w, h=bounds.h;
        
        if (vertical)
        {
            Rect b1=new Rect(x,y,w*a,h);
            Rect b2=new Rect(x+w*a,y,w*(1-a),h);
            layout(items, start,mid, b1, !vertical);
            layout(items, mid+1,end, b2, !vertical);
        }
        else
        {
            Rect b1=new Rect(x,y,w,h*a);
            Rect b2=new Rect(x,y+h*a,w,h*(1-a));
            layout(items, start,mid, b1, !vertical);
            layout(items, mid+1,end, b2, !vertical);
        }
        
    }
    
    private double sum(Mappable[] items, int start, int end)
    {
        double s=0;
        for (int i=start; i<=end; i++)
            s+=items[i].getSize();
        return s;
    }
    
    public String getName()
    {
        return "Binary Tree";
    }
    
    public String getDescription()
    {
        return "Uses a static binary tree layout.";
    }
        
    
}
