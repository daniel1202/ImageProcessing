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
    public Image originalimage;
    public AnchorPane panel;
    public Slider kontrast;
    public Slider nasycenie;
    public Slider jasnosc;
    public Slider temperatura;
    public Slider ostrosc;
    //balans slidery
    public Slider balansCzerwony;
    public Slider balansZielony;
    public Slider balansNiebieski;
    //pliki histogramów i zdjeć
    private File outfile = new File("swap\\image.png");
    private File processedfile = new File("swap\\processed.png");
    private File redFile = new File("swap\\red.png");
    private File greenFile = new File("swap\\green.png");
    private File blueFile = new File("swap\\blue.png");
    private File lumFile = new File("swap\\lum.png");
    public ImageProcessor processor;
    public ImageView lumView;
    public ImageView greenView;
    public ImageView blueView;
    public ImageView redView;
    public Image red;
    public Image green;
    public Image blue;
    public Image lum;

    public void setProcessor(ImageProcessor processor) {
        this.processor = processor;
        System.out.println("Connection established");
    }
    
    public void loadImage(ActionEvent actionEvent) throws IOException {
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
              originalimage = SwingFXUtils.toFXImage(bufferedImage,null);
              imageView.setImage(image);
          } catch (IOException e) {
              e.printStackTrace();
          }
        }
        BufferedImage temp = SwingFXUtils.fromFXImage(originalimage, null);
        ImageIO.write(temp, "PNG", outfile);
        histograms("swap/image.png");
        resetSliders();
    }
    public void resetSliders()
    {
        kontrast.setValue(0);
        nasycenie.setValue(0);
        jasnosc.setValue(0);
        ostrosc.setValue(1);
        temperatura.setValue(6500);
        balansCzerwony.setValue(1);
        balansNiebieski.setValue(1);
        balansZielony.setValue(1);
    }
    public void saveImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Załaduj zdjęcie");
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


    public void loadHistograms(ActionEvent actionEvent) throws IOException {
        histograms("swap/image.png");
    }

    public void slidersChange(MouseEvent mouseEvent) throws IOException {
        callProcessor();
    }

    private void histograms(String path) throws IOException {
        processor.histograms(path);
        bufferedImage = ImageIO.read(redFile);
        red = SwingFXUtils.toFXImage(bufferedImage,null);
        redView.setImage(red);

        bufferedImage = ImageIO.read(greenFile);
        green = SwingFXUtils.toFXImage(bufferedImage,null);
        greenView.setImage(green);

        bufferedImage = ImageIO.read(blueFile);
        blue = SwingFXUtils.toFXImage(bufferedImage,null);
        blueView.setImage(blue);

        bufferedImage = ImageIO.read(lumFile);
        lum = SwingFXUtils.toFXImage(bufferedImage,null);
        lumView.setImage(lum);
    }

    public void callProcessor() throws IOException {
        BufferedImage temp = SwingFXUtils.fromFXImage(originalimage, null);
        ImageIO.write(temp, "PNG", outfile);
        processor.all_operations((int)Math.round(kontrast.getValue()),
                (int)Math.round(jasnosc.getValue()),
                (int)Math.round(nasycenie.getValue()),
                (int)ostrosc.getValue(),
                (int)temperatura.getValue(),
                balansCzerwony.getValue(), balansZielony.getValue(), balansNiebieski.getValue());
        bufferedImage = ImageIO.read(processedfile);
        image = SwingFXUtils.toFXImage(bufferedImage,null);
        imageView.setImage(image);
        histograms("swap/processed.png");
    }

    public void kontrastReset(ActionEvent actionEvent) throws IOException {
        kontrast.setValue(0);
        callProcessor();
    }

    public void nasycenieReset(ActionEvent actionEvent) throws IOException {
        nasycenie.setValue(0);
        callProcessor();
    }

    public void jasnoscReset(ActionEvent actionEvent) throws IOException {
        jasnosc.setValue(0);
        callProcessor();
    }

    public void temperaturaReset(ActionEvent actionEvent) throws IOException {
        temperatura.setValue(5600);
        callProcessor();
    }

    public void ostroscReset(ActionEvent actionEvent) throws IOException {
        ostrosc.setValue(1);
        callProcessor();
    }

    public void czerwonyReset(ActionEvent actionEvent) throws IOException {
        balansCzerwony.setValue(1);
        callProcessor();
    }

    public void zielonyReset(ActionEvent actionEvent) throws IOException {
        balansZielony.setValue(1);
        callProcessor();
    }

    public void niebieskiReset(ActionEvent actionEvent) throws IOException {
        balansNiebieski.setValue(1);
        callProcessor();
    }
}

