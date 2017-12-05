package view.orden;

import data.OrdenDetalle;
import data.Producto;
import data.Suplidor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import view.Main;
import view.producto.ProductoActivoController;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;

public class OrdenActivaController implements Initializable {

    private static Suplidor suplidor_;
    private static Stage primaryStage = new Stage();
    private static ObservableList<OrdenDetalle> detalles_ = FXCollections.observableArrayList();
    private static HashMap<Producto, Integer> cantidades_;
    private static HashSet<Producto> productos_;

    public static void start(Stage stage, Suplidor suplidor, HashSet<Producto> productos, HashMap<Producto, Integer> cantidades) throws IOException {
        primaryStage = stage;
        suplidor_ = suplidor;
        productos_ = productos;
        cantidades_ = cantidades;
        Parent root = load(OrdenActivaController.class.getResource("OrdenActiva.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Confirmar orden");
    }


    @FXML
    private Label id;
    @FXML
    private Label nombre;
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
    private TreeView<String> treeView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTabla();
        cargarDetalles();
        calcularDetalles();
        tableView.setItems(detalles_);
        id.setText(String.valueOf(suplidor_.getId()));
        nombre.setText(suplidor_.getNombre());
        fecha.setText(LocalDate.now().toString());
        hora.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        treeView.setRoot(Main.iniciarItems());
        treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
                Main.cambiarScene(primaryStage, newValue));
    }

    private void calcularDetalles() {
        BigDecimal bruto = new BigDecimal("0.00");
        BigDecimal descuento = new BigDecimal("0.00");
        BigDecimal impuesto = new BigDecimal("0.00");
        BigDecimal cargo = new BigDecimal(Math.random() * 300);
        BigDecimal neto = new BigDecimal(cargo.doubleValue());
        for (OrdenDetalle ordenDetalle : detalles_) {
            bruto = bruto.add(ordenDetalle.getPrecio());
            descuento = descuento.add(ordenDetalle.getDescuento());
            impuesto = impuesto.add(ordenDetalle.getImpuesto());
            neto = neto.add(ordenDetalle.getNeto());
        }
        totalBruto.setText(bruto.setScale(2, RoundingMode.CEILING).toPlainString());
        totalDescuento.setText(descuento.setScale(2, RoundingMode.CEILING).toPlainString());
        totalImpuesto.setText(impuesto.setScale(2, RoundingMode.CEILING).toPlainString());
        totalCargo.setText(cargo.setScale(2, RoundingMode.CEILING).toPlainString());
        totalNeto.setText(neto.setScale(2, RoundingMode.CEILING).toPlainString());
    }

    private void cargarDetalles() {
        short linea = 0;
        for (Producto producto : productos_) {
            OrdenDetalle detalle = new OrdenDetalle(0, producto, cantidades_.get(producto));
            detalle.setLinea(++linea);
            detalle.setDescuento(new BigDecimal(Math.random() * 300).setScale(2, RoundingMode.CEILING));
            detalle.setImpuesto(new BigDecimal(0.05 * producto.getPrecio().doubleValue()).setScale(2, RoundingMode.CEILING));
            detalle.setNeto(producto.getPrecio().add(detalle.getImpuesto()).subtract(detalle.getDescuento()).setScale(2, RoundingMode.CEILING));
            detalles_.add(detalle);
        }
    }

    @FXML
    private void confirmar() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("¿Desea continuar?");
        alerta.setHeaderText("Presione ok para confirmar la orden");
        if (alerta.showAndWait().isPresent()) {
            Alert insercion = new Alert(Alert.AlertType.INFORMATION,
                    business.Orden.insertar(suplidor_, detalles_));
            insercion.show();
        }
    }

    @FXML
    private void cancelar() throws IOException {
        detalles_.clear();
        ProductoActivoController.start(primaryStage, suplidor_);
    }

}
