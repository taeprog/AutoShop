package sample.controls;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.model.Part;
import sample.model.PartsModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    private final ArrayList<CartPart> sums = new ArrayList<>();
    private final PartsModel partsModel = new PartsModel();


    public class CartPart extends Part{

        protected Double sum;

        public CartPart(Part part){
            this.setCount(part.getCount());
            this.setPrice(part.getPrice());
            this.setName(part.getName());
            this.setId(part.getId());
            this.setCompat(part.getCompat());
        }

        public void setPrice(Double price){
            super.setPrice(price);
        }

        public void setCount(int count){
            super.setCount(count);
        }

        public Double getSum() {
            return this.getPrice()*this.getCount();
        }

        public void setSum(Double sum){}
    }

    @FXML
    TableView dataTable;

    @FXML
    Label sum;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillTable();
        double sumValue = 0;
        for (int i=0; i<sums.size(); i++){
            sumValue+=sums.get(i).getSum();
        }
        sum.setText(sumValue+"");
    }


    void fillTable() {
        for(int i=0; i<Controller.cart.size(); i++){
            sums.add(new CartPart(Controller.cart.get(i)));
        }


        ((TableColumn)dataTable.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<CartPart, String>("name"));
        ((TableColumn)dataTable.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<CartPart, String>("compat"));
        ((TableColumn)dataTable.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<CartPart, String>("count"));
        ((TableColumn)dataTable.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<CartPart, String>("price"));
        ((TableColumn)dataTable.getColumns().get(4)).setCellValueFactory(new PropertyValueFactory<CartPart, String>("sum"));
        //((TableColumn)dataTable.getColumns().get(5)).
        ObservableList<CartPart> list = FXCollections.observableList(sums);
        dataTable.setItems(list);
    }

    public void sell(ActionEvent actionEvent) {
        partsModel.sell(Controller.cart);
        Controller.cart.clear();
        ((Stage)dataTable.getScene().getWindow()).close();
    }

    public void cancel(ActionEvent actionEvent) {
        ((Stage)dataTable.getScene().getWindow()).close();
    }
}
