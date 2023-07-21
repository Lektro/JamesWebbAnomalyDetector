package org.example.processing.files;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FitsFolderProcessor {

    private final String inputFolderPath;
    private final String outputFolderPath;
    private final double hotPixelThreshold;
    private final int hotPixelMarkerSize;

    public FitsFolderProcessor(String inputFolderPath, String outputFolderPath,
                               double hotPixelThreshold, int hotPixelMarkerSize) {
        this.inputFolderPath = inputFolderPath;
        this.outputFolderPath = outputFolderPath;
        this.hotPixelThreshold = hotPixelThreshold;
        this.hotPixelMarkerSize = hotPixelMarkerSize;
    }

    public void processAllFiles() {
        File folder = new File(inputFolderPath);
        File outputDir = new File(outputFolderPath);

        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".fits"));

        if (files == null || files.length == 0) {
            System.out.println("No FITS files found in the folder.");
            return;
        }

        List<String> fitsFilePaths = Arrays.stream(files)
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());

        fitsFilePaths.forEach(fitsFilePath -> {
            FitsFileProcessor fileProcessor = new FitsFileProcessor(fitsFilePath, outputFolderPath,
                    hotPixelThreshold, hotPixelMarkerSize);
            fileProcessor.processAndSaveImages();
        });

        System.out.println("All files processed.");
    }
}
