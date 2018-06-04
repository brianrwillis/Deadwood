import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
public class PSpotHolder{//Class that holds properties of Player Spot to ease importing into TableView.
   private final SimpleStringProperty neededRank;
   private final SimpleStringProperty partName;
   private final SimpleStringProperty number;
   
   public PSpotHolder(){//contructor
      this.neededRank=new SimpleStringProperty("1");
      this.partName=new SimpleStringProperty("");
      this.number=new SimpleStringProperty("0");
   }
   public String getNeededRank(){//rank getter
      return neededRank.get();
   }
   public String getPartName(){//name getter
      return partName.get();
   }
   public void setNeededRank(String i){//rank setter
      neededRank.set(i);
   }
   public void setPartName(String i){//name setter
      partName.set(i);
   }
   public String getNumber(){//number getter
      return number.get();
   }
   public void setNumber(String i){//number setter
      number.set(i);
   }   
}