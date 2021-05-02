package Entities;

import javafx.scene.image.Image;

public class Starfish extends Fishes{

    // Hauteur initiale qui est l'ordonnée à l'origine de la fonction d'onde
    private double yInit;

    /**
     * Constructeur d'un étoile de mer
     * @param level Niveau au moment de la génération du poisson
     */
    public Starfish(int level) {
        super(level);

        // Hauteur initiale - largeur/2 pour remonter un peu la position en fonction de la largeur
        yInit = Math.random()*3*HEIGHT/5 + (double) HEIGHT/5 - largeur/2;

        // Chargement de l'image de l'étoile de mer
        entityImage = new Image("/Images/star.png", largeur, hauteur, false, false);

        // EN X - Vitesse de sens aléatoire (flip de l'image selon le côté du spawn)
        if (Math.random() < 0.5) {
            x = -largeur;
            vx = v;
        } else {
            entityImage = ImageHelpers.flop(entityImage);
            x = WIDTH;
            vx = -v;
        }
    }

    /**
     * Met à jour la position en x et en y de l'étoile de mer
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    @Override
    public void update(double dt) {

        x += dt * vx;

        // Équation d'onde pour déterminer la position de y en fonction de la position en x (allure de vague)
        y = yInit + 50*Math.sin(2*Math.PI/300*x);

    }

}
