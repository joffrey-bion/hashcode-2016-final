package org.ohutouch.hashcode;

import static org.ohutouch.hashcode.Simulation.*;

public class Satellite {

    public final int maxOrientationChangePerTurn;

    public final int maxOrientationValue;

    public int latitudeVelocity;

    public int[] position;

    public int[] orientation = new int[] {0, 0};

    public int turnOfLastPictureTaken = 0;

    public Satellite(int latitude, int longitude, int v0, int maxOrientationChangePerTurn, int maxOrientationValue) {
        this.position = new int[2];
        this.position[LATITUDE] = latitude;
        this.position[LONGITUDE] = longitude;
        this.latitudeVelocity = v0;
        this.maxOrientationChangePerTurn = maxOrientationChangePerTurn;
        this.maxOrientationValue = maxOrientationValue;
    }

    public void moveOneStep() {
        boolean crossedPole = Coord.move(position, latitudeVelocity, -15);
        if (crossedPole) {
            latitudeVelocity = -latitudeVelocity;
        }
    }

    public void orientTo(int[] newTarget) {
        orientation = Coord.difference(newTarget, position);
    }

    public boolean canTakePictureOf(Location location, int currentTurn) {
        int[] distanceFromPosition = Coord.distance(location.coords, position);
        if (distanceFromPosition[LATITUDE] > maxOrientationValue) {
            //log(currentTurn, "location too far in latitude for this satellite");
            return false;
        }
        if (distanceFromPosition[LONGITUDE] > maxOrientationValue) {
            //log(currentTurn, "location too far in longitude for this satellite");
            return false;
        }

        int turnsPassedSinceLastPic = currentTurn - turnOfLastPictureTaken;
        int maxOrientationChange = maxOrientationChangePerTurn * turnsPassedSinceLastPic;
        int[] currentGroundTarget = new int[2];
        currentGroundTarget[LATITUDE] = position[LATITUDE] + orientation[LATITUDE];
        currentGroundTarget[LONGITUDE] = position[LONGITUDE] + orientation[LONGITUDE];
        int[] distanceFromTarget = Coord.distance(location.coords, currentGroundTarget);
        if (distanceFromTarget[LATITUDE] > maxOrientationChange) {
            return false;
        }
        if (distanceFromTarget[LONGITUDE] > maxOrientationChange) {
            return false;
        }

        return true;
    }

    public int getMinAcceptableLongitude() {
        // NOT normalized because this is a rough minimum
        return position[LONGITUDE] - maxOrientationValue;
    }

    public int getMaxAcceptableLongitude() {
        // NOT normalized because this is a rough maximum
        return position[LONGITUDE] + maxOrientationValue;
    }
}
