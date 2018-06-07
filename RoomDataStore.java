import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
public class RoomDataStore{//purpose of class is to store a string(name of room) and a string(shots remaining) to ease in making tables.
    private  SimpleStringProperty name;
    private  SimpleStringProperty shots;
//all class is is getters and setters
    public RoomDataStore(){//empty initialization
        this.name=new SimpleStringProperty("");
        this.shots= new SimpleStringProperty("0");
    }
    public String getName(){//name getter
        return name.get();
    }
    public void setName(String n){//name setter
        name.set(n);
    }

    public String getShots(){//shots getter
        return shots.get();
    }
    public void setShots(String s){//shots setter
        shots.set(s);
    }
}