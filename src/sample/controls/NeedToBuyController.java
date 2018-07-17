package sample.controls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.model.Part;
import sample.model.PartsModel;

import java.net.URL;
import java.util.ResourceBundle;

public class NeedToBuyController implements Initializable {

    private final PartsModel partsModel = new PartsModel();

    @FXML
    TableView dataTable;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillDataTable();
    }

    void fillDataTable() {

        ((TableColumn)dataTable.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        ((TableColumn)dataTable.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<Part, String>("compat"));
        ((TableColumn)dataTable.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<Part, String>("count"));
        ObservableList<Part> list = FXCollections.observableList(partsModel.getNeedToBuy());
        dataTable.setItems(list);
    }
}
