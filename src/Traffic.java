import TrafficSim.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.util.Duration;

/**
 * Třída Traffic zařizuje vykreslování jednotlivých komponent
 *  JavaFX
 *
 * @author Tomáš Rozsypal
*/
public class Traffic {
    private final Duration duration = Duration.millis(70);  /** časový interval vykreslování */
    private final KeyFrame oneFrame = new KeyFrame(duration, event -> update());  /** updatování - vykreslování */
    private Timeline timeline;

    private final GraphicsContext g;
    private final Simulator sim;
    private String[] scenarios;

    private final Car[] cars;
    private final CrossRoad[] crossRoads;
    private final RoadSegment[] roadSegments;

    private final Color CAR_COLOR = Color.BLACK;            /** jednotlivé barvy pro jednotlivý struktury */
    private final Color CR_RS_COLOR = Color.GRAY;           /** auta mají černou barvu, silnice a křižovatky šedivou */

    private final double DIVING_STRIP_WIDTH = 2;        /** šířka dělícího pruhu */
    private double carWidth = 2;                        /** šířka auta, pak se mění v závislosti na šířce silnice */
    private double roadWidth;
    private double roadCounter;

    private double width;
    private double height;
    private double scaleX;
    private double scaleY;

    private double maxXM = 0;
    private double minXM = 0;
    private double maxYM = 0;
    private double minYM = 0;

    /**
     * Konstruktor
     * Definice aut, křižovatek a silnic
     *
     * @param g     Grafický kontext
     * @param sim   simulátor
     */
    public Traffic(GraphicsContext g, Simulator sim) {
        this.g = g;
        this.sim = sim;
        this.scenarios = sim.getScenarios();
        this.width = g.getCanvas().getWidth();
        this.height = g.getCanvas().getHeight();

        timeline = new Timeline(oneFrame);
        timeline.setCycleCount(Animation.INDEFINITE);

        this.sim.runScenario(scenarios[0]);

        cars = this.sim.getCars();
        crossRoads = this.sim.getCrossroads();
        roadSegments = this.sim.getRoadSegments();

        roadCounter = roadSegments.length;

        computeModelDimensions();
        updateRatio(width, height);

        draw();

        start();
        update();
        // stop();
    }

    /**
     * metoda draw zařizuje vykreslení okna na bílo a vykreslení jednotlivých struktur
     */
    public void draw() {
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, g.getCanvas().getWidth(), g.getCanvas().getHeight());

//        System.out.println("Auta: " + cars.length);
//        System.out.println("Silnice: " + roadSegments.length);
//        System.out.println("Krizovatky: " + crossRoads.length);

        for (CrossRoad cR : crossRoads)
            drawCrossRoad(cR);

        for (RoadSegment rS : roadSegments)
            drawRoadSegment(rS);

