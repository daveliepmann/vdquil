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
 * The interface for all treemap layout algorithms.
 * If you write your own algorith, it should conform
 * to this interface.
 * <p>
 * IMPORTANT: if you want to be able to automatically plug 
 * your algorithm into the various demos and test harnesses
 * included in the treemap package, it should have
 * an empty constructor.
 * 
 */
public interface MapLayout
{
    /**
     * Arrange the items in the given MapModel to fill the given rectangle.
     *
     * @param model The MapModel.
     * @param bounds The boundary rectangle for the layout.
     */
    public void layout(MapModel model, Rect bounds);
    
    /**
     * Arrange the items in the given MapModel to fill the given rectangle.
     *
     * @param model The MapModel.
     * @param x coordinate of the layout bounds.
     * @param y coordinate of the layout bounds.
     * @param w width of the layout bounds.
     * @param h height of the layout bounds.
     */
    public void layout(MapModel model, double x, double y, double w, double h);
    
    /**
     * Return a human-readable name for this layout;
     * used to label figures, tables, etc.
     *
     * @return String naming this layout.
     */
    public String getName();
    
    /**
     * Return a longer description of this layout;
     * Helpful in creating online-help,
     * interactive catalogs or indices to lists of algorithms.
     *
     * @return String describing this layout.
     */
    public String getDescription();
}
