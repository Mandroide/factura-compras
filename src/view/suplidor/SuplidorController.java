package view.suplidor;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import data.Estatus;
import data.Suplidor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import view.Main;
import view.RadioButtonCell;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Locale;
import java.util.ResourceBundle;

public class SuplidorController implements Initializable {

    private static Stage primaryStage;

    public static void start(Stage stage) throws IOException {
        primaryStage = stage;
        Parent root = FXMLLoader.load(SuplidorController.class.getResource("Suplidor.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ver Suplidores");
    }

    @FXML
    private JFXTextField nombre;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXTextField telefono;
    @FXML
    private JFXTextField direccion;
    @FXML
    private JFXTextField ciudad;
    @FXML
    private JFXTextField codigoPostal;

    @FXML
    private JFXComboBox<String> paises;

    @FXML
    private TableView<data.Suplidor> tableView;
    @FXML
    private TableColumn<data.Suplidor, Integer> columnaNo;
    @FXML
    private TableColumn<data.Suplidor, String> columnaNombre;
    @FXML
    private TableColumn<data.Suplidor, String> columnaEmail;
    @FXML
    private TableColumn<data.Suplidor, String> columnaTelefono;
    @FXML
    private TableColumn<data.Suplidor, String> columnaDireccion;
    @FXML
    private TableColumn<data.Suplidor, String> columnaPais;
    @FXML
    private TableColumn<data.Suplidor, String> columnaCiudad;
    @FXML
    private TableColumn<data.Suplidor, String> columnaCodigoPostal;
    @FXML
    private TableColumn<data.Suplidor, Estatus> columnaEstatus;

    @FXML
    private TreeView<String> treeView;


    private void initTabla() {
        columnaNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        columnaDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        columnaPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        columnaCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        columnaCodigoPostal.setCellValueFactory(new PropertyValueFactory<>("codigoPostal"));
        columnaEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));

        columnaNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaTelefono.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaDireccion.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaPais.setCellFactory(ChoiceBoxTableCell.forTableColumn(data));
        columnaCiudad.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaCodigoPostal.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaEstatus.setCellFactory((param) -> new RadioButtonCell<>(EnumSet.allOf(Estatus.class)));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        llenarPaises();
        initTabla();
        tableView.setItems(business.Suplidor.mostrar());
        tableView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
                suplidor = newValue
        );
        treeView.setRoot(Main.iniciarItems());
        treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
                Main.cambiarScene(primaryStage, newValue));
    }

    private ObservableList<String> data = FXCollections.observableArrayList();
    private void llenarPaises() {
        data.add("");
        for (var countrylist : Locale.getISOCountries()) {
            var pais = new Locale("", countrylist);
            data.add(pais.getDisplayCountry());
        }
        Collections.sort(data);
        paises.setItems(data);
    }

    private void clear() {
        nombre.setText("");
        email.setText("");
        telefono.setText("");
        direccion.setText("");
        codigoPostal.setText("");
        ciudad.setText("");
        paises.getSelectionModel().select(null);
    }

    @FXML
    private void buscar() {
        tableView.setItems(business.Suplidor.buscar(nombre.getText()));
    }

    private boolean haConfirmado() {
        String mensaje = ("Nombre: " + nombre.getText() + "\n") +
                "Email: " + email.getText() + "\n" +
                "Telefono: " + telefono.getText() + "\n" +
                "Direccion: " + direccion.getText() + "\n" +
                "Pais: " + paises.getSelectionModel().getSelectedItem() + "\n" +
                "Ciudad: " + ciudad.getText() + "\n" +
                "Codigo Postal: " + codigoPostal.getText() + "\n";

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("\"¿Desea continuar?\"");
        alerta.setHeaderText(mensaje);
        return alerta.showAndWait().isPresent();
    }


    @FXML
    private void agregar() {
        if (haConfirmado()) {
            String context = business.Suplidor.insertar(
                    nombre.getText(), direccion.getText(), ciudad.getText(), email.getText(), telefono.getText(),
                    codigoPostal.getText(), paises.getValue());
            Alert insercion = new Alert(Alert.AlertType.INFORMATION, context);
            insercion.show();
            tableView.setItems(business.Suplidor.mostrar());
            clear();
        }

    }

    private Suplidor suplidor = new Suplidor();

    @FXML
    private void eliminar() {
        String mensaje = "No.: " + suplidor.getId() + "\n"
                + "Nombre: " + suplidor.getNombre() + "\n"
                + "Email: " + suplidor.getEmail() + "\n"
                + "Telefono: " + suplidor.getTelefono() + "\n"
                + "Direccion: " + suplidor.getDireccion() + "\n"
                + "Codigo Postal: " + suplidor.getCodigoPostal() + "\n"
                + "Ciudad: " + suplidor.getCiudad() + "\n"
                + "Pais: " + suplidor.getPais() + "\n";
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("¿Desea eliminarlo?");
        alerta.setHeaderText(mensaje);
        if (alerta.showAndWait().isPresent()) {
            String context = business.Suplidor.eliminar(suplidor.getId());
            Alert insercion = new Alert(Alert.AlertType.INFORMATION, context);
            insercion.show();
            tableView.setItems(business.Suplidor.mostrar());
        }
    }

    @FXML
    private void actualizar(TableColumn.CellEditEvent newValue) {
        var suplidor = (Suplidor) newValue.getTableView().getItems().get(
                newValue.getTablePosition().getRow()
        );
        if (suplidor == null)
            return;
        if (newValue.getNewValue().equals(newValue.getOldValue()))
            return;

        TableColumn col = newValue.getTableColumn();
        String value = newValue.getNewValue().toString();

        if (col.equals(columnaNombre)){
            suplidor.setNombre(value);
        } else if(col.equals(columnaEmail)){
            if (value.toLowerCase().equals(newValue.getOldValue())) {
                suplidor.setEmail(newValue.getOldValue().toString());
                initTabla();
                return;
            } else{
                suplidor.setEmail(value.toLowerCase());
            }
        } else if (col.equals(columnaTelefono)){
            suplidor.setTelefono(value);
        } else if (col.equals(columnaDireccion)){
            suplidor.setDireccion(value);
        } else if (col.equals(columnaCodigoPostal)){
            suplidor.setCodigoPostal(value);
        } else if (col.equals(columnaCiudad)){
            suplidor.setCiudad(value);
        } else if (col.equals(columnaPais)){
            suplidor.setPais(value);
        } else {
            suplidor.setEstatus(Estatus.valueOf(newValue.getNewValue().toString().toUpperCase()));
        }

        String context = business.Suplidor.actualizar(
                suplidor.getId(), suplidor.getNombre(), suplidor.getDireccion(), suplidor.getCiudad(), suplidor.getEmail(),
                suplidor.getTelefono(), suplidor.getCodigoPostal(), suplidor.getPais(), suplidor.getEstatus()
        );
        Alert insercion = new Alert(Alert.AlertType.INFORMATION, context);
        insercion.show();
        tableView.setItems(business.Suplidor.mostrar());

    }
}
