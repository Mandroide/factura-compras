package view.orden;

import data.Orden;
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
import java.util.ResourceBundle;

public class OrdenController implements Initializable {

    private static Stage primaryStage;

    public static void start(Stage stage) throws IOException {
        primaryStage = stage;
        Parent root = FXMLLoader.load(OrdenController.class.getResource("Orden.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ver Ordenes");
    }

    @FXML
    TableView<data.Orden> tableView;
    @FXML
    private TableColumn<?, ?> columnaSuplidorNo;
    @FXML
    private TableColumn<?, ?> columnaSuplidorNombre;
    @FXML
    private TableColumn<?, ?> columnaNo;
    @FXML
    private TableColumn<?, ?> columnaFecha;
    @FXML
    private TableColumn<?, ?> columnaHora;
    @FXML
    private TableColumn<?, ?> columnaTotalBruto;
    @FXML
    private TableColumn<?, ?> columnaTotalDescuento;
    @FXML
    private TableColumn<?, ?> columnaTotalImpuesto;
    @FXML
    private TableColumn<?, ?> columnaTotalCargo;
    @FXML
    private TableColumn<?, ?> columnaTotalNeto;

    private void initTabla() {
        columnaSuplidorNo.setCellValueFactory(new PropertyValueFactory<>("suplidorId"));
        columnaSuplidorNombre.setCellValueFactory(new PropertyValueFactory<>("suplidorNombre"));
        columnaNo.setCellValueFactory(new PropertyValueFactory<>("numero"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        columnaTotalBruto.setCellValueFactory(new PropertyValueFactory<>("totalBruto"));
        columnaTotalDescuento.setCellValueFactory(new PropertyValueFactory<>("totalDescuento"));
        columnaTotalImpuesto.setCellValueFactory(new PropertyValueFactory<>("totalImpuesto"));
        columnaTotalCargo.setCellValueFactory(new PropertyValueFactory<>("totalCargo"));
        columnaTotalNeto.setCellValueFactory(new PropertyValueFactory<>("totalNeto"));
    }

    @FXML
    private TreeView<String> treeView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTabla();
        tableView.setItems(business.Orden.mostrar());
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (v, oldValue, newValue) -> {
                    if (newValue == null)
                        return;
                    orden = newValue;
                    numero.setText(String.valueOf(orden.getNumero()));
                    botonVerDetalles.setDisable(false);
                }
        );
        treeView.setRoot(Main.iniciarItems());
        treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
                Main.cambiarScene(primaryStage, newValue));
    }


    @FXML
    private TextField numero;
    @FXML
    private Button botonVerDetalles;
    private Orden orden;

    @FXML
    private void verDetalles() throws IOException {
        // Cargame un stage con los detalles, envia suplidor No y SuplidorNombre
        Stage primaryStage = new Stage();
        OrdenDetallesController.start(primaryStage, orden);

    }


}
