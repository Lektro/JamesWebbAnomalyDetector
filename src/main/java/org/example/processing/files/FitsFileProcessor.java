package org.example.processing.files;

import nom.tam.fits.FitsException;
import org.example.processing.AnomalyDetectionProcessor;

import java.io.IOException;

public class FitsFileProcessor {

    private final String fitsFilePath;
    private final String outputFolder;
    private final double hotPixelThreshold;
    private final int hotPixelMarkerSize;

    public FitsFileProcessor(String fitsFilePath, String outputFolder,
                             double hotPixelThreshold, int hotPixelMarkerSize) {
        this.fitsFilePath = fitsFilePath;
        this.outputFolder = outputFolder;
        this.hotPixelThreshold = hotPixelThreshold;
        this.hotPixelMarkerSize = hotPixelMarkerSize;
    }

    public void processAndSaveImages() {
        try {
            AnomalyDetectionProcessor processor = new AnomalyDetectionProcessor(fitsFilePath,
                    outputFolder, hotPixelThreshold, hotPixelMarkerSize);
            processor.processAndSaveImages();
        } catch (FitsException | IOException e) {
            e.printStackTrace();
        }
    }
}
