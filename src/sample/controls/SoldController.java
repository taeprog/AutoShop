package sample.controls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import sample.Main;
import sample.model.Check;
import sample.model.Part;
import sample.model.PartsModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SoldController implements Initializable {
    private PartsModel partsModel = new PartsModel();
    private ArrayList<Check> shortList = new ArrayList<>();


    @FXML
    HBox passPane;

    @FXML
    PasswordField password;


    @FXML
    TableView shortTable, fullTable;

    @FXML
    Label sum,wrongPass;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillShortTable();
        if(shortList.size() != 0){
            fillFullTable(0);
        }
    }

    public void fillShortTable()
    {
        shortTable.setRowFactory(tv->{
            TableRow<Check> row = new TableRow<>();
            row.setOnMouseClicked(event->{
                if(event.getClickCount() == 2 && !row.isEmpty())
                {
                    fillFullTable(row.getIndex());
                }
            });
            return row;
        });
        shortList = partsModel.getCheckList();
        ((TableColumn)shortTable.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<Part, String>("dateTime"));
        ((TableColumn)shortTable.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<Part, String>("sum"));
        ObservableList<Check> list = FXCollections.observableList(shortList);
        shortTable.setItems(list);
    }

    public void fillFullTable(int i){
        ((TableColumn)fullTable.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        ((TableColumn)fullTable.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<Part, String>("count"));
        ((TableColumn)fullTable.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<Part, String>("price"));
        ObservableList<Part> list = FXCollections.observableList(partsModel.getFullCheck(shortList.get(i).getDateTime()));
        fullTable.setItems(list);
        sum.setText(shortList.get(i).getSum()+"");
    }

    public void checkPass(ActionEvent actionEvent) {
        if(Main.checkPassword(password.getText()))
        {
            passPane.setVisible(false);
        }
        else{
            wrongPass.setVisible(true);
        }
    }
}
