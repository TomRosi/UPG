import TrafficSim.Car;
import TrafficSim.CrossRoad;
import TrafficSim.RoadSegment;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class DrawingPanel extends JPanel {
    public Graphics2D g;
    Traffic traffic;
    private int posun = 0;
    private int jizdniPruhyPosun = 0;

    public void paintComponent(Graphics g) {
        this.g = (Graphics2D) g;

        this.g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.g.setColor(Color.BLACK);
    }

    public void drawCar(Car car) {

    }

    public void drawCrossRoad(CrossRoad cR) {

    }

    public void drawRoadSegment(RoadSegment rS) {
        int sirkaPruhu = (int)rS.getLaneWidth();
        g.setStroke(new BasicStroke((float)sirkaPruhu/2));
        g.setColor(Color.gray);
        if(rS.getStartPosition().getX() == rS.getEndPosition().getX()) {
            g.drawLine(d2wx(rS.getStartPosition().getX()), d2wy(rS.getStartPosition().getY()) + posun*sirkaPruhu, d2wx(rS.getEndPosition().getX()), d2wy(rS.getEndPosition().getY())+posun*sirkaPruhu);
        }
        if(rS.getStartPosition().getY() == rS.getEndPosition().getY()) {
            g.drawLine(d2wx(rS.getStartPosition().getX())+posun*sirkaPruhu, d2wy(rS.getStartPosition().getY()), d2wx(rS.getEndPosition().getX())+posun*sirkaPruhu, d2wy(rS.getEndPosition().getY()));
        }

        if(jizdniPruhyPosun == rS.getForwardLanesCount()) {
            //posun
            if(rS.getStartPosition().getX() == rS.getEndPosition().getX()) {
                g.drawLine(d2wx(rS.getStartPosition().getX()), d2wy(rS.getStartPosition().getY()) + posun*sirkaPruhu, d2wx(rS.getEndPosition().getX()), d2wy(rS.getEndPosition().getY())+posun*sirkaPruhu);
            }
            if(rS.getStartPosition().getY() == rS.getEndPosition().getY()) {
                g.drawLine(d2wx(rS.getStartPosition().getX())+posun*sirkaPruhu, d2wy(rS.getStartPosition().getY()), d2wx(rS.getEndPosition().getX())+posun*sirkaPruhu, d2wy(rS.getEndPosition().getY()));
            }
        }
        posun += 5;
        jizdniPruhyPosun += 1;

       /* AffineTransform osaXY = g.getTransform();
        g.translate(d2wx(rS.getStartPosition().getX()), d2wy(rS.getStartPosition().getY()));
        //g.scale(size, size);
        g.setColor(Color.gray);
        g.drawLine(0, 0, d2wx(rS.getEndPosition().getX()), d2wy(rS.getEndPosition().getY()));
        g.setTransform(osaXY);
        */
    }

    private int d2wx(double dx) {
        return (int)(dx * traffic.getScale() + traffic.getxOffSet());
    }

    private int d2wy(double dy) {
        return (int)(dy * traffic.getScale() + traffic.getyOffSet());
    }
}
