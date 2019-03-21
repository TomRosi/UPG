import TrafficSim.CrossRoad;
import TrafficSim.RoadSegment;
import TrafficSim.Scenarios.Scenario;
import TrafficSim.Simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class Main extends JFrame {
    private double scale;
    private double xOffSet;
    private double yOffSet;

    private double maxXM = 0;
    private double minXM = 0;
    private double maxYM = 0;
    private double minYM = 0;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        DrawingPanel panel = new DrawingPanel();
        Simulator sim = new Simulator();
        Scenario scenar = new Scenario(sim);

        scenar.create();
        sim.addScenario(scenar);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Traffic");

        panel.setPreferredSize(new Dimension(640,400));
        panel.setBackground(Color.WHITE);

        frame.add(panel);

        frame.pack();

        frame.setVisible(true);
    }

    public void computeModelDimensions(Simulator sim) {
        /**Metoda stanoví minimální a maximální souřadnice v metrech ve směru X a Y pro všechny
        úseky silnic spuštěného scénáře, tj. vypočte min-max box takový, že veškeré elementy
        dopravní sítě budou uvnitř, a uloží je do stavových proměnných */

        RoadSegment[] roads = sim.getRoadSegments();
        for(RoadSegment roadSegment : roads) {
            if(roadSegment.getEndPosition().getX() > maxXM) {
                maxXM = roadSegment.getEndPosition().getX();
            }
            if(roadSegment.getStartPosition().getX() < minXM) {
                minXM = roadSegment.getStartPosition().getX();
            }

            if(roadSegment.getEndPosition().getY() > maxYM) {
                maxYM = roadSegment.getEndPosition().getY();
            }
            if(roadSegment.getStartPosition().getY() < minYM) {
                minYM = roadSegment.getStartPosition().getY();
            }
        }
    }

    public void computeModel2WindowTransformation(int width, int height) {
        /**Metoda inicializuje stavové proměnné používané pro přepočet souřadnic modelu (v metrech)
        na souřadnice okna (v pixelech). Metoda určí vhodnou změnu měřítka a posun ve směru X a
        Y tak, aby bylo zaručeno, že se veškeré souřadnice modelu (v min-max boxu vypočteného v
        metodě computeModelDimensions ) transformují do okna o rozměrech width , height , a to
        včetně „přesahů“ grafických reprezentací elementů umístěných na extrémních souřadnicích.*/
        double xScale = maxXM / width;
        double yScale = maxYM / height;

        scale = 1;
        xOffSet = 0;
        yOffSet = 0;

        if (xScale < yScale) {
            scale = xScale;
            yOffSet = (maxYM - height * xScale) / 2;
        } else {
            scale = yScale;
            xOffSet = (maxXM - width * yScale) / 2;
        }
    }

    int d2wx(double dx) {
        return (int)(dx * scale + xOffSet);
    }

    int d2wy(double dy) {
        return (int)(dy * scale + yOffSet);
    }

    public Point2D model2window(Point2D m) {
        /**Metoda převede souřadnice modelu na souřadnice okna s využitím hodnot stavových
        proměnných určených v metodě computeModel2WindowTransformation. */

        return m;
    }

    public void drawCar(double xC, double yC, double orientation, int lenght, int width, Graphics2D g) {
        /**Metoda vykreslí grafickou reprezentaci vozidla na souřadnicích position, udaných v
        pixelech v souřadném systému okna. Souřadnice position odpovídá středu předního
        nárazníku vozidla, orientation říká úhel natočení vozidla vůči ose x.Velikost grafické
        reprezentace bude maximálně length X width pixelů ve směru natočení vozidla.*/

    }

    public void drawLane(double startX, double startY, double endX, double endY, int size, Graphics2D g) {
        /**Metoda vykreslí grafickou reprezentaci jízdního pruhu (ať již na úseku silnice nebo
        křižovatky)vedoucího ze souřadnic start do souřadnic end udaných v pixelech v
        souřadném systému okna.Velikost grafické reprezentace bude maximálně size pixelů, tj.
                size / 2 pixelů na každou stranu od přímky spojující body start a end.*/
        g.scale(size, size);
        g.drawLine(d2wx(startX), d2wy(startY), d2wx(endX), d2wy(endY));

    }

    public void drawRoadSegment(RoadSegment road, Graphics2D g) {
        /***Metoda vykreslí grafickou reprezentaci úseku silnice s využitím volání výše uvedených
        metod.*/

    }

    public void drawCrossRoad(CrossRoad cr, Graphics2D g) {
        /**Metoda vykreslí grafickou reprezentaci úseku křižovatky s využitím volání výše uvedených
        metod.*/

    }

    public void drawTrafficState(Simulator sim, Graphics2D g) {
        /**Metoda vykreslí celou dopravní síť reprezentovanou instancí sim, tj.voláním výše
        uvedených metod vykreslí všechny elementy sítě ve stavu poskytnutém instancí třídy
        Simulator.*/

    }

   /** Kromě těchto metod bude nutné v metodě paint(…) zajistit volání metod
    computeModel2WindowTransformation a drawTrafficState , a v metodě Main
    programu zajistit vytvoření instance třídy Simulator a smyčky zajišťující pravidelné
    překreslování okna v intervalu 100 ms.*/
}
