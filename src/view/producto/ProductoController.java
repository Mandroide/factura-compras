package view.producto;

import data.Producto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import view.Main;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;

public class ProductoController implements Initializable {

    private static Stage primaryStage;

    public static void start(Stage stage) throws IOException {
        primaryStage = stage;
        Parent root = load(ProductoController.class.getResource("Producto.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ver Productos");
    }

    @FXML
    private TableView<data.Producto> tableView;
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
    @FXML
    private TableColumn<?, ?> columnaEstatus;

    private void initTabla() {
        columnaNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaUnidadesStock.setCellValueFactory(new PropertyValueFactory<>("unidadesStock"));
        columnaCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        columnaEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
    }

    @FXML
    private TreeView<String> treeView;

    @FXML
    private TextField nombre;
    @FXML
    private TextArea descripcion;
    @FXML
    private TextField unidadesStock;
    @FXML
    private TextField precio;
    @FXML
    private TextField codigo;
    @FXML
    private Button botonActualizar;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTabla();
        tableView.setItems(business.Producto.mostrar());
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    if (newValue == null)
                        return;
                    producto = newValue;
                    botonActualizar.setDisable(false);
                }
        );

        treeView.setRoot(Main.iniciarItems());
        treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
                Main.cambiarScene(primaryStage, newValue));
    }

    @FXML
    private void buscar() {
        botonActualizar.setDisable(true);
        tableView.setItems(business.Producto.buscar(nombre.getText()));
    }


    private Optional<ButtonType> confirmar() {
        String mensaje = ("Codigo: " + codigo.getText() + "\n") +
                "Nombre: " + nombre.getText() + "\n" +
                "Descripcion: " + descripcion.getText() + "\n" +
                "Unidades en Stock: " + unidadesStock.getText() + "\n" +
                "Precio: " + precio.getText() + "\n";

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("\"¿Desea continuar?\"");
        alerta.setHeaderText(mensaje);
        return alerta.showAndWait();
    }


    @FXML
    private void agregar() {
        Optional<ButtonType> resultado = confirmar();
        if (resultado.isPresent()) {
            String context = business.Producto.insertar(codigo.getText(), nombre.getText(), descripcion.getText(),
                    new BigDecimal(precio.getText()), Integer.parseInt(unidadesStock.getText()));
            Alert insercion = new Alert(Alert.AlertType.INFORMATION, context);
            insercion.show();
            tableView.setItems(business.Producto.mostrar());
        }
    }

    private Producto producto = new Producto();

    @FXML
    private void eliminar() {
        String mensaje = "No.: " + producto.getId() + "\n"
                + "Codigo: " + producto.getCodigo() + "\n"
                + "Nombre: " + producto.getNombre() + "\n"
                + "Descripcion: " + producto.getDescripcion() + "\n"
                + "Unidades en Stock: " + producto.getUnidadesStock() + "\n"
                + "Precio: " + producto.getPrecio() + "\n";
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("¿Desea eliminarlo?");
        alerta.setHeaderText(mensaje);
        if (alerta.showAndWait().isPresent()) {
            String context = business.Producto.eliminar(producto.getId());
            Alert insercion = new Alert(Alert.AlertType.INFORMATION, context);
            insercion.show();
            tableView.setItems(business.Producto.mostrar());
        }

    }

    @FXML
    private void actualizar() {
        String mensaje = "No.: " + producto.getId() + "\n"
                + "Codigo: " + codigo.getText() + "\n"
                + "Nombre: " + nombre.getText() + "\n"
                + "Descripcion: " + descripcion.getText() + "\n"
                + "Unidades en Stock: " + unidadesStock.getText() + "\n"
                + "Precio: " + precio.getText() + "\n";
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("¿Desea continuar?");
        alerta.setHeaderText(mensaje);
        if (alerta.showAndWait().isPresent()) {
            String context = business.Producto.actualizar(producto.getId(), codigo.getText(),
                    nombre.getText(), descripcion.getText(), new BigDecimal(precio.getText()),
                    Integer.parseInt(unidadesStock.getText()), "A");
            Alert insercion = new Alert(Alert.AlertType.INFORMATION, context);
            insercion.show();
            tableView.setItems(business.Producto.mostrar());
        }

    }


}
