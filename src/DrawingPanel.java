import TrafficSim.Car;
import TrafficSim.CrossRoad;
import TrafficSim.RoadSegment;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class DrawingPanel extends JPanel {
    public Graphics2D g;
    Traffic traffic;

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
        AffineTransform osaXY = g.getTransform();
        g.translate(d2wx(rS.getStartPosition().getX()), d2wy(rS.getStartPosition().getY()));
        //g.scale(size, size);
        g.setColor(Color.gray);
        g.drawLine(0, 0, d2wx(rS.getEndPosition().getX()), d2wy(rS.getEndPosition().getY()));
        g.setTransform(osaXY);
    }

    private int d2wx(double dx) {
        return (int)(dx * traffic.getScale() + traffic.getxOffSet());
    }

    private int d2wy(double dy) {
        return (int)(dy * traffic.getScale() + traffic.getyOffSet());
    }
}
