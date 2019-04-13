import TrafficSim.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

/**
 * Třída Traffic zařizuje vykreslování jednotlivých komponent
 *  JavaFX
 *
 * @author Tomáš Rozsypal
*/
public class Traffic {
    private final Duration duration = Duration.millis(10000);  /** časový interval vykreslování */
    private final KeyFrame oneFrame = new KeyFrame(duration, event -> update());  /** updatování - vykreslování */
    private Timeline timeline;

    private final GraphicsContext g;
    private final Simulator sim;
    private String[] scenarios;
    private RoadSegment[] poleSilnci;

    private final Car[] cars;
    private final CrossRoad[] crossRoads;
    private final RoadSegment[] roadSegments;

    private final Color CAR_COLOR = Color.BLACK;            /** jednotlivé barvy pro jednotlivý struktury */
    private final Color CR_RS_COLOR = Color.GRAY;           /** auta mají černou barvu, silnice a křižovatky šedivou */

    private final double DIVING_STRIP_WIDTH = 2;        /** šířka dělícího pruhu */
    private double carWidth;                        /** šířka auta, pak se mění v závislosti na šířce silnice */
    private double roadWidth;
    private double roadCounter;
    private double sideRoadCounter;

    private double width;
    private double height;
    private double scaleX;
    private double scaleY;


    private double scale = 1;

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

//        g.translate(0, height);
//        g.scale(1,-1);

        timeline = new Timeline(oneFrame);
        timeline.setCycleCount(Animation.INDEFINITE);

        this.sim.runScenario(scenarios[1]);

        cars = this.sim.getCars();
        crossRoads = this.sim.getCrossroads();
        roadSegments = this.sim.getRoadSegments();

        roadCounter = roadSegments.length;
        sideRoadCounter = roadCounter - 2;          /** Vždycky budou jen 2 hlavní. Více jích nemůže. */

        computeModelDimensions();       //this.sim.getRoadSegments()
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

        for (RoadSegment rS : roadSegments)
            drawRoadSegmentLine(rS);
//            drawRoadSegmentVector(rS);
//          drawRoadSegment(rS);
//
        for (CrossRoad cR : crossRoads)
            drawCrossRoadLine(cR);
//            drawCrossRoad(cR);

        for (Car car : cars)
            drawCarLine(car);
//            drawCarVector(car);
 //           drawCar(car);
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

//        System.out.println("maxX: "+ maxXM+", minX: "+minXM+", maxY: "+maxYM+", minY: "+minYM);

