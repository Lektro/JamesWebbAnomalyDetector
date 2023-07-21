package org.example;

import java.io.File;

public class AnomalyDetectionMain {

    public static void main(String[] args) {
        String folderPath = "C:/Temp/DataInput";
        String outputFolder = "C:/Temp/DataOutput";
        double hotPixelThreshold = 550.0;
        int hotPixelMarkerSize = 15;

        File folder = new File(folderPath);
        File outputDir = new File(outputFolder);

        // Create a folder to store the output images if it doesn't exist
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".fits"));

        if (files == null || files.length == 0) {
            System.out.println("No FITS files found in the folder.");
            return;
        }

        for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
            File file = files[fileIndex];
            String fitsFilePath = file.getAbsolutePath();

            try {
                AnomalyDetectionProcessor processor = new AnomalyDetectionProcessor(fitsFilePath,
                        outputFolder, hotPixelThreshold, hotPixelMarkerSize);
                processor.processAndSaveImages();

                if (fileIndex < files.length - 1) {
                    System.out.println("Processing next file...");
                } else {
                    System.out.println("All files processed.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
