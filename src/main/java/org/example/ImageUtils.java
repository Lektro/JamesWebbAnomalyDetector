package org.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    public static void saveAsJPEG(String outputFilePath, double[][] pixelData) throws IOException {
        int width = pixelData[0].length;
        int height = pixelData.length;

        // Create a BufferedImage to store the pixel data
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // Set the pixel values to the BufferedImage
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelValue = (int) pixelData[y][x];
                bufferedImage.setRGB(x, y, (pixelValue << 16) | (pixelValue << 8) | pixelValue);
            }
        }

        // Save the BufferedImage as a JPEG file
        File outputImageFile = new File(outputFilePath);
        ImageIO.write(bufferedImage, "JPEG", outputImageFile);
    }
}
