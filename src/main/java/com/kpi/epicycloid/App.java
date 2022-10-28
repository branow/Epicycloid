package com.kpi.epicycloid;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

/**
 * the class describe the window of the application
 * */
public class App extends Application {

    private Button draw;
    private TextField A, a, k;
    private Slider slider;
    private Canvas canvas;
    private GraphicsContext gc;
    private Color color = Color.rgb(50,0,120);

    /**
     * turn on the application
     * */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initialize(primaryStage);
        draw();
    }

    /**
     * @param stage - main window of application
     * the method initializes scene with all elements and show stage.
     * */
    private void initialize(Stage stage) {
        stage.setResizable(false);
        slider = new Slider();
        slider.setValue(10);
        slider.setMin(1);
        slider.setMax(100);
        slider.valueProperty().addListener(e->draw());
        draw = new Button("Draw");
        draw.setOnAction(e->draw());
        A = new TextField("8");
        A.setPrefWidth(50);
        A.setTooltip(new Tooltip("radius of a fixed circle"));
        a = new TextField("2");
        a.setPrefWidth(50);
        a.setTooltip(new Tooltip("the radius of the moving circle"));
        k = new TextField("10");
        k.setPrefWidth(50);
        k.setTooltip(new Tooltip("accuracy of sine and cosine measurement, the optimal value is 10"));
        canvas = new Canvas(500, 500);
        gc = canvas.getGraphicsContext2D();
        canvas.widthProperty().addListener(e->draw());
        canvas.heightProperty().addListener(e->draw());

        FlowPane fp = new FlowPane(A, a, k, draw);
        fp.setAlignment(Pos.CENTER);
        fp.setPrefHeight(40);
        fp.setVgap(5);
        fp.setHgap(5);

        AnchorPane.setTopAnchor(slider, 10.);
        AnchorPane.setLeftAnchor(slider, 10.);
        AnchorPane.setRightAnchor(slider, 10.);
        AnchorPane.setTopAnchor(canvas, 50.);
        AnchorPane.setLeftAnchor(canvas, 10.);
        AnchorPane.setRightAnchor(canvas, 10.);
        AnchorPane.setBottomAnchor(canvas, 50.);
        AnchorPane.setLeftAnchor(fp, 10.);
        AnchorPane.setRightAnchor(fp, 10.);
        AnchorPane.setBottomAnchor(fp, 10.);
        AnchorPane ap = new AnchorPane(slider, canvas, fp);
        Scene scene = new Scene(ap);
        stage.setTitle("Epicycloid");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * the method draws the coordinate system and the figure on the canvas
     * */
    private void draw() {
        double proportion = slider.getValue();
        gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        gc.setLineCap(StrokeLineCap.ROUND);
        drawDSC(proportion);
        drawFigure(proportion);
    }

    private void drawFigure(double p) {
        gc.setFill(color);
        double dA = Double.parseDouble(A.getText()), da = Double.parseDouble(a.getText());
        int dk = Integer.parseInt(k.getText());
        double angle = Math.PI*2*(da/dA);
        for (double i=0; i<angle; i+=angle/(canvas.getHeight()*10)) {
            double[] d = Logic.getPoint(dA*p, da*p, i, dk);
            for (double a = 0; a+i<Math.PI*2; a+=angle) {
                double x = d[0]*Math.cos(a)-d[1]*Math.sin(a);
                double y = d[0]*Math.sin(a)+d[1]*Math.cos(a);
                x+=canvas.getWidth()/2;
                y*=-1;
                y+=canvas.getHeight()/2;
                gc.fillOval(x, y, 1.5, 1.5);
            }
        }
    }

    private void drawDSC(double p) {
        int gap = 10;
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        Line x = new Line(0, h/2, w, h/2);
        Line y = new Line(w/2, 0, w/2, h);
        gc.strokeLine(x.getStartX(), x.getStartY(), x.getEndX(), x.getEndY());
        gc.strokeLine(y.getStartX(), y.getStartY(), y.getEndX(), y.getEndY());
        gc.strokeText("x", w-gap, h/2-gap);
        gc.strokeText("y", w/2-gap, gap);
        drawArrows(x, 15, 10, false);
        drawArrows(y, 15, 10, true);
        drawNums(p);
    }

    private void drawNums(double p) {
        int gap = 10;
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        int num = (int) (w/p);
        int d = 0;
        if (num<=5) {
            d = 1;
        } else if (num<=10) {
            d = 2;
        } else if (num<=20) {
            d = 5;
        } else if (num<=50) {
            d = 10;
        } else if (num<=100) {
            d = 20;
        } else if (num<=250) {
            d = 50;
        } else {
            d = 100;
        }
        for (int i = d; i*p<w/2; i+=d) {
            gc.fillOval(w/2+i*p, h/2, 2,2);
            gc.fillOval(w/2-i*p, h/2, 2,2);
            gc.fillOval(w/2, h/2-i*p, 2,2);
            gc.fillOval(w/2, h/2+i*p, 2,2);
            gc.strokeText("" + i, w/2+i*p, h/2-gap);
            gc.strokeText("" + -i, w/2-i*p, h/2-gap);
            gc.strokeText("" + i, w/2+gap, h/2-i*p);
            gc.strokeText("" + -i, w/2+gap, h/2+i*p);
        }
    }

    private void drawArrows(Line line, double angle, double length, boolean start) {
        double angleL = Math.atan((line.getStartY()-line.getEndY())/(line.getEndX()-line.getStartX()));
        double angle1 = angleL + Math.toRadians(angle);
        double angle2 = angleL - Math.toRadians(angle);
        double x1 = 0, y1 = 0, ax2 = 0, ay2 = 0, bx2 = 0, by2 = 0;
        if (start) {
            x1 = line.getStartX();
            y1 = line.getStartY();
        } else {
            x1 = line.getEndX();
            y1 = line.getEndY();
        }
        if (line.getEndX()>line.getStartX() || line.getEndY()<line.getStartY()) {
            ax2 = x1 - length*Math.cos(angle1);
            ay2 = y1 + length*Math.sin(angle1);
            bx2 = x1 - length*Math.cos(angle2);
            by2 = y1 + length*Math.sin(angle2);
        } else {
            ax2 = x1 + length*Math.cos(angle1);
            ay2 = y1 - length*Math.sin(angle1);
            bx2 = x1 + length*Math.cos(angle2);
            by2 = y1 - length*Math.sin(angle2);
        }
        gc.strokeLine(x1, y1, ax2, ay2);
        gc.strokeLine(x1, y1, bx2, by2);
    }

}