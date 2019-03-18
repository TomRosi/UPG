import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class DrawingPanel extends JPanel {
    double scale, xOffSet, yOffSet, time;

    public void setTime(double t) {
        time = t;
    }

    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        double xScale = this.getWidth() / 640.0;
        double yScale = this.getHeight() / 480.0;

        scale = 1;
        xOffSet = 0;
        yOffSet = 0;

        if (xScale < yScale) {
            scale = xScale;
            yOffSet = (this.getHeight() - 480 * xScale) / 2;
        } else {
            scale = yScale;
            xOffSet = (this.getWidth() - 640 * yScale) / 2;
        }
/*
        g2.drawOval(d2wx(100), d2wy(100), (int) (200 * scale), (int) (200 * scale));
        g2.drawRect(d2wx(0), d2wy(0), (int) (639 * scale), (int) (479 * scale));
        g2.drawRect(d2wx(100), d2wy(100), (int) (440 * scale), (int) (280 * scale));
        */
        drawArrow(100,100,200,200,30, g2);
        drawArrow(100,200,200,100,30, g2);
    }

    int d2wx(double dx) {
        return (int)(dx*scale + xOffSet);
    }
    int d2wy(double dy) {
        return (int)(dy*scale + yOffSet);
    }

    void drawArrow(double aX, double aY, double bX, double bY, double s, Graphics2D g2) {
        double vX = aX - bX;
        double vY = aY - bY;
        double d = Math.sqrt(vX*vX + vY*vY);

        double v2X = vX * s/d;
        double v2Y = vY * s/d;
        double qX = bX + v2X;
        double qY = bY + v2Y;

        double qX2 = aX + v2X;
        double qY2 = aY + v2Y;

        double v3X = v2Y * 0.5;
        double v3Y = - v2X * 0.5;
        double cX2 = qX2 + v3X;
        double cY2 = qY2 + v3Y;
        double dX2 = qX2 - v3X;
        double dY2 = qY2 - v3Y;

        double cX = qX + v3X;
        double cY = qY + v3Y;
        double dX = qX - v3X;
        double dY = qY - v3Y;

        g2.drawLine(d2wx(aX), d2wy(aY), d2wx(bX), d2wy(bY));
        g2.drawLine(d2wx(cX), d2wy(cY), d2wx(bX), d2wy(bY));
        g2.drawLine(d2wx(dX), d2wy(dY), d2wx(bX), d2wy(bY));

        g2.drawLine(d2wx(cX2), d2wy(cY2), d2wx(aX), d2wy(aY));
        g2.drawLine(d2wx(dX2), d2wy(dY2), d2wx(aX), d2wy(aY));
        g2.drawLine(d2wx(cX2), d2wy(cY2), d2wx(dX2), d2wy(dY2));

    }
}
