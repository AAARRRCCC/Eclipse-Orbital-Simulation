import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * TODO
 * <p>Scale: The size of the Earth and the Sun relative to the size of the orbit should be to scale, or at least proportionally representative if you're not using a true-to-size scale. This helps with visual understanding.</p>
 * <p>Earth's Rotation: If you're showing Earth's rotation, there might be an arrow or some indicator of its orientation. If it's not there yet, it's something you might add later.</p>
 */

public class EclipseVis extends Application {
    private double elapsedTime = 0.0;
    private static final double ORBITAL_PERIOD = 10; //arbitrary num for sim speed
    private static final double ECCENTRICITY = 0.3; // Earth's Eccentricity is 0.0167





    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Orbital Eclipse Diagram");

        Canvas canvas = new Canvas(600,600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //gui
        Slider speedSlider = new Slider(.1,2.0,1.0);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(.5);
        speedSlider.setBlockIncrement(.1);
        speedSlider.setPrefWidth(500);


        //animation
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            //update angles and push every 10 millis, full orbit every ORBITAL_PERIOD seconds
            elapsedTime += .04 * speedSlider.getValue();
            double meanAnomaly = (2 * Math.PI * elapsedTime / ORBITAL_PERIOD) % (2 * Math.PI);
            double trueAnomaly = OrbitalMechanics.trueAnomaly(meanAnomaly, ECCENTRICITY);
            drawOrbits(gc, trueAnomaly);

        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


        BorderPane root = new BorderPane();
        HBox controls = new HBox(10); //spacing bw controls
        controls.getChildren().addAll(speedSlider); //can add more here

        root.setBottom(controls);
        root.setCenter(canvas);

        primaryStage.setScene((new Scene(root, 600, 700)));
        primaryStage.show();
        primaryStage.setAlwaysOnTop(true);


    }


    private void drawOrbits(GraphicsContext gc, double trueAnomaly){
        //setting background black
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillRect(0,0,gc.getCanvas().getHeight(), gc.getCanvas().getWidth());


        //constants
        double semiMajorAxis = 150;
        double focalDistance = semiMajorAxis * ECCENTRICITY;
//        double semiMinorAxis = Math.sqrt((semiMajorAxis * semiMajorAxis) - (focalDistance * focalDistance));
//        double semiMinorAxis = semiMajorAxis * (1-ECCENTRICITY); //exaggerated for effect
        double semiMinorAxis = semiMajorAxis * Math.sqrt(1 - ECCENTRICITY * ECCENTRICITY);
        double centerX = 300, centerY = 300;
        double sunX = centerX - focalDistance;
        double sunY = centerY;
        double sunRadius = 20;
        double earthRadius = 5;

        //draw orbit path (ellipse)
        gc.setStroke(Color.NAVAJOWHITE);
        gc.strokeOval(centerX - semiMajorAxis, centerY - semiMinorAxis, semiMajorAxis *2, semiMinorAxis *2);

        //drawing sun at one of the foci
        gc.setFill(Color.PAPAYAWHIP);
        gc.fillOval(sunX - sunRadius, sunY - sunRadius, sunRadius *2, sunRadius *2);

        //Earth pos
        double earthX = centerX - (semiMajorAxis * Math.cos(trueAnomaly)) - earthRadius;
        double earthY = centerY + (semiMinorAxis * Math.sin(trueAnomaly)) - earthRadius;

        //Draw Earth at pos
        gc.setFill(Color.LIGHTSTEELBLUE);
        gc.fillOval(earthX,earthY,earthRadius *2, earthRadius *2);


//        //earth point line
//        gc.setStroke(Color.WHITE);
//        gc.setLineWidth(2);
//        double lineLength = 20;
//        gc.strokeLine(earthX+earthRadius,
//                earthY+earthRadius,
//                earthX +earthRadius +lineLength *Math.cos(earthAngle),
//                earthY +earthRadius +lineLength *Math.sin(earthAngle));


    }

    public static void main(String[] args){
        launch(args);
    }
}