        if (scaleX < scaleY) {
            scale = scaleX;
            scaleY = (height * scaleX) / 2;
        } else {
            scale = scaleY;
            scaleX = (width * scaleY) / 2;
        }
    }

    public void drawRoadSegmentLine(RoadSegment rS) {
        g.setStroke(CR_RS_COLOR);
        roadWidth = rS.getLaneWidth();          /** inicializace šířky silnice */
        //if(scaleX > scaleY) g.setLineWidth(roadWidth * scaleX);
        //if(scaleY >= scaleX) g.setLineWidth(roadWidth * scaleY);
        g.setLineWidth(roadWidth);

//        System.out.println(roadWidth);
        double startX = rS.getStartPosition().getX();               /** definování jednotlivách proměnných */
        double startY = rS.getStartPosition().getY();
        double endX = rS.getEndPosition().getX();
        double endY = rS.getEndPosition().getY();

        System.out.println();
        System.out.println("ID: "+rS.getId());
        System.out.println("Start " +startX + ", "+ startY +", end " + endX + ", " + endY);
        System.out.println("#forward: "+rS.getForwardLanesCount()+", #backward: "+rS.getBackwardLanesCount());
//        System.out.println("ScaleX: " + scaleX + ", ScaleY: "+scaleY+", scale: " + scale);

//        if (rS.getForwardLanesCount() <= 2 && rS.getForwardLanesCount() > 0) {
//            for (int i = 0; i < rS.getForwardLanesCount(); i++) {       /** vykreslení jednoltivých pruhů hlavní silnice */ //rS.getForwardLanesCount()
//                g.strokeLine((startX + i * roadWidth) * scale, startY * scale, (endX + i * roadWidth) * scale, endY * scale);
//                System.out.println("ID: "+rS.getId());
//                System.out.println("0FStart " +startX + ", "+ startY +", end " + endX + ", " + endY);
//            }
//        }
//        if (rS.getBackwardLanesCount() <= 2 && rS.getBackwardLanesCount() > 0){
//            for (int i = 0; i < rS.getForwardLanesCount(); i++) {       /** vykreslení jednoltivých pruhů hlavní silnice */ //rS.getForwardLanesCount()
//                g.strokeLine((startX - ((i + 1) * roadWidth)) * scale, startY * scale, (endX - ((i + 1) * roadWidth)) * scale, endY * scale);
//                System.out.println("ID: "+rS.getId());
//                System.out.println("0BStart " +startX + ", "+ startY +", end " + endX + ", " + endY);
//            }
//        }

        if (rS.getForwardLanesCount() > 0) {
            for (int i = 0; i < rS.getForwardLanesCount(); i++) {
                //g.strokeLine((startX - i * roadWidth)* scale, (startY - i * roadWidth) * scale, (endX - i * roadWidth)* scale, (endY - i * roadWidth) * scale);
                g.strokeLine(startX * scale, (startY - i * roadWidth) * scale, endX * scale, (endY - i * roadWidth) * scale);

                System.out.println("ID: "+rS.getId());
                System.out.println("1FStart " +startX + ", "+ startY +", end " + endX + ", " + endY);
             }
        }
        if (rS.getBackwardLanesCount() > 0) {
            for (int i = 0; i < rS.getForwardLanesCount(); i++) {
//                g.strokeLine(startX * scale, (startY + ((i + 1) * roadWidth) + DIVING_STRIP_WIDTH) * scale,
//                        endX * scale, (endY + ((i + 1) * roadWidth) + DIVING_STRIP_WIDTH) * scale);
                g.strokeLine(startX * scale, (startY + ((i + 1) * roadWidth + DIVING_STRIP_WIDTH)) * scale,
                        endX * scale, (endY + ((i + 1) * roadWidth + DIVING_STRIP_WIDTH)) * scale);
                System.out.println("ID: "+rS.getId());
                System.out.println("1BStart " +startX + ", "+ startY +", end " + endX + ", " + endY);
            }
        }

//        roadWidth = rS.getLaneWidth();
    }

    public void drawCarLine(Car car) {
        g.setStroke(CAR_COLOR);
        Affine t = g.getTransform();
        carWidth = roadWidth - 2;
        double carLength = car.getLength();
        double carX = car.getPosition().getX();
        double carY = car.getPosition().getY();
        double rotation = Math.toDegrees(car.getOrientation());

        g.translate((carX * scale), (carY ) * scale);
        g.setLineWidth(carWidth);
        g.rotate(rotation);
        g.strokeLine(0, 0, carLength, 0);
        g.setTransform(t);
    }



    public void drawCrossRoadLine(CrossRoad cR) {
//        g.setStroke(CR_RS_COLOR);
        g.setStroke(Color.PINK);
        g.setLineWidth(roadWidth);
        Lane[] lanes = cR.getLanes();
        EndPoint[] ends = cR.getEndPoints();
        RoadSegment[] roadsC = cR.getRoads();

        double aX=0, aY=0, bX=0, bY=0;
        double cX, cY, dX, dY;
        for(int i = 0; i < lanes.length; i++) {
            if (lanes[i] == null) continue;
            for (int j = 0; j < roadsC.length ; j++) {
                if (roadsC[j] == null) continue;

                if(lanes[i].getStartRoad().equals(roadsC[j])) { // == roadsC[j]) {              //lanes[i].getStartRoad().getId() == roadsC[j].getId())
                    if(ends[j] == EndPoint.START){
                        aX = lanes[i].getStartRoad().getStartPosition().getX();
                        aY = lanes[i].getStartRoad().getStartPosition().getY();
                    } else {
                        aX = lanes[i].getStartRoad().getEndPosition().getX();
                        aY = lanes[i].getStartRoad().getEndPosition().getY();
                    }
                }

                if(lanes[i].getEndRoad().equals(roadsC[j])) {// == roadsC[j]) {
                    if(ends[j] == EndPoint.START){
                        bX = lanes[i].getEndRoad().getStartPosition().getX();
                        bY = lanes[i].getEndRoad().getStartPosition().getY();
                    } else {
                        bX = lanes[i].getEndRoad().getEndPosition().getX();
                        bY = lanes[i].getEndRoad().getEndPosition().getY();
                    }
                }
            }
//            System.out.println(/*lanes[i].getStartRoad().getLaneWidth()+*/"SLN: "+
//            lanes[i].getStartLaneNumber()+", ELN: "+lanes[i].getEndLaneNumber()/*+", "+lanes[i].*/);
//            System.out.println(/*lanes[i].getStartRoad().getLaneWidth()+*/"SLN2: "+
//                    Math.abs(lanes[i].getStartLaneNumber())+", ELN2: "+ Math.abs(lanes[i].getEndLaneNumber())/*+", "+lanes[i].*/);
//            System.out.println(aX+ ", "+aY+"; "+bY +","+ bX);

            double vecXS = bX - aX;               /** x prvek směrnicového vektoru */
            double vecYS = bY - aY;               /** y prvek směrnicového vektoru */
            double normX = vecYS;                 /** x prvek normálového vektoru */
            double normY = -vecXS;                /** y prvek normálového vektoru */
            double lenNorm = Math.sqrt((normX * normX) + (normY * normY));

            cX = aX + normX * (roadWidth / lenNorm);
            cY = aY + normY * (roadWidth / lenNorm);
            dX = bX + normX * (roadWidth / lenNorm);
            dY = bY + normY * (roadWidth / lenNorm);

            double startLN = lanes[i].getStartLaneNumber();
//            for (int k = 0; k < Math.abs(startLN) - 1; k++) {
//                if(startLN < k) {
//                   g.strokeLine((aX - ((k + 1) * roadWidth)) * scale, aY * scale, (bX - ((k + 1) * roadWidth)) * scale, bY * scale);
//                }
//                if(startLN >= k) {
//                    g.strokeLine((aX + k * roadWidth) * scale, (aY + k * roadWidth)* scale, (bX + k * roadWidth) * scale, (bY + k * roadWidth) * scale);
//                }
//            }
                g.strokeLine(aX * scale, aY * scale, bX * scale, bY * scale);
                g.strokeLine(cX * scale, cY * scale, dX * scale, dY * scale);
//                g.strokeLine((aX + k * roadWidth) * scale, (aY + k * roadWidth)* scale, (bX + k * roadWidth) * scale, (bY + k * roadWidth) * scale);
//            }
//            for (int k = 0; k < Math.abs(lanes[i].getEndLaneNumber())-1; k++) {
//                g.strokeLine(aX * scale, aY * scale, bX * scale, bY * scale);
////                g.strokeLine((aX - ((k + 1) * roadWidth)) * scale, aY * scale, (bX - ((k + 1) * roadWidth)) * scale, bY * scale);
//            }
        }


//           System.out.println(lanes[i].getStartRoad().getStartPosition()+" a "+ lanes[i].getStartRoad().getEndPosition() +", " +
//                    lanes[i].getEndRoad().getStartPosition()+" a "+ lanes[i].getEndRoad().getEndPosition() +";;;; " +
//                    "" +lanes[i].getStartRoad().getEndPointPosition(ends[i]));

            //System.out.println(roadsC[i].getStartPosition()+ ", " + roadsC[i].getEndPosition());
           // System.out.println(lanes[i].getStartLaneNumber()+ ", " + lanes[i].getEndLaneNumber());
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

        g.setStroke(CR_RS_COLOR);
        g.setLineWidth(roadWidth*2);

        double startX = rS.getStartPosition().getX();               /** definování jednotlivách proměnných */
        double startY = rS.getStartPosition().getY();
        double endX = rS.getEndPosition().getX();
        double endY = rS.getEndPosition().getY();
        double lenX = endX - startX;
        double lenY = endY - startY;
        double roadLen = Math.sqrt(lenX * lenX + lenY * lenY);
        double angle = Math.toDegrees(Math.atan2(lenY, lenX));

        if(rS.getId().contains("Hlavni")) {
            g.translate((startX * scaleX), ((startY ) * scaleY)); //startY - roadWidth
            g.rotate(angle);
            for (int i = 0; i < rS.getForwardLanesCount(); i++) {       /** vykreslení jednoltivých pruhů hlavní silnice */
                g.strokeLine(0, i * (roadWidth * scaleY), roadLen * scaleX, i*(roadWidth*scaleY));      /**  - roadWidth * scaleY*/
                g.strokeLine(0, i * (roadWidth * 2.75) + DIVING_STRIP_WIDTH + roadWidth +scaleX, roadLen * scaleX, i*(roadWidth * scaleY) + DIVING_STRIP_WIDTH + roadWidth +scaleX);
               // g.fillRect(0, i * (roadWidth * 2.75) - roadWidth * 2.75, roadLen * scaleX, roadWidth * scaleY);
               // g.fillRect(0, i * (roadWidth * 2.75) + DIVING_STRIP_WIDTH + roadWidth * 2.75, roadLen * scaleX, roadWidth * scaleY);
            }
            g.setTransform(t);          /** vrácení původní soustavy souřadnic */
        } else {
        //if(rS.getId().contains("Vedlejsi")) {

            g.translate(((startX ) * scaleX), ((startY) * scaleY)); //startX-6.5
            g.rotate(angle);
            roadCounter = roadCounter - 2;                      /** Vždycky budou jen 2 hlavní. Více jích nemůže. */
     //       for (int i = 0; i < roadCounter(); i++) {
//                g.strokeLine(0, - 0 * (roadWidth ) - roadWidth * 2.75, roadLen * scaleX, roadWidth * scaleY); /** vykreslení silnice pro vedlejší cestu */
//                g.strokeLine(0, - 1 * (roadWidth * 2.75) - roadWidth * 2.75, roadLen * scaleX, roadWidth * scaleY);
//            }
            g.setTransform(t);
        }

//        g.setTransform(t);*/

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
        RoadSegment[] roadsC = cR.getRoads();

        double aX = 0, aY = 0, bX = 0, bY = 0;
        for(int i = 0; i < roadsC.length; i++) {
            if(roadsC[i] == null) continue;
            if(ends[i] == EndPoint.START) {
                aX = roadsC[i].getEndPointPosition(ends[i]).getX();
                aY = roadsC[i].getEndPointPosition(ends[i]).getY();
            } else {
                bX = roadsC[i].getEndPointPosition(ends[i]).getX();
                bY = roadsC[i].getEndPointPosition(ends[i]).getY();
            }
            double lenX = aX - bX;
            double lenY = aY - bY;
            double roadLen = Math.sqrt(lenX * lenX + lenY * lenY);
            double angle = Math.toDegrees(Math.atan2(lenY, lenX));

//            g.translate((aX * scaleX), ((aY ) * scaleY)); //- roadWidth
//            g.rotate(angle);
            g.fillRect(0, roadWidth * 2.75, roadLen * scaleX, roadWidth * scaleY);
            g.setTransform(t);
        }
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
            carY = car.getPosition().getY();//+1 //roadWidth/2;
        } else if(rotation == 90 ) { //&& car.getPosition().getX() >= (width / 2)-2 ) {
            carX = car.getPosition().getX();//+1 //roadWidth/2;
            carY = car.getPosition().getY();
        } else if(rotation == -90 && car.getPosition().getX() <= width / 2) {
            carX = car.getPosition().getX();//-1 //roadWidth/2;
            carY = car.getPosition().getY();
        } else {
            carX = car.getPosition().getX();
            carY = car.getPosition().getY();
        }
