package view.producto;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.DoubleValidator;
import com.jfoenix.validation.IntegerValidator;
import data.Estatus;
import data.Producto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;
import view.Main;
import view.RadioButtonCell;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.EnumSet;
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
    private TableColumn<Producto, Integer> columnaNo;
    @FXML
    private TableColumn<Producto, String> columnaNombre;
    @FXML
    private TableColumn<Producto, String> columnaDescripcion;
    @FXML
    private TableColumn<Producto, BigDecimal> columnaPrecio;
    @FXML
    private TableColumn<Producto, Integer> columnaUnidadesStock;
    @FXML
    private TableColumn<Producto, String> columnaCodigo;
    @FXML
    private TableColumn<Producto, Estatus> columnaEstatus;

    private void initTabla() {
        columnaNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaUnidadesStock.setCellValueFactory(new PropertyValueFactory<>("unidadesStock"));
        columnaCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        columnaEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));

        columnaCodigo.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaDescripcion.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaUnidadesStock.setCellFactory(TextFieldTableCell.forTableColumn(
                new IntegerStringConverter()
        ));
        columnaPrecio.setCellFactory(TextFieldTableCell.forTableColumn(
                new BigDecimalStringConverter()
        ));

        columnaEstatus.setCellFactory((param) -> new RadioButtonCell<>(EnumSet.allOf(Estatus.class)));
    }

    @FXML
    private TreeView<String> treeView;

    @FXML
    private JFXTextField nombre;
    @FXML
    private JFXTextArea descripcion;
    @FXML
    private JFXTextField unidadesStock;
    @FXML
    private JFXTextField precio;
    @FXML
    private JFXTextField codigo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTabla();
        tableView.setItems(business.Producto.mostrar());

        treeView.setRoot(Main.iniciarItems());
        treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
                Main.cambiarScene(primaryStage, newValue));
        tableView.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) ->
                producto = newValue
        );

        unidadesStock.getValidators().add(new IntegerValidator("Solo numeros naturales"));
        unidadesStock.focusedProperty().addListener((o, oldValue, newValue) -> {
            if (!newValue)
                unidadesStock.validate();
        });

        precio.getValidators().add(new DoubleValidator("Solo numeros reales"));

        precio.focusedProperty().addListener((o, oldValue, newValue) -> {
            if (!newValue)
                precio.validate();
        });
    }

    @FXML
    private void buscar() {
        tableView.setItems(business.Producto.buscar(nombre.getText()));
    }


    private boolean haConfirmado() {
        String mensaje = ("Codigo: " + codigo.getText() + "\n") +
                "Nombre: " + nombre.getText() + "\n" +
                "Descripcion: " + descripcion.getText() + "\n" +
                "Unidades en Stock: " + unidadesStock.getText() + "\n" +
                "Precio: " + precio.getText() + "\n";

        var alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("\"¿Desea continuar?\"");
        alerta.setHeaderText(mensaje);
        return alerta.showAndWait().isPresent();
    }


    @FXML
    private void agregar() {
        if (haConfirmado()) {
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
    private void actualizar(TableColumn.CellEditEvent newValue){

        var producto = (Producto) newValue.getTableView().getItems().get(
                newValue.getTablePosition().getRow()
        );
        if (producto == null)
            return;
        if (newValue.getNewValue().equals(newValue.getOldValue()))
            return;

        TableColumn col = newValue.getTableColumn();
        String value = newValue.getNewValue().toString();
        if(col.equals(columnaNombre)){
            producto.setNombre(value);
        } else if (col.equals(columnaCodigo)){
            producto.setCodigo(value);
        } else if (col.equals(columnaDescripcion)){
            producto.setDescripcion(value);
        } else if(col.equals(columnaPrecio)){
            producto.setPrecio(new BigDecimal(value));
        } else if (col.equals(columnaEstatus)){
            producto.setEstatus(Estatus.valueOf(newValue.getNewValue().toString().toUpperCase()));
        }

        String context = business.Producto.actualizar(producto.getId(), producto.getCodigo(),
                producto.getNombre(), producto.getDescripcion(), producto.getPrecio(),
                producto.getUnidadesStock(), producto.getEstatus());
        Alert insercion = new Alert(Alert.AlertType.INFORMATION, context);
        insercion.show();
        tableView.setItems(business.Producto.mostrar());

    }


}
