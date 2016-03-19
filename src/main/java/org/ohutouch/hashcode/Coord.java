package org.ohutouch.hashcode;

import static org.ohutouch.hashcode.Simulation.*;

public class Coord {

    public static final int MIN_LATITUDE = -324000;

    public static final int MAX_LATITUDE = 324000;

    public static final int MIN_LONGITUDE = -648000;

    private static final int LONGITUDE_WRAP_VALUE = 648000;

    /**
     * Modifies the given position
     *
     * @param position
     *         the position to modify
     * @param latitudeOffset
     * @param longitudeOffset
     *
     * @return true if velocity changed sign
     */
    public static boolean move(int[] position, int latitudeOffset, int longitudeOffset) {
        int newLatitude = position[LATITUDE] + latitudeOffset;
        int newLongitude = normalizeLongitude(position[LONGITUDE] + longitudeOffset);
        boolean velocityInversion = false;

        if (newLatitude > MAX_LATITUDE) {
            newLatitude = 180 * 3600 - newLatitude;
            newLongitude = MIN_LONGITUDE + newLongitude;
            velocityInversion = true;
        }

        if (newLatitude < MIN_LATITUDE) {
            newLatitude = -180 * 3600 - newLatitude;
            newLongitude = MIN_LONGITUDE + newLongitude;
            velocityInversion = true;
        }

        position[LATITUDE] = newLatitude;
        position[LONGITUDE] = newLongitude;
        return velocityInversion;
    }

    private static int normalizeLongitude(int longitude) {
        int adjustedLongitude = longitude + LONGITUDE_WRAP_VALUE;
        int modulo = Math.floorMod(adjustedLongitude, 2 * LONGITUDE_WRAP_VALUE);
        return modulo - LONGITUDE_WRAP_VALUE;
    }

    public static int[] distance(int[] pos1, int[] pos2) {
        int[] distance = difference(pos1, pos2);
        distance[LATITUDE] = Math.abs(distance[LATITUDE]);
        distance[LONGITUDE] = Math.abs(distance[LONGITUDE]);
        return distance;
    }

    /**
     * Gets normalized pos1 - pos2
     *
     * @param pos1
     * @param pos2
     *
     * @return
     */
    public static int[] difference(int[] pos1, int[] pos2) {
        int[] diff = new int[2];

        diff[LATITUDE] = pos1[LATITUDE] - pos2[LATITUDE];
        diff[LONGITUDE] = normalizeLongitude(pos1[LONGITUDE] - pos2[LONGITUDE]);

        return diff;
    }
}
