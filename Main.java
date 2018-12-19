/**
  * L-System-Generator
  *
  * 2018-12-19
  *
  * Mikko Halmela
  *
  *GPLv2
  */

package source.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class Main extends Application {

    public static void main(String[] args) {
        launch(Main.class);
    }

    public void start(Stage stage) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Width:");
        int width = 1200;
        //int width = Integer.valueOf(scan.nextLine());

        System.out.print("Height:");
        int height = 1200;
        //int height = Integer.valueOf(scan.nextLine());

        //Examples of L-Systems
        //Koch curve
        HashMap<String, String> kVar = new HashMap<>();
        kVar.put("F", "F+F-F-F+F");

        ArrayList<String> kConstants = new ArrayList<>();
        kConstants.add("+");
        kConstants.add("-");

        LSystem kochCurve = new LSystem("F", kVar, kConstants, 90);
        kochCurve.addRule("F", "forward");
        kochCurve.addRule("-", "left");
        kochCurve.addRule("+", "right");
        kochCurve.setStart(new Point(10,height - 10, 0));

        //Sierpinski approximator
        HashMap<String, String> sVar = new HashMap<>();
        sVar.put("A", "B-A-B");
        sVar.put("B", "A+B+A");

        ArrayList<String> sConstants = new ArrayList<>();
        sConstants.add("+");
        sConstants.add("-");

        LSystem sierpinski = new LSystem("A", sVar, sConstants, 60);
        sierpinski.addRule("A", "forward");
        sierpinski.addRule("B", "forward");
        sierpinski.addRule("-", "left");
        sierpinski.addRule("+", "right");
        sierpinski.setStart(new Point(width *0.01,height * 0.99, 0));

        //Sierpinski
        HashMap<String, String> s1Var = new HashMap<>();
        s1Var.put("F", "F-G+F+G-G");
        s1Var.put("G", "GG");

        ArrayList<String> s1Constants = new ArrayList<>();
        s1Constants.add("+");
        s1Constants.add("-");

        LSystem sierpinski1 = new LSystem("F-F-F", s1Var, s1Constants, 120);
        sierpinski1.addRule("F", "forward");
        sierpinski1.addRule("G", "forward");
        sierpinski1.addRule("-", "right");
        sierpinski1.addRule("+", "left");
        sierpinski1.setStart(new Point(10,height - 10, 0));

        //Binary tree
        HashMap<String, String> binVar = new HashMap<>();
        binVar.put("0", "1[0]0");
        binVar.put("1", "11");

        ArrayList<String> binConstants = new ArrayList<>();
        binConstants.add("[");
        binConstants.add("]");

        LSystem binaryTree = new LSystem("0", binVar, binConstants, 45);
        binaryTree.addRule("1", "forward");
        binaryTree.addRule("0", "forward end");
        binaryTree.addRule("[", "push left");
        binaryTree.addRule("]", "pop right");
        binaryTree.setStart(new Point(width / 2.0, height, 90));

        //Dragon curve
        HashMap<String, String> draVar = new HashMap<>();
        draVar.put("X", "X+YF+");
        draVar.put("Y", "-FX-Y");

        ArrayList<String> draConstants = new ArrayList<>();
        draConstants.add("F");
        draConstants.add("+");
        draConstants.add("-");

        LSystem dragonCurve = new LSystem("FX", draVar, draConstants, 90);
        dragonCurve.addRule("F", "forward");
        dragonCurve.addRule("-", "left");
        dragonCurve.addRule("+", "right");
        dragonCurve.addRule("X", "none");
        dragonCurve.addRule("Y", "none");
        dragonCurve.setStart(new Point(width * 0.75 , height *0.5, 0));

        //Fractal plant
        HashMap<String, String> fVar = new HashMap<>();
        fVar.put("X", "F+[[X]-X]-F[-FX]+X");
        fVar.put("F", "FF");

        ArrayList<String> fConstants = new ArrayList<>();
        fConstants.add("+");
        fConstants.add("-");
        fConstants.add("[");
        fConstants.add("]");

        LSystem fractalPlant = new LSystem("X", fVar, fConstants, 25);
        fractalPlant.addRule("F", "forward");
        fractalPlant.addRule("-", "left");
        fractalPlant.addRule("+", "right");
        fractalPlant.addRule("[", "push");
        fractalPlant.addRule("]", "pop");
        fractalPlant.addRule("X", "none");
        fractalPlant.setStart(new Point(width * 0.1, height *0.99, 65));


        //Setup canvas

        System.out.println("1. Koch curve\n" +
                "2. Sierpinski triangle\n" +
                "3. Sierpinski arrowhead curve (approximates Sierpinski triangle\n" +
                "4. Binary tree\n" +
                "5. Dragon curve\n" +
                "6. Fractal plant");
        String input = scan.nextLine();

        System.out.print("Iterations: ");
        int iterations = Integer.valueOf(scan.nextLine());

        Canvas canvas = new Canvas(width, height);
        GraphicsContext drawer = canvas.getGraphicsContext2D();
        drawer.setFill(Color.BLACK);

        switch (input){
            case "1" :
                kochCurve.setIterations(iterations);
                draw(drawer, kochCurve);
                break;

            case "2" :
                sierpinski1.setIterations(iterations);
                draw(drawer, sierpinski1);
                break;

            case "3" :
                sierpinski.setIterations(iterations);
                draw(drawer, sierpinski);
                break;

            case "4" :
                binaryTree.setIterations(iterations);
                draw(drawer, binaryTree);
                break;

            case "5" :
                dragonCurve.setIterations(iterations);
                draw(drawer, dragonCurve);
                break;

            case "6" :
                fractalPlant.setIterations(iterations);
                draw(drawer, fractalPlant);
                break;
        }

        BorderPane pane = new BorderPane();
        pane.setCenter(canvas);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    public void draw(GraphicsContext drawer, LSystem lSystem) {

        double initAngle = Math.toRadians(lSystem.getStart().getAngle());
        double angleChange = Math.toRadians(lSystem.getAngle());
        double x = lSystem.getStart().getX();
        double y = lSystem.getStart().getY();
        double line = 7;
        Stack<Point> stack = new Stack<>();

        for (String s : lSystem.lastString().split("")) {
            ArrayList<String> rules = lSystem.getRule(s);

            if (rules.contains("none")) {
                continue;
            }

            if (rules.contains("push")) {
                stack.push(new Point(x, y, initAngle));
            }

            if (rules.contains("pop")) {
                Point point = stack.pop();
                x = point.getX();
                y = point.getY();
                initAngle = point.getAngle();
            }

            if (rules.contains("left")) {
                initAngle -= angleChange;
            }

            if (rules.contains("right")) {
                initAngle += angleChange;
            }

            if (rules.contains("forward")) {
                double newX = x + line * Math.cos(initAngle);
                double newY = y - line * Math.sin(initAngle);

                drawer.strokeLine(x, y, newX, newY);
                //drawer.strokeOval(newX, newY, 4, 4);

                x = newX;
                y = newY;
            }

            if (rules.contains("end")) {
                drawer.strokeOval(x, y, 2, 2);
            }
        }


    }
}

