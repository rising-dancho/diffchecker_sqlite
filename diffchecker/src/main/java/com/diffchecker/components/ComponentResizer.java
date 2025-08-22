package com.diffchecker.components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class ComponentResizer extends MouseAdapter {

  private final static Dimension DEFAULT_MINIMUM_SIZE = new Dimension(10, 10);
  private final static Dimension DEFAULT_MAXIMUM_SIZE = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
  private static final Map<Integer, Integer> cursors = new HashMap<>();

  static {
    cursors.put(1, Cursor.N_RESIZE_CURSOR);
    cursors.put(2, Cursor.W_RESIZE_CURSOR);
    cursors.put(4, Cursor.S_RESIZE_CURSOR);
    cursors.put(8, Cursor.E_RESIZE_CURSOR);
    cursors.put(3, Cursor.NW_RESIZE_CURSOR);
    cursors.put(9, Cursor.NE_RESIZE_CURSOR);
    cursors.put(6, Cursor.SW_RESIZE_CURSOR);
    cursors.put(12, Cursor.SE_RESIZE_CURSOR);
  }

  private Insets dragInsets;
  private Dimension snapSize;
  private int direction;
  private Cursor sourceCursor;
  private boolean resizing;
  private Rectangle bounds;
  private Point pressed;
  private boolean autoscrolls;

  private Dimension minimumSize = DEFAULT_MINIMUM_SIZE;
  private Dimension maximumSize = DEFAULT_MAXIMUM_SIZE;

  public ComponentResizer() {
    this(new Insets(5, 5, 5, 5), new Dimension(1, 1));
  }

  public ComponentResizer(Component... components) {
    this(new Insets(5, 5, 5, 5), new Dimension(1, 1), components);
  }

  public ComponentResizer(Insets dragInsets, Component... components) {
    this(dragInsets, new Dimension(1, 1), components);
  }

  public ComponentResizer(Insets dragInsets, Dimension snapSize, Component... components) {
    setDragInsets(dragInsets);
    setSnapSize(snapSize);
    registerComponent(components);
  }

  // ✅ SAFE constructor to avoid dragInsets < minSize crash
  public ComponentResizer(Insets dragInsets, Dimension snapSize, Dimension minimumSize, Component... components) {
    setMinimumSize(minimumSize); // first
    setDragInsets(dragInsets); // then safe to call
    setSnapSize(snapSize);
    registerComponent(components);
  }

  public Insets getDragInsets() {
    return dragInsets;
  }

  public void setDragInsets(Insets dragInsets) {
    validateMinimumAndInsets(minimumSize, dragInsets);
    this.dragInsets = dragInsets;
  }

  public Dimension getMaximumSize() {
    return maximumSize;
  }

  public void setMaximumSize(Dimension maximumSize) {
    this.maximumSize = maximumSize;
  }

  public Dimension getMinimumSize() {
    return minimumSize;
  }

  public void setMinimumSize(Dimension minimumSize) {
    validateMinimumAndInsets(minimumSize, dragInsets);
    this.minimumSize = minimumSize;
  }

  public void deregisterComponent(Component... components) {
    for (Component component : components) {
      component.removeMouseListener(this);
      component.removeMouseMotionListener(this);
    }
  }

  public void registerComponent(Component... components) {
    for (Component component : components) {
      component.addMouseListener(this);
      component.addMouseMotionListener(this);
    }
  }

  public Dimension getSnapSize() {
    return snapSize;
  }

  public void setSnapSize(Dimension snapSize) {
    this.snapSize = snapSize;
  }

  private void validateMinimumAndInsets(Dimension minimum, Insets drag) {
    if (drag == null || minimum == null)
      return;
    int minW = drag.left + drag.right;
    int minH = drag.top + drag.bottom;
    if (minimum.width < minW || minimum.height < minH) {
      throw new IllegalArgumentException("Minimum size cannot be less than drag insets");
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    Component source = e.getComponent();
    Point location = e.getPoint();
    direction = 0;

    if (location.x < dragInsets.left)
      direction += WEST;
    if (location.x >= source.getWidth() - dragInsets.right)
      direction += EAST;
    if (location.y < dragInsets.top)
      direction += NORTH;
    if (location.y >= source.getHeight() - dragInsets.bottom)
      direction += SOUTH;

    if (direction == 0) {
      source.setCursor(sourceCursor);
    } else {
      Integer cursorType = cursors.get(direction);
      if (cursorType != null) {
        source.setCursor(Cursor.getPredefinedCursor(cursorType));
      } else {
        source.setCursor(Cursor.getDefaultCursor());
      }
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    if (!resizing) {
      Component source = e.getComponent();
      sourceCursor = source.getCursor();
    }
  }

  @Override
  public void mouseExited(MouseEvent e) {
    if (!resizing) {
      Component source = e.getComponent();
      source.setCursor(sourceCursor);
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (direction == 0)
      return;

    resizing = true;
    Component source = e.getComponent();
    pressed = e.getPoint();
    SwingUtilities.convertPointToScreen(pressed, source);
    bounds = source.getBounds();

    if (source instanceof JComponent jc) {
      autoscrolls = jc.getAutoscrolls();
      jc.setAutoscrolls(false);
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    resizing = false;
    Component source = e.getComponent();
    source.setCursor(sourceCursor);

    if (source instanceof JComponent jc) {
      jc.setAutoscrolls(autoscrolls);
    }
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if (!resizing)
      return;

    Component source = e.getComponent();
    Point dragged = e.getPoint();
    SwingUtilities.convertPointToScreen(dragged, source);
    changeBounds(source, direction, bounds, pressed, dragged);
  }

  protected void changeBounds(Component source, int direction, Rectangle bounds, Point pressed, Point current) {
    int x = bounds.x;
    int y = bounds.y;
    int width = bounds.width;
    int height = bounds.height;

    if ((direction & WEST) != 0) {
      int drag = getDragDistance(pressed.x, current.x, snapSize.width);
      int max = Math.min(width + x, maximumSize.width);
      drag = getDragBounded(drag, snapSize.width, width, minimumSize.width, max);
      x -= drag;
      width += drag;
    }

    if ((direction & NORTH) != 0) {
      int drag = getDragDistance(pressed.y, current.y, snapSize.height);
      int max = Math.min(height + y, maximumSize.height);
      drag = getDragBounded(drag, snapSize.height, height, minimumSize.height, max);
      y -= drag;
      height += drag;
    }

    if ((direction & EAST) != 0) {
      int drag = getDragDistance(current.x, pressed.x, snapSize.width);
      int max = Math.min(getBoundingSize(source).width - x, maximumSize.width);
      drag = getDragBounded(drag, snapSize.width, width, minimumSize.width, max);
      width += drag;
    }

    if ((direction & SOUTH) != 0) {
      int drag = getDragDistance(current.y, pressed.y, snapSize.height);
      int max = Math.min(getBoundingSize(source).height - y, maximumSize.height);
      drag = getDragBounded(drag, snapSize.height, height, minimumSize.height, max);
      height += drag;
    }

    source.setBounds(x, y, width, height);
    source.validate();
  }

  private int getDragDistance(int larger, int smaller, int snapSize) {
    int halfway = snapSize / 2;
    int drag = larger - smaller;
    drag += (drag < 0) ? -halfway : halfway;
    drag = (drag / snapSize) * snapSize;

    return drag;
  }

  private int getDragBounded(int drag, int snapSize, int dim, int min, int max) {
    while (dim + drag < min)
      drag += snapSize;
    while (dim + drag > max)
      drag -= snapSize;
    return drag;
  }

  private Dimension getBoundingSize(Component source) {
    if (source instanceof Window) {
      Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
      return new Dimension(bounds.width, bounds.height);
    } else {
      return source.getParent().getSize();
    }
  }

  protected static final int NORTH = 1;
  protected static final int WEST = 2;
  protected static final int SOUTH = 4;
  protected static final int EAST = 8;
}
