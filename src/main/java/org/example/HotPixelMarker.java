package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import static org.example.HotPixelDetector.*;

public class HotPixelMarker {

    public static void markHotPixelsAndSave(FitsImage fitsImage, int markerSize, String outputFilePath) {
        double[][] data = fitsImage.getData();
        int imageWidth = fitsImage.getWidth();
        int imageHeight = fitsImage.getHeight();
        List<HotPixel> hotPixels = fitsImage.getHotPixels();

        BufferedImage markedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        // Find the min and max pixel values in the data
        double minValue = getMinPixelValue(data);
        double maxValue = getMaxPixelValue(data);

        // Draw the original image onto the new image with appropriate scaling
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                double pixelValue = getPixelValue(data, x, y);
                int rgbValue = scalePixelValueToRGB(pixelValue, minValue, maxValue);
                markedImage.setRGB(x, y, rgbValue);
            }
        }

        // Draw markers on the hot pixel locations
        Graphics graphics = markedImage.getGraphics();
        graphics.setColor(Color.RED);
        for (HotPixel hotPixel : hotPixels) {
            int x = hotPixel.getX();
            int y = hotPixel.getY();
            graphics.fillRect(x - markerSize / 2, y - markerSize / 2, markerSize, markerSize);
        }
        graphics.dispose();

        // Save the marked image
        try {
            String imageFormat = "JPEG"; // You can change the format to PNG, BMP, etc.
            java.io.File outputFile = new java.io.File(outputFilePath);
            javax.imageio.ImageIO.write(markedImage, imageFormat, outputFile);
            System.out.println("Marked image saved as '" + outputFilePath + "'.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Utility method to scale a pixel value to an RGB color value (0-255)
    static int scalePixelValueToRGB(double pixelValue, double minValue, double maxValue) {
        double normalizedValue = (pixelValue - minValue) / (maxValue - minValue);
        int rgbValue = (int) (normalizedValue * 255);
        return new Color(rgbValue, rgbValue, rgbValue).getRGB();
    }

    // Utility methods (same as before)
    // ...
}
