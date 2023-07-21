package org.example.processing.files;

import nom.tam.fits.FitsException;
import org.example.processing.AnomalyDetectionProcessor;

import java.io.IOException;

public class FitsFileProcessor {

    // Fields to store the paths and parameters for processing
    private final String fitsFilePath;
    private final String outputFolder;
    private final double hotPixelThreshold;
    private final int hotPixelMarkerSize;

    // Constructor to initialize the processor with necessary parameters.
    public FitsFileProcessor(String fitsFilePath, String outputFolder,
                             double hotPixelThreshold, int hotPixelMarkerSize) {
        this.fitsFilePath = fitsFilePath;
        this.outputFolder = outputFolder;
        this.hotPixelThreshold = hotPixelThreshold;
        this.hotPixelMarkerSize = hotPixelMarkerSize;
    }

    // Method to process the FITS file and save the resulting images.
    public void processAndSaveImages() {
        try {

            // Create an instance of AnomalyDetectionProcessor to process the FITS file.
            AnomalyDetectionProcessor processor = new AnomalyDetectionProcessor(fitsFilePath,
                    outputFolder, hotPixelThreshold, hotPixelMarkerSize);
            // Process the FITS file and save the resulting images.
            processor.processAndSaveImages();

        // If there is an exception during processing, print the stack trace for debugging.
        } catch (FitsException | IOException e) {
            e.printStackTrace();
        }
    }
}