//        System.out.println(carX + " - " + carY + " . " + rotation);
//        System.out.println(rotation);
        carY = carY-2;
        g.translate((carX * scaleX), ((carY) * scaleY)); //(carY - carWidth / 2)
        g.rotate(rotation);

        g.fillRect(0, 0, car.getLength() * scaleX, carWidth * scaleY);

        g.setTransform(t);
    }

    /**
     * aktualizuje vsechny objekty
     */
    public void update() {
        this.width = g.getCanvas().getWidth();
        this.height = g.getCanvas().getHeight();
        updateRatio(g.getCanvas().getWidth(), g.getCanvas().getHeight());

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
     * Metoda stanoví minimální a maximální souřadnice v metrech ve směru X a Y pro všechny
     * úseky silnic spuštěného scénáře, tj. vypočte min-max box takový, že veškeré elementy
     * dopravní sítě budou uvnitř, a uloží je do stavových proměnných
     */
    private void computeModelDimensions() { //RoadSegment[] roadSeg
        minXM = Double.MAX_VALUE;
        minYM = Double.MAX_VALUE;
        maxXM = Double.MIN_VALUE;
        maxYM = Double.MIN_VALUE;

        for (int i = 0; i < roadSegments.length; i++) {
            if(roadSegments[i].getEndPosition().getX() > maxXM) {
                maxXM = roadSegments[i].getEndPosition().getX();
            }
            if(roadSegments[i].getStartPosition().getX() > minXM) {
                minXM = roadSegments[i].getStartPosition().getX();
            }
            if(roadSegments[i].getEndPosition().getY() > maxYM) {
                maxYM = roadSegments[i].getEndPosition().getY();
            }
            if(roadSegments[i].getStartPosition().getY() > minYM) {
                minYM = roadSegments[i].getStartPosition().getY();
            }
        }

//        for (int i = 0; i < roadSeg.length; i++) {
//            if(roadSeg[i].getEndPosition().getX() > maxXM) {
//                maxXM = roadSeg[i].getEndPosition().getX();
//            }
//            if(roadSeg[i].getStartPosition().getX() > minXM) {
//                minXM = roadSeg[i].getStartPosition().getX();
//            }
//            if(roadSeg[i].getEndPosition().getY() > maxYM) {
//                maxYM = roadSeg[i].getEndPosition().getY();
//            }
//            if(roadSeg[i].getStartPosition().getY() > minYM) {
//                minYM = roadSeg[i].getStartPosition().getY();
//            }
//        }
    }


//
//    public void drawCarVector(Car car) {
//        g.setFill(CAR_COLOR);
//        Affine t = g.getTransform();
//        carWidth = roadWidth - 2;
//        double carLength = car.getLength();
//        double carX;
//        double carY;
//        this.height = g.getCanvas().getHeight();
//        this.width = g.getCanvas().getWidth();
//
//        double rotation = Math.toDegrees(car.getOrientation());
//        carX = car.getPosition().getX();
//        carY = car.getPosition().getY();
//
//        //System.out.println(car.getPosition());
////        System.out.println(rotation);
//        //carY = carY - 2;
//        g.translate((carX), (carY)); //(carX * scaleX), (carY - carWidth / 2) * scaleY
//        g.rotate(rotation);
//
//        g.fillRect(0, 0, carLength * scaleX, carWidth * scaleY);
//
//        g.setTransform(t);
//    }

    public void drawRoadSegmentVector(RoadSegment rS) {
        roadWidth = rS.getLaneWidth();          /** inicializace šířky silnice */
        g.setLineWidth(roadWidth);
        g.setStroke(CR_RS_COLOR);

        for (int i = 0; i < roadCounter/2; i++) {
            double startX = rS.getStartPosition().getX();
            double startY = rS.getStartPosition().getY();
            double endX = rS.getEndPosition().getX();
            double endY = rS.getEndPosition().getY();
            double vecXS = endX - startX;               /** x prvek směrnicového vektoru */
            double vecYS = endY - startY;               /** y prvek směrnicového vektoru */
//            double roadLen = Math.sqrt(vecXS * vecXS + vecYS * vecYS);
            double vecXN = vecYS;                       /** x prvek normálového vektoru */
            double vecYN = -vecXS;                      /** y prvek normálového vektoru */
            double roadLen = Math.sqrt(vecXN * vecXN + vecYN * vecYN);

            System.out.println("Start " +startX + ", "+ startY +", end " + endX + ", " + endY);
            g.strokeLine(startX*scale, startY*scale, endX*scale, endY*scale);

            for (int j = 1; j <= rS.getForwardLanesCount(); j++) {
                startX += (vecXN * i*2)/roadLen;// + roadWidth + DIVING_STRIP_WIDTH;       //roadWidth * 2.75
                startY += (/*i*/(vecYN * i*2)/roadLen + roadWidth + DIVING_STRIP_WIDTH);// + roadWidth + DIVING_STRIP_WIDTH;       //roadWidth * 2.75
                endX += (vecXN * i*2)/roadLen;
                endY += (vecYN * i*2)/roadLen + roadWidth + DIVING_STRIP_WIDTH;
                System.out.println("Start2 " +startX + ", "+ startY +", end2 " + endX + ", " + endY);
                g.strokeLine(startX*scale, startY*scale, endX*scale, endY*scale);
            }

            for (int j = 1; j <= rS.getBackwardLanesCount(); j++) {
                startX -= (vecXN * i)/roadLen;// + roadWidth + DIVING_STRIP_WIDTH;       //roadWidth * 2.75
                startY -= (/*i*/(vecYN * i)/roadLen + roadWidth + DIVING_STRIP_WIDTH);// + roadWidth + DIVING_STRIP_WIDTH;       //roadWidth * 2.75
                endX -= (vecXN * i)/roadLen;
                endY -= (vecYN * i)/roadLen + roadWidth + DIVING_STRIP_WIDTH;
                System.out.println("Start3 " +startX + ", "+ startY +", end3 " + endX + ", " + endY);
                g.strokeLine(startX*scale, startY*scale, endX*scale, endY*scale);
            }

            //g.strokeLine(startX * scaleX, (startY + i * roadWidth) * scaleY, endX * scaleX, (endY + i * roadWidth)* scaleY);
            //g.strokeLine(startX * scaleX, (startY - ((i+1) * roadWidth) + DIVING_STRIP_WIDTH) * scaleY, endX * scaleX, (endY - ((i) * roadWidth) + DIVING_STRIP_WIDTH) * scaleY);
            //g.fillRect(startX, startY, roadLen * scaleX, roadWidth * scaleY);
//            double newStartX = startX +((vecXN * roadWidth)/roadLen);// + roadWidth + DIVING_STRIP_WIDTH;       //roadWidth * 2.75
//            double newStartY = startY +(/*i*/(vecYN * roadWidth)/roadLen);// + roadWidth + DIVING_STRIP_WIDTH;       //roadWidth * 2.75
//            double newEndX = endX + ((vecXN * roadWidth)/roadLen);
//            double newEndY = endY + ((vecYN * roadWidth)/roadLen);


            //g.strokeLine(newStartX*scaleX, newStartY*scaleY, newEndX*scaleX, newEndY*scaleY);
            //System.out.println(newStartX+", "+newStartY);
            //g.fillRect(newStartX, newStartY, roadLen * scaleX, roadWidth * scaleY);
        }
    }
}