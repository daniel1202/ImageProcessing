package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.server.ExportException;


public class Controller {
    public ImageView imageView;
    public BufferedImage bufferedImage;
    public Image image;
    public AnchorPane panel;
    public Slider kontrast;
    public Slider nasycenie;
    public Slider jasnosc;
    public Slider temperatura;
    public Slider balans;
    public Slider wyostrzanie;
    public Slider rozmycie;
    private File outfile = new File("image.jpg");
    private File processedfile = new File("processed.jpg");

    public ImageProcessor processor;
    public BarChart luminacja;
    public BarChart kanalZielony;
    public BarChart kanalNiebieski;
    public BarChart kanalCzerwony;

    public void setProcessor(ImageProcessor processor) {
        this.processor = processor;
        int i = 0;
        System.out.println("XDDDDDDD");
        //System.out.println(this.processor);
    }
    
    public void loadImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("załaduj zdjęcie");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("GIF", "*.gif")

        );
        File  file = fileChooser.showOpenDialog(panel.getScene().getWindow());
        if(file!=null)
        {
          try{
           bufferedImage = ImageIO.read(file);
              image = SwingFXUtils.toFXImage(bufferedImage,null);
              imageView.setImage(image);
          } catch (IOException e) {
              e.printStackTrace();
          }
        }
    }

    public void saveImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("załaduj zdjęcie");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("GIF", "*.gif")

        );
        File file = fileChooser.showSaveDialog(panel.getScene().getWindow());
        if (file != null) {
            try{
               FileChooser.ExtensionFilter extensionFilter = fileChooser.getSelectedExtensionFilter();
               String extension = extensionFilter.getExtensions().get(0).substring(1);
                if(extension.substring(1).equalsIgnoreCase("bmp")) ImageIO.write(bufferedImage,(extension.substring(1).toUpperCase()),file);
                else {
                    BufferedImage temp = SwingFXUtils.fromFXImage(image, null);
                    ImageIO.write(temp,(extension.substring(1).toUpperCase()),file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void KontrastChange(MouseEvent mouseEvent) throws IOException {
        BufferedImage temp = SwingFXUtils.fromFXImage(image, null);
        ImageIO.write(temp, "jpg", outfile);
//        processor.contrast((int)Math.round(kontrast.getValue()));
//        bufferedImage = ImageIO.read(processedfile);
//        image = SwingFXUtils.toFXImage(bufferedImage,null);
//        imageView.setImage(image);

    }

    public void NasycenieChanged(MouseEvent mouseEvent) {
        System.out.println(nasycenie.getValue());
    }

    public void JasnoscChanged(MouseEvent mouseEvent) {
        System.out.println(jasnosc.getValue());
    }

    public void TemperaturaChanged(MouseEvent mouseEvent) {
        System.out.println(temperatura.getValue());
    }

    public void BalansChanged(MouseEvent mouseEvent) {
        System.out.println(balans.getValue());
    }

    public void WyostrzenieChanged(MouseEvent mouseEvent) {
        System.out.println(wyostrzanie.getValue());
    }

    public void RozmycieChanged(MouseEvent mouseEvent) {
        System.out.println(rozmycie.getValue());
    }

    @Override
    public String toString() {
        return "Instance";
    }
}
