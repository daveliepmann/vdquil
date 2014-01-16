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
 * Calculations on treemap layouts. Currently
 * holds routines for readability and aspect ratios. This is a good
 * place to add future metrics.
 *
 */
public class LayoutCalculations
{
    
    public static double averageAspectRatio(MapModel model)
    {
        return averageAspectRatio(model.getItems());
    }
    
    public static double averageAspectRatio(Mappable[] m)
    {
        double s=0;
        int n=m.length;
        if (m==null || n==0) 
        {
            System.out.println("Can't measure aspect ratio.");
            return 0;
        }
        for (int i=0; i<n; i++)
        {
            s+=m[i].getBounds().aspectRatio();
        }
        return s/n;
    }

    public static double getReadability(TreeModel tree)
    {
        return getReadability(tree.getLeafModels());
    }
    
    public static double getReadability(MapModel[] model)
    {
        int weight=0;
        double r=0;
        for (int i=0; i<model.length; i++)
        {
            int n=model[i].getItems().length;
            weight+=n;
            r+=n*getReadability(model[i]);
        }
        return weight==0 ? 1 : r/weight;
    }
    
    /**
     * Compute the readability of the model.
     * Readability is defined by the number of changes in directions rects are layed out in.
     */
    public static double getReadability(MapModel model) {
	int numTurns = 0;
	double prevAngle = 0;
	double angle = 0;
	double angleChange = 0;
	Mappable[] items = model.getItems();
	Rect b1, b2;
	double dx, dy;
	double readability;

	for (int i=1; i<items.length; i++) {
	    b1 = items[getItemIndex(i-1, model)].getBounds();
	    b2 = items[getItemIndex(i, model)].getBounds();
	    dx = (b2.x + 0.5*b2.w) - (b1.x + 0.5*b1.w);
	    dy = (b2.y + 0.5*b2.h) - (b1.y + 0.5*b1.h);
	    angle = Math.atan2(dy, dx);
	    if (i >= 2) {
		angleChange = Math.abs(angle - prevAngle);
		if (angleChange > Math.PI) {
		    angleChange = Math.abs(angleChange - (2 * Math.PI));
		}
		if (angleChange > 0.1) { // allow for rounding
		    numTurns++;
		}
	    }
	    prevAngle = angle;
	}

	readability = 1.0 - ((double)numTurns /items.length);

	return readability;
    }

    public static int getItemIndex(int order, MapModel map) {
	int i;
	Mappable[] items = map.getItems();

	for (i=0; i<items.length; i++) {
	    if (items[i].getOrder() == order) {
		break;
	    }
	}

	return i;
    }
}
