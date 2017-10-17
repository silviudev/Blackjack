import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.Cursor;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Blackjack extends Application{

   @Override
   public void start(Stage mainStage){
      BooleanValue hit = new BooleanValue(false);
      BooleanValue dealersTurn = new BooleanValue(false);
      BooleanValue gameStarted = new BooleanValue(false);
      BooleanValue gameOver = new BooleanValue(false);
      BooleanValue betFinished = new BooleanValue(false);
      BooleanValue initialCardsDealt = new BooleanValue(false);
      
      Connection conn = SQLConnection.DbConnector();
      checkConnection(conn);
   
      IntValue xOffset = new IntValue(96);
      IntValue previousX = new IntValue(-81);
      
      Player player = new Player();
      Player dealer = new Player();
      
      Alert zeroBetWarning = new Alert(AlertType.WARNING, "Bet cannot be zero! Click the chips to bet.");
      zeroBetWarning.setHeaderText(null);
      
      Alert zeroMoneyWarning = new Alert(AlertType.WARNING, "Out of Money! Try again another day");
      zeroMoneyWarning.setHeaderText(null);
      
      Alert savedScoreAlert = new Alert(AlertType.INFORMATION, "Your score was saved!");
      zeroMoneyWarning.setHeaderText(null);
      
      player.setMoney(1000);
      
      IntValue oldMoney = new IntValue(player.getMoney());
   
      mainStage.setTitle("Blackjack by Silviu Popovici");
      mainStage.sizeToScene();
      mainStage.setResizable(false);
      mainStage.getIcons().add(new Image(Blackjack.class.getResourceAsStream( "assets/images/ace_icon.png" ))); 
      
      Group root = new Group();
      Group menu = new Group();
      Group instructionsGroup = new Group();
      Group saveScoreGroup = new Group();
      
      Circle redChip = new Circle(327, 356, 44, Color.TRANSPARENT);
      Circle blueChip = new Circle(433, 356, 44, Color.TRANSPARENT);
      Circle greenChip = new Circle(535, 356, 44, Color.TRANSPARENT);
      Circle blackChip = new Circle(641, 355, 44, Color.TRANSPARENT);
      Circle yellowChip = new Circle(745, 354, 44, Color.TRANSPARENT);
      
      Button startGameButton = new Button("Start Game");
      Button instructionsButton = new Button("View Instructions");
      Button viewScoresButton = new Button("View High Score List");
      Button exitButton = new Button("Exit Game");
      
      Button backToMenuButton = new Button("Back to Menu");
      Button insertScoreButton = new Button("Save my Score!");
      
      
      Button hitButton = new Button("Hit Me!");
      Button clearButton = new Button("Play Again");
      Button stayButton = new Button("Stay");
      Button saveScoreButton = new Button("Save High Score And Quit");
      Button finishBetButton = new Button("Start Game");
      Button resetBetButton = new Button("Reset Bet");
      Button exitToMenuFromGameButton = new Button("Exit To Menu");
      
      TextField nameBox = new TextField();
      
      Text playerMoneyScore = new Text();
      playerMoneyScore.setX(265);
      playerMoneyScore.setY(210);
      playerMoneyScore.setFill(Color.YELLOW);
      playerMoneyScore.setFont(Font.font(null, FontWeight.BOLD, 26));
      
      Text betInstructionsDisplay = new Text();
      betInstructionsDisplay.setX(330);
      betInstructionsDisplay.setY(280);
      betInstructionsDisplay.setFill(Color.YELLOW);
      betInstructionsDisplay.setFont(Font.font(null, FontWeight.BOLD, 26));
      
      Text playerHandValueDisplay = new Text();
      playerHandValueDisplay.setX(20.0f);
      playerHandValueDisplay.setY(30.0f);
      playerHandValueDisplay.setFill(Color.YELLOW);
      playerHandValueDisplay.setFont(Font.font(null, FontWeight.BOLD, 26));
      
      Text dealerHandValueDisplay = new Text();
      dealerHandValueDisplay.setX(20.0f);
      dealerHandValueDisplay.setY(60.0f);
      dealerHandValueDisplay.setFill(Color.YELLOW);
      dealerHandValueDisplay.setFont(Font.font(null, FontWeight.BOLD, 26));
      
      Text moneyDisplay = new Text();
      moneyDisplay.setX(330.0f);
      moneyDisplay.setY(30.0f);
      moneyDisplay.setFill(Color.YELLOW);
      moneyDisplay.setFont(Font.font(null, FontWeight.BOLD, 26));
      
      Text betDisplay = new Text();
      betDisplay.setX(330.0f);
      betDisplay.setY(60.0f);
      betDisplay.setFill(Color.YELLOW);
      betDisplay.setFont(Font.font(null, FontWeight.BOLD, 26));
      
      Text gameOverDisplay = new Text();
      gameOverDisplay.setX(20.0f);
      gameOverDisplay.setY(110.0f);
      gameOverDisplay.setFill(Color.YELLOW);
      gameOverDisplay.setFont(Font.font(null, FontWeight.BOLD, 30));
      
      Pane pane = new Pane();
      Pane menuPane = new Pane();
      Pane instructionsPane = new Pane();
      Pane saveScorePane = new Pane();
      
      pane.getChildren().add(hitButton);
      pane.getChildren().add(clearButton);
      pane.getChildren().add(stayButton);
      pane.getChildren().add(saveScoreButton);
      pane.getChildren().add(finishBetButton);
      pane.getChildren().add(resetBetButton);
      pane.getChildren().add(exitToMenuFromGameButton);
      pane.getChildren().add(playerHandValueDisplay);
      pane.getChildren().add(dealerHandValueDisplay);
      pane.getChildren().add(moneyDisplay);
      pane.getChildren().add(betDisplay);
      pane.getChildren().add(gameOverDisplay);
      pane.getChildren().add(betInstructionsDisplay);
      pane.getChildren().add(redChip);
      pane.getChildren().add(blueChip);
      pane.getChildren().add(greenChip);
      pane.getChildren().add(blackChip);
      pane.getChildren().add(yellowChip);
      
      menuPane.getChildren().add(startGameButton);
      menuPane.getChildren().add(instructionsButton);
      menuPane.getChildren().add(viewScoresButton);
      menuPane.getChildren().add(exitButton);
      
      instructionsPane.getChildren().add(backToMenuButton);
      
      saveScorePane.getChildren().add(insertScoreButton);
      saveScorePane.getChildren().add(nameBox);
      saveScorePane.getChildren().add(playerMoneyScore);
      
      startGameButton.setTranslateX(288);
      startGameButton.setTranslateY(395);
      startGameButton.setStyle("-fx-font-size: 18px; -fx-border-color: red; -fx-cursor: hand");
      startGameButton.setMinWidth(200);
      
      instructionsButton.setTranslateX(288);
      instructionsButton.setTranslateY(445);
      instructionsButton.setStyle("-fx-font-size: 18px; -fx-border-color: red; -fx-cursor: hand");
      instructionsButton.setMinWidth(200);
      
      viewScoresButton.setTranslateX(288);
      viewScoresButton.setTranslateY(495);
      viewScoresButton.setStyle("-fx-font-size: 18px; -fx-border-color: red; -fx-cursor: hand");
      viewScoresButton.setMinWidth(200);
      
      exitButton.setTranslateX(288);
      exitButton.setTranslateY(545);
      exitButton.setStyle("-fx-font-size: 18px; -fx-border-color: red; -fx-cursor: hand");
      exitButton.setMinWidth(200);
      
      backToMenuButton.setTranslateX(288);
      backToMenuButton.setTranslateY(550);
      backToMenuButton.setStyle("-fx-font-size: 18px; -fx-border-color: red; -fx-cursor: hand");
      backToMenuButton.setMinWidth(200);
      
      exitToMenuFromGameButton.setTranslateX(675);
      exitToMenuFromGameButton.setTranslateY(555);
      exitToMenuFromGameButton.setStyle("-fx-font-size: 16px; -fx-border-color: red; -fx-cursor: hand");
      exitToMenuFromGameButton.setMinWidth(100);
    
      hitButton.setTranslateX(10);
      hitButton.setTranslateY(325);
      hitButton.setStyle("-fx-font-size: 27px; -fx-cursor: hand");
      
      
      stayButton.setTranslateX(140);
      stayButton.setTranslateY(325);
      stayButton.setStyle("-fx-font-size: 27px; -fx-cursor: hand");
      
      clearButton.setTranslateX(285);
      clearButton.setTranslateY(90);
      clearButton.setStyle("-fx-font-size: 24px; -fx-cursor: hand");
      clearButton.setVisible(false);
      
      saveScoreButton.setTranslateX(440);
      saveScoreButton.setTranslateY(90);
      saveScoreButton.setStyle("-fx-font-size: 14px; -fx-cursor: hand");
      saveScoreButton.setVisible(false);
      saveScoreButton.setMinHeight(52);
      
      finishBetButton.setTranslateX(380);
      finishBetButton.setTranslateY(420);
      finishBetButton.setStyle("-fx-font-size: 24px; -fx-cursor: hand");
      
      resetBetButton.setTranslateX(560);
      resetBetButton.setTranslateY(420);
      resetBetButton.setStyle("-fx-font-size: 24px; -fx-cursor: hand");
      
      insertScoreButton.setTranslateX(280);
      insertScoreButton.setTranslateY(300);
      insertScoreButton.setStyle("-fx-font-size: 24px; -fx-cursor: hand");
      
      nameBox.setTranslateX(230);
      nameBox.setTranslateY(230);
      nameBox.setStyle("-fx-font-size: 24px");
      
      Scene gameScene = new Scene(root);
      Scene menuScene = new Scene(menu);
      Scene instructionsScene = new Scene(instructionsGroup);
      Scene saveScoreScene = new Scene(saveScoreGroup);
      
      Canvas canvas = new Canvas(800,600);
      Canvas menuCanvas = new Canvas(800,600);
      Canvas instructionsCanvas = new Canvas(800,600);
      Canvas saveScoreCanvas = new Canvas(800,600);
      
      Deck theDeck = new Deck(); 
      theDeck.shuffle();     
        
      Image background = new Image("assets/images/background.png");
      Image deckImage = new Image("assets/images/deck.png");
      Image cardBack = new Image("assets/images/cardBack.png");
      Image menuBackground = new Image("assets/images/menuBackground.png");
      Image instructionsBackground = new Image("assets/images/instructions.png");
      Image nameBackground = new Image("assets/images/nameBackground.png");
      
      Timeline gameLoop = new Timeline();
      
      mainStage.setScene(menuScene);
      
      root.getChildren().add(canvas);
      root.getChildren().add(pane);
      menu.getChildren().add(menuCanvas);
      menu.getChildren().add(menuPane);
      instructionsGroup.getChildren().add(instructionsCanvas);
      instructionsGroup.getChildren().add(instructionsPane);
      saveScoreGroup.getChildren().add(saveScoreCanvas);
      saveScoreGroup.getChildren().add(saveScorePane);
      
      
      GraphicsContext gc = canvas.getGraphicsContext2D();  
      GraphicsContext mgc = menuCanvas.getGraphicsContext2D(); 
      GraphicsContext igc = instructionsCanvas.getGraphicsContext2D();
      GraphicsContext sgc = saveScoreCanvas.getGraphicsContext2D();
      
      gameLoop.setCycleCount(Timeline.INDEFINITE);
           
      redChip.setOnMouseClicked(e->{
         if(!betFinished.value && 5 <= player.getMoney()){
            player.increaseBet(5);
            player.incrementMoney(-5);
         }
      });
      
      blueChip.setOnMouseClicked(e->{
         if(!betFinished.value && 10 <= player.getMoney()){
            player.increaseBet(10);
            player.incrementMoney(-10);
         }
      });
      
      greenChip.setOnMouseClicked(e->{
         if(!betFinished.value && 25 <= player.getMoney()){
            player.increaseBet(25);
            player.incrementMoney(-25);
         }
      });
      
      blackChip.setOnMouseClicked(e->{
         if(!betFinished.value && 100 <= player.getMoney()){
            player.increaseBet(100);
            player.incrementMoney(-100);
         }
      });
      
      yellowChip.setOnMouseClicked(e->{
         if(!betFinished.value && 500 <= player.getMoney()){
            player.increaseBet(500);
            player.incrementMoney(-500);
         }
      });
      
      startGameButton.setOnAction(e->{
         mainStage.setScene(gameScene);
         resetGame(gc, previousX, player, dealer, gameOver, dealersTurn, betFinished);
      });
      
      instructionsButton.setOnAction(e->mainStage.setScene(instructionsScene));
      
      exitButton.setOnAction(e->{
         Platform.exit();
      });
      
      backToMenuButton.setOnAction(e-> mainStage.setScene(menuScene));
      
      exitToMenuFromGameButton.setOnAction(e->{
         resetGame(gc, previousX, player, dealer, gameOver, dealersTurn, betFinished);
         oldMoney.value = 1000;
         player.setMoney(1000);
         player.setBet(0);
         mainStage.setScene(menuScene);
      });
      
      hitButton.setOnAction(e->{
         if(player.getHand().size() < 8) {
            dealCard(player, theDeck); 
         }
      });
      
      clearButton.setOnAction(e->{
         if(player.getMoney() == 0){
            zeroMoneyWarning.showAndWait();
            resetGame(gc, previousX, player, dealer, gameOver, dealersTurn, betFinished);
            oldMoney.value = 1000;
            player.setMoney(1000);
            mainStage.setScene(menuScene);
            player.setBet(0);
         }else{
            resetGame(gc, previousX, player, dealer, gameOver, dealersTurn, betFinished);
            drawBackground(gc,background,deckImage);
         }
      });
      
      stayButton.setOnAction(e->{
         dealersTurn.value = true;
         runDealersTurn(dealer,theDeck);
      });
      
      resetBetButton.setOnAction(e->{
         player.setMoney(oldMoney.value);
         player.setBet(0);
      });
      
      finishBetButton.setOnAction(e->{
         if(player.getBet() > 0){
            betFinished.value = true;
            initialCardsDealt.value = false;
         }else{
            zeroBetWarning.showAndWait();
         }
      });
      
      insertScoreButton.setOnAction(e->{
         try{
            if(!nameBox.getText().trim().equals("")){
               String query = "INSERT INTO scores (name,score) VALUES(?,?)";
               PreparedStatement pst = conn.prepareStatement(query);
               pst.setString(1, nameBox.getText());
               pst.setInt(2, player.getMoney());
               pst.execute();
               pst.close();
               resetGame(gc, previousX, player, dealer, gameOver, dealersTurn, betFinished);
               oldMoney.value = 1000;
               player.setMoney(1000);
               player.setBet(0);
               savedScoreAlert.showAndWait();
               mainStage.setScene(menuScene);
            }
         }catch(Exception e1){
            System.out.println(e1);
         }
      });
      
      saveScoreButton.setOnAction(e->mainStage.setScene(saveScoreScene));
   
      drawBackground(gc,background,deckImage);
      
      KeyFrame frame = new KeyFrame(
            Duration.seconds(0.017),   // 1000/60 for 60 FPS
            e ->{  
                   if(!betFinished.value){
                     betInstructionsDisplay.setText("-Click on the Chips to increase bet-");
                     resetBetButton.setVisible(true);
                     finishBetButton.setVisible(true);
                   }else if(betFinished.value && !initialCardsDealt.value){
                     betInstructionsDisplay.setText("");
                     dealInitial(player,dealer,theDeck);
                     initialCardsDealt.value = true;
                     resetBetButton.setVisible(false);
                     finishBetButton.setVisible(false);
                   }   
                     
                   String check;
                   check = checkGameOver(player,dealer,dealersTurn);
                   if(check != ""){
                     gameOver.value = true;
                     if(check == "bust"){
                        gameOverDisplay.setText("YOU BUSTED!");
                        player.setBet(0);
                     }else if(check == "win"){
                        gameOverDisplay.setText("YOU WIN!");
                        player.incrementMoney(player.getBet()*2);
                        player.setBet(0);
                     }else if(check == "blackjack"){
                        gameOverDisplay.setText("BLACKJACK!!");
                        player.incrementMoney(player.getBet()*2);
                        player.setBet(0);
                     }else if(check =="dealerBust"){
                        gameOverDisplay.setText("DEALER BUSTED!");
                        player.incrementMoney(player.getBet()*2);
                        player.setBet(0);
                     }else if(check=="lose"){
                        gameOverDisplay.setText("DEALER WINS!");
                        player.setBet(0);
                     }else{
                       gameOverDisplay.setText("IT'S A PUSH!");
                       player.incrementMoney(player.getBet());
                       player.setBet(0);
                     }
                   }else{
                     gameOverDisplay.setText("");
                   }
                   
                   if(gameOver.value){
                     clearButton.setVisible(true);
                     saveScoreButton.setVisible(true);
                     oldMoney.value = player.getMoney();
                   }else{
                     clearButton.setVisible(false);
                     saveScoreButton.setVisible(false);
                   }

                   
                   if(gameOver.value || dealersTurn.value || !betFinished.value){
                     hitButton.setDisable(true);
                     stayButton.setDisable(true);
                   }else if(!gameOver.value && !dealersTurn.value && betFinished.value){
                     hitButton.setDisable(false);
                     stayButton.setDisable(false);
                   }
                                   
                   drawBackground(gc,background,deckImage);
                   Card currentCard;
                    
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
                  if(!dealersTurn.value && dealerHand.size() > 0){
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
                   
                   if(!dealersTurn.value && dealerHand.size() > 0){
                     dealerHandValueDisplay.setText("Dealer Showing: " + dealer.getHand().get(0).getValue());
                   }else{
                     dealerHandValueDisplay.setText("Dealer Showing: " + dealer.getHandValue());
                   }

                   moneyDisplay.setText("Money: $" + player.getMoney());
                   betDisplay.setText("Current Bet: $" + player.getBet());
                   
                   /*Draw menu with the menu graphics context (mgc)
                     (if statement to check scene not necessary since menu scene
                     has seperate graphics context object)
                   */
                   mgc.drawImage(menuBackground,0,0);
                   
                   /*Draw instructions with instructions graphics context (igc)*/
                   igc.drawImage(instructionsBackground, 0 ,0);
                   
                   /*Draw save name page with save score graphics context (sgc)*/
                   sgc.drawImage(nameBackground, 0 ,0);
                   playerMoneyScore.setText("Your Score: $" + player.getMoney());
                   
               });
               gameLoop.getKeyFrames().add(frame);
               gameLoop.play();
               mainStage.show();
            }
   
   public void drawBackground(GraphicsContext gc, Image bg, Image deck){
      gc.drawImage(bg, 0,0);
      gc.drawImage(deck, 660, 10);
   }
   
   public void resetGame(GraphicsContext gc, IntValue prev, Player player, Player dealer, BooleanValue gameOver, 
                         BooleanValue dealersTurn, BooleanValue betFinished){
      gc.clearRect(0, 404, 800,600);
      prev.value = -85;
      player.resetHand();
      dealer.resetHand();
      gameOver.value = false;
      dealersTurn.value = false;
      betFinished.value = false;
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
   
   public String checkGameOver(Player player, Player dealer, BooleanValue dealersTurn){
      if(player.getHandValue() == 21 && player.getHand().size() == 2){
         return "blackjack";
      }else if(player.getHandValue() == 21){
         return "win";
      }else if(player.getHandValue() > 21){
         return "bust";
      }else if(dealer.getHandValue() > 21){
         return "dealerBust";
      }else if(player.getHandValue() > dealer.getHandValue() && dealersTurn.value){
         return "win";
      }else if(player.getHandValue() == dealer.getHandValue() && dealersTurn.value){
         return "push";
      }else if(player.getHandValue() < dealer.getHandValue() && dealersTurn.value){
         return "lose";
      }
      return "";
   }
   
   public void runDealersTurn(Player dealer, Deck deck){
      while(dealer.getHandValue() < 18){
         dealer.addToHand(deck.drawCard());
      }
   }
   
   public void checkConnection(Connection conn){
      if(conn == null){
         System.out.println("Connection failed");
      }else{
         System.out.println("Connection successful!");
      }
   }
   
   public static void main(String[] args){
      launch(args);
   }

}