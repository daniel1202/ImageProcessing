package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class Controller {
    public ImageView imageView;
    public BufferedImage bufferedImage;
    public Image image;
    public AnchorPane panel;

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
                else { BufferedImage temp = SwingFXUtils.fromFXImage(image, null);
               ImageIO.write(temp,(extension.substring(1).toUpperCase()),file); }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
