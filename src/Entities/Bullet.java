package Entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bullet extends Entities{

    // Le rayon d'une balle tirée et sa vitesse d'éloignement
    private double rayon;
    private double vRayon;

    /**
     * Constructeur de balle lorsque l'on fait l'action de tirer
     * @param x Position en x du clique de souris agissant comme centre de la balle
     * @param y Position en y du clique de souris agissant comme centre de la balle
     */
    public Bullet(double x, double y) {

        this.x = x;
        this.y = y;
        this.vRayon = -300;
        this.rayon = 50;
        this.color = Color.BLACK;
    }

    /**
     * Méthode qui rend mort un poisson qui serait en intersection
     * @param other Poisson en évaluation
     */
    public void testCollision(Fishes other) {
        if (intersects(other)) {
            other.alive = false;
        }
    }

    /**
     * Si le centre de la balle se retrouve dans le rectangle formant l'image du poissons,
     * on retourne alors true.
     * @param other Poisson en pleine évaluation de son intersection
     * @return Retourne un booléen qui prouve l'intersection des deux entités
     */
    public boolean intersects(Fishes other) {
        return !(this.x - rayon < other.x
                || other.x + other.largeur < this.x - rayon
                || this.y - rayon < other.y
                || other.y + other.hauteur < this.y - rayon);
    }

    /**
     * Met à jour le rayon en fonction de dt et d'une vitesse de rétrécissement du rayon
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    @Override
    public void update(double dt) {
        rayon += dt * vRayon;
    }

    /**
     * Dessine sur le context graphique l'oval représentant la balle qui s'éloigne
     * @param context Contexte graphique général du canevas de l'aplication
     */
    public void draw(GraphicsContext context) {
        context.setFill(Color.BLACK);
        context.fillOval(x - rayon, y - rayon, 2 * rayon, 2 * rayon);
    }

    /*
     * Getters
     */
    public double getRayon() {
        return rayon;
    }
}