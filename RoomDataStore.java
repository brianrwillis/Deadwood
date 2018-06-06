import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
public class RoomDataStore{
   private  SimpleStringProperty name;
   private  SimpleStringProperty shots;
   
   public RoomDataStore(){
      this.name=new SimpleStringProperty("");
      this.shots= new SimpleStringProperty("0");
      }
   public String getName(){
      return name.get();
         }
    public void setName(String n){
      name.set(n);
      }
    
    public String getShots(){
         return shots.get();    
         }
    public void setShots(String s){
         shots.set(s);
}
}
//add to top of Display
   private static TableView currPlayerTable;
//add right after the initialization of stats.
                                       shotsTable=new TableView<RoomDataStore>();
                                       ObservableList<RoomDataStore> shotsData = FXCollections.observableArrayList();
                                       TableColumn RoomNameCol=new TableColumn("Room Name:");
                                       RoomNameCol.setCellValueFactory(new PropertyValueFactory<RoomDataStore, String>("name"));
                                       TableColumn RoomShotsCol=new TableColumn("Takes:");
                                       RoomShotsCol.setCellValueFactory(new PropertyValueFactory<RoomDataStore, String>("shots"));  
                                       for(int i=0; i<12; i++){
                                          RoomDataStore currDatStore=board.getRoom(i).getStore();
                                          shotsData.addAll(currDatStore);
                                          }
                                       shotsTable.getColumns().addAll(RoomNameCol, RoomShotsCol);
                                       shotsTable.setItems(shotsData);     
                                       shotsTable.setLayoutX(1625);
                                       shotsTable.setLayoutY(0);      
                                       shotsTable.setVisible(true);
                                       root.getChildren().add(shotsTable); 
                                       
                                       //add to endDay();
                                       shotsTable.refresh();