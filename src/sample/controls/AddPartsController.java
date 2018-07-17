package sample.controls;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.CheckTreeView;
import sample.model.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AddPartsController implements Initializable {

    private CarsModel carsModel;
    private PartsModel partsModel;
    private ArrayList<String> parts;
    private ArrayList<String> compats;
    private ArrayList<Vendor> vendors;
    private Map<String, Integer> carsMap;

    @FXML
    ComboBox<String> nameField, compat;
    @FXML
    TextField price, count;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carsModel = new CarsModel();
        partsModel = new PartsModel();
        carsMap = new HashMap<>();
        compats = new ArrayList<>();
        parts = new ArrayList<>();
        fillSearch(partsModel.getList());

        nameField.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                nameField.hide();
                fillSearch(partsModel.search(newValue, compat.getSelectionModel().getSelectedItem(), ""));
                nameField.show();
            }
        });

        compat.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                compat.hide();
                fillSearch(partsModel.search(nameField.getSelectionModel().getSelectedItem(), newValue, ""));
                compat.show();
            }
        });


    }

    public void fillSearch(ArrayList<Part> partsList){
        ArrayList<Part> tmp = partsList;
        nameField.getItems().clear();
        for(int i=0; i<tmp.size(); i++)
        {
            parts.add(tmp.get(i).getName());
            compats.add(tmp.get(i).getCompat());
        }
        ObservableList<String> list = FXCollections.observableList(parts);
        nameField.setItems(list);
        ObservableList<String> compatList = FXCollections.observableList(compats);
        compat.setItems(compatList);
    }


}
