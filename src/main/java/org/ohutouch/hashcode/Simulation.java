package org.ohutouch.hashcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simulation {

    private final String filename;

    public static final int LATITUDE = 0;

    public static final int LONGITUDE = 1;

    public final int nTurns;

    public final ImageCollection[] collections;

    public final Satellite[] satellites;

    public Simulation(String filename, int nTurns, int nSatellites, int nImages) {
        this.filename = filename;
        this.nTurns = nTurns;
        this.collections = new ImageCollection[nImages];
        this.satellites = new Satellite[nSatellites];
    }

    public List<Picture> run() {
        List<Picture> picturesTaken = new ArrayList<>();
        int turnsToSimulate = Math.min(nTurns, 10000); // FIXME remove this max number of simulated turns
        for (int turn = 0; turn < turnsToSimulate; turn++) {
            System.out.println("Simulating turn " + turn + "\tfor file " + filename);
            picturesTaken.addAll(takePictures(turn));
            moveSatellites();
        }

        return picturesTaken;
    }

    private void moveSatellites() {
        Arrays.stream(satellites).forEach(Satellite::move);
    }

    private List<Picture> takePictures(int turn) {
        List<Picture> picturesTaken = new ArrayList<>();
        for (int sat = 0; sat < satellites.length; sat++) {
            Picture pic = takeBestPicture(turn, sat);
            if (pic != null) {
                picturesTaken.add(pic);
            }
        }
        return picturesTaken;
    }

    private Picture takeBestPicture(int turn, int sat) {
        Satellite satellite = satellites[sat];
        List<Location> locationsInRange = findTakeableLocations(turn, satellite);
        if (!locationsInRange.isEmpty()) {
            Location location = pickLocation(locationsInRange, satellite);
            location.pictureTaken = true;
            satellite.turnOfLastPictureTaken = turn;
            satellite.orientTo(location.coords);
            return new Picture(location.coords, turn, sat);
        }
        return null;
    }

    private List<Location> findTakeableLocations(int turn, Satellite satellite) {
        List<Location> locationsInRange = new ArrayList<>();
        for (ImageCollection collection : collections) {
            if (!collection.canBeShotAt(turn)) {
                //log(turn, "cannot shot the given collection at the current turn");
                continue;
            }
            for (Location location : collection.locations) {
                if (location.pictureTaken) {
                    log(turn, "picture already taken for this location");
                    continue;
                }
                if (satellite.canTakePictureOf(location, turn)) {
                    locationsInRange.add(location);
                }
            }
        }
        return locationsInRange;
    }

    private Location pickLocation(List<Location> locations, Satellite satellite) {

        // TODO refine the choice of picture among the takeable ones

        return locations.get(0);
    }

    private void log(int turn, String msg) {
        System.out.println(String.format("File %s\tturn %d\t%s", filename, turn, msg));
    }
}
