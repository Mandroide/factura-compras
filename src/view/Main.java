package view;

import javafx.application.Application;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.orden.OrdenController;
import view.producto.ProductoController;
import view.suplidor.SuplidorActivoController;
import view.suplidor.SuplidorController;

import java.io.IOException;

import static javafx.application.Platform.exit;


public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        boolean b = primaryStage.getIcons().add(new Image("view/resources/main.png"));
        primaryStage.setOnCloseRequest((WindowEvent e) ->
                exit()
        );
        SuplidorController.start(primaryStage);
        primaryStage.show();
    }

    public static TreeItem<String> iniciarItems() {
        ImageView iconSuplidor = new ImageView(
                new Image(Main.class.getResourceAsStream("/view/resources/suplidores.png"))
        );
        iconSuplidor.setFitWidth(15);
        iconSuplidor.setFitHeight(12);
        TreeItem<String> nodoSuplidores = new TreeItem<>("Ver suplidores", iconSuplidor);
        TreeItem<String> rootSuplidor = new TreeItem<>("Suplidor");
        rootSuplidor.getChildren().add(nodoSuplidores);

        ImageView iconProducto = new ImageView(
                new Image(Main.class.getResourceAsStream("/view/resources/productos.png"))
        );
        iconProducto.setFitWidth(15);
        iconProducto.setFitHeight(12);
        TreeItem<String> nodoProductos = new TreeItem<>("Ver productos", iconProducto);
        TreeItem<String> rootProducto = new TreeItem<>("Producto");
        rootProducto.getChildren().add(nodoProductos);


        ImageView iconOrden = new ImageView(
                new Image(Main.class.getResourceAsStream("/view/resources/ordenes.png"))
        );
        iconOrden.setFitWidth(15);
        iconOrden.setFitHeight(12);
        TreeItem<String> nodoOrdenes = new TreeItem<>("Ver ordenes", iconOrden);
        ImageView iconComprar = new ImageView(
                new Image(Main.class.getResourceAsStream("/view/resources/comprar.png"))
        );
        iconComprar.setFitWidth(15);
        iconComprar.setFitHeight(12);
        TreeItem<String> nodoCompras = new TreeItem<>("Efectuar una compra", iconComprar);
        TreeItem<String> rootOrden = new TreeItem<>("Orden");
        rootOrden.getChildren().add(nodoOrdenes);
        rootOrden.getChildren().add(nodoCompras);


        TreeItem<String> root = new TreeItem<>("NULL");
        root.getChildren().addAll(rootSuplidor, rootProducto, rootOrden);

        return root;

    }

    public static void cambiarScene(Stage primaryStage, TreeItem<String> item) {
        if (item == null)
            return;
        try {
            switch (item.getValue()) {
                case "Ver suplidores":
                    SuplidorController.start(primaryStage);
                    break;
                case "Ver ordenes":
                    OrdenController.start(primaryStage);
                    break;
                case "Ver productos":
                    ProductoController.start(primaryStage);
                    break;
                case "Efectuar una compra":
                    SuplidorActivoController.start(primaryStage);
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
