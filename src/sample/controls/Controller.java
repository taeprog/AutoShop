package sample.controls;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.MaskerPane;
import sample.model.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private final PartsModel partsModel = new PartsModel();
    private final CarsModel carsModel = new CarsModel();
    private ArrayList<Part> parts;
    public static ArrayList<Part> cart = new ArrayList<>();
    private double sumValue;


    @FXML
    Button goToCart;
    @FXML
    TextField searchField;
    @FXML
    Label sum;

    @FXML
    ComboBox<String> vendorList;

    @FXML
    MaskerPane mask;

    @FXML
    TableView dataTable;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> search(searchField.getText(), vendorList.getSelectionModel().getSelectedItem()));
        vendorList.getEditor().textProperty().addListener((observable, oldValue, newValue) -> search(searchField.getText(), newValue));
        updateTable();
    }


    void updateTable(){
        fillDataTable(partsModel.getList());
        fillVendors(carsModel.getVendors());
    }


    void fillDataTable(ArrayList<Part> parts) {
        dataTable.setRowFactory(tv->{
            TableRow<Part> row = new TableRow<>();
            row.setOnMouseClicked(event->{
                if(event.getClickCount() == 2 && !row.isEmpty())
                {
                    addToCart(row.getIndex());
                }
            });
            return row;
        });
        ((TableColumn)dataTable.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        ((TableColumn)dataTable.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<Part, String>("compat"));
        ((TableColumn)dataTable.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<Part, String>("price"));
        ((TableColumn)dataTable.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<Part, String>("count"));
        //((TableColumn)dataTable.getColumns().get(5)).
        this.parts = parts;
        ObservableList<Part> list = FXCollections.observableList(parts);
        dataTable.setItems(list);
    }

    public void addToCart(int i) {
        showAddToCart(parts.get(i));
        sum.setText("В корзине "+cart.size()+" позиций");
    }


    public void goToCart(ActionEvent actionEvent) {
        try {
            Parent panel = FXMLLoader.load(getClass().getResource("/sample/view/cart.fxml"));
            Stage cartStage = new Stage();
            cartStage.setTitle("Корзина");
            cartStage.initModality(Modality.WINDOW_MODAL);
            cartStage.initOwner(dataTable.getScene().getWindow());
            cartStage.setScene(new Scene(panel, 700, 500));
            cartStage.showAndWait();
            updateTable();
            sum.setText("В корзине "+cart.size()+" позиций");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addParts(ActionEvent actionEvent) {
        try {
            Parent panel = FXMLLoader.load(getClass().getResource("/sample/view/addParts.fxml"));
            Stage cartStage = new Stage();
            cartStage.setTitle("Добавить запчасти");
            cartStage.initModality(Modality.WINDOW_MODAL);
            cartStage.initOwner(dataTable.getScene().getWindow());
            cartStage.setScene(new Scene(panel, 700, 500));
            cartStage.showAndWait();
            updateTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fillVendors(ArrayList<Vendor> vendors){
        ArrayList<String> vends = new ArrayList<>();
        vends.add("");
        for(int i=0; i<vendors.size(); i++){
            vends.add(vendors.get(i).getName());
        }
        ObservableList<String> list = FXCollections.observableList(vends);
        vendorList.setItems(list);
    }


    public void showAddToCart(Part part)
    {
        AddToCartController.part = part;
        try {
            Parent panel = FXMLLoader.load(getClass().getResource("/sample/view/addToCart.fxml"));
            Stage cartStage = new Stage();
            cartStage.setTitle("Добавить в корзину");
            cartStage.initModality(Modality.WINDOW_MODAL);
            cartStage.initOwner(dataTable.getScene().getWindow());
            cartStage.setScene(new Scene(panel, 600, 270));
            cartStage.showAndWait();
            sum.setText("В корзине "+cart.size()+" позиций");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showImportedParts(ArrayList<Part> parts) {
        ImportedPartsController.setImportedData(parts);
        try {
            Parent panel = FXMLLoader.load(getClass().getResource("/sample/view/importedParts.fxml"));
            Stage cartStage = new Stage();
            cartStage.setTitle("Импорт");
            cartStage.initModality(Modality.WINDOW_MODAL);
            cartStage.initOwner(dataTable.getScene().getWindow());
            cartStage.setScene(new Scene(panel, 700, 500));
            cartStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importFromExcel(ActionEvent actionEvent) {
        FileChooser choser = new FileChooser();
        choser.setTitle("Выберите файл xlsx");
        choser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel документы", "*.xlsx"));
        choser.setInitialDirectory(new File(System.getProperty("user.home")));
        String path = choser.showOpenDialog(dataTable.getScene().getWindow()).getPath();
        ArrayList<Part> importedParts = ExcelWorker.parse(path);
        showImportedParts(importedParts);
        updateTable();
    }

    public void search(String name, String vendor){
        fillDataTable(partsModel.search(name, vendor, ""));
    }

    public void vendorSelected(ActionEvent actionEvent) {
        search(searchField.getText(), vendorList.getSelectionModel().getSelectedItem());
    }

    public void exportToExcel(ActionEvent actionEvent) {
        FileChooser choser = new FileChooser();
        choser.setTitle("Экспортировать в Excel");
        choser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel документы", "*.xlsx"));
        choser.setInitialDirectory(new File(System.getProperty("user.home")));
        String path = choser.showSaveDialog(dataTable.getScene().getWindow()).getPath();
        mask.setVisible(true);
        ThreadRunner runner = new ThreadRunner() {
            @Override
            public void doWork() {
                ExcelWorker.exportParts(partsModel.getList(), path, "Прайс лист");
            }
        };
        runner.setFinishedLitener(new ThreadRunner.ThreadRunnerFinishedListener() {
            @Override
            public void onStop() {
                mask.setVisible(false);
            }
        });
        new Thread(runner).start();
    }

    public void needToBuy(ActionEvent actionEvent) {

        try {
            Parent panel = FXMLLoader.load(getClass().getResource("/sample/view/needtobuy.fxml"));
            Stage cartStage = new Stage();
            cartStage.setTitle("Заявка");
            cartStage.initModality(Modality.WINDOW_MODAL);
            cartStage.initOwner(dataTable.getScene().getWindow());
            cartStage.setScene(new Scene(panel, 700, 500));
            cartStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sold(ActionEvent actionEvent) {
        try {
            Parent panel = FXMLLoader.load(getClass().getResource("/sample/view/sold.fxml"));
            Stage cartStage = new Stage();
            cartStage.setTitle("Отчёт");
            cartStage.initModality(Modality.WINDOW_MODAL);
            cartStage.initOwner(dataTable.getScene().getWindow());
            cartStage.setScene(new Scene(panel, 700, 500));
            cartStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
