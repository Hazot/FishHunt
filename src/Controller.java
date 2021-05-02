
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Controller {

    private static Game game;

    /**
     * Constructeur du jeu, débute une nouvelle partie
     */
    public Controller() {
        game = new Game();
    }

    /**
     * Méthodes déjà expliquées
     */
    public void draw(GraphicsContext context, double mouseX, double mouseY) { game.draw(context, mouseX, mouseY); }

    /**
     * Mise à jour du temps entre la vue et le jeu
     *
     * @param deltaTime temps écoulé depuis le dernier "update"
     */
    public void update(double deltaTime) { game.update(deltaTime); }

    /**
     * Méthode pour créer une nouvelle partie
     */
    public static void restart() { game = new Game(); }

    /**
     * Méthode qui retourne à la vue si l'utilisateur a fait un Game Over
     *
     * @return Retoune vrai si on est en game over depuis 3 secondes, faux sinon
     */
    public boolean changeScene() {
        return game.isSwitchScene();
    }

    /**
     * Retourne le score final au moment du game over
     *
     * @return Retourne un int de score final
     */
    public int getFinalScore() {
        return game.getFinalScore();
    }

    /**
     * Méthode qui fait le lien entre la vue et le modèle sur la détermination d'un nouveau score
     *
     * @param scores ArrayList des HighScores jusqu'à date
     * @param finalScore Score final sauvegardé au moment où le game over apparait
     * @return L'index calculé par la méthode dans le modèle
     */
    public int newHighScore(ArrayList<String> scores, int finalScore) {
        return game.newHighScore(scores, finalScore);
    }

    /**
     * Méthode qui fait le lien entre la vue et le modèle sur l'ajout d'un novueau score à l'arrayList scores
     *
     * @param scores ArrayList des HighScores jusqu'à date
     * @param index index calculé avec la méthode newHighScore
     * @param writtenName Nom choisi par l'utilisateur avant d'appuyer sur le bouton ajouter
     * @param finalScore Score final sauvegardé au moment où le game over apparait
     */
    public void addHighScore(ArrayList<String> scores, int index, String writtenName, int finalScore) {
        game.addHighScore(scores, index, writtenName, finalScore);
    }

    /**
     * Méthode qui fait le lien entre la vue et le modèle pour obtenir une ArrayList de scores à partir d'un fichier
     * @return Retourne l'arrayList de scores lu du fichier
     */
    public ArrayList<String> getHighScores() {
        return game.getHighScores();
    }


    // Controls
    public void shoot(double mouseX, double mouseY) {
        game.shoot(mouseX, mouseY);
    }

    public void debugLevelUp() {
        game.debugLevelUp();
    }

    public void debugScoreUp() {
        game.debugScoreUp();
    }

    public void debugLifeUp() {
        game.debugLifeUp();
    }

    public void debugLifeDown() {
        game.debugLifeDown();
    }

}
