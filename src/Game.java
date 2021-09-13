
import Entities.*;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;

public class Game {

    // Taille de l'application (résolution)
    public static final int WIDTH = 640, HEIGHT = 480;

    // Initialisation des entités
    private ArrayList<Fishes> fishes;
    private ArrayList<Bullet> bullets;
    private LinkedList<Bubble> bubbles;

    // Game parameters
    private int level;
    private int score;
    private int finalScore;
    private int lives;
    private double timeAlive;
    private Image crosshair;
    private Image life1;
    private Image life2;
    private Image life3;

    // Timers qui s'active sur l'appel de certaines méthodes
    private double tempTimer = 0;
    private double tempTimerGameOver = 0;
    private boolean timerGameOverActive = false;

    // Valeur booléenne qui garde en mémoire si on a déjà ajouté une entité à une liste
    private boolean activeBubbles = false;
    private boolean activeNormalFish = false;
    private boolean activeSpecialFish = false;

    // Quand le niveau commence le spawn est désactivé, car on montre le level
    private boolean spawnDisabled = true;

    // Compte jusqu'à 5 et reset ensuite
    private int scoreCounterTo5 = 0;

    // Miscellaneous game things
    private boolean debuglevelUp = false;
    private boolean switchScene = false;
    private int nbFishCatched;
    private int nbBulletShot;
    private double accuracy = 0;
    private double finalAccuracy;
    private DecimalFormat accuracyFormat = new DecimalFormat( "#.##" );

    /**
     * Initiatlise les listes, les variables et les images pour une partie
     */
    public Game() {

        // Initialisation des compteurs de base du jeu
        level = 1;
        score = 0;
        lives = 3;
        timeAlive = 0;

        // Initialisation des listes d'entités
        fishes = new ArrayList<>();
        bullets = new ArrayList<>();
        bubbles = new LinkedList<>();

        // Chargement des images du HUD
        crosshair = new Image("/Images/cible.png", 50, 50, false, false);
        life1 = new Image("/Images/fish/00.png", 40, 40, false, false);
        life2 = new Image("/Images/fish/00.png", 40, 40, false, false);
        life3 = new Image("/Images/fish/00.png", 40, 40, false, false);

    }

    /**
     * Méthode qui ajoute 3 groupes de bulles aléatoires (soit 15 bulles) à une linkedList lorsque appelée
     */
    public void addBubbles() {
        // Génère 3 groupes de bulles avec des coordonnées de bases aléatoire
        for (int i = 1; i < 4; i++) {
            double baseX = Math.random() * WIDTH;
            // Génère 5 bulles de bulles sous le niveau
            for (int j = 1; j < 6; j++) {
                double randomX = baseX - 20 + 40 * Math.random();
                Bubble b = new Bubble(randomX);
                bubbles.add(b);
            }
        }
    }

    /**
     * Méthode qui ajoute un seul poisson spécial à chaque appel dans le tableau de poissons générés
     * Choix entre 2 poisson en fonction d'une probabilité de 1/2 pour chaque cas.
     */
    public void addSpecialFish() {
        // Génère 3 groupes de bulles avec des coordonnées de bases aléatoire
        double proba = Math.random();
        if (proba < 0.5) {
            Starfish starfish = new Starfish(level);
            fishes.add(starfish);
        } else {
            Crab crab = new Crab(level);
            fishes.add(crab);
        }
    }

    /**
     * Méthode qui génère des bulles après chaque 3 secondes écoulés
     */
    public void generateBubbles() {
        if ((int) timeAlive % 3 == 0 && !activeBubbles) {
            activeBubbles = true;
            addBubbles();
        }
        if ((int) timeAlive % 3 == 1) {
            activeBubbles = false;
        }
    }

