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

public class Blackjack extends Application{

   @Override
   public void start(Stage mainStage){
      BooleanValue dealCard = new BooleanValue(false);
      IntValue xOffset = new IntValue(93);
      IntValue previousX = new IntValue(-85);
      IntValue playerHandSize = new IntValue(0);
   
      mainStage.setTitle("Blackjack by Silviu Popovici");
      mainStage.sizeToScene();
      mainStage.setResizable(false);
      
      Group root = new Group();
      VBox menu = new VBox();
      Button button1 = new Button("Go to Game");
      Button button2 = new Button("Hit Me!");
      Button button3 = new Button("Clear Player Cards");
      
      Pane pane = new Pane();
      pane.getChildren().add(button2);
      pane.getChildren().add(button3);
      
      button2.setTranslateX(10);
      button2.setTranslateY(350);
      
      button3.setTranslateX(60);
      button3.setTranslateY(350);
      
      Scene gameScene = new Scene(root);
      Scene menuScene = new Scene(menu);
      Canvas canvas = new Canvas(800,600);
      
      Card card1 = new Card("assets/images/2C.png", 2, "Clubs");
      Image background = new Image("assets/images/background.png");
      Image deck = new Image("assets/images/deck.png");
      
      Timeline gameLoop = new Timeline();
      
      mainStage.setScene(gameScene);
      root.getChildren().add(canvas);
      root.getChildren().add(pane);
      menu.getChildren().add(button1);
      GraphicsContext gc = canvas.getGraphicsContext2D();  
      gameLoop.setCycleCount(Timeline.INDEFINITE);
      
      final long timeStart = System.currentTimeMillis();
      
      button1.setOnAction(e->mainStage.setScene(gameScene));
      button2.setOnAction(e->{
         if(playerHandSize.value < 8)dealCard.value = true;
      });
      button3.setOnAction(e->{
         clearCards(gc, previousX, playerHandSize);
         drawBackground(gc,background,deck);
      });
      
      drawBackground(gc,background,deck);
      
      KeyFrame frame = new KeyFrame(
            Duration.seconds(0.017),   // 1000/60 for 60 FPS
            e ->{ 
                  //double t = (System.currentTimeMillis() - timeStart) / 1000.0; 
                    
                  if(dealCard.value && playerHandSize.value < 8){
                     gc.drawImage(card1.getImage(), previousX.value+xOffset.value, 405);
                     previousX.value+=xOffset.value;
                     dealCard.value = false;
                     playerHandSize.value++;
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
   
   public void clearCards(GraphicsContext gc, IntValue prev, IntValue playerHand){
      gc.clearRect(0, 404, 800,600);
      prev.value = -85;
      playerHand.value = 0;
   }
   
   public static void main(String[] args){
      launch(args);
   }

}