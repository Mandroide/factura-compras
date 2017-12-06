package view.suplidor;

import data.Suplidor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import view.Main;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
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
    private ChoiceBox<String> paises;

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

    @FXML
    private ToggleGroup estatus = new ToggleGroup();

    @FXML
    private Button botonActualizar;
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
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTabla();
        tableView.setItems(business.Suplidor.mostrar());
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    if (newValue == null)
                        return;
                    suplidor = newValue;
                    botonActualizar.setDisable(false);
                }
        );
        llenarPaises();
        treeView.setRoot(Main.iniciarItems());
        treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
                Main.cambiarScene(primaryStage, newValue));
    }

    private void llenarPaises() {
        ObservableList<String> data = FXCollections.observableArrayList();
        for (String countrylist : Locale.getISOCountries()) {
            Locale pais = new Locale("", countrylist);
            data.add(pais.getDisplayCountry());
        }
        data.add("");
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
        botonActualizar.setDisable(true);
        tableView.setItems(business.Suplidor.buscar(nombre.getText()));
    }

    private Optional<ButtonType> confirmar() {
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
        return alerta.showAndWait();
    }


    @FXML
    private void agregar() {
        Optional<ButtonType> resultado = confirmar();
        if (resultado.isPresent()) {
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
    private void actualizar() {
        String mensaje = "No.: " + suplidor.getId() + "\n"
                + "Nombre: " + nombre.getText() + "\n"
                + "Email: " + email.getText() + "\n"
                + "Telefono: " + telefono.getText() + "\n"
                + "Direccion: " + direccion.getText() + "\n"
                + "Codigo Postal: " + codigoPostal.getText() + "\n"
                + "Ciudad: " + ciudad.getText() + "\n"
                + "Pais: " + paises.getValue() + "\n";
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("¿Desea continuar?");
        alerta.setHeaderText(mensaje);
        if (alerta.showAndWait().isPresent()) {
            String context = business.Suplidor.actualizar(suplidor.getId(), nombre.getText(), direccion.getText(),
                    ciudad.getText(), email.getText(), telefono.getText(), codigoPostal.getText(), paises.getValue(), "A");
            Alert insercion = new Alert(Alert.AlertType.INFORMATION, context);
            insercion.show();
            tableView.setItems(business.Suplidor.mostrar());
        }

    }
}
