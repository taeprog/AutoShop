package sample.controls;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import sample.model.Part;

import java.net.URL;
import java.util.ResourceBundle;

public class AddToCartController implements Initializable {


    public static Part part = new Part();

    @FXML
    Label name, compat, price, sum;

    @FXML
    Spinner<Integer> countSpinner;

    @FXML
    Button toCart, cancel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCounter();
        name.setText(part.getName());
        compat.setText(part.getCompat());
        price.setText(part.getPrice()+"");
        sum.setText(part.getPrice()+"");
    }

    void setCounter() {
        SpinnerValueFactory<Integer> factory =new SpinnerValueFactory<Integer>() {
            @Override
            public void decrement(int steps) {
                if(this.getValue()>1)
                {
                    this.setValue(this.getValue()-1);
                }
            }

            @Override
            public void increment(int steps) {
                if(this.getValue()<AddToCartController.part.getCount())
                {
                    this.setValue(this.getValue()+1);
                }
            }
        };
        factory.setValue(1);
        countSpinner.setValueFactory(factory);
        countSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                sum.setText((part.getPrice()*newValue)+"");
            }
        });
    }

    public void addToCart(ActionEvent actionEvent) {
        Part partToCart = AddToCartController.part;
        partToCart.setCount(countSpinner.getValue());
        Controller.cart.add(partToCart);
        ((Stage)name.getScene().getWindow()).close();
    }

    public void cancel(ActionEvent actionEvent) {
        ((Stage)name.getScene().getWindow()).close();
    }
}
