package view;

import business.Producto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductoController implements Initializable {

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
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaUnidadesStock.setCellValueFactory(new PropertyValueFactory<>("unidadesStock"));
        columnaCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        columnaEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTabla();
        tableView.setItems(Producto.mostrar());
        activo.setSelected(true);
        mapEstatus.put("Activo", "A");
        mapEstatus.put("Inactivo", "I");

    }

    private Optional<ButtonType> confirmar(){
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


    public void agregar(ActionEvent event) {
        Optional<ButtonType> resultado = confirmar();
        if (resultado.isPresent()){
            String context = business.Producto.insertar("Valor", descripcion.getText(),
                    new BigDecimal(precio.getText()), Integer.parseInt(unidadesStock.getText()), codigo.getText());
            Alert insercion = new Alert(Alert.AlertType.INFORMATION, context);
            insercion.show();
            tableView.setItems(Producto.mostrar());
            System.out.println("Dentro de metodo agregar " + nombre.getText());
        }

    }
}
