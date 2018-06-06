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