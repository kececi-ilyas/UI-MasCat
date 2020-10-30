package calculator3;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.Hashtable;


public class CalDes implements LayoutManager2 {

  
    private static final int MAX_ROWS = 5;
   
    private static final int MAX_COLUMNS = 8;

    private int gap;
    
    private Hashtable<Component, SetPos> LayComp;

    Dimension componentDimension = null;

    public CalDes() {
        this(0);
    }

  
      
     
    public CalDes(int gap) {
        LayComp = new Hashtable<Component, SetPos>();
        this.gap = gap;
    }

   
    public void addLayoutComponent(String name, Component comp) {
       
    }

   
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int maxWidth = parent.getWidth() - (insets.left + insets.right);
        int maxHeight = parent.getHeight() - (insets.top + insets.bottom);
        int width = 0;
        int height = 0;

        width = ((maxWidth - gap * (MAX_COLUMNS + 1)) / MAX_COLUMNS);
        height = ((maxHeight - gap * (MAX_ROWS + 1)) / MAX_ROWS);
        componentDimension = new Dimension(width, height);

        for (Component comp : parent.getComponents()) {
            SetPos position = LayComp.get(comp);
            int row = position.getRow();
            int column = position.getColumn();
            if (position != null) {
                int x = insets.left + column * gap + (column - 1)
                        * componentDimension.width;
                int y = insets.top + row * gap + (row - 1)
                        * componentDimension.height;
                if (row == 1 && column == 1) {
                    comp.setBounds(x, y,
                            componentDimension.width * 5 + 4 * gap,
                            componentDimension.height);
                } else {
                    comp.setBounds(x, y, componentDimension.width,
                            componentDimension.height);
                }
            }
        }
    }

       public Dimension minimumLayoutSize(Container parent) {
        int width = 0;
        int height = 0;
        for (Component comp : parent.getComponents()) {
            Dimension size = comp.getMinimumSize();
            if (size != null) {
                width = Math.max(width, size.width);
                height = Math.max(height, size.height);
            }
        }
        componentDimension = new Dimension(width, height);
        return new Dimension(MAX_COLUMNS * (width + gap) + gap, MAX_ROWS
                * (height + gap) + gap);
    }

  
    public Dimension preferredLayoutSize(Container parent) {
        int width = 0;
        int height = 0;
        for (Component comp : parent.getComponents()) {
            Dimension size = comp.getPreferredSize();
            if (size != null) {
                width = Math.max(width, size.width);
                height = Math.max(height, size.height);
            }
        }
        componentDimension = new Dimension(width, height);
        int w = MAX_COLUMNS * (width + gap) + gap;
        int h = MAX_ROWS * (height + gap) + gap;
        Dimension dimension = new Dimension(w, h);
        return dimension;
    }

   
    public void removeLayoutComponent(Component arg0) {
        LayComp.remove(arg0);
    }

    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints instanceof SetPos) {
            SetPos position = (SetPos) constraints;
            if (LayComp.containsValue(constraints)) {
                throw new IllegalArgumentException(
                        "This position has another item");
            } else if (position.getRow() < 1 || position.getRow() > MAX_ROWS
                    || position.getColumn() < 1
                    || position.getColumn() > MAX_COLUMNS) {
                throw new IllegalArgumentException("Look row or column number!");
            } else if (position.getRow() == 1
                    && (position.getColumn() > 1 && position.getColumn() < 6)) {
                throw new IllegalArgumentException("Look row or column number!");
            }
            LayComp.put(comp, position);
        } else if (constraints instanceof String) {
            String[] args = ((String) constraints).split(",");
            if (args.length != 2) {
                throw new IllegalArgumentException("Given points are invalid!");
            }
            try {
                int xValue = Integer.parseInt(args[0]);
                int yValue = Integer.parseInt(args[1]);
                this.addLayoutComponent(comp, new SetPos(xValue, yValue));
            } catch (Exception e) {
                throw new IllegalArgumentException("These points have another item!");
            }
        } else {
            throw new IllegalArgumentException(
                    " It must be  \"x,y\"!");
        }

    }

    public float getLayoutAlignmentX(Container arg0) {
        return 0.5f;
    }

    public float getLayoutAlignmentY(Container arg0) {
        return 0.5f;
    }

 
    public void invalidateLayout(Container arg0) {
        
    }

    public Dimension maximumLayoutSize(Container parent) {
        int width = 0;
        int height = 0;
        for (Component comp : parent.getComponents()) {
            Dimension size = comp.getMaximumSize();
            if (size != null) {
                width = Math.max(width, size.width);
                height = Math.max(height, size.height);
            }
        }

        return new Dimension(MAX_COLUMNS * (width + gap) + gap, MAX_ROWS
                * (height + gap) + gap);
    }

}