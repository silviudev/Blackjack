import javafx.scene.image.Image;

public class Card{
   private Image img;
   private int value;
   private String suit;
   
   public Card(String imageURL, int value, String suit){
      this.img = new Image(imageURL);
      this.value = value;
      this.suit = suit;
   }
   
   public Image getImage(){
      return img;
   }
   
   public int getValue(){
      return value;
   }
   
   public String getSuit(){
      return suit;
   }

}