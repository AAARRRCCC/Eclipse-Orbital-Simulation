import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.*;
import javafx.util.Duration;

public class EclipseVis extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Orbital Eclipse Diagram");

        Canvas canvas = new Canvas(600,600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setAlwaysOnTop(true);

        //animation
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            //update angles and push every 40 millis, full orbit every 100 seconds
            double angle = (System.currentTimeMillis() % 100000)/ 100000.0 *2 *Math.PI;
            double earthAngle = angle*365.242374; //this many rotations per "year"
            drawOrbits(gc, angle, earthAngle);

        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


    }


    private void drawOrbits(GraphicsContext gc, double angle, double earthAngle){
        //setting background black
        gc.setFill(Color.DARKSLATEGRAY);
        gc.fillRect(0,0,gc.getCanvas().getHeight(), gc.getCanvas().getWidth());

        //constants
        double sunX = 300, sunY = 300, sunRadius = 20;
        double earthRadius = 5;
        double orbitRadius = 150;

        //Earth pos
        double earthX = sunX +orbitRadius * Math.cos(angle) - earthRadius;
        double earthY = sunY +orbitRadius * Math.sin(angle) - earthRadius;

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
        gc.strokeOval(sunX - orbitRadius, sunY - orbitRadius, orbitRadius *2, orbitRadius *2);

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






