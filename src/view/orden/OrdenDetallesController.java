package view.orden;

import data.Orden;
import data.OrdenDetalle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class OrdenDetallesController implements Initializable {
    private static Stage primaryStage;
    private static Orden orden_;

    public static void start(Stage stage, Orden orden) throws IOException {
        primaryStage = stage;
        orden_ = orden;
        Parent root = FXMLLoader.load(OrdenDetallesController.class.getResource("OrdenDetalles.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ver Ordenes");
        primaryStage.showAndWait();
    }


    @FXML
    private TableView<OrdenDetalle> tableView;
    @FXML
    private TableColumn<?, ?> columnaNo;
    @FXML
    private TableColumn<?, ?> columnaCodigo;
    @FXML
    private TableColumn<?, ?> columnaNombre;
    @FXML
    private TableColumn<?, ?> columnaDescripcion;
    @FXML
    private TableColumn<?, ?> columnaCantidad;
    @FXML
    private TableColumn<?, ?> columnaPrecio;
    @FXML
    private TableColumn<?, ?> columnaDescuento;
    @FXML
    private TableColumn<?, ?> columnaImpuesto;
    @FXML
    private TableColumn<?, ?> columnaNeto;

    private void initTabla() {
        columnaNo.setCellValueFactory(new PropertyValueFactory<>("linea"));
        columnaCodigo.setCellValueFactory(new PropertyValueFactory<>("productoCodigo"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("productoNombre"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("productoDescripcion"));
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaDescuento.setCellValueFactory(new PropertyValueFactory<>("descuento"));
        columnaImpuesto.setCellValueFactory(new PropertyValueFactory<>("impuesto"));
        columnaNeto.setCellValueFactory(new PropertyValueFactory<>("neto"));
    }

    @FXML
    private Label id;
    @FXML
    private Label nombre;
    @FXML
    private Label numero;
    @FXML
    private Label fecha;
    @FXML
    private Label hora;
    @FXML
    private Label totalBruto;
    @FXML
    private Label totalDescuento;
    @FXML
    private Label totalImpuesto;
    @FXML
    private Label totalCargo;
    @FXML
    private Label totalNeto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTabla();
        initLabels();
        tableView.setItems(business.Orden.mostrarDetalles(orden_.getId()));
    }

    private void initLabels() {
        id.setText(String.valueOf(orden_.getSuplidorId()));
        nombre.setText(orden_.getSuplidorNombre());
        numero.setText(String.valueOf(orden_.getNumero()));
        fecha.setText(orden_.getFecha().toString());
        hora.setText(orden_.getHora().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        totalBruto.setText(orden_.getTotalBruto().toPlainString());//.setScale(2, RoundingMode.CEILING).toPlainString()
        totalDescuento.setText(orden_.getTotalDescuento().toPlainString());
        totalImpuesto.setText(orden_.getTotalImpuesto().toPlainString());
        totalCargo.setText(orden_.getTotalCargo().toPlainString());
        totalNeto.setText(orden_.getTotalNeto().toPlainString());
    }

    @FXML
    private void cancelarOrden() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("¿Desea cancelar la orden?");
        alerta.setHeaderText("La orden va a ser cancelada.");
        if (alerta.showAndWait().isPresent()) {
            Alert insercion = new Alert(Alert.AlertType.INFORMATION,
                    business.Orden.eliminar(orden_.getId()));
            insercion.show();
            primaryStage.close();
        }

    }

}
