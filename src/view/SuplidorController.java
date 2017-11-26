package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import business.Suplidor;


public class SuplidorController implements Initializable{

    @FXML
    private TextField empresa;
    @FXML
    private TextField email;
    @FXML
    private TextField telefono;
    @FXML
    private TextField direccion;
    @FXML
    private TextField ciudad;
    @FXML
    private TextField codigoPostal;

    @FXML
    private ComboBox<String> paises;

    @FXML
    private ToggleGroup estatus = new ToggleGroup();

    @FXML
    private Button agrega;
    @FXML
    private Button elimina;

    @FXML
    private RadioButton activo = new RadioButton("Activo");
    @FXML
    private RadioButton inactivo = new RadioButton("Inactivo");
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        activo.setDisable(false);
        ObservableList<String> data = FXCollections.observableArrayList();
        for (String countrylist : Locale.getISOCountries()) {
            Locale pais = new Locale("", countrylist);
            data.add(pais.getDisplayCountry());
        }
        paises.setItems(data);

    }

    public void eliminar(ActionEvent event){

    }

    private char getEstatus(){
        return 'A';
    }

    public void agregar(ActionEvent event){
        Suplidor.insertar(empresa.getText(), direccion.getText(), ciudad.getText(), email.getText(),
                telefono.getText(),codigoPostal.getText(), paises.getValue(), getEstatus());
    }
}
