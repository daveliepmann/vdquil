/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package treemap;

/**
 * A simple implementation of the Mappable interface.
 */
public class MapItem implements Mappable {
  double size;
  Rect bounds;
  int order = 0;
  int depth;

  public MapItem() {
    this(1,0);
  }

  public MapItem(double size, int order) {
    this.size = size;
    this.order = order;
    bounds = new Rect();
  }

  public double getSize() {
    return size;
  }

  public void setSize(double size) {
    this.size = size;
  }

  public Rect getBounds() {
    return bounds;
  }

  public void setBounds(Rect bounds) {
    this.bounds = bounds;
  }

  public void setBounds(double x, double y, double w, double h) {
    bounds.setRect(x, y, w, h);
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }
  
  public void setDepth(int depth) {
    this.depth = depth;
  }

  public int getDepth() {
    return depth;
  }
  
  public void draw() {
  }
}
