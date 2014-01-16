/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package treemap;

/**
 * A JDK 1.0 - compatible rectangle class that
 * accepts double-valued parameters.
 */
public class Rect
{
    public double x,y,w,h;

    public Rect()
    {
        this(0,0,1,1);
    }

    public Rect(Rect r)
    {
	setRect(r.x, r.y, r.w, r.h);
    }

    public Rect(double x, double y, double w, double h)
    {
	setRect(x, y, w, h);
    }

    public void setRect(double x, double y, double w, double h) {
	this.x = x;
	this.y = y;
	this.w = w;
	this.h = h;
    }
    
    public double aspectRatio()
    {
        return Math.max(w/h, h/w);
    }
    
    public double distance(Rect r)
    {
        return Math.sqrt((r.x-x)*(r.x-x)+
                         (r.y-y)*(r.y-y)+
                         (r.w-w)*(r.w-w)+
                         (r.h-h)*(r.h-h));
    }
    
    public Rect copy()
    {
        return new Rect(x,y,w,h);
    }
    
    public String toString()
    {
        return "Rect: "+x+", "+y+", "+w+", "+h;
    }

}
