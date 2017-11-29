package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.*;


import business.Suplidor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


public class SuplidorController implements Initializable {

    @FXML
    private TextField nombre;
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
    private TableView<data.Suplidor> tableView;
    @FXML
    private TableColumn<?, ?> columnaNo;
    @FXML
    private TableColumn<?, ?> columnaNombre;
    @FXML
    private TableColumn<?, ?> columnaEmail;
    @FXML
    private TableColumn<?, ?> columnaTelefono;
    @FXML
    private TableColumn<?, ?> columnaDireccion;
    @FXML
    private TableColumn<?, ?> columnaPais;
    @FXML
    private TableColumn<?, ?> columnaCiudad;
    @FXML
    private TableColumn<?, ?> columnaCodigoPostal;
    @FXML
    private TableColumn<?, ?> columnaEstatus;

   // ObservableList<data.Suplidor> data = FXCollections.observableArrayList();
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


    private HashMap<String, String> mapEstatus = new HashMap<>();

    private void initTabla(){
        columnaNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        columnaDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        columnaPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        columnaCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        columnaCodigoPostal.setCellValueFactory(new PropertyValueFactory<>("codigoPostal"));
        columnaEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTabla();
        tableView.setItems(Suplidor.mostrar());
        llenarPaises();
        activo.setSelected(true);
        mapEstatus.put("Activo", "A");
        mapEstatus.put("Inactivo", "I");

    }

    private void llenarPaises() {
        ObservableList<String> data = FXCollections.observableArrayList();
        for (String countrylist : Locale.getISOCountries()) {
            Locale pais = new Locale("", countrylist);
            data.add(pais.getDisplayCountry());
        }
        Collections.sort(data);
        paises.setItems(data);
    }

    public void setEmpresa() {

    }

    public void eliminar(ActionEvent event) {

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("\"¿Desea continuar?\"");
        alerta.setHeaderText("El suplidor va a ser eliminado");
        alerta.show();

    }

    public void buscar(ActionEvent event){
        tableView.setItems(Suplidor.buscar(nombre.getText()));
    }

    private String getEstatus() {
        String estado;
        if (activo.isSelected()) {
            estado = activo.getText();
        } else if (inactivo.isSelected()) {
            estado = inactivo.getText();
        } else {
            estado = "Ninguno";
        }
        return estado;
    }

    private Optional<ButtonType> confirmar(){
        String mensaje = ("Nombre: " + nombre.getText() + "\n") +
                "Email: " + email.getText() + "\n" +
                "Telefono: " + telefono.getText() + "\n" +
                "Direccion: " + direccion.getText() + "\n" +
                "Pais: " + paises.getSelectionModel().getSelectedItem() + "\n" +
                "Ciudad: " + ciudad.getText() + "\n" +
                "Codigo Postal: " + codigoPostal.getText() + "\n" +
                "Estado: " + getEstatus() + "\n";

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("\"¿Desea continuar?\"");
        alerta.setHeaderText(mensaje);
        return alerta.showAndWait();
    }


    public void agregar(ActionEvent event) {
        Optional<ButtonType> resultado = confirmar();
        if (resultado.isPresent()){
            String context = business.Suplidor.insertar(
                    nombre.getText(), direccion.getText(), ciudad.getText(), email.getText(), telefono.getText(),
                    codigoPostal.getText(), paises.getValue());
            Alert insercion = new Alert(Alert.AlertType.INFORMATION, context);
            insercion.show();
            tableView.setItems(Suplidor.mostrar());
        }

    }
}
