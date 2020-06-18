package sample;

public interface ImageProcessor {
    void histograms();
    void contrast(int value);
    void brightness(int value);
    void saturation(int value);
    void smoothing();
    void sharpening();
    void color_temperature(int value);
    void all_operations(int contrast, int brightness, int saturation, int sharpness, int temperature, double r_scalar, double g_scalar, double b_scalar);
}
