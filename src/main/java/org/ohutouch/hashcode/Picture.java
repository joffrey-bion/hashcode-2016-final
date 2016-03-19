package org.ohutouch.hashcode;

import static org.ohutouch.hashcode.Simulation.*;

public class Picture {

    public final int[] position;

    public final int turnTakenAt;

    public final int satellite;

    public Picture(int[] position, int turnTakenAt, int satellite) {
        this.position = position;
        this.turnTakenAt = turnTakenAt;
        this.satellite = satellite;
    }

    public String toOutputLine() {
        return position[LATITUDE] + " " + position[LONGITUDE] + " " + turnTakenAt + " " + satellite;
    }
}
