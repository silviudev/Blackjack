import java.util.*;

public class Player{
      private ArrayList hand; 
      private int handValue;
      private int bet;
      private int money;
      
      public Player(){
         hand = new ArrayList();
      }
      
      public int getHandValue(){
         return handValue;
      }
      
      public int getBet(){
         return bet;
      }
      
      public int getMoney(){
         return money;
      }
      
      public ArrayList getHand(){
         return hand;
      }
      
      public void setBet(int value){
         bet = value;
      }
      
      public void setMoney(int value){
         money = value;
      }
      
      public void incrementMoney(int value){
         money += value;
      }
      
      public void addToHand(Card card){
         hand.add(card);
         incrementHandValue(card.getValue());
      }
      
      public void resetHand(){
         hand = new ArrayList();
      }
      
      private void incrementHandValue(int value){
         handValue += value;
      }

}