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
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import java.util.ArrayList;

public class Blackjack extends Application{

   @Override
   public void start(Stage mainStage){
      BooleanValue hit = new BooleanValue(false);
      BooleanValue dealersTurn = new BooleanValue(false);
      BooleanValue gameStarted = new BooleanValue(false);
      BooleanValue playerStayed = new BooleanValue(false);
      
      IntValue xOffset = new IntValue(96);
      IntValue previousX = new IntValue(-81);
      
      Player player = new Player();
      Player dealer = new Player();
      
      player.setMoney(1000);
      player.setBet(50);
   
      mainStage.setTitle("Blackjack by Silviu Popovici");
      mainStage.sizeToScene();
      mainStage.setResizable(false);
      
      Group root = new Group();
      VBox menu = new VBox();
      Button startGameButton = new Button("Start Game");
      Button hitButton = new Button("Hit Me!");
      Button clearButton = new Button("Clear Cards");
      
      Text playerHandValueDisplay = new Text();
      playerHandValueDisplay.setX(20.0f);
      playerHandValueDisplay.setY(50.0f);
      playerHandValueDisplay.setFill(Color.YELLOW);
      playerHandValueDisplay.setFont(Font.font(null, FontWeight.BOLD, 26));
      
      Text dealerHandValueDisplay = new Text();
      dealerHandValueDisplay.setX(20.0f);
      dealerHandValueDisplay.setY(80.0f);
      dealerHandValueDisplay.setFill(Color.YELLOW);
      dealerHandValueDisplay.setFont(Font.font(null, FontWeight.BOLD, 26));
      
      Text moneyDisplay = new Text();
      moneyDisplay.setX(330.0f);
      moneyDisplay.setY(50.0f);
      moneyDisplay.setFill(Color.YELLOW);
      moneyDisplay.setFont(Font.font(null, FontWeight.BOLD, 26));
      
      Text betDisplay = new Text();
      betDisplay.setX(330.0f);
      betDisplay.setY(80.0f);
      betDisplay.setFill(Color.YELLOW);
      betDisplay.setFont(Font.font(null, FontWeight.BOLD, 26));
      
      Pane pane = new Pane();
      pane.getChildren().add(hitButton);
      pane.getChildren().add(clearButton);
      pane.getChildren().add(playerHandValueDisplay);
      pane.getChildren().add(dealerHandValueDisplay);
      pane.getChildren().add(moneyDisplay);
      pane.getChildren().add(betDisplay);

      
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
      Image cardBack = new Image("assets/images/cardBack.png");
      
      Timeline gameLoop = new Timeline();
      
      mainStage.setScene(menuScene);
      root.getChildren().add(canvas);
      root.getChildren().add(pane);
      menu.getChildren().add(startGameButton);
      GraphicsContext gc = canvas.getGraphicsContext2D();  
      gameLoop.setCycleCount(Timeline.INDEFINITE);
      
      final long timeStart = System.currentTimeMillis();
      
//             
//       gc.setFill(Color.RED);
//       gc.setStroke(Color.BLACK);
//       gc.setLineWidth(1);
//       
//       Font theFont = Font.font("Times New Roman", FontWeight.NORMAL, 26);
//       gc.setFont(theFont);
      
      startGameButton.setOnAction(e->mainStage.setScene(gameScene));
      hitButton.setOnAction(e->{
         if(player.getHand().size() < 8) {
            dealCard(player, theDeck);  
         }
      });
      clearButton.setOnAction(e->{
         clearCards(gc, previousX, player, dealer);
         dealInitial(player,dealer,theDeck);
         drawBackground(gc,background,deckImage);
      });
      
      drawBackground(gc,background,deckImage);
      dealInitial(player,dealer,theDeck);
      
      KeyFrame frame = new KeyFrame(
            Duration.seconds(0.017),   // 1000/60 for 60 FPS
            e ->{     
                   drawBackground(gc,background,deckImage);
                   //double t = (System.currentTimeMillis() - timeStart) / 1000.0; 
                   Card currentCard;
                   //Draw a card for the player   
                   ArrayList<Card> playerHand = player.getHand();
                   ArrayList<Card> dealerHand = dealer.getHand();
                   
                   //display player's hand
                   for(int i = 0; i < playerHand.size(); i++){
                      currentCard = playerHand.get(i);
                      gc.drawImage(currentCard.getImage(), previousX.value + xOffset.value, 425);
                      previousX.value+=xOffset.value;
                   }
                   
                   previousX.value = -81;
                   
                   //display dealer's hand
                  if(!playerStayed.value){
                        gc.drawImage(dealerHand.get(0).getImage(), previousX.value + xOffset.value, 150);
                        previousX.value+=xOffset.value;
                        gc.drawImage(cardBack, previousX.value + xOffset.value, 150); 
                  }else{
                     for(int i = 0; i < dealerHand.size(); i++){
                        currentCard = dealerHand.get(i);
                        gc.drawImage(currentCard.getImage(), previousX.value + xOffset.value, 150);
                        previousX.value+=xOffset.value;
                     }
                  } 

                   
                   previousX.value = -81;
                   
                   //Display the UI text   
                   playerHandValueDisplay.setText("Player Showing: " + player.getHandValue());
                   
                   if(!playerStayed.value){
                     dealerHandValueDisplay.setText("Dealer Showing: " + dealer.getHand().get(0).getValue());
                   }else{
                     dealerHandValueDisplay.setText("Dealer Showing: " + dealer.getHandValue());
                   }

                   moneyDisplay.setText("Money: $" + player.getMoney());
                   betDisplay.setText("Current Bet: $" + player.getBet());
               });
               gameLoop.getKeyFrames().add(frame);
               gameLoop.play();
               mainStage.show();
            }
   
   public void drawBackground(GraphicsContext gc, Image bg, Image deck){
      gc.drawImage(bg, 0,0);
      gc.drawImage(deck, 660, 10);
   }
   
   public void clearCards(GraphicsContext gc, IntValue prev, Player player, Player dealer){
      gc.clearRect(0, 404, 800,600);
      prev.value = -85;
      player.resetHand();
      dealer.resetHand();
   }
   
   public void dealInitial(Player player, Player dealer, Deck deck){
      dealCard(player, deck);
      dealCard(player, deck);
      dealCard(dealer, deck);
      dealCard(dealer, deck);
   }
   
   public void dealCard(Player player, Deck deck){
      player.addToHand(deck.drawCard());
   }
   
   public static void main(String[] args){
      launch(args);
   }

}