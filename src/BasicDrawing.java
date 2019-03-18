import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BasicDrawing {
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DrawingPanel panel = new DrawingPanel();

        frame.setTitle("Traffic");
        //frame.setLocationRelativeTo(null);
        //frame.setLocation(100, 100);

        panel.setPreferredSize(new Dimension(640,400));
        panel.setBackground(Color.WHITE);

        long startTime = System.currentTimeMillis();

        Timer timer = new Timer(1000 / 25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long now = System.currentTimeMillis();
                panel.setTime((now - startTime)/1000.0);
                panel.repaint();
            }
        });

        timer.start();

        frame.add(panel);

        frame.pack();

        frame.setVisible(true);
    }
}
