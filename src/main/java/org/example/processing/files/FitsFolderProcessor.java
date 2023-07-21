package org.example.processing.files;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FitsFolderProcessor {

    // Fields to store the paths and parameters for processing
    private final String inputFolderPath;
    private final String outputFolderPath;
    private final double hotPixelThreshold;
    private final int hotPixelMarkerSize;

    // Constructor to initialize the processor with necessary parameters.
    public FitsFolderProcessor(String inputFolderPath, String outputFolderPath,
                               double hotPixelThreshold, int hotPixelMarkerSize) {
        this.inputFolderPath = inputFolderPath;
        this.outputFolderPath = outputFolderPath;
        this.hotPixelThreshold = hotPixelThreshold;
        this.hotPixelMarkerSize = hotPixelMarkerSize;
    }

    // Method to process all FITS files in the input folder.
    public void processAllFiles() {
        // Create a File object for the input folder.
        File folder = new File(inputFolderPath);

        // Create a File object for the output folder.
        File outputDir = new File(outputFolderPath);

        // Create the output directory if it does not exist.
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        // Get all files in the input folder with a ".fits" extension.
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".fits"));

        // Check if there are no FITS files in the folder.
        if (files == null || files.length == 0) {
            System.out.println("No FITS files found in the folder.");
            return;
        }

        // Convert the array of files into a list of absolute file paths.
        List<String> fitsFilePaths = Arrays.stream(files)
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());

        // Process each FITS file one by one.
        fitsFilePaths.forEach(fitsFilePath -> {
            // Create a new FitsFileProcessor for each file, passing the necessary parameters.
            FitsFileProcessor fileProcessor = new FitsFileProcessor(fitsFilePath, outputFolderPath,
                    hotPixelThreshold, hotPixelMarkerSize);
            // Process the FITS file and save the resulting images.
            fileProcessor.processAndSaveImages();
        });

        // Display a message indicating that all files have been processed.
        System.out.println("All files processed.");
    }
}
