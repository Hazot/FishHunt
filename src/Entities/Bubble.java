package Entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bubble extends Entities{

    /**
     * Constructeur de bulle comprenant 1 seul argument
     * @param x position en x (généré au hasard d'avance)
     */
    public Bubble(double x) {
        this.x = x;
        this.y = 500 + 10*Math.random();
        this.color = Color.rgb(0, 0, 255, 0.4);
        this.vy = -350 - 100*Math.random();

        // Rayon entre 5 et 20 pixels
        double r = 5 + 15*Math.random();
        this.largeur = r * 2;
        this.hauteur = r * 2;
    }

    /**
     * Dessine l'affichage d'une plateforme sur le canevas
     * @param context contexte graphique général du canevas de l'aplication
     */
    public void draw(GraphicsContext context) {
        context.setFill(this.color);
        context.fillOval(x, y, largeur, hauteur);
    }
}