    /**
     * Méthode qui génère des poissons en fonction du niveau et du temps.
     * Timer de 3 secondes pour les poissons normaux
     * Timer de 5 secondes pour les poissons spéciaux qui commence au niveau 2.
     * Une fois les poissons générés pour une certaine seconde (modulo 3 ou modulo 5)
     * du temps total du jeu, on ne génère pas de poisson pour le restant de la seconde.
     */
    public void generateFishes() {
        if (!spawnDisabled) {
            if ((int) timeAlive % 3 == 0 && !activeNormalFish) {
                activeNormalFish = true;
                NormalFish normalFish = new NormalFish(level);
                fishes.add(normalFish);
            }
            if ((int) timeAlive % 3 == 1) {
                activeNormalFish = false;
            }
            if (level > 1) {
                if ((int) timeAlive % 5 == 0 && !activeSpecialFish) {
                    activeSpecialFish = true;
                    addSpecialFish();
                }
                if ((int) timeAlive % 5 == 1) {
                    activeSpecialFish = false;
                }
            }
        }
    }

    /**
     *  Méthode qui permet de mettre à jour, en fonction de la logique du jeu,
     *  la position des graphiques par rapport au temps écoulé
     * @param dt temps écoulé depuis le dernier "update"
     */
    public void update(double dt) {

        // Temps total depuis le début du niveau
        timeAlive += dt;

        // On évalue s'il y a un intersection entre une balle et un poisson pour chaque balle créée
        for (Bullet b : bullets) {
            if (b.getRayon() <= 0) {
                for (Fishes f : fishes) {
                    b.testCollision(f);
                    if (!f.isAlive()) {
                        fishes.remove(f);
                        score += 1;
                        scoreCounterTo5 += 1;
                        nbFishCatched += 1;
                        break;
                    }
                }
            }
        }

        // Ajoute des bulles et des poissons aux niveaux selon des timers
        generateBubbles();
        generateFishes();

        // Update la position des bulles
        for (Bubble b : bubbles) {
            b.update(dt);
        }

        // Update la position des poissons
        for (Fishes f : fishes) {
            f.update(dt);
        }

        // Update la position des balles
        for (Bullet b : bullets) {
            b.update(dt);
        }

        // Pour optimiser l'utilisation de la mémoire des poissons, des balles et des bulles
        memoryManager();
    }

