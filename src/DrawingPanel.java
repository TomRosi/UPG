import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class DrawingPanel extends JPanel {
    double time;

    public void setTime(double t) {
        time = t;
    }

    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

    }
}
