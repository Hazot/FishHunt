package Entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Fishes extends Entities{

    protected double v;
    protected Image entityImage;

    /**
     *  Constructeur de paramètres communs à tous les poisson
     * @param level Niveau au moment de la génération du poisson
     */
    public Fishes(int level) {

        // Grosseur initial d'un poisson
        this.largeur = (int) (Math.random() * 41) + 120;
        this.hauteur = largeur;

        // Détermine la vitesse de base d'un poisson
        v = 100*Math.pow(level, (double)1/3) + 200;
}

    /**
     * Dessine l'affichage de n'importe quel poisson sur le canevas
     * @param context Contexte graphique général du canevas de l'aplication
     */
    public void draw(GraphicsContext context) {
        context.drawImage(entityImage, x, y , largeur, hauteur);
    }
}
