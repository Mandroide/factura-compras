package view.suplidor;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
import view.producto.ProductoActivoController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SuplidorActivoController implements Initializable {

    private static Stage primaryStage;

    public static void start(Stage stage) throws IOException {
        primaryStage = stage;
        Parent root = FXMLLoader.load(SuplidorActivoController.class.getResource("SuplidorActivo.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Seleccione suplidor");

    }

    @FXML
    private TreeView<String> treeView;

    @FXML
    private TableView<Suplidor> tableView;
    private ObservableList<Suplidor> suplidores = FXCollections.observableArrayList();
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

    private void initTabla() {
        columnaNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        columnaDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        columnaPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        columnaCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        columnaCodigoPostal.setCellValueFactory(new PropertyValueFactory<>("codigoPostal"));
    }

    @FXML
    private JFXTextField nombre;
    @FXML
    private JFXButton botonSiguiente;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTabla();
        tableView.setItems(business.Suplidor.mostrarActivos());
        tableView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
                {
                    suplidor = newValue;
                    botonSiguiente.setDisable(suplidor == null);
                }
        );
        treeView.setRoot(Main.iniciarItems());
        treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
                Main.cambiarScene(primaryStage, newValue));

    }

    @FXML
    private void completarNombre() {
        tableView.setItems(business.Suplidor.buscarActivos(nombre.getText()));
    }

    private Suplidor suplidor;

    @FXML
    private void continuar() throws IOException {
        if (suplidor == null)
            return;
        ProductoActivoController.start(primaryStage, suplidor);
    }

}
