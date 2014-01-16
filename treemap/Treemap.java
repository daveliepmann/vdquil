package treemap;


/**
 * Class that simplifies a Treemap layout. The constructor takes a MapModel
 * object plus boundary coordinates. This displays a treemap for only a single
 * layer. 
 * <br/>
 * This class was not part of the original TreeMap library distribution.
 * @author Ben Fry
 */
public class Treemap {
  private MapModel model;
  private MapLayout algorithm;
  private Rect bounds;
  
  
  public Treemap(MapModel model, double x, double y, double w, double h) {
    this.model = model;
    
    algorithm = new PivotBySplitSize();
    updateLayout(x, y, w, h);
  }

  
  public void setLayout(MapLayout algorithm) {
    this.algorithm = algorithm;
  }
  
  
  public void updateLayout() {
    algorithm.layout(model, bounds);
  }
  

  /**
   * Convenience function to call setBounds() and then updateLayout().
   */
  public void updateLayout(double x, double y, double w, double h) {
    bounds = new Rect(x, y, w, h);
    updateLayout();
  }
  
  public void draw() {
    Mappable[] items = model.getItems();
    for (int i = 0; i < items.length; i++) {
      items[i].draw();
    }
  }
}