import TrafficSim.CrossRoad;
import TrafficSim.RoadSegment;
import TrafficSim.Scenarios.Scenario;
import TrafficSim.Simulator;

import java.awt.*;
import java.awt.geom.Point2D;

public class Main {

    public static void main(String[] args) {

        Simulator sim = new Simulator();
        Scenario scenar = new Scenario(sim);

        scenar.create();
        sim.addScenario(scenar);
    }

    public void computeModelDimensions() {
        /*Metoda stanoví minimální a maximální souřadnice v metrech ve směru X a Y pro všechny
        úseky silnic spuštěného scénáře, tj. vypočte min-max box takový, že veškeré elementy
        dopravní sítě budou uvnitř, a uloží je do stavových proměnných */


    }

    public void computeModel2WindowTransformation(int width, int height) {
        /*Metoda inicializuje stavové proměnné používané pro přepočet souřadnic modelu (v metrech)
        na souřadnice okna (v pixelech). Metoda určí vhodnou změnu měřítka a posun ve směru X a
        Y tak, aby bylo zaručeno, že se veškeré souřadnice modelu (v min-max boxu vypočteného v
        metodě computeModelDimensions ) transformují do okna o rozměrech width , height , a to
        včetně „přesahů“ grafických reprezentací elementů umístěných na extrémních souřadnicích.*/

    }

    public Point2D model2window(Point2D m) {
        /*Metoda převede souřadnice modelu na souřadnice okna s využitím hodnot stavových
        proměnných určených v metodě computeModel2WindowTransformation. */

        return m;
    }

    public void drawCar(Point2D position, double orientation, int lenght, int width, Graphics2D g) {
        /*Metoda vykreslí grafickou reprezentaci vozidla na souřadnicích position, udaných v
        pixelech v souřadném systému okna.Souřadnice position odpovídá středu předního
        nárazníku vozidla, orientation říká úhel natočení vozidla vůči ose x.Velikost grafické
        reprezentace bude maximálně length X width pixelů ve směru natočení vozidla.*/

    }

    public void drawLane(Point2D start, Point2D end, int size, Graphics2D g) {
        /*Metoda vykreslí grafickou reprezentaci jízdního pruhu (ať již na úseku silnice nebo
        křižovatky)vedoucího ze souřadnic start do souřadnic end udaných v pixelech v
        souřadném systému okna.Velikost grafické reprezentace bude maximálně size pixelů, tj.
                size / 2 pixelů na každou stranu od přímky spojující body start a end.*/

    }

    public void drawRoadSegment(RoadSegment road, Graphics2D g) {
        /*Metoda vykreslí grafickou reprezentaci úseku silnice s využitím volání výše uvedených
        metod.*/

    }

    public void drawCrossRoad(CrossRoad cr, Graphics2D g) {
        /*Metoda vykreslí grafickou reprezentaci úseku křižovatky s využitím volání výše uvedených
        metod.*/

    }

    public void drawTrafficState(Simulator sim, Graphics2D g) {
        /*Metoda vykreslí celou dopravní síť reprezentovanou instancí sim, tj.voláním výše
        uvedených metod vykreslí všechny elementy sítě ve stavu poskytnutém instancí třídy
        Simulator.*/

    }

   /* Kromě těchto metod bude nutné v metodě paint(…) zajistit volání metod
    computeModel2WindowTransformation a drawTrafficState , a v metodě Main
    programu zajistit vytvoření instance třídy Simulator a smyčky zajišťující pravidelné
    překreslování okna v intervalu 100 ms.*/
}
