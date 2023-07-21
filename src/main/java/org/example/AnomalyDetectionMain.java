package org.example;

import java.io.File;

public class AnomalyDetectionMain {

    public static void main(String[] args) {
        // Adjust these value's to your liking
        double hotPixelThreshold = 550.0;
        int hotPixelMarkerSize = 15;

        // Def folder paths
        String folderPath = "C:/Temp/DataInput";
        String outputFolder = "C:/Temp/DataOutput";
        File folder = new File(folderPath);
        File outputDir = new File(outputFolder);

        // Create a folder to store the output images if it doesn't exist
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        // convert filenames to lower case
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".fits"));

        // check if there fit files in the folder or not
        if (files == null || files.length == 0) {
            System.out.println("No FITS files found in the folder.");
            return;
        }

        // Loop through all the files in the given folder
        for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
            File file = files[fileIndex];
            String fitsFilePath = file.getAbsolutePath();

            // Starting the anomaly detection process. You can add your own detection processes there
            try {
                AnomalyDetectionProcessor processor = new AnomalyDetectionProcessor(fitsFilePath,
                        outputFolder, hotPixelThreshold, hotPixelMarkerSize);
                processor.processAndSaveImages();

                // keep processing files until there are no new files to process
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
