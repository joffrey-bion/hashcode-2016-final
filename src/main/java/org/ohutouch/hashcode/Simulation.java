package org.ohutouch.hashcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class Simulation {

    private final String filename;

    /**
     * Index of the latitude in an array of coordinates.
     */
    public static final int LATITUDE = 0;

    /**
     * Index of the longitude in an array of coordinates.
     */
    public static final int LONGITUDE = 1;

    /**
     * Total number of turns (seconds) in the simulation.
     */
    public final int nTurns;

    /**
     * Collections of images to shoot.
     */
    public final ImageCollection[] collections;

    /**
     * The satellites to use to shoot the pictures.
     */
    public final Satellite[] satellites;

    /**
     * The pictures taken since the beginning of the simulation.
     */
    public final List<Picture> picturesTaken = new ArrayList<>();

    public Simulation(String filename, int nTurns, int nSatellites, int nImages) {
        this.filename = filename;
        this.nTurns = nTurns;
        this.collections = new ImageCollection[nImages];
        this.satellites = new Satellite[nSatellites];
    }

    public List<Picture> run() {
        picturesTaken.clear();
        for (int turn = 0; turn < nTurns; turn++) {
            if (turn % 10000 == 0) {
                System.out.println("Simulating turn " + turn + "\tfor file " + filename);
            }
            takePictures(turn);
            moveSatellitesOneStep();
        }
        return picturesTaken;
    }

    private void moveSatellitesOneStep() {
        for (Satellite satellite :satellites) {
            satellite.moveOneStep();
        }
    }

    private void takePictures(int turn) {
        TreeMap<Integer, Location> locationsByLongitude = new TreeMap<>();
        Arrays.stream(collections)
                .filter(col -> col.canBeShotAt(turn))
                .flatMap(col -> Arrays.stream(col.locations))
                .forEach(loc -> locationsByLongitude.put(loc.coords[LONGITUDE], loc));
        for (int sat = 0; sat < satellites.length; sat++) {
            takeBestPicture(turn, sat, locationsByLongitude);
        }
    }

    private void takeBestPicture(int turn, int sat, TreeMap<Integer, Location> locationsByLongitude) {
        Satellite satellite = satellites[sat];
        List<Location> locationsInRange = findTakeableLocations(turn, satellite, locationsByLongitude);
        if (!locationsInRange.isEmpty()) {
            Location location = pickLocation(locationsInRange, satellite);
            location.pictureTaken = true;
            satellite.turnOfLastPictureTaken = turn;
            satellite.orientTo(location.coords);
            picturesTaken.add(new Picture(location.coords, turn, sat));
            log(turn, "picture taken by " + sat + " at " + Arrays.toString(location.coords));
        }
    }

    private List<Location> findTakeableLocations(int turn, Satellite satellite,
            TreeMap<Integer, Location> locationsByLongitude) {
        List<Location> locationsInRange = new ArrayList<>();
        int minLongitude = satellite.getMinAcceptableLongitude();
        int maxLongitude = satellite.getMaxAcceptableLongitude();
        Map<Integer, Location> acceptableLocations = locationsByLongitude.subMap(minLongitude, true, maxLongitude,
                true);

        for (Location location : acceptableLocations.values()) {
            if (location.pictureTaken) {
                continue;
            }
            if (satellite.canTakePictureOf(location, turn)) {
                locationsInRange.add(location);
            }
        }

        return locationsInRange;
    }

    private Location pickLocation(List<Location> locations, Satellite satellite) {

        // TODO refine the choice of picture among the takeable ones

        Function<Location, Long> collectionFullness = loc ->
                loc.parentCollection.numberOfPicturesLeftToTake() * 1000000 / loc.parentCollection.value;
        Comparator<Location> comparator = Comparator.comparing(collectionFullness);
        return locations.stream().sorted(comparator).findFirst().orElse(null);
    }

    private void log(int turn, String msg) {
        System.out.println(String.format("File %s\tturn %d\t%s", filename, turn, msg));
    }
}
