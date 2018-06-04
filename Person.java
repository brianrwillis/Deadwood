import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
public  class Person {//Class that holds properties of Player to ease importing into TableView.

   private final SimpleStringProperty firstName;
   private final SimpleStringProperty rank;
   private final SimpleStringProperty fame;
   private final SimpleStringProperty credit;
   private final SimpleStringProperty rehearsals;
   

   public Person(String fName, String rank, String fame, String credit, String rehearsals) {//Constructor
      this.firstName = new SimpleStringProperty(fName);
      this.rank = new SimpleStringProperty(rank);
      this.fame = new SimpleStringProperty(fame);
      this.credit = new SimpleStringProperty(credit);
      this.rehearsals= new SimpleStringProperty(rehearsals);
      
   }
   public String getRehearsals(){//rehearsals getter
      return rehearsals.get();
      }
   public void setRehearsals(String i){//rehearsals setter
      rehearsals.set(i);
   }
   public String getFirstName() {//name getter
      return firstName.get();
   }

   public void setFirstName(String fName) {//name setter
      firstName.set(fName);
   }

   public String getCredit() {//credit getter
      return credit.get();
   }

   public void setCredit(String fName) {//credit setter
      credit.set(fName);
   }

   public String getFame() {//fame getter
      return fame.get();
   }

   public void setFame(String fName) {//fame setter
      fame.set(fName);
   }
   public String getRank(){//rank getter
      return rank.get();
      }
   public void setRank(String Rank){//rank setter
      rank.set(Rank);
      }
}