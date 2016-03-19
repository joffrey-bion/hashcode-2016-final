package org.ohutouch.hashcode;

public class Location {

    public int[] coords = new int[2];

    public boolean pictureTaken = false;

    public Location(int latitude, int longitude) {
        this.coords[Simulation.LATITUDE] = latitude;
        this.coords[Simulation.LONGITUDE] = longitude;
    }

    public int getLongitude() {
        return coords[Simulation.LONGITUDE];
    }
}
