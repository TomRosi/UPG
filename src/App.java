import TrafficSim.Simulator;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

/**
 * Třída App startuje celý program
 * JavaFX
 *
 * @author Tomáš Rozsypal
 */
public class App extends Application {

    private static final int DEF_WIDTH = 500;       /** základní šířka */
    private static final int DEF_HEIGTH = 500;      /** základní výška */

    /**
     * Metoda Main launchuje celý program
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Metoda start, přepsaná.
     * Startuje traffic a vytváří okno.
     *
     * @param primaryStage dané okno
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Traffic");

        Group root = new Group();
        Canvas canvas = new Canvas(DEF_WIDTH, DEF_HEIGTH);
        root.getChildren().addAll(canvas);

        Scene s = new Scene(root, DEF_WIDTH, DEF_HEIGTH);
        primaryStage.setScene(s);
        primaryStage.show();

        Traffic traffic = new Traffic(canvas.getGraphicsContext2D(), new Simulator());

        setResizing(primaryStage, traffic, canvas);

        traffic.start();
    }

    /**
     * Metoda setResizing
     *
     * @param stage dáná stage
     * @param t traffic
     * @param canvas papír
     */
    public void setResizing(Stage stage, Traffic t, Canvas canvas) {
        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            canvas.setWidth(newValue.doubleValue());
        });

        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            canvas.setHeight(newValue.doubleValue());

    });
    }
}
