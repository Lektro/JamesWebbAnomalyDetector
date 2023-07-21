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

        // Process Folders/Files in preparation to api input
        FitsFolderProcessor folderProcessor = new FitsFolderProcessor(inputFolderPath, outputFolderPath,
                hotPixelThreshold, hotPixelMarkerSize);
        folderProcessor.processAllFiles();
    }
}




 /*       // Initialize File Objects
        File folder = new File(inputFolderPath);
        File outputDir = new File(outputFolderPath);

        // Create a folder to store the output images if it doesn't exist
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        // Convert .fits filenames to lower case
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".fits"));

        // check if there .fits files in the folder or not
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
                        outputFolderPath, hotPixelThreshold, hotPixelMarkerSize);
                processor.processAndSaveImages();

                // keep processing files until there are no new files to process
                if (fileIndex < files.length - 1) {
                    System.out.println("Processing next file...");
                } else {
                    System.out.println("All files processed.");
                }

            // Catching any exceptions
            } catch (Exception e) {
                e.printStackTrace();
            }*/

