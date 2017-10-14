import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import java.util.ArrayList;

public class Blackjack extends Application{

   @Override
   public void start(Stage mainStage){
      BooleanValue dealCard = new BooleanValue(false);
      BooleanValue dealersTurn = new BooleanValue(false);
      
      IntValue xOffset = new IntValue(96);
      IntValue previousX = new IntValue(-81);
      IntValue turn = new IntValue(0); 
      
      Player player = new Player();
      Player dealer = new Player();
      
      player.setMoney(1000);
      player.setBet(50);
   
      mainStage.setTitle("Blackjack by Silviu Popovici");
      mainStage.sizeToScene();
      mainStage.setResizable(false);
      
      Group root = new Group();
      VBox menu = new VBox();
      Button startGameButton = new Button("Go to Game");
      Button hitButton = new Button("Hit Me!");
      Button clearButton = new Button("Clear Player Cards");
      
      Pane pane = new Pane();
      pane.getChildren().add(hitButton);
      pane.getChildren().add(clearButton);
      
      hitButton.setTranslateX(10);
      hitButton.setTranslateY(350);
      
      clearButton.setTranslateX(70);
      clearButton.setTranslateY(350);
      
      Scene gameScene = new Scene(root);
      Scene menuScene = new Scene(menu);
      Canvas canvas = new Canvas(800,600);
      
      Deck theDeck = new Deck(); 
      theDeck.shuffle();     
        
      Image background = new Image("assets/images/background.png");
      Image deckImage = new Image("assets/images/deck.png");
      
      Timeline gameLoop = new Timeline();
      
      mainStage.setScene(gameScene);
      root.getChildren().add(canvas);
      root.getChildren().add(pane);
      menu.getChildren().add(startGameButton);
      GraphicsContext gc = canvas.getGraphicsContext2D();  
      gameLoop.setCycleCount(Timeline.INDEFINITE);
      
      final long timeStart = System.currentTimeMillis();
      
      startGameButton.setOnAction(e->mainStage.setScene(gameScene));
      hitButton.setOnAction(e->{
         if(player.getHand().size() < 8) dealCard.value = true;
      });
      clearButton.setOnAction(e->{
         clearCards(gc, previousX, player);
         drawBackground(gc,background,deckImage);
      });
      
      drawBackground(gc,background,deckImage);
      
      KeyFrame frame = new KeyFrame(
            Duration.seconds(0.017),   // 1000/60 for 60 FPS
            e ->{ 
                  //double t = (System.currentTimeMillis() - timeStart) / 1000.0; 
                  Card currentCard;
                  //Draw a card for the player   
                  if(dealCard.value && player.getHand().size() < 8){
                     currentCard = theDeck.drawCard();
                     gc.drawImage(currentCard.getImage(), previousX.value + xOffset.value, 425);
                     previousX.value+=xOffset.value;
                     dealCard.value = false;
                     player.addToHand(currentCard);
                  }
               });
               gameLoop.getKeyFrames().add(frame);
               gameLoop.play();
               mainStage.show();
            }
   
   public void drawBackground(GraphicsContext gc, Image bg, Image deck){
      gc.drawImage(bg, 0,0);
      gc.drawImage(deck, 660, 10);
   }
   
   public void clearCards(GraphicsContext gc, IntValue prev, Player player){
      gc.clearRect(0, 404, 800,600);
      prev.value = -85;
      player.resetHand();
   }
   
   public static void main(String[] args){
      launch(args);
   }

}