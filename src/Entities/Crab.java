package Entities;

import javafx.scene.image.Image;

public class Crab extends Fishes{

    // Compteur en secondes
    private double timer = 0;

    // Compteur A pour le temps qu'un crabe irait à droite
    private double timerA;

    // Compteur B pour le temps qu'un crabe irait à gauche
    private double timerB;

    // Booléen qui stocke la direction initial du crabe au moment de son spawn
    private boolean crabGoingRight;

    /**
     * Constructeur d'un crabe
     * @param level Niveau au moment de la génération du poisson
     */
    public Crab(int level) {
        super(level);

        // Hauteur initiale - largeur/2 pour remonter un peu la position en fonction de la largeur
        y = Math.random()*3*HEIGHT/5 + (double) HEIGHT/5 - largeur/2;

        // Chargement de l'image du crabe
        entityImage = new Image("/Images/crabe.png", largeur, hauteur, false, false);

        // EN X - Vitesse plus rapide de sens aléatoire et sens du crab (flip de l'image selon le côté du spawn)
        if (Math.random() < 0.5) {
            x = -largeur;
            vx = 1.3*v;
            crabGoingRight = true;
        } else {
            entityImage = ImageHelpers.flop(entityImage);
            x = WIDTH;
            vx = -1.3*v;
            crabGoingRight = false;
        }

        // Set des timers se comportant comme une limite de temps en fonction du côté initial du spawn
        if (crabGoingRight) {
            timerA = 0.5;
            timerB = 0.25;
        } else {
            timerA = 0.25;
            timerB = 0.5;
        }
    }

    /**
     * Met à jour la position et de la vitesse en x d'un crabe
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    @Override
    public void update(double dt) {

        // Compteur en secondes créé en additionnant les différences de temps entre les mises à jours
        timer += dt;

        // Quand le timer se rend à la limite (TimerA ou timerB), le sens de la vitesse est inversée
        if (timer >= timerA && crabGoingRight) {
            crabGoingRight = false;
            vx -= 2 * vx;
            timer = 0;
        }
        if (timer >= timerB && !crabGoingRight) {
            crabGoingRight = true;
            vx -= 2 * vx;
            timer = 0;
        }

        // Mise à jour de la position avec la nouvelle vitesse calculée
        x += dt * vx;

    }
}
