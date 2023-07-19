package org.example;

import nom.tam.fits.*;

import java.io.IOException;

public class FitsDataReader {

    public FitsDataReader(String fitsFilePath) {
    }

    public static double[][] readFitsImageData(String fitsFilePath) throws FitsException, IOException {
        Fits fits = new Fits(fitsFilePath);
        BasicHDU<?> hdu = fits.getHDU(0);
        if (hdu == null) {
            throw new FitsException("FITS imageHDU is null or empty.");
        }

        if (!(hdu instanceof ImageHDU)) {
            throw new FitsException("The primary HDU is not an ImageHDU.");
        }

        ImageHDU imageHDU = (ImageHDU) hdu;
        Object imageData = imageHDU.getData().getData();

        if (imageData instanceof double[][]) {
            return (double[][]) imageData;
        } else if (imageData instanceof float[][]) {
            // Convert float data to double for consistent processing
            float[][] floatData = (float[][]) imageData;
            double[][] doubleData = new double[floatData.length][floatData[0].length];
            for (int i = 0; i < floatData.length; i++) {
                for (int j = 0; j < floatData[i].length; j++) {
                    doubleData[i][j] = floatData[i][j];
                }
            }
            return doubleData;
        } else if (imageData instanceof int[][]) {
            // Convert int data to double for consistent processing
            int[][] intData = (int[][]) imageData;
            double[][] doubleData = new double[intData.length][intData[0].length];
            for (int i = 0; i < intData.length; i++) {
                for (int j = 0; j < intData[i].length; j++) {
                    doubleData[i][j] = intData[i][j];
                }
            }
            return doubleData;
        } else {
            throw new FitsException("Unsupported data type.");
        }
    }
}
