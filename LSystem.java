package source.main;

import java.util.ArrayList;
import java.util.HashMap;

public class LSystem {

    /* axiom is the first word
     * variables contain symbols and words they produce
     * angle is constructed in degrees
     * constants are symbols that produce only themselves
     * rules specify what each symbol does
     */


    private String axiom;
    private int iterations;
    private HashMap<String, String> variables;
    private String[] outputs;
    private double angle;
    private ArrayList<String> constants;
    private HashMap<String, ArrayList<String>> rules;
    private Point start;


    public LSystem(String axiom, HashMap<String, String> vars, ArrayList<String> cons, double angle) {
        this.axiom = axiom;
        this.iterations = 0;
        this.variables = vars;
        this.outputs = new String[iterations];
        this.angle = angle;
        this.constants = cons;
        this.rules = new HashMap<>();
        this.start = new Point(0, 0, 0);
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getIterations() {
        return iterations;
    }

    public HashMap<String, String> generateRules() {
        HashMap<String, String> newRules = new HashMap<>();

        newRules.put("F", "forward");
        newRules.put("G", "forward");
        newRules.put("-", "left");
        newRules.put("+", "right");
        newRules.put("X", "none");
        newRules.put("Y", "none");
        newRules.put("[", "push");
        newRules.put("]", "pop");

        return newRules;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getStart() {
        return this.start;
    }

    //one or more: none, push, pop, left, right, forward, end
    //order matters
    //separator: " "
    public void addRule(String v, String rule) {
        ArrayList<String> rules = new ArrayList<>();
        for (String s : rule.split(" ")) {
            rules.add(s);
        }
        this.rules.put(v, rules);
    }

    public ArrayList<String> getRule(String v) {
        return this.rules.get(v);
    }


    public void axiomParser() {
        String[] axioms = new String[this.iterations];
        String a = this.axiom;

        for (int i = 0; i < this.iterations; i++) {
            StringBuilder builder = new StringBuilder();

            //On every iteration a character is either replaced by a production
            //or applied again to a new string
            for (String s : a.split("")) {
                if (s.isEmpty()) {
                    break;
                }
                if (s.equals("+") && builder.substring(builder.length() - 1).equals("-") ||
                        (s.equals("-") && builder.substring(builder.length() - 1).equals("+"))) {
                            builder.append("-");
                } else if (this.isConstant(s)) {
                    builder.append(s);
                } else {
                        builder.append(this.variables.get(s));
                    }

            }

            a = builder.toString();
            axioms[i] = a;
        }
        for (String s: axioms) {
            System.out.println(s);
        }
        this.outputs = axioms;
    }

    public String lastString() {
        this.axiomParser();
        return this.outputs[this.outputs.length - 1];
    }

    public double getAngle() {
        return angle;
    }


    public boolean isConstant(String comp) {
        if (this.constants.contains(comp)) {
            return true;
        }
        return false;
    }


}
