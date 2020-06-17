package sample;

public interface ImageProcessor {
    void histograms();
    void contrast(int value);
    void brightness(int value);
    void saturation(int value);
    void smoothing();
    void sharpening();
    void color_temperature(int value);
}
