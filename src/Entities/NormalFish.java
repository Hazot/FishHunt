package Entities;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class NormalFish extends Fishes{

    /**
     * Constructeur d'un poisson normal
     * @param level Niveau au moment de la génération du poisson
     */
    public NormalFish(int level) {
        super(level);

        // Détermination de l'image du poisson aléatoirement
        String fishKind = "/Images/fish/0" + (int) (Math.random() * 8) + ".png";
        entityImage = new Image(fishKind, largeur, hauteur, false, false);
        entityImage = ImageHelpers.colorize(entityImage, Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256), 1));

        // EN Y - Vitesse aléatoire initiale et gravité (accélération)
        y = Math.random()*3*HEIGHT/5 + (double) HEIGHT/5 - largeur/2;
        vy = -(((int) (Math.random() * 101)) + 100);
        ay = 100;

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

}
