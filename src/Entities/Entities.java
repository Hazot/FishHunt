package Entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Entities {

    // Resolution
    protected static final int WIDTH = 640, HEIGHT = 480;

    // Attributs présents dans la "majorité" des sous-classes
    protected double largeur, hauteur;
    protected double x, y;
    protected double vx, vy;
    protected double ax, ay;
    protected Color color;
    protected boolean alive = true;

    /**
     * Met à jour la position et la vitesse de l'entité en x et en y
     *
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    public void update(double dt) {

        vx += dt * ax;
        vy += dt * ay;
        x += dt * vx;
        y += dt * vy;

    }

    public abstract void draw(GraphicsContext context);

    /*
     * Getters and setters utilisés
     */
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isAlive() {
        return alive;
    }
}
