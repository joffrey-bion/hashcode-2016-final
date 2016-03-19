package org.ohutouch.hashcode;

public class ImageCollection {

    public int value;

    public Location[] locations;

    /**
     * pictureTaken[i] is true if location[i] has been taken
     */
    public boolean[] pictureTaken;

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
}
