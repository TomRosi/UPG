import TrafficSim.Car;
import TrafficSim.CrossRoad;
import TrafficSim.RoadSegment;
import TrafficSim.Simulator;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.awt.*;

public class Traffic {
    private final Duration duration = Duration.millis(30);
    private final KeyFrame oneFrame = new KeyFrame(duration, event -> update());
    private Timeline timeline;

    private final Graphics2D g;
    private final DrawingPanel panel;
    private final Simulator sim;

    private final Car[] cars;
    private final CrossRoad[] crossRoads;
    private final RoadSegment[] roadSegments;

    private double scale;
    private double xOffSet;
    private double yOffSet;

    private double maxXM = 0;
    private double minXM = 0;
    private double maxYM = 0;
    private double minYM = 0;


    public Traffic(DrawingPanel panel, Simulator sim) {
        this.panel = panel;
        this.g = panel.g;
        this.sim = sim;

        timeline = new Timeline(oneFrame);
        timeline.setCycleCount(Animation.INDEFINITE);

        cars = sim.getCars();
        crossRoads = sim.getCrossroads();
        roadSegments = sim.getRoadSegments();

        computeModelDimensions(sim);

       draw();
       // start();
       // update();
       // stop();
    }

    public void draw() {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, panel.getWidth(), panel.getHeight());

//        for (Car car : cars)
//            panel.drawCar(car);

//        for (CrossRoad cR : crossRoads)
//            panel.drawCrossRoad(cR);

        for (RoadSegment rS : roadSegments)
            panel.drawRoadSegment(rS);
    }

    /**
     * aktualizuje vsechny objekty
     */
    public void update() {
        computeModel2WindowTransformation(panel.getWidth(), panel.getHeight());

        sim.nextStep(1);
    }

    /**
     * stusti herni smycku
     */
    public void start() {
        timeline.play();
    }

    /**
     * zastavi herni smycku
     */
    public void stop() {
//        stopVisualize();
        timeline.stop();

//        draw();
    }

    /**
     * pozastavi herni smycku
     */
    public void pause() {
        timeline.pause();
    }

    /**
     * Metoda inicializuje stavové proměnné používané pro přepočet souřadnic modelu (v metrech)
     * na souřadnice okna (v pixelech). Metoda určí vhodnou změnu měřítka a posun ve směru X a
     * Y tak, aby bylo zaručeno, že se veškeré souřadnice modelu (v min-max boxu vypočteného v
     * metodě computeModelDimensions ) transformují do okna o rozměrech width , height , a to
     * včetně „přesahů“ grafických reprezentací elementů umístěných na extrémních souřadnicích.
     */
    public void computeModel2WindowTransformation(int width, int height) {
        double xScale = maxXM / width;
        double yScale = maxYM / height;

        this.scale = 1;
        this.xOffSet = 0;
        this.yOffSet = 0;

        if (xScale < yScale) {
            scale = xScale;
            yOffSet = (maxYM - height * xScale) / 2;
        } else {
            scale = yScale;
            xOffSet = (maxXM - width * yScale) / 2;
        }
    }

    /**
     * Metoda stanoví minimální a maximální souřadnice v metrech ve směru X a Y pro všechny
     * úseky silnic spuštěného scénáře, tj. vypočte min-max box takový, že veškeré elementy
     * dopravní sítě budou uvnitř, a uloží je do stavových proměnných
     */
    private void computeModelDimensions(Simulator sim) {
        for (RoadSegment roadSegment : this.roadSegments) {
            if(roadSegment.getEndPosition().getX() > this.maxXM) {
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

    public double getxOffSet() {
        return this.xOffSet;
    }

    public double getyOffSet() {
        return this.yOffSet;
    }

    public double getScale() {
        return this.scale;
    }
}
