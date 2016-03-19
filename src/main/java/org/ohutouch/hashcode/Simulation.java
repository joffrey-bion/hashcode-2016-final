package org.ohutouch.hashcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Simulation {

    private final String filename;

    public static final int LATITUDE = 0;

    public static final int LONGITUDE = 1;

    public final int nTurns;

    public final ImageCollection[] collections;

    public final Satellite[] satellites;

    public final List<Picture> picturesTaken = new ArrayList<>();

    public Simulation(String filename, int nTurns, int nSatellites, int nImages) {
        this.filename = filename;
        this.nTurns = nTurns;
        this.collections = new ImageCollection[nImages];
        this.satellites = new Satellite[nSatellites];
    }

    public List<Picture> run() {
        picturesTaken.clear();
        int turnsToSimulate = Math.min(nTurns, 2000); // max number of simulated turns for faster runs
        for (int turn = 0; turn < nTurns; turn++) {
            if (turn % 10000 == 0) {
                System.out.println("Simulating turn " + turn + "\tfor file " + filename);
            }
            takePictures(turn);
            moveSatellites();
        }
        return picturesTaken;
    }

    private void moveSatellites() {
        Arrays.stream(satellites).forEach(Satellite::move);
    }

    private void takePictures(int turn) {
        TreeMap<Integer, Location> locationsByLongitude = new TreeMap<>();
        Arrays.stream(collections)
                .filter(col -> col.canBeShotAt(turn))
                .flatMap(col -> Arrays.stream(col.locations))
                .forEach(loc -> locationsByLongitude.put(loc.getLongitude(), loc));
        //        log(turn, locationsByLongitude.size() + " sorted locations");
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
        } else {
            //            log(turn, "no location in range for satellite " + sat);
        }
    }

    private List<Location> findTakeableLocations(int turn, Satellite satellite,
            TreeMap<Integer, Location> locationsByLongitude) {
        List<Location> locationsInRange = new ArrayList<>();
        int minLongitude = satellite.getMinAcceptableLongitude();
        int maxLongitude = satellite.getMaxAcceptableLongitude();
        Map<Integer, Location> acceptableLocations = locationsByLongitude.subMap(minLongitude, true, maxLongitude,
                true);
        //        log(turn, acceptableLocations.size() + " acceptable locations");

        for (Location location : acceptableLocations.values()) {
            if (location.pictureTaken) {
                //                log(turn, "picture already taken for this location");
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