        for (Car car : cars)
            drawCar(car);
    }

    /**
     * Metoda drawRoadSegment vykreslí silnice. RoadSegment obsahuje informace jednotlivých počátečních a koncových bodů v Point2D.
     * Kladu důraz na rozdíl silnic - hlavní nebo vedlejší.
     *
     * @param rS
     */
    public void drawRoadSegment(RoadSegment rS) {
        g.setFill(CR_RS_COLOR);
        Affine t = g.getTransform();            /** uložení původní souřadné soustavy */
        roadWidth = rS.getLaneWidth();          /** inicializace šířky silnice */

//        double startX = rS.getStartPosition().getX();
//        double startY = rS.getStartPosition().getY();
//        double endX = rS.getEndPosition().getX();
//        double endY = rS.getEndPosition().getY();
//
//        double vecXS = endX - startX;
//        double vecYS = endY - startY;
//
//        double roadLen = Math.sqrt(vecXS * vecXS + vecYS * vecYS);
//
//        double vecXN = vecYS;
//        double vecYN = -vecXS;
//
//        g.fillRect(startX, startY, roadLen * scaleX, roadWidth * scaleY);
//
//        double newStartX = startX + ((vecXN * roadWidth * 2.75)/roadLen);
//        double newStartY = startY + ((vecYN * roadWidth * 2.75)/roadLen);
//        g.fillRect(newStartX, newStartY, roadLen * scaleX, roadWidth * scaleY);

        double startX = rS.getStartPosition().getX();               /** definování jednotlivách proměnných */
        double startY = rS.getStartPosition().getY();
        double endX = rS.getEndPosition().getX();
        double endY = rS.getEndPosition().getY();
        double lenX = endX - startX;
        double lenY = endY - startY;
        double roadLen = Math.sqrt(lenX * lenX + lenY * lenY);
        double angle = Math.toDegrees(Math.atan2(lenY, lenX));

        if(rS.getId().contains("Hlavni")) {
        g.translate((startX * scaleX), ((startY - 2) * scaleY));
        g.rotate(angle);
        for (int i = 0; i < rS.getForwardLanesCount(); i++) {       /** vykreslení jednoltivých pruhů hlavní silnice */
            g.fillRect(0, i * (roadWidth * 2.75) - roadWidth * 2.75, roadLen * scaleX, roadWidth * scaleY);
            g.fillRect(0, i * (roadWidth * 2.75) + DIVING_STRIP_WIDTH + roadWidth * 2.75, roadLen * scaleX, roadWidth * scaleY);
        }
        g.setTransform(t);          /** vrácení původní soustavy souřadnic */
        } else {
        //if(rS.getId().contains("Vedlejsi")) {

            g.translate(((startX-6.5) * scaleX), ((startY ) * scaleY));
            g.rotate(angle);
            roadCounter = roadCounter - 2;                      /** Vždycky budou jen 2 hlavní. Více jích nemůže. */
     //       for (int i = 0; i < roadCounter(); i++) {
                g.fillRect(0, - 0 * (roadWidth ) - roadWidth * 2.75, roadLen * scaleX, roadWidth * scaleY); /** vykreslení silnice pro vedlejší cestu */
                g.fillRect(0, - 1 * (roadWidth * 2.75) - roadWidth * 2.75, roadLen * scaleX, roadWidth * scaleY);
//            }
            g.setTransform(t);
        }

//        g.setTransform(t);*/

    }

    /**
     * Metoda drawCar, vykresli auta dle jejich souradnic do silnice.
     * Pro jednotlivé auta se translatuje souřadný systém a poté rotuje dle zadaných hodnot.
     *
     * @param car auta která chtějí být vykreslena
     */
    public void drawCar(Car car) {
        g.setFill(CAR_COLOR);
        Affine t = g.getTransform();
        carWidth = roadWidth - 2;
        double carLength = car.getLength();
        double carX;
        double carY;
        this.height = g.getCanvas().getHeight();
        this.width = g.getCanvas().getWidth();

        double rotation = Math.toDegrees(car.getOrientation());

        if(rotation == 0 && car.getPosition().getY() <= height / 2) {
            carX = car.getPosition().getX() - carLength;
            carY = car.getPosition().getY();
        } else if (Math.abs(rotation) == 180 ) { //&& car.getPosition().getY() >= height / 2){
            carX = car.getPosition().getX() + carLength;
            carY = car.getPosition().getY() + 1;
        } else if(rotation == 90 ) { //&& car.getPosition().getX() >= (width / 2)-2 ) {
            carX = car.getPosition().getX() + 1;
            carY = car.getPosition().getY();
        } else if(rotation == -90 && car.getPosition().getX() <= width / 2 ) {
            carX = car.getPosition().getX() - 1;
            carY = car.getPosition().getY();
        } else {
            carX = car.getPosition().getX();
            carY = car.getPosition().getY();
        }
//        System.out.println(carX + " - " + carY + " . " + rotation);
//        System.out.println(rotation);

        g.translate((carX * scaleX), ((carY - carWidth / 2) * scaleY)); //(carY - CAR_WIDTH / 2)
        g.rotate(rotation);

        g.fillRect(0, 0, car.getLength() * scaleX, carWidth * scaleY);

        g.setTransform(t);
    }


    /**
     * Metoda vykresluje křižovatky dle zadaných parametrů. To jsou koncové a počáteční pozici silnci.
     *
     * @param cR
     */
    public void drawCrossRoad(CrossRoad cR) {
        g.setFill(CR_RS_COLOR);
        Affine t = g.getTransform();
        Lane[] lanes = cR.getLanes();
        EndPoint[] ends = cR.getEndPoints();

        double aX, aY, bX, bY;
        aX = cR.getEndPoints();

        g.setTransform(t);
    }

    /**
     * aktualizuje vsechny objekty
     */
    public void update() {
        this.width = g.getCanvas().getWidth();
        this.height = g.getCanvas().getHeight();
//        computeModel2WindowTransformation(g.getCanvas().getWidth(), panel.getHeight());

        sim.nextStep(1);
        draw();
    }

    /**
     * stusti smycku
     */
    public void start() {
        timeline.play();
    }

    /**
     * zastavi smycku
     */
    public void stop() {
//        stopVisualize();
        timeline.stop();

//        draw();
    }

    /**
     * pozastavi smycku
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
    public void updateRatio(double width, double height) {
        this.scaleX = width / maxXM;    //maxXM / width;
        this.scaleY = height / maxYM;   //maxYM / height;
//
//        this.scale = 1;
//        this.xOffSet = 0;
//        this.yOffSet = 0;
//
//        if (xScale < yScale) {
//            scale = xScale;
//            yOffSet = (maxYM - height * xScale) / 2;
//        } else {
//            scale = yScale;
//            xOffSet = (maxXM - width * yScale) / 2;
//        }
    }

    /**
     * Metoda stanoví minimální a maximální souřadnice v metrech ve směru X a Y pro všechny
     * úseky silnic spuštěného scénáře, tj. vypočte min-max box takový, že veškeré elementy
     * dopravní sítě budou uvnitř, a uloží je do stavových proměnných
     */
    private void computeModelDimensions() {
        minXM = Double.MAX_VALUE;
        minYM = Double.MAX_VALUE;
        maxXM = Double.MIN_VALUE;
        maxYM = Double.MIN_VALUE;

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
}