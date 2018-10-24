import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //application
        primaryStage.setMaximized(true);
        BorderPane app = new BorderPane();

        //menu
        Menu importer = new Menu("Importer");
        Menu exporter = new Menu("Exporter");

        MenuItem lignes = new MenuItem("lignes");
        MenuItem regions = new MenuItem("Régions");
        MenuItem barres = new MenuItem("Barres");
        MenuItem png = new MenuItem("PNG");
        MenuItem tiff = new MenuItem("GIF");

        importer.getItems().addAll(lignes, regions, barres);
        exporter.getItems().addAll(png, tiff);

        MenuBar menuBar = new MenuBar(importer, exporter);
        app.setTop(menuBar);

        //fichier lecture
        FileChooser fc = new FileChooser();
        fc.setTitle("Veuillez sélectionner un fichier");

        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Fichier DAT", "*.dat")
        );

        //lineChart
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxix = new NumberAxis();
        xAxis.setLabel("Mois");
        yAxix.setLabel("Température");
        final LineChart<String, Number> root =
                new LineChart<String, Number>(xAxis, yAxix);
        root.setTitle("Tempérarure moyennes");

        //areaChart
        final CategoryAxis xAxis2 = new CategoryAxis();
        final NumberAxis yAxix2 = new NumberAxis();
        xAxis.setLabel("Mois");
        yAxix.setLabel("Température");
        final AreaChart<String, Number> root2 =
                new AreaChart<String, Number>(xAxis2, yAxix2);
        root2.setTitle("Températures moyennes");

        //barChart
        final CategoryAxis xAxis3 = new CategoryAxis();
        final NumberAxis yAxix3 = new NumberAxis();
        xAxis.setLabel("Mois");
        yAxix.setLabel("Température");
        final BarChart<String, Number> root3 =
                new BarChart<String, Number>(xAxis3, yAxix3);
        root3.setTitle("Températures moyennes");

        lignes.setOnAction(event -> {

            File fichier = fc.showOpenDialog(primaryStage);

            try {
                XYChart.Series series = creationSerie(fichier);

                root.getData().addAll(series);
                app.setCenter(root);
            }
            catch (NullPointerException e){}


        });

        png.setOnAction(event -> {
            if (app.getCenter() == root){
                saveAsPng(root, primaryStage);
            }
            else if (app.getCenter() == root2){
                saveAsPng(root2, primaryStage);
            }
            else if (app.getCenter() == root3){
                saveAsPng(root3, primaryStage);
            }

        });

        tiff.setOnAction(event -> {
            if (app.getCenter() == root){
                saveAsGif(root, primaryStage);
            }
            else if (app.getCenter() == root2){
                saveAsGif(root2, primaryStage);
            }
            else if (app.getCenter() == root3){
                saveAsGif(root3, primaryStage);
            }

        });

        regions.setOnAction(event -> {

            File fichier = fc.showOpenDialog(primaryStage);

            try{
                XYChart.Series series = creationSerie(fichier);
                root2.getData().addAll(series);
                app.setCenter(root2);
            }
            catch (NullPointerException e){}


        });

        barres.setOnAction(event -> {
            File fichier = fc.showOpenDialog(primaryStage);

           try{
               XYChart.Series series = creationSerie(fichier);
               root3.getData().addAll(series);
               app.setCenter(root3);
           }
           catch (NullPointerException e){}



        });

        primaryStage.setScene(new Scene(app));
        primaryStage.show();
    }

    public XYChart.Series creationSerie(File file){

        //data
        XYChart.Series series = new XYChart.Series();
        series.setName("Données");

        try{
            List<String> liste = Files.readAllLines(Paths.get(file.getPath()));
            String mois[] = liste.get(0).split(",");
            String temperature[] = liste.get(1).split(",");

            for (int i = 0; i < liste.size(); i++){
                series.getData().add(new XYChart.Data(mois[0], Float.parseFloat(temperature[0])));
                series.getData().add(new XYChart.Data(mois[1], Float.parseFloat(temperature[1])));
                series.getData().add(new XYChart.Data(mois[2], Float.parseFloat(temperature[2])));
                series.getData().add(new XYChart.Data(mois[3], Float.parseFloat(temperature[3])));
                series.getData().add(new XYChart.Data(mois[4], Float.parseFloat(temperature[4])));
                series.getData().add(new XYChart.Data(mois[5], Float.parseFloat(temperature[5])));
                series.getData().add(new XYChart.Data(mois[6], Float.parseFloat(temperature[6])));
                series.getData().add(new XYChart.Data(mois[7], Float.parseFloat(temperature[7])));
            }

        }
        catch (IOException e){ }
        catch (NullPointerException e2){}

        return series;
    }

    public void saveAsPng(Chart Chart, Stage primaryStage) {

        FileChooser fc = new FileChooser();
        fc.setTitle("Veuillez sélectionner un fichier");

        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Fichier PNG", "*.png")
        );

        WritableImage image = Chart.snapshot(new SnapshotParameters(), null);
        File fichier = fc.showSaveDialog(primaryStage);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", fichier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAsGif(Chart Chart, Stage primaryStage) {

        FileChooser fc = new FileChooser();
        fc.setTitle("Veuillez sélectionner un fichier");

        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Fichier Gif", "*.gif")
        );

        WritableImage image = Chart.snapshot(new SnapshotParameters(), null);
        File fichier = fc.showSaveDialog(primaryStage);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "gif", fichier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
