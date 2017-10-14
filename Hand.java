import java.util.ArrayList;

public class Hand{
   private ArrayList cards = new ArrayList();
   
   public void addCard(Card card){
      cards.add(card);
   }
   
   public ArrayList getCards();{
      return cards;
   }
}