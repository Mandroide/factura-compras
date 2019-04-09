package view.producto;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import data.Producto;
import data.Suplidor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import view.Main;
import view.orden.OrdenActivaController;
import view.suplidor.SuplidorActivoController;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

public class ProductoActivoController implements Initializable {
    @FXML
    private Label id_;
    @FXML
    private Label nombre_;
    @FXML
    private Label email_;

    private static Stage primaryStage;
    private static Suplidor suplidor_;

    public static void start(Stage stage, Suplidor suplidor) throws IOException {
        primaryStage = stage;
        suplidor_ = suplidor;
        Parent root = FXMLLoader.load(ProductoActivoController.class.getResource("ProductoActivo.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Seleccione productos");

    }

    @FXML
    private TableView<Producto> tableView;
    @FXML
    private TableColumn<?, ?> columnaNo;
    @FXML
    private TableColumn<?, ?> columnaNombre;
    @FXML
    private TableColumn<?, ?> columnaDescripcion;
    @FXML
    private TableColumn<?, ?> columnaPrecio;
    @FXML
    private TableColumn<?, ?> columnaUnidadesStock;
    @FXML
    private TableColumn<?, ?> columnaCodigo;

    private void initTabla() {
        columnaNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaUnidadesStock.setCellValueFactory(new PropertyValueFactory<>("unidadesStock"));
        columnaCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
    }


    @FXML
    private TreeView<String> treeView;

    private HashSet<Producto> productos = new HashSet<>();
    private HashMap<Producto, Integer> cantidades = new HashMap<>();
    private Producto producto = new Producto();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializa labels
        id_.setText(String.valueOf(suplidor_.getId()));
        nombre_.setText(suplidor_.getNombre());
        email_.setText(suplidor_.getEmail());
        //---------------------

        initTabla();
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    if (newValue == null)
                        return;
                    producto = newValue;
                    cantidad.setDisable(false);
                    botonAgregar.setDisable(false);
                }
        );
        tableView.getSelectionModel().selectedItemProperty().removeListener(
                (v, oldValue, newValue) -> {
                    cantidad.setDisable(true);
                    botonAgregar.setDisable(true);
                }
        );
        tableView.setItems(business.Producto.mostrarActivos());
        treeView.setRoot(Main.iniciarItems());
        treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
                Main.cambiarScene(primaryStage, newValue));
    }

    @FXML
    private void volver() throws IOException {
        SuplidorActivoController.start(primaryStage);
    }

    @FXML
    private void continuar() throws IOException {

        OrdenActivaController.start(primaryStage, suplidor_, productos, cantidades);
    }


    @FXML
    private void seleccionar() {
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    @FXML
    private JFXButton botonContinuar;
    @FXML
    private JFXButton botonAgregar;
    @FXML
    private JFXButton botonRemover;
    @FXML
    private JFXTextField cantidad;

    @FXML
    private void agregar() {
        productos.add(producto);
        cantidades.put(producto, Integer.parseUnsignedInt(cantidad.getText()));
        botonRemover.setDisable(false);
        botonContinuar.setDisable(false);
    }

    @FXML
    private void remover() {
        productos.remove(producto);
        cantidades.remove(producto);
        if (productos.isEmpty()) {
            botonRemover.setDisable(true);
            botonContinuar.setDisable(true);
        }
    }
}
