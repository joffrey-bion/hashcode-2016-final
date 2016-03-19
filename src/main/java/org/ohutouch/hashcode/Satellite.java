package org.ohutouch.hashcode;

import static org.ohutouch.hashcode.Simulation.*;

public class Satellite {

    public final int maxOrientationChangePerTurn;

    public final int maxOrientationValue;

    public int latitudeVelocity;

    public int[] position;

    public int[] orientation = new int[] {0, 0};

    public Satellite(int latitude, int longitude, int v0, int maxOrientationChangePerTurn, int maxOrientationValue) {
        this.position = new int[2];
        this.position[LATITUDE] = latitude;
        this.position[LONGITUDE] = longitude;
        this.latitudeVelocity = v0;
        this.maxOrientationChangePerTurn = maxOrientationChangePerTurn;
        this.maxOrientationValue = maxOrientationValue;
    }

    public void move() {
        boolean invertVelocity = Coord.move(position, latitudeVelocity, -15);
        if (invertVelocity) {
            latitudeVelocity = -latitudeVelocity;
        }
    }

    public void orientTo(int[] newTarget) {
        orientation = Coord.difference(newTarget, position);
    }

    public boolean canTakePictureOf(Location location) {
        int[] distanceFromPosition = Coord.distance(location.coords, position);
        if (distanceFromPosition[LATITUDE] > maxOrientationValue) {
            return false;
        }
        if (distanceFromPosition[LONGITUDE] > maxOrientationValue) {
            return false;
        }

        int[] currentGroundTarget = new int[2];
        currentGroundTarget[LATITUDE] = position[LATITUDE] + orientation[LATITUDE];
        currentGroundTarget[LONGITUDE] = position[LONGITUDE] + orientation[LONGITUDE];
        int[] distanceFromTarget = Coord.distance(location.coords, currentGroundTarget);
        if (distanceFromTarget[LATITUDE] > maxOrientationChangePerTurn) {
            return false;
        }
        if (distanceFromTarget[LONGITUDE] > maxOrientationChangePerTurn) {
            return false;
        }

        return false;
    }
}