    /**
     * Optimise la mémoire en effaçant les éléments non affichés.
     * Génère la plateformes au fur et à mesure que la méduse monte.
     */
    public void memoryManager() {

        // Enlève les poissons s'ils sortent de l'écran par la gauche de 150 px (enlève une vie en même temps)
        for (int i = 0; i < fishes.size(); i++) {
            if (fishes.get(i).getX() < -150) {
                fishes.remove(i);
                lives -= 1;
            }
        }

        // Enlève les poissons s'ils sortent de l'écran par la droite de 50px (enlève une vie en même temps)
        for (int i = 0; i < fishes.size(); i++) {
            if (fishes.get(i).getX() > WIDTH + 50) {
                fishes.remove(i);
                lives -= 1;
            }
        }

        /*
         * Enlève les bulles lorsqu'elles sont 100 pixels au dessus de l'affichage,
         * ainsi les bulles prennent le temps de sortir au complet de l'écran avant de disparaître
         */
        for (int i = 0; i < bubbles.size(); i++) {
            if (bubbles.get(i).getY() < -100) {
                bubbles.remove(i);
            }
        }

        // Clean up pour les bullets, quand rayon -5, tu le delete gg ez
        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).getRayon() <= -5) {
                bullets.remove(i);
            }
        }
    }

    /**
     * Affichage du level Up (à l'aide de compteurs)
     */
    public void showLevelUp(GraphicsContext context) {
        // Si on level up avec la touche de déboggage, on start un timer, on disable le spawn
        if (debuglevelUp) {
            level += 1;
            spawnDisabled = true;
            tempTimer = timeAlive;
            debuglevelUp =  false;
        }
        /* Si le score est un multiple de 5, on level up, on bloque le spawn, on part un timer et on reset le compteur
         * de multiple de 5.
         */
        if (scoreCounterTo5 == 5) {
            level += 1;
            spawnDisabled = true;
            tempTimer = timeAlive;
            scoreCounterTo5 = 0;
        }
        // Affiche le level Up pendant les 3 prochaines secondes
        if (tempTimer + 3 >= timeAlive && !timerGameOverActive) {
            context.setFill(Color.WHITE);
            context.setFont(Font.font("Verdana", 80));
            context.setTextAlign(TextAlignment.CENTER);
            context.setTextBaseline(VPos.CENTER);
            context.fillText("Level " + level, Math.round(WIDTH) / 2, Math.round(HEIGHT) / 2);
        }
        // Réactive le spawn après 3 secondes
        if (tempTimer + 3 < timeAlive) {
            spawnDisabled = false;
        }
    }

    /**
     * Cette méthode permet d'afficher tous les éléments graphiques du jeu à la suite de la mise à jour
     * @param context Contexte graphique général du jeu permettant l'affichage sur le canevas
     * @param mouseX Position en x de la souris
     * @param mouseY Position en y de la souris
     */
    public void draw(GraphicsContext context, double mouseX, double mouseY) {

        // Affiche le fond marin
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);

        // Affiche les bulles
        for (Bubble b : bubbles) {
            b.draw(context);
        }

        // On affiche les plateformes
        for (Fishes f : fishes) {
            f.draw(context);
        }

        // On affiche les plateformes
        for (Bullet b : bullets) {
            b.draw(context);
        }

        // Affiche le score en haut de la fenêtre
        context.setFill(Color.WHITE);
        context.setFont(Font.font("Verdana", 30));
        context.setTextAlign(TextAlignment.CENTER);
        context.setTextBaseline(VPos.CENTER);
        context.fillText(String.valueOf(score), Math.round(WIDTH)/2, 40);
        context.setFont(Font.font("Verdana", 25));
        updateAccuracy();
        context.fillText("Accuracy: " + accuracy + " %", Math.round(WIDTH)/2, 460);

        // Affiche Level up + le level de départ
        showLevelUp(context);

        // Affichage du nombre de vies restantes ou du game over s'il ne reste aucune vie
        if (lives > 0) {
            context.drawImage(life1, 240, 70);
            if (lives > 1) {
                context.drawImage(life2, 300, 70);
                if (lives > 2) {
                    context.drawImage(life3, 360, 70);
                }
            }
        } else {
            // Si le timer de game over n'est pas actif alors qu'on a 0 vie, on l'active
            if (!timerGameOverActive) {
                timerGameOverActive = true;
                tempTimerGameOver = timeAlive;
                finalScore = score;
                finalAccuracy = accuracy;
            }
            // On affiche game over pendant les 3 prochaines secondes, ensuite on change la scène
            if (tempTimerGameOver + 3 >= timeAlive) {
                context.setFill(Color.RED);
                context.setTextBaseline(VPos.CENTER);
                context.setFont(Font.font("Verdana", 50));
                context.fillText("GAME OVER", Math.round(WIDTH) / 2, Math.round(HEIGHT) / 2);
            } else if (tempTimerGameOver + 3 < timeAlive) {
                switchScene = true;
            }
        }

        // Dessine le crosshair
        context.drawImage(crosshair, mouseX - 25, mouseY - 25);
    }

    /**
     * Update la précision de l'utilisateur ("accuracy")
     */
    public void updateAccuracy() {
        if (nbBulletShot != 0 && bullets.size() == 0) {
            accuracy = Double.parseDouble(accuracyFormat.format(((double) nbFishCatched / nbBulletShot) * 100));
        }
    }

    /**
     * Tirer une seule balle par appel de fonction en cliquant avec la souris
     * @param mouseX Position de la souris en x
     * @param mouseY Position de la souris en y
     */
    public void shoot(double mouseX, double mouseY) {
        Bullet b = new Bullet(mouseX, mouseY);
        bullets.add(b);
        nbBulletShot += 1;
    }

    /*
     * Les prochaines méthodes sont pour gérer toute la logique pour les highScores
     */

    /**
     * Permet d'obtenir la liste de HighScores depuis le fichier HighScores.txt sous forme d'arrayList de Strings
     */
    public ArrayList<String> getHighScores() {

        ArrayList<String> tempScores = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader("HighScores.txt");
            BufferedReader br = new BufferedReader(fileReader);

            String line;

            while((line = br.readLine()) != null) {
                tempScores.add(line);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tempScores;
    }

    /**
     * Permet de déterminer s'il y a un nouveau HighScore à ajouter.
     * Retourne l'index où insérer le nouveau highScore, si le score n'est pas un nouveau highScore, retourne -1.
     *
     * @param scores ArrayList des HighScores jusqu'à date
     * @param finalScore Score final sauvegardé au moment où le game over apparait
     * @param finalAccuracy Précision finale sauvegardé au moment où le game over apparait
     * @return L'index où insérer le nouveau highScore et si le score n'est pas un nouveau highScore, retourne -1.
     */
    public int newHighScore(ArrayList<String> scores, int finalScore, double finalAccuracy) {

        if (scores.size() == 0) {
            return 0;

        } else if (scores.size() == 10) {

            for (int i = 0; i < scores.size(); i++) {

                String line = scores.get(i);
                // Le substring suivant permet d'enlever la dernière position du score dans le fichier et l'arrayList
                String[] stringArray = line.split(" ");

                int highScore = Integer.parseInt(stringArray[stringArray.length - 3]);
                double accuracyScore = Double.parseDouble(stringArray[stringArray.length - 1]);

                if (finalScore > highScore) {
                    return i;
                } else if (finalScore == highScore && finalAccuracy > accuracyScore) {
                    return i;
                }
            }
            return -1;

        } else {

            for (int i = 0; i < scores.size(); i++) {

                String line = scores.get(i);
                String[] stringArray = line.split(" ");

                int highScore = Integer.parseInt(stringArray[stringArray.length - 3]);
                double accuracyScore = Double.parseDouble(stringArray[stringArray.length - 1]);

                if (finalScore > highScore) {
                    return i;
                } else if (finalScore == highScore && finalAccuracy > accuracyScore) {
                    return i;
                }
            }
            return scores.size();
        }
    }

    /**
     * Insère un nouveau HighScore à l'index dans l'arrayList scores, Réécrit le fichier HighScores.txt au complet
     * avec le nouveau tableau
     *
     * @param scores ArrayList des HighScores jusqu'à date
     * @param index index calculé avec la méthode newHighScore
     * @param writtenName Nom choisi par l'utilisateur avant d'appuyer sur le bouton ajouter
     * @param score Score final sauvegardé au moment où le game over apparait
     * @param accuracy Précision finale sauvegardé au moment où le game over apparait
     */
    public void addHighScore(ArrayList<String> scores, int index, String writtenName, int score, double accuracy) {

        // La position dans le scoreboard est 1 de plus que l'index dans le tableau
        int positionNewScore = index + 1;
        scores.add(index, "#" + positionNewScore + " - " + writtenName + " - " + score + " - " + accuracy);

        try {
            FileWriter scoreWriter = new FileWriter("HighScores.txt");

            String newLine = System.getProperty("line.separator");

            for (int i = 0; i < scores.size(); i++) {
                String scoreInfos = scores.get(i);
                // Le substring suivant permet d'enlever la dernière position du score dans le fichier et l'arrayList
                String[] stringArray = scoreInfos.split(" ", 3);
                String scoreWithoutPastPosition = stringArray[stringArray.length - 1];
                int position = i + 1;
                if (i < 10) {
                    scoreWriter.write("#" + position + " - " + scoreWithoutPastPosition + newLine);
                }
            }
            scoreWriter.close();
        } catch (IOException ex) {
            System.out.println("A file writing error occured");
            ex.printStackTrace();
        }

    }

    /*
     * Les prochaines fonctions permettent de gérer les inputs de déboggage par l'utilisateur face au jeu.
     */

    public void debugLevelUp() {
        debuglevelUp = true;
    }

    public void debugScoreUp() {
        score += 1;
        scoreCounterTo5 +=1;
    }

    public void debugLifeUp() {
        if (lives < 3) {
            lives += 1;
        }
    }

    public void debugLifeDown() {
        if (lives > 0) {
            lives -= 1;
        }
    }

    /*
     * Getters utilisés
     */

    public boolean isSwitchScene() {
        return switchScene;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public double getFinalAccuracy() {
        return finalAccuracy;
    }
}
