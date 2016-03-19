package org.ohutouch.hashcode;

import java.util.Arrays;

public class ImageCollection {

    public int value;

    public Location[] locations;

    public int[][] ranges;

    public ImageCollection(int value, int nLocations, int nRanges) {
        this.value = value;
        this.locations = new Location[nLocations];
        this.ranges = new int[nRanges][2];
    }

    public boolean canBeShotAt(int turn) {
        for (int[] range : ranges) {
            if (range[0] <= turn && turn <= range[1]) {
                return true;
            }
        }
        return false;
    }

    public long numberOfPicturesLeftToTake() {
        return Arrays.stream(locations).filter(loc -> loc.pictureTaken).count();
    }
}
