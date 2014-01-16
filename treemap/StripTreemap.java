/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package treemap;

public class StripTreemap implements MapLayout {
    static public final boolean DEBUG = false;

    Mappable[] items;
    Rect layoutBox; 
    boolean lookahead = true;

    public StripTreemap() {
    }

    public String getName() {
	return "StripTreemap";
    }

    public String getDescription() {
	return "An Ordered Squarified Treemap";
    }

    public void setLookahead(boolean lookahead) {
	this.lookahead = lookahead;
    }

    public void layout(MapModel model, double x, double y, double w, double h) {
        layout(model, new Rect(x, y, w, h));
    }
    
    public void layout(MapModel model, Rect bounds) {
	items = model.getItems();
	layoutBox = bounds;

	int i;
	double totalSize = 0;
	for (i=0; i<items.length; i++) {
	    totalSize += items[i].getSize();
	}

	double area = layoutBox.w * layoutBox.h;
	double scaleFactor = Math.sqrt(area / totalSize);

	int finishedIndex = 0;
	int numItems = 0;
	double height;
	double yoffset = 0;
	Rect box = new Rect(layoutBox);
	box.x /= scaleFactor;
	box.y /= scaleFactor;
	box.w /= scaleFactor;
	box.h /= scaleFactor;

	while (finishedIndex < items.length) {
	    debug("A: finishedIndex = " + finishedIndex);

				// Layout strip
	    numItems = layoutStrip(box, finishedIndex);

				// Lookahead to second strip
	    if (lookahead) {
		if ((finishedIndex + numItems) < items.length) {
		    int numItems2;
		    double ar2a;
		    double ar2b;

				// Layout 2nd strip and compute AR of first strip plus 2nd strip
		    numItems2 = layoutStrip(box, finishedIndex + numItems);
		    ar2a = computeAverageAspectRatio(finishedIndex, numItems + numItems2);
				// Layout 1st and 2nd strips together
		    computeHorizontalBoxLayout(box, finishedIndex, numItems + numItems2);
		    ar2b = computeAverageAspectRatio(finishedIndex, numItems + numItems2);
		    debug("F: numItems2 = " + numItems2 + ", ar2a="+ar2a+", ar2b="+ar2b);

		    if (ar2b < ar2a) {
			numItems += numItems2;
			debug("G: numItems = " + numItems);
		    } else {
			computeHorizontalBoxLayout(box, finishedIndex, numItems);
			debug("H: backup numItems = " + numItems);
		    }
		}
	    }

	    for (i=finishedIndex; i<(finishedIndex+numItems); i++) {
		items[i].getBounds().y += yoffset;
	    }
	    height = items[finishedIndex].getBounds().h;
	    yoffset += height;
	    box.y += height;
	    box.h -= height;

	    finishedIndex += numItems;
	}

	Rect rect;
	for (i=0; i<items.length; i++) {
	    rect = items[i].getBounds();
	    rect.x *= scaleFactor;
	    rect.y *= scaleFactor;
	    rect.w *= scaleFactor;
	    rect.h *= scaleFactor;

	    rect.x += bounds.x;
	    rect.y += bounds.y;
	    items[i].setBounds(rect);
	}
    }

    protected int layoutStrip(Rect box, int index) {
	int numItems = 0;
	double prevAR;
	double ar = Double.MAX_VALUE;

	do {
	    prevAR = ar;
	    numItems++;
	    ar = computeAverageAspectRatio(index, numItems);
	    debug("L.1: numItems="+numItems+", prevAR="+prevAR+", ar="+ar);
	} while ((ar < prevAR) && ((index + numItems) < items.length));
	if (ar >= prevAR) {
	    numItems--;
	    ar = computeAverageAspectRatio(index, numItems);
	    debug("L.2: backup: numItems="+numItems);
	}

	return numItems;
    }

    protected double computeHorizontalBoxLayout(Rect box, int index, int numItems) {
	int i;
	double totalSize = computeSize(index, numItems);
	double height = totalSize / box.w;
	double width;
	double x = 0;

	for (i=0; i<numItems; i++) {
	    width = items[i + index].getSize() / height;
	    items[i + index].setBounds(x, 0, width, height);
	    x += width;
	}

	return height;
    }

    public void debug(String str) {
	if (DEBUG) {
	    System.out.println(str);
	}
    }

    double computeSize(int index, int num) {
	double size = 0;
	for (int i=0; i<num; i++) {
	    size += items[i+index].getSize();
	}

	return size;
    }

    double computeAverageAspectRatio(int index, int numItems) {
	double ar;
	double tar = 0;
	double w, h;
	int i;

	for (i=0; i<numItems; i++) {
	    w = items[i+index].getBounds().w;
	    h = items[i+index].getBounds().h;
	    ar = Math.max((w / h), (h / w));
	    tar += ar;
	}
	tar /= numItems;

	return tar;
    }

    double computeAspectRatio(int index) {
	double w = items[index].getBounds().w;
	double h = items[index].getBounds().h;
	double ar = Math.max((w / h), (h / w));

	return ar;
    }
}
