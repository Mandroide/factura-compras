package view;

import business.Orden;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrdenController implements Initializable {

    @FXML
    TableView<data.Orden> tableView;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void agregar(ActionEvent event) {
        Optional<ButtonType> resultado = confirmar();
        if (resultado.isPresent()){
            String context = business.Producto.insertar("Valor", descripcion.getText(),
                    new BigDecimal(precio.getText()), Integer.parseInt(unidadesStock.getText()), codigo.getText());
            Alert insercion = new Alert(Alert.AlertType.INFORMATION, context);
            insercion.show();
            tableView.setItems(Orden.mostrar());
            System.out.println("Dentro de metodo agregar " + nombre.getText());
        }

    }
}
