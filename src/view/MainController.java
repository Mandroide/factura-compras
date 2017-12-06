package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TreeView<String> treeView;

    private Image icon = new Image(getClass().getResourceAsStream("resources/main.png"));


    public void mostrarSuplidor() {
        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("suplidor/Suplidor.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Suplidores");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mostrarOrden() {

    }

    public void mostrarProducto() {
        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("producto/Producto.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Productos");
            //boolean b = primaryStage.getIcons().add(new Image("view/resources/main.png"));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<String> root = new TreeItem<>("Compras", new ImageView(icon));

        TreeItem<String> rootSuplidor = new TreeItem<>("Suplidor");
        TreeItem<String> nodoSuplidores = new TreeItem<>("Ver suplidores");
        rootSuplidor.getChildren().add(nodoSuplidores);

        TreeItem<String> rootProducto = new TreeItem<>("Producto");
        TreeItem<String> nodoProductos = new TreeItem<>("Ver productos");
        rootProducto.getChildren().add(nodoProductos);

        TreeItem<String> rootOrden = new TreeItem<>("Orden");
        TreeItem<String> nodoOrdenes = new TreeItem<>("Ver ordenes");
        TreeItem<String> nodoCompras = new TreeItem<>("Efectuar una compra");
        rootOrden.getChildren().add(nodoOrdenes);
        rootOrden.getChildren().add(nodoCompras);

        root.getChildren().addAll(rootSuplidor, rootProducto, rootOrden);

        root.setExpanded(false);
        treeView.setRoot(root);
    }

    public void cargarFormulario() {
        TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
        switch (item.getValue()) {
            case "Ver Suplidores":
                break;
            case "Ver Productos":
                break;
            default:
                System.out.println("Default");
                break;
        }
    }
}
