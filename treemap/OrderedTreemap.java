/**
 * Copyright (C) 2001 by University of Maryland, College Park, MD 20742, USA 
 * and Martin Wattenberg, w@bewitched.com
 * All rights reserved.
 * Authors: Benjamin B. Bederson and Martin Wattenberg
 * http://www.cs.umd.edu/hcil/treemaps
 */

package treemap;

/*
  Copyright (c) 2001
  University of Maryland
  All Rights Reserved

  Written by Benjamin B. Bederson
  bederson@cs.umd.edu
  http://www.cs.umd.edu/~bederson

  June 2001
 */

public class OrderedTreemap implements MapLayout 
{
  static public final int PIVOT_BY_MIDDLE = 1;
  static public final int PIVOT_BY_SPLIT_SIZE = 2;
  static public final int PIVOT_BY_BIGGEST = 3;

  boolean DEBUG = false;

  Mappable[] items;
  Rect layoutBox;
  int pivotType = PIVOT_BY_MIDDLE;
  Rect[] resultRects = null;

  public String getName() {
    return "OrderedTreemap";
  }

  public String getDescription() {
    return "An Ordered Squarified Treemap";
  }

  public void setPivotType(int pivotType) {
    this.pivotType = pivotType;
  }

  public void layout(MapModel model, Rect bounds)
  {
    Rect r = new Rect(0, 0, bounds.w, bounds.h);
    layoutAtOrigin(model, r);
    Mappable[] m = model.getItems();
    for (int i = 0; i < m.length; i++) {
//      Rect mbounds = m[i].getBounds();
//      mbounds.x += bounds.x;
//      mbounds.y += bounds.y;
//      m[i].setBounds(mbounds);
      
      Rect mbounds = m[i].getBounds();
      m[i].setBounds(mbounds.x + bounds.x, 
                     mbounds.y + bounds.y, 
                     mbounds.w, mbounds.h);

      //m[i].getBounds().x+=bounds.x;
      //m[i].getBounds().y+=bounds.y;
    }
  }

  public void layout(MapModel model, double x, double y, double w, double h)
  {
    layout(model, new Rect(x, y, w, h));
  }

  public void layoutAtOrigin(MapModel model, Rect bounds) {
    items = model.getItems();
    layoutBox = bounds;

    int i;
    double totalSize = 0;
    double area = layoutBox.w * layoutBox.h;

    for (i=0; i<items.length; i++) {
      totalSize += items[i].getSize();
    }

    double scaleFactor = Math.sqrt(area / totalSize);
    Rect box = new Rect(layoutBox);
    box.x /= scaleFactor;
    box.y /= scaleFactor;
    box.w /= scaleFactor;
    box.h /= scaleFactor;

    double[] sizes = new double[items.length];
    for (i=0; i<items.length; i++) {
      sizes[i] = items[i].getSize();
    }
    Rect[] results = orderedLayoutRecurse(sizes, box);

    Rect rect;
    for (i=0; i<items.length; i++) {
      rect = items[i].getBounds();
      rect.x = results[i].x * scaleFactor;
      rect.y = results[i].y * scaleFactor;
      rect.w = results[i].w * scaleFactor;
      rect.h = results[i].h * scaleFactor;

      rect.x += bounds.x;
      rect.y += bounds.y;
      items[i].setBounds(rect);
    }
  }

