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

public class EclipseVis extends Application {
    private double elapsedTime = 0.0;

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
            //update angles and push every 10 millis, full orbit every 100 seconds
            elapsedTime += .04 * speedSlider.getValue();
            double angle = (elapsedTime/100) *2 *Math.PI;
            double earthAngle = angle*365.242374; //this many rotations per "year"
            drawOrbits(gc, angle, earthAngle);

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


    private void drawOrbits(GraphicsContext gc, double angle, double earthAngle){
        //setting background black
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillRect(0,0,gc.getCanvas().getHeight(), gc.getCanvas().getWidth());


        //constants
        double earthRadius = 5;
        double orbitRadius = 150;

        double semiMajorAxis = 150;
        double eccentricity = 0.0167; //earths orbital eccentricity
        double focalDistance = semiMajorAxis * eccentricity;
        double semiMinorAxis = Math.sqrt(Math.pow(semiMajorAxis,2) - Math.pow(focalDistance,2));

        double centerX = 300, centerY = 300;
        double sunX = centerX + focalDistance; //center of ellipse is at (centerX, centerY)
        double sunY = centerY;
        double sunRadius = 20;

        //Earth pos

        double earthX = sunX +semiMajorAxis * Math.cos(angle) - earthRadius;
        double earthY = centerY +semiMinorAxis * Math.sin(angle) - earthRadius;

        //earth point line

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        double lineLength = 20;
        gc.strokeLine(earthX+earthRadius,
                earthY+earthRadius,
                earthX +earthRadius +lineLength *Math.cos(earthAngle),
                earthY +earthRadius +lineLength *Math.sin(earthAngle));

        //draw orbit path
        gc.setStroke(Color.NAVAJOWHITE);
        gc.strokeOval(centerX - semiMajorAxis, centerY - semiMinorAxis, semiMajorAxis *2, semiMinorAxis *2);

        //drawing sun at the center
        gc.setFill(Color.PAPAYAWHIP);
        gc.fillOval(sunX - sunRadius, sunY - sunRadius, sunRadius *2, sunRadius *2);

        //drawing earth
        gc.setFill(Color.LIGHTSTEELBLUE);
        gc.fillOval(earthX, earthY, earthRadius *2, earthRadius *2);

    }

    public static void main(String[] args){
        launch(args);
    }
}






