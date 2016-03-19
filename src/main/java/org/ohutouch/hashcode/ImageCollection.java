package org.ohutouch.hashcode;

public class ImageCollection {

    public int value;

    public int[][] locations;

    public int[][] ranges;

    public ImageCollection(int value, int nLocations, int nRanges) {
        this.value = value;
        this.locations = new int[nLocations][2];
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
}
