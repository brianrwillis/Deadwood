import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.input.*;
import java.lang.*;
import javafx.event.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
public class CardTableMaker{//Creates and returns tables of playerspots given the board and room.
   public CardTableMaker(){//constructor
   }
   
   public TableView getOnCard(Board board, int index){//returns table of spots on the card
      if (index==10||index==11){//checks if room is casting or trailers.
         return null;}
      Room room=board.getRoom(index);
      SceneCard card=room.getCard();
      ArrayList<PlayerSpot> rList=card.getRanksOnCard();
      ObservableList<PSpotHolder> data1 = FXCollections.observableArrayList();
      for(int i=0; i<rList.size();i++){
         PlayerSpot curr=rList.get(i);
         curr.getPSpot().setNumber(Integer.toString(i));
         data1.addAll(curr.getPSpot());
      }
      TableView table=new TableView<PSpotHolder>();
      table.setEditable(true);      
      TableColumn big=new TableColumn("On Card");
      big.setMinWidth(300);
      TableColumn PNum=new TableColumn("#");
      PNum.setCellValueFactory(new PropertyValueFactory<PSpotHolder, String>("number"));//gets spot number
      PNum.setMaxWidth(20);
      TableColumn PNameCol=new TableColumn("Name");
      PNameCol.setCellValueFactory(new PropertyValueFactory<PSpotHolder, String>("partName"));//gets spot name
      PNameCol.setMinWidth(150);
      PNameCol.setMaxWidth(150);
      TableColumn PRankCol=new TableColumn("Req Rank");
      PRankCol.setCellValueFactory(new PropertyValueFactory<PSpotHolder, String>("neededRank"));//gets spot rank
      PRankCol.setMinWidth(130);
      PRankCol.setMaxWidth(130);      
      table.setItems(data1);      
      big.getColumns().addAll(PNum, PNameCol, PRankCol);
      table.setLayoutX(0);
      table.setLayoutY(400);        
      table.setMaxHeight(200); 
      table.getColumns().addAll(big);
      table.setMaxWidth(300);
      table.setMinWidth(300);
      return table;
   }
      
      
   public TableView getOffCard(Board board, int index){//returns table of spots off card. 
      if (index==10||index==11){//checks if room is casting or trailers.
         return null;}
      Room room=board.getRoom(index);
      SceneCard card=room.getCard();
      ArrayList<PlayerSpot> rList=room.getRanksOffCard();
      ObservableList<PSpotHolder> data1 = FXCollections.observableArrayList();
      for(int i=0; i<rList.size();i++){
         PlayerSpot curr=rList.get(i);
         curr.getPSpot().setNumber(Integer.toString(i));
         data1.addAll(curr.getPSpot());
      }
      TableView table=new TableView<PSpotHolder>();
      table.setEditable(true);
      TableColumn big=new TableColumn("Off Card");
      big.setMinWidth(300);
      TableColumn PNum=new TableColumn("#");
      PNum.setMaxWidth(20);
      PNum.setCellValueFactory(new PropertyValueFactory<PSpotHolder, String>("number"));//gets spot number
      TableColumn PNameCol=new TableColumn("Name");
      PNameCol.setCellValueFactory(new PropertyValueFactory<PSpotHolder, String>("PartName"));//gets spot name
      PNameCol.setMinWidth(150);
      PNameCol.setMaxWidth(150);
      TableColumn PRankCol=new TableColumn("Req Rank");
      PRankCol.setCellValueFactory(new PropertyValueFactory<PSpotHolder, String>("NeededRank"));//gets spot rank
      PRankCol.setMinWidth(130);
      PRankCol.setMaxWidth(130);
      table.setItems(data1);
      big.getColumns().addAll(PNum, PNameCol, PRankCol);
      table.getColumns().addAll(big);
      table.setMaxWidth(300);
      table.setMinWidth(300);
      table.setLayoutX(0);
      table.setLayoutY(600);
      table.setMaxHeight(200);      
      return table;
   }      
}