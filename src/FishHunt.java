
// Alexis Chevrier et Kevin Lessard

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class FishHunt extends Application {

    // Taille de l'application (résolution)
    private static final int WIDTH = 640, HEIGHT = 480;

    // Position de la souris
    private double mouseX, mouseY;

    // Initialisation de la fenêtre et du controlleur pour ensuite faire le choix de la scène
    private Stage primaryStage;
    private Controller controller;

    // variables du tableau de scores
    private Text title = new Text("Meilleurs scores");
    private ListView<String> list = new ListView<>();
    private ArrayList<String> scores = new ArrayList<>();
    private int finalScore;
    private double finalAccuracy;

    /**
     * fonction main
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Démarre les processus de l'application
     *
     * @param primaryStage fenêtre de l'application
     * @throws Exception Exception si possible
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        controller = new Controller();

        primaryStage.setScene(createSceneMenu());

        // Paramètres de la fenêtre de l'application
        primaryStage.setTitle("LOCC Hunt");
        primaryStage.getIcons().add(new Image("/Images/logo.png"));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();

        this.primaryStage.show();
    }

    /**
     * Scène du menu
     *
     * @return Scène affichant le menu et ces boutons
     */
    private Scene createSceneMenu() {

        Pane root = new Pane();
        VBox vertPane = new VBox();
        Scene sceneMenu = new Scene(root, WIDTH, HEIGHT);

        // Paramètre d'affichage
        vertPane.setPadding(new Insets(20));

        // Affichage du logo (Splash Art Cover) du jeu
        Image image = new Image("/Images/logo.png");
        ImageView logo = new ImageView(image);
        logo.setFitWidth(380);
        logo.setPreserveRatio(true);

        // Background
        BackgroundFill fillFondBleu = new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY);
        Background fondBleu = new Background(fillFondBleu);

        // Boutons
        Button bGame = new Button("Nouvelle partie!");
        Button bScores = new Button("Meilleurs scores");
        bGame.setFont(Font.font("Verdana", 14));
        bScores.setFont(Font.font("Verdana", 14));

        // Actions des boutons
        bGame.setOnAction((e) -> {
            Controller.restart();
            primaryStage.setScene(createSceneGame());
        });

        bScores.setOnAction((e) -> {
            primaryStage.setScene(createSceneHighScores());
        });

        // Allignement et marges
        vertPane.setAlignment(Pos.CENTER);
        vertPane.setSpacing(10);
        VBox.setMargin(logo, new Insets(40, 0, 0, 100));
        VBox.setMargin(bGame, new Insets(30, 0, 0, 100));
        VBox.setMargin(bScores, new Insets(0, 0, 0, 100));

        // Arbre d'affichage graphique
        vertPane.getChildren().addAll(logo, bGame, bScores);
        root.setBackground(fondBleu);
        root.getChildren().add(vertPane);

        // Fermer l'application avec ESCAPE depuis le menu
        sceneMenu.setOnKeyPressed((value) -> {
            if (value.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        });

        return sceneMenu;
    }

    /**
     * Scène qui permet d'afficher les HighScores sans pouvoir les modifier
     *
     * @return Scène affichant les HighScores non modifiable
     */
    private Scene createSceneHighScores() {

        Pane root = new Pane();
        VBox vertPane = new VBox();
        Scene sceneScore = new Scene(root, WIDTH, HEIGHT, Color.DARKBLUE);

        // Paramètre d'affichage
        vertPane.setAlignment(Pos.CENTER);
        vertPane.setSpacing(10);
        vertPane.setPadding(new Insets(20));

        // Boutons de retour au menu
        Button bMenu = new Button("Menu");
        bMenu.setFont(Font.font("Verdana", 14));
        bMenu.setOnAction((e) -> {
            primaryStage.setScene(createSceneMenu());
            scores = controller.getHighScores();
        });

        // Affichage de la liste + titre
        title.setFont(Font.font("Verdana", 30));
        scores = controller.getHighScores();
        list.getItems().setAll(scores);
        list.setMinSize(600, 340);
        list.setMaxSize(600, 300);

        // Arbre d'affichage graphique
        vertPane.getChildren().addAll(title, list, bMenu);
        root.getChildren().add(vertPane);

        // Fermer l'application avec ESCAPE depuis les meilleurs scores
        sceneScore.setOnKeyPressed((value) -> {
            if (value.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        });

        return sceneScore;
    }


    /**
     * Scène pour ajouter des HighScores à la suite d'un game over
     *
     * @return Scène affichant les HighScores permettant d'être modifier
     */
    private Scene createSceneNewHighScore() {

        Pane root = new Pane();
        VBox vertPane = new VBox();
        HBox newScore = new HBox();
        Scene sceneScore = new Scene(root, WIDTH, HEIGHT);

        // Paramètres d'affichage
        vertPane.setAlignment(Pos.CENTER);
        vertPane.setSpacing(10);
        vertPane.setPadding(new Insets(20));
        newScore.setSpacing(10);
        VBox.setMargin(newScore, new Insets(0, 0, 0, 100));

        // Éléments à afficher
        TextField name = new TextField();
        Text votreNom = new Text("Votre nom :");
        finalScore = controller.getFinalScore();
        Text vosPoints = new Text("a fait " + finalScore + " points!");
        title.setFont(Font.font("Verdana", 30));

        // Affichage de la liste
        scores = controller.getHighScores();
        list.getItems().setAll(scores);
        list.setMinSize(600, 300);
        list.setMaxSize(600, 300);

        // Bouton de retour au menu
        Button bMenu = new Button("Menu");
        bMenu.setOnAction((e) -> primaryStage.setScene(createSceneMenu()));

        // Bouton pour ajouter le nouveau HighScore
        Button bAdd = new Button("Ajouter!");
        bAdd.setOnAction((e) -> {

            String writtenName = name.getText();

            // Ajoute le nouveau highScore au tableau avec le nom déterminer juste au-dessus
            controller.addHighScore(scores, controller.newHighScore(scores, finalScore, finalAccuracy), writtenName, finalScore, finalAccuracy);

            // Retourne au menu après avoir ajouter le nouveau HighScore
            primaryStage.setScene(createSceneMenu());

        });

        // Complétion de l'arbre d'affichage
        newScore.getChildren().addAll(votreNom, name, vosPoints, bAdd);
        vertPane.getChildren().addAll(title, list, newScore, bMenu);
        root.getChildren().add(vertPane);

        // Fermer l'application avec ESCAPE depuis les meilleurs scores modifiable
        sceneScore.setOnKeyPressed((value) -> {
            if (value.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        });

        return sceneScore;
    }


    /**
     * Scène du jeu qui gère les inputs, événements et les animations dans le temps
     *
     * @return Scène affichant le jeu en fonction du temps
     */
    private Scene createSceneGame() {
        Pane root = new Pane();
        Scene sceneJeu = new Scene(root, WIDTH, HEIGHT);
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        GraphicsContext context = canvas.getGraphicsContext2D();

        // Mouvement de la souris pour animer le crosshair
        canvas.setOnMouseMoved((e) -> {
            mouseX = e.getX();
            mouseY = e.getY();
        });
        // Permet de faire garder le crosshair sur la souris même si on tient le clique pour une meilleur fluidité
        canvas.setOnMouseDragged((e) -> {
            mouseX = e.getX();
            mouseY = e.getY();
        });

        // Clique gauche pour tirer 1 balle à la fois
        canvas.setOnMousePressed((e) -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                mouseX = e.getX();
                mouseY = e.getY();
                controller.shoot(mouseX, mouseY);
            }
        });

        // Touches de l'utilisateur (Ajout de la touche R pour restart et ESCAPE pour quitter le jeu)
        sceneJeu.setOnKeyPressed((value) -> {
            switch (value.getCode()) {
                case ESCAPE:
                    Platform.exit();
                case H:
                    controller.debugLevelUp();
                    break;
                case J:
                    controller.debugScoreUp();
                    break;
                case K:
                    controller.debugLifeUp();
                    break;
                case L:
                    controller.debugLifeDown();
                    break;
                case R:
                    Controller.restart();
            }
        });

        // Timer de l'aplication calculant les "deltaTimes"
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;
            double maxDt = 0.01;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) * 1e-9;

                while (deltaTime > maxDt) {
                    controller.update(maxDt);
                    deltaTime -= maxDt;
                }

                controller.update(deltaTime);
                controller.draw(context, mouseX, mouseY);

                // Si le joueur est en game over, après le 3 secondes, la condition devient true
                if (controller.changeScene()) {
                    // On sauvegarde le score final du jeu
                    finalScore = controller.getFinalScore();
                    finalAccuracy = controller.getFinalAccuracy();
                    // S'il n'y a pas de nouveau highScore à rajouter, on montre les highScores seulement
                    if (controller.newHighScore(scores, finalScore, finalAccuracy) != -1) {
                        primaryStage.setScene(createSceneNewHighScore());
                    } else {
                        primaryStage.setScene(createSceneHighScores());
                    }
                    // On remet le jeu à 0 et on arrête le handle de l'ancienne partie
                    Controller.restart();
                    stop();
                }

                lastTime = now;
            }

        };
        timer.start();
        sceneJeu.setCursor(Cursor.NONE);

        return sceneJeu;
    }
}
