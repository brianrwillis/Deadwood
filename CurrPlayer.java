import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
public class CurrPlayer{
   private final SimpleStringProperty currPlayer;
   public CurrPlayer(){
   
      this.currPlayer=new SimpleStringProperty("");
   }
   public void setCurrPlayer(String I){
      currPlayer.set(I);
      }
   public String getCurrPlayer(){
      return currPlayer.get();

}
}