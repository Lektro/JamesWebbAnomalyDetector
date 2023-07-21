package org.example;

import org.example.processing.files.FitsFolderProcessor;

public class AnomalyDetectionMain {

    public static void main(String[] args) {

        // Adjust these value's to your liking
        double hotPixelThreshold = 520.0;
        int hotPixelMarkerSize = 15;

        // Define folder paths
        String inputFolderPath = "C:/Temp/DataInput";
        String outputFolderPath = "C:/Temp/DataOutput";

        try {
            // Initialize the FitsFolderProcessor to process the FITS files in the input folder.
            FitsFolderProcessor folderProcessor = new FitsFolderProcessor(inputFolderPath, outputFolderPath,
                    hotPixelThreshold, hotPixelMarkerSize);

            // Call the processAllFiles method to start processing the FITS files.
            folderProcessor.processAllFiles();

        } catch (Exception e) {

            // Catch any exception that might occur during the processing.
            e.printStackTrace();

            // user-friendly message or log the error.
            System.out.println("An error occurred during processing: " + e.getMessage());
        }
    }
}