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

        // Initialize Folder / File Processor in prep for api connection
        FitsFolderProcessor folderProcessor = new FitsFolderProcessor(inputFolderPath, outputFolderPath,
                hotPixelThreshold, hotPixelMarkerSize);
        folderProcessor.processAllFiles();
    }
}