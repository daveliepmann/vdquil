package treemap;


/**
 * Class that simplifies a TreeMap layout. The constructor takes a MapModel
 * object plus boundary coordinates. The class extends SimpleMapItem, so that
 * TreeMap objects can be placed recursively.
 * <br/>
 * This class was not part of the original TreeMap library distribution.
 * @author Ben Fry
 */
public class RecursiveTreemap extends SimpleMapItem {
  private MapModel model;
  private MapLayout algorithm;
  
  
  public RecursiveTreemap(MapModel model, double x, double y, double w, double h) {
    this.model = model;
    
    setLayout(new PivotBySplitSize());
    setBounds(x, y, w, h);
    calcSize();
  }

  
  public void setLayout(MapLayout algorithm) {
    this.algorithm = algorithm;
  }
  
  
  public void updateLayout() {
    algorithm.layout(model, bounds);
    
  }
  

  public void setBounds(Rect bounds) {
    super.setBounds(bounds);
    updateLayout();
  }
  
  
  public void setBounds(double x, double y, double w, double h) {
    super.setBounds(x, y, w, h);
    updateLayout();
  }

  
  public void draw() {
    Mappable[] items = model.getItems();
    for (int i = 0; i < items.length; i++) {
      items[i].draw();
    }
  }
  
  
  /*
  public double getSize() {
    double sum = 0;
    Mappable[] items = model.getItems();
    for (int i = 0; i < items.length; i++) {
      sum += items[i].getSize();
    }
    return sum;
  }
  */
  
  private void calcSize() {
    double sum = 0;
    Mappable[] items = model.getItems();
    for (int i = 0; i < items.length; i++) {
      sum += items[i].getSize();
    }
    setSize(sum);
  }

}