  protected Rect[] orderedLayoutRecurse(double[] sizes, Rect box) {
    int i;
    double[] l1 = null;
    double[] l2 = null;
    double[] l3 = null;
    double l1Size = 0;
    double l2Size = 0;
    double l3Size = 0;
    Rect r1 = null;
    Rect r2 = null;
    Rect r3 = null;
    Rect rp = null;
    int pivotIndex = computePivotIndex(sizes);
    double pivotSize = sizes[pivotIndex];
    double pivotAR;
    Rect[] boxes = null;
    double boxAR;
    Rect box2;
    int d;
    double w, h;
    double ratio;

    // Stopping condition
    boxAR = box.w / box.h;
    d = sizes.length - pivotIndex - 1;

    if (sizes.length == 1) {
      boxes = new Rect[1];
      boxes[0] = box;

      return boxes;
    }

    if (sizes.length == 2) {
      boxes = new Rect[2];
      ratio = sizes[0] / (sizes[0] + sizes[1]);
      if (boxAR >= 1) {
        w = ratio * box.w;
        boxes[0] = new Rect(box.x, box.y, w, box.h);
        boxes[1] = new Rect(box.x + w, box.y, box.w - w, box.h);
        debug("A: b0="+boxes[0]+", b1="+boxes[1]);
      } else {
        h = ratio * box.h;
        boxes[0] = new Rect(box.x, box.y, box.w, h);
        boxes[1] = new Rect(box.x, box.y + h, box.w, box.h - h);
        debug("s0="+sizes[0]+", s1="+sizes[1]+", ratio="+ratio+", h="+h+", height="+box.h);
        debug("B: b0="+boxes[0]+", b1="+boxes[1]);
      }

      return boxes;
    }

    // First, compute R1
    l1 = new double[pivotIndex];
    System.arraycopy(sizes, 0, l1, 0, pivotIndex);
    l1Size = computeSize(l1);
    if (boxAR >= 1) {
      h = box.h;
      w = l1Size / h;
      r1 = new Rect(box.x, box.y, w, h);
      box2 = new Rect(r1.x + r1.w, box.y, 
                      box.w - r1.w, box.h);
    } else {
      w = box.w;
      h = l1Size / w;
      r1 = new Rect(box.x, box.y, w, h);
      box2 = new Rect(r1.x, r1.y + r1.h, 
                      box.w, box.h - r1.h);
    }

    // Then compute R2 and R3
    if (d >= 3) {
      // First, split up l2 and l3
      boolean first = true;
      double bestAR = 0;
      double bestW = 0;
      double bestH = 0;
      int bestIndex = 0;
      for (i=pivotIndex+1; i<sizes.length; i++) {
        l2Size = computeSize(sizes, pivotIndex+1, i);
        l3Size = computeSize(sizes, i+1, sizes.length-1);
        ratio = (double)(pivotSize + l2Size) / (pivotSize + l2Size + l3Size);
        if (boxAR >= 1) {
          w = ratio * box2.w;
          ratio = (double)pivotSize / (pivotSize + l2Size);
          h = ratio * box2.h;
        } else {
          h = ratio * box2.h;
          ratio = (double)pivotSize / (pivotSize + l2Size);
          w = ratio * box2.w;
        }
        pivotAR = w / h;
        if (first) {
          first = false;
          bestAR = pivotAR;
          bestW = w;
          bestH = h;
          bestIndex = i;
        } else if (Math.abs(pivotAR - 1) < Math.abs(bestAR - 1)) {
          bestAR = pivotAR;
          bestW = w;
          bestH = h;
          bestIndex = i;
        }		    
      }
      l2 = new double[bestIndex - pivotIndex];
      System.arraycopy(sizes, pivotIndex+1, l2, 0, l2.length);
      if ((sizes.length-1-bestIndex) > 0) {
        l3 = new double[sizes.length-1 - bestIndex];
        System.arraycopy(sizes, bestIndex+1, l3, 0, l3.length);
      } else {
        l3 = null;
      }
      if (boxAR >= 1) {
        rp = new Rect(box2.x, box2.y, bestW, bestH);
        r2 = new Rect(box2.x, box2.y + bestH, bestW, box2.h - bestH);
        if (l3 != null) {
          r3 = new Rect(box2.x + bestW, box2.y, box2.w - bestW, box2.h);
        }
      } else {
        rp = new Rect(box2.x, box2.y, bestW, bestH);
        r2 = new Rect(box2.x + bestW, box2.y, box2.w - bestW, bestH);
        if (l3 != null) {
          r3 = new Rect(box2.x, box2.y + bestH, box2.w, box2.h - bestH);
        }
      }
    } else if (d > 0) {
      // l3 is null
      l2 = new double[d];
      debug("d="+d+", l2.len="+l2.length);
      System.arraycopy(sizes, pivotIndex+1, l2, 0, d);
      ratio = (double)pivotSize / (pivotSize + computeSize(l2));
      if (boxAR >= 1) {
        h = ratio * box2.h;
        rp = new Rect(box2.x, box2.y, box2.w, h);
        r2 = new Rect(box2.x, box2.y + h, box2.w, box2.h - h);
      } else {
        w = ratio * box2.w;
        rp = new Rect(box2.x, box2.y, w, box2.h);
        r2 = new Rect(box2.x+ w, box2.y, box2.w - w, box2.h);
      }
    } else {
      rp = box2;
    }

    // Finally, recurse on sublists
    Rect[] l1boxes = null;
    Rect[] l2boxes = null;
    Rect[] l3boxes = null;
    int numBoxes = 0;

    if (l1.length > 1) {
      debug("Recurse R1");
      l1boxes = orderedLayoutRecurse(l1, r1);
      debug("l1boxes.len = " + l1boxes.length);
    } else if (l1.length == 1) {
      l1boxes = new Rect[1];
      l1boxes[0] = r1;
      debug("l1boxes.len = " + l1boxes.length);
    }

    if (l2 != null) {
      if (l2.length > 1) {
        debug("Recurse R2");
        l2boxes = orderedLayoutRecurse(l2, r2);
        debug("l2boxes.len = " + l2boxes.length);
      } else if (l2.length == 1) {
        l2boxes = new Rect[1];
        l2boxes[0] = r2;
        debug("l2boxes.len = " + l2boxes.length);
      }
    }

    if (l3 != null) {
      if (l3.length > 1) {
        debug("Recurse R3");
        l3boxes = orderedLayoutRecurse(l3, r3);
        debug("l3boxes.len = " + l3boxes.length);
      } else if (l3.length == 1) {
        l3boxes = new Rect[1];
        l3boxes[0] = r3;
        debug("l3boxes.len = " + l3boxes.length);
      }
    }

    numBoxes = l1.length + 1;
    if (l2 != null) {
      numBoxes += l2.length;
    }
    if (l3 != null) {
      numBoxes += l3.length;
    }
    boxes = new Rect[numBoxes];
    i = 0;
    if (l1boxes != null) {
      System.arraycopy(l1boxes, 0, boxes, 0, l1boxes.length);
      i = l1boxes.length;
    }
    boxes[i] = rp;
    i++;
    if (l2 != null) {
      debug("l2 copy: i="+i+", boxes.len="+boxes.length+", l2.len="+l2boxes.length);
      System.arraycopy(l2boxes, 0, boxes, i, l2boxes.length);
    }
    if (l3 != null) {
      i += l2boxes.length;
      debug("l3 copy: i="+i+", boxes.len="+boxes.length+", l3.len="+l3boxes.length);
      System.arraycopy(l3boxes, 0, boxes, i, l3boxes.length);
    }

    for (i=0; i<boxes.length; i++) {
      debug("boxes["+i+"] = " +boxes[i]);
    }

    boxes = tryAlternativeLayouts(sizes, box, boxes);

    return boxes;
  }

