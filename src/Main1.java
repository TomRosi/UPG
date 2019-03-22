import TrafficSim.CrossRoad;
import TrafficSim.RoadSegment;
import TrafficSim.Scenarios.Scenario;
import TrafficSim.Simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class Main extends JFrame {
    private static double scale;
    private static double xOffSet;
    private static double yOffSet;

    private static double maxXM = 0;
    private static double minXM = 0;
    private static double maxYM = 0;
    private static double minYM = 0;

    public static Simulator sim;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        DrawingPanel panel = new DrawingPanel();
        sim = new Simulator();
        Scenario scenar = new Scenario(sim);
        String[] scenare = sim.getScenarios();
        RoadSegment roads = new RoadSegment();
        Graphics2D g2 = new Graphics2D();

        for (String scenario : scenare) {
            System.out.println(scenario);
        }
        sim.runScenario(scenar.getId());
        computeModelDimensions(sim);

        scenar.create();
        sim.addScenario(scenar);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Traffic");

        panel.setPreferredSize(new Dimension(640,400));
        panel.setBackground(Color.WHITE);

        computeModel2WindowTransformation(640, 400);

        drawLane(roads.getStartPosition(), roads.getEndPosition(), roads.getLaneWidth(), g);

        frame.add(panel);

        frame.pack();

        frame.setVisible(true);
    }

    public static void computeModelDimensions(Simulator sim) {
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

    public static void computeModel2WindowTransformation(int width, int height) {
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

    static int d2wx(double dx) {
        return (int)(dx * scale + xOffSet);
    }

    static int d2wy(double dy) {
        return (int)(dy * scale + yOffSet);
    }

    public Point2D model2window(Point2D m) {
        /**Metoda převede souřadnice modelu na souřadnice okna s využitím hodnot stavových
        proměnných určených v metodě computeModel2WindowTransformation. */


        return m;
    }

    public void drawCar(Point2D position, double orientation, int lenght, int width, Graphics2D g) {
        /**Metoda vykreslí grafickou reprezentaci vozidla na souřadnicích position, udaných v
        pixelech v souřadném systému okna. Souřadnice position odpovídá středu předního
        nárazníku vozidla, orientation říká úhel natočení vozidla vůči ose x.Velikost grafické
        reprezentace bude maximálně length X width pixelů ve směru natočení vozidla.*/

    }

    public static void drawLane(Point2D start, Point2D end, int size, Graphics2D g) {
        /**Metoda vykreslí grafickou reprezentaci jízdního pruhu (ať již na úseku silnice nebo
        křižovatky)vedoucího ze souřadnic start do souřadnic end udaných v pixelech v
        souřadném systému okna. Velikost grafické reprezentace bude maximálně size pixelů, tj.
                size / 2 pixelů na každou stranu od přímky spojující body start a end.*/
        g.scale(size, size);
        g.drawLine(d2wx(start.getX()), d2wy(start.getY()), d2wx(end.getX()), d2wy(end.getY()));

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
