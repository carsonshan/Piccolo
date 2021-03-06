package edu.umd.cs.piccolo.examples;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PDimension;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolox.PFrame;
import edu.umd.cs.piccolox.handles.PHandle;
import edu.umd.cs.piccolox.util.PLocator;

/**
 * This shows how to create a simple node that has two handles and can be used
 * for specifying angles. The nodes UI desing isn't very exciting, but the
 * example shows one way to create a custom node with custom handles.
 */
public class AngleNodeExample extends PFrame {

	public AngleNodeExample() {
		this(null);
	}
	
	public AngleNodeExample(PCanvas aCanvas) {
		super("AngleNodeExample", false, aCanvas);
	}

	public void initialize() {
		PCanvas c = getCanvas();
		PLayer l = c.getLayer();
		l.addChild(new AngleNode());
	}
	
	public static void main(String[] args) {
		new AngleNodeExample();
	}	

	// the angle node class
	public static class AngleNode extends PNode {
		protected Point2D.Double pointOne;
		protected Point2D.Double pointTwo;
		protected Stroke stroke;
	
		public AngleNode() {
			pointOne = new Point2D.Double(100, 0);
			pointTwo = new Point2D.Double(0, 100);
			stroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
			setPaint(Color.BLACK);
			updateBounds();
			addHandles();
		}
	
		public void addHandles() {
			// point one
			PLocator l = new PLocator() {
				public double locateX() { return pointOne.getX(); }
				public double locateY() { return pointOne.getY(); }
			};
			PHandle h = new PHandle(l) {
				public void dragHandle(PDimension aLocalDimension, PInputEvent aEvent) {
					localToParent(aLocalDimension);
					pointOne.setLocation(pointOne.getX() + aLocalDimension.getWidth(),
									     pointOne.getY() + aLocalDimension.getHeight());
					updateBounds();
					relocateHandle();
				}
			};
			addChild(h);
	
			// point two
			l = new PLocator() {
				public double locateX() { return pointTwo.getX(); }
				public double locateY() { return pointTwo.getY(); }
			};
			h = new PHandle(l) {
				public void dragHandle(PDimension aLocalDimension, PInputEvent aEvent) {
					localToParent(aLocalDimension);
					pointTwo.setLocation(pointTwo.getX() + aLocalDimension.getWidth(),
										 pointTwo.getY() + aLocalDimension.getHeight());
					updateBounds();
					relocateHandle();
				}
			};
			addChild(h);
		}
	
		protected void paint(PPaintContext paintContext) {
			Graphics2D g2 = paintContext.getGraphics();
			g2.setStroke(stroke);
			g2.setPaint(getPaint());
			g2.draw(getAnglePath());
		}
	
		protected void updateBounds() {
			GeneralPath p = getAnglePath();
			Rectangle2D b = stroke.createStrokedShape(p).getBounds2D();
			super.setBounds(b.getX(), b.getY(), b.getWidth(), b.getHeight());
		}
	
		public GeneralPath getAnglePath() {
			GeneralPath p = new GeneralPath();
			p.moveTo((float)pointOne.getX(), (float)pointOne.getY());
			p.lineTo(0, 0);
			p.lineTo((float)pointTwo.getX(), (float)pointTwo.getY());
			return p;
		}
	
		public boolean setBounds(double x, double y, double width, double height) {
			return false; // bounds can be set externally
		}			
	}
}
