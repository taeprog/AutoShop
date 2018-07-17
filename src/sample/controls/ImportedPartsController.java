package sample.controls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.controlsfx.control.MaskerPane;
import javafx.application.Platform;
import sample.model.Part;
import sample.model.PartsModel;
import sample.model.ThreadRunner;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ImportedPartsController implements Initializable {

    private static ArrayList<Part> imported = new ArrayList<>();

    @FXML
    TableView dataTable;

    @FXML
    MaskerPane mask;

    @FXML
    Label bypricesum, pricesum;

    @FXML
    Button importButton, cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillDataTable();
        countSums();
    }

    void fillDataTable() {

        ((TableColumn)dataTable.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        ((TableColumn)dataTable.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<Part, String>("compat"));
        ((TableColumn)dataTable.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<Part, String>("count"));
        ((TableColumn)dataTable.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<Part, String>("byprice"));
        ((TableColumn)dataTable.getColumns().get(4)).setCellValueFactory(new PropertyValueFactory<Part, String>("price"));

        ObservableList<Part> list = FXCollections.observableList(imported);
        dataTable.setItems(list);
    }

    void countSums() {
        Double bysum = 0.0;
        Double sum = 0.0;
        for(int i=0; i<imported.size(); i++){
            try {
                bysum += imported.get(i).getByprice() * imported.get(i).getCount();
                sum += imported.get(i).getPrice() * imported.get(i).getCount();
            }catch (Exception e){
//                e.printStackTrace();
            }
        }
        bypricesum.setText(bysum+"");
        pricesum.setText(sum+"");
    }

    public static void setImportedData(ArrayList<Part> data) {
        imported = data;
    }

    public void saveParts(ActionEvent actionEvent) {
        PartsModel model = new PartsModel();
        mask.setVisible(true);
        ThreadRunner runner = new ThreadRunner() {
            @Override
            public void doWork() {
                model.addParts(imported);
            }
        };
        runner.setFinishedLitener(new ThreadRunner.ThreadRunnerFinishedListener(){
            @Override
            public void onStop() {cancelImport(new ActionEvent());}
        });
        Thread thread = new Thread(runner);
        thread.start();

    }

    static void close(ImportedPartsController controller){
        controller.cancelImport(new ActionEvent());
    }

    public void cancelImport(ActionEvent actionEvent) {
        imported.clear();
        ((Stage)dataTable.getScene().getWindow()).close();
    }
}
