import java.util.*;

public class Player{
      private ArrayList<Card> hand; 
      private int handValue;
      private int bet;
      private int money;
      
      public Player(){
         hand = new ArrayList<Card>();
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
      
      public ArrayList<Card> getHand(){
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
         handValue = 0;
      }
      
      private void incrementHandValue(int value){
         handValue += value;
      }

}