/* Based on code from Bederson and Wattenberg, updated by Ben Fry */

package treemap;

/**
 * An implementation of the Mappable interface that adds a few useful bits
 * for use with Processing: an incrementSize() function and local x,y,w,h 
 * fields for the bounds that can be accessed directly.
 * <br/>
 * This class is modified since the original TreeMap library distribution.
 * @author Ben Fry
 */
public class SimpleMapItem implements Mappable {
  protected double size;
  protected Rect bounds;
  protected int order = 0;
  protected int depth;

  // Handy local floats for use with Processing
  public float x, y, w, h;
  
  public SimpleMapItem() {
    this(1, 0);
  }

  public SimpleMapItem(double size, int order) {
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

  public void incrementSize() {
    size++;
  }

  public Rect getBounds() {
    return bounds;
  }

  public void setBounds(Rect bounds) {
    this.bounds = bounds;
    
    x = (float) bounds.x;
    y = (float) bounds.y;
    w = (float) bounds.w;
    h = (float) bounds.h;
  }

  public void setBounds(double bx, double by, double bw, double bh) {
    setBounds(new Rect(bx, by, bw, bh));
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