  Rect[] tryAlternativeLayouts(double[] sizes, Rect box, Rect[] layoutBoxes) {
    Rect[] boxes = layoutBoxes;
    Rect[] nboxes = null;
    double ratio1, ratio2, ratio3, ratio4, ratio5;
    double w, h;
    double w1, w2, w3, w4, w5;
    double h1, h2, h3, h4, h5;
    double boxAR = box.w / box.h;
    double origAvgAR;
    double newAvgAR;

    if (sizes.length == 3) {
      // Try snake alg.
      nboxes = new Rect[3];
      ratio1 = (double)(sizes[0]) / (sizes[0] + sizes[1] + sizes[2]);
      ratio2 = (double)(sizes[1]) / (sizes[0] + sizes[1] + sizes[2]);
      ratio3 = (double)(sizes[2]) / (sizes[0] + sizes[1] + sizes[2]);
      if (boxAR >= 1) {
        h = box.h;
        w1 = ratio1 * box.w;
        w2 = ratio2 * box.w;
        w3 = ratio3 * box.w;
        nboxes[0] = new Rect(box.x, box.y, w1, h);
        nboxes[1] = new Rect(box.x + w1, box.y, w2, h);
        nboxes[2] = new Rect(box.x + w1 + w2, box.y, w3, h);
      } else {
        w = box.w;
        h1 = ratio1 * box.h;
        h2 = ratio2 * box.h;
        h3 = ratio3 * box.h;
        nboxes[0] = new Rect(box.x, box.y, w, h1);
        nboxes[1] = new Rect(box.x, box.y + h1, w, h2);
        nboxes[2] = new Rect(box.x, box.y + h1 + h2, w, h3);
      }

      origAvgAR = computeAverageAspectRatio(boxes);
      newAvgAR = computeAverageAspectRatio(nboxes);
      if (newAvgAR < origAvgAR) {
        boxes = nboxes;
      }
    }

    if (sizes.length == 4) {
      // Try quad alg.
      nboxes = new Rect[4];
      ratio1 = (double)(sizes[0] + sizes[1]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3]);
      if (boxAR >= 1) {
        w = ratio1 * box.w;
        ratio2 = (double)(sizes[0]) / (sizes[0] + sizes[1]);
        h = ratio2 * box.h;
        nboxes[0] = new Rect(box.x, box.y, w, h);
        nboxes[1] = new Rect(box.x, box.y + h, w, box.h - h);
        ratio2 = (double)(sizes[2]) / (sizes[2] + sizes[3]);
        h = ratio2 * box.h;
        nboxes[2] = new Rect(box.x + w, box.y, box.w - w, h);
        nboxes[3] = new Rect(box.x + w, box.y + h, box.w - w, box.h - h);
      } else {
        h = ratio1 * box.h;
        ratio2 = (double)(sizes[0]) / (sizes[0] + sizes[1]);
        w = ratio2 * box.w;
        nboxes[0] = new Rect(box.x, box.y, w, h);
        nboxes[1] = new Rect(box.x, box.y + h, w, box.h - h);
        ratio2 = (double)(sizes[2]) / (sizes[2] + sizes[3]);
        h = ratio2 * box.h;
        nboxes[2] = new Rect(box.x + w, box.y, box.w - w, h);
        nboxes[3] = new Rect(box.x + w, box.y + h, box.w - w, box.h - h);
      }

      origAvgAR = computeAverageAspectRatio(boxes);
      newAvgAR = computeAverageAspectRatio(nboxes);
      if (newAvgAR < origAvgAR) {
        boxes = nboxes;
      }

      // Then try 4 snake alg.
      nboxes = new Rect[4];
      ratio1 = (double)(sizes[0]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3]);
      ratio2 = (double)(sizes[1]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3]);
      ratio3 = (double)(sizes[2]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3]);
      ratio4 = (double)(sizes[3]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3]);
      if (boxAR >= 1) {
        h = box.h;
        w1 = ratio1 * box.w;
        w2 = ratio2 * box.w;
        w3 = ratio3 * box.w;
        w4 = ratio4 * box.w;
        nboxes[0] = new Rect(box.x, box.y, w1, h);
        nboxes[1] = new Rect(box.x + w1, box.y, w2, h);
        nboxes[2] = new Rect(box.x + w1 + w2, box.y, w3, h);
        nboxes[3] = new Rect(box.x + w1 + w2 + w3, box.y, w4, h);
      } else {
        w = box.w;
        h1 = ratio1 * box.h;
        h2 = ratio2 * box.h;
        h3 = ratio3 * box.h;
        h4 = ratio4 * box.h;
        nboxes[0] = new Rect(box.x, box.y, w, h1);
        nboxes[1] = new Rect(box.x, box.y + h1, w, h2);
        nboxes[2] = new Rect(box.x, box.y + h1 + h2, w, h3);
        nboxes[3] = new Rect(box.x, box.y + h1 + h2 + h3, w, h4);
      }

      origAvgAR = computeAverageAspectRatio(boxes);
      newAvgAR = computeAverageAspectRatio(nboxes);
      if (newAvgAR < origAvgAR) {
        boxes = nboxes;
      }
    }

    if (sizes.length == 5) {
      // Try 5 snake alg.
      nboxes = new Rect[5];
      ratio1 = (double)(sizes[0]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3] + sizes[4]);
      ratio2 = (double)(sizes[1]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3] + sizes[4]);
      ratio3 = (double)(sizes[2]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3] + sizes[4]);
      ratio4 = (double)(sizes[3]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3] + sizes[4]);
      ratio5 = (double)(sizes[4]) / (sizes[0] + sizes[1] + sizes[2] + sizes[3] + sizes[4]);
      if (boxAR >= 1) {
        h = box.h;
        w1 = ratio1 * box.w;
        w2 = ratio2 * box.w;
        w3 = ratio3 * box.w;
        w4 = ratio4 * box.w;
        w5 = ratio5 * box.w;
        nboxes[0] = new Rect(box.x, box.y, w1, h);
        nboxes[1] = new Rect(box.x + w1, box.y, w2, h);
        nboxes[2] = new Rect(box.x + w1 + w2, box.y, w3, h);
        nboxes[3] = new Rect(box.x + w1 + w2 + w3, box.y, w4, h);
        nboxes[4] = new Rect(box.x + w1 + w2 + w3 + w4, box.y, w5, h);
      } else {
        w = box.w;
        h1 = ratio1 * box.h;
        h2 = ratio2 * box.h;
        h3 = ratio3 * box.h;
        h4 = ratio4 * box.h;
        h5 = ratio5 * box.h;
        nboxes[0] = new Rect(box.x, box.y, w, h1);
        nboxes[1] = new Rect(box.x, box.y + h1, w, h2);
        nboxes[2] = new Rect(box.x, box.y + h1 + h2, w, h3);
        nboxes[3] = new Rect(box.x, box.y + h1 + h2 + h3, w, h4);
        nboxes[4] = new Rect(box.x, box.y + h1 + h2 + h3 + h4, w, h5);
      }

      origAvgAR = computeAverageAspectRatio(boxes);
      newAvgAR = computeAverageAspectRatio(nboxes);
      if (newAvgAR < origAvgAR) {
        boxes = nboxes;
      }
    }

    return boxes;
  }

  protected int computePivotIndex(double[] sizes) {
    int i;
    int index = 0;
    double leftSize, rightSize;
    double ratio;
    double bestRatio = 0;
    double biggest;
    boolean first = true;

    switch (pivotType) {
    case PIVOT_BY_MIDDLE:
      index = (sizes.length - 1) / 2;
      break;
    case PIVOT_BY_SPLIT_SIZE:
      leftSize = 0;
      rightSize = computeSize(sizes);

      for (i=0; i<sizes.length; i++) {
        ratio = Math.max(((double)leftSize / rightSize), ((double)rightSize / leftSize));
        if (first || (ratio < bestRatio)) {
          first = false;
          bestRatio = ratio;
          index = i;
        }

        leftSize += sizes[i];
        rightSize -= sizes[i];
      }
      break;
    case PIVOT_BY_BIGGEST:
      biggest = 0;
      for (i=0; i<sizes.length; i++) {
        if (first || (sizes[i] > biggest)) {
          first = false;
          biggest = sizes[i];
          index = i;
        }
      }
      break;
    }

    return index;
  }

  double computeSize(double[] sizes) {
    double size = 0;
    for (int i=0; i<sizes.length; i++) {
      size += sizes[i];
    }

    return size;
  }

  double computeSize(double[] sizes, int i1, int i2) {
    double size = 0;
    for (int i=i1; i<=i2; i++) {
      size += sizes[i];
    }

    return size;
  }

  double computeAverageAspectRatio(Rect[] rects) {
    double ar;
    double tar = 0;
    double w, h;
    int i;
    int numRects = 0;

    for (i=0; i<rects.length; i++) {
      w = rects[i].w;
      h = rects[i].h;
      if ((w != 0) && (h != 0)) {
        ar = Math.max((w / h), (h / w));
        tar += ar;
        numRects++;
      }
    }
    tar /= numRects;

    return tar;
  }

  void debug(String str) {
    if (DEBUG) {
      System.out.println(str);
    }
  }
}
