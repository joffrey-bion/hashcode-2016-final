package org.ohutouch.hashcode;

import java.util.List;

public class InputReader {

    private static final int N_TURNS_LINE = 0;
    private static final int N_SATELLITES_LINE = 1;
    private static final int FIRST_SAT_LINE = 2;

    public static Simulation read(List<String> lines) {
        try {
            int nTurns = Integer.parseInt(lines.get(N_TURNS_LINE));
            int nSatellites = Integer.parseInt(lines.get(N_SATELLITES_LINE));
            final int IMAGE_COUNT_LINE = N_SATELLITES_LINE + nSatellites + 1;

            int nImageCollections = Integer.parseInt(lines.get(IMAGE_COUNT_LINE));
            final int FIRST_IMG_COLLECTION_LINE = IMAGE_COUNT_LINE + 1;

            Simulation simulation = new Simulation(nTurns, nSatellites, nImageCollections);

            for (int s = 0; s < nSatellites; s++) {
                simulation.satellites[s] = parseSatellite(lines.get(FIRST_SAT_LINE + s).split(" "));
            }

            int collectionLineNum = FIRST_IMG_COLLECTION_LINE;
            for (int col = 0; col < nImageCollections; col++) {
                String[] collectionLine = lines.get(collectionLineNum).split(" ");
                int value = Integer.parseInt(collectionLine[0]);
                int nLocations = Integer.parseInt(collectionLine[1]);
                int nRanges = Integer.parseInt(collectionLine[2]);
                simulation.collections[col] = new ImageCollection(value, nLocations, nRanges);

                for (int loc = 0; loc < nLocations; loc++) {
                    String[] locationLine = lines.get(collectionLineNum + 1 + loc).split(" ");
                    simulation.collections[col].locations[loc][Simulation.LATITUDE] = Integer.parseInt(locationLine[0]);
                    simulation.collections[col].locations[loc][Simulation.LONGITUDE] = Integer.parseInt(locationLine[1]);
                }

                for (int rg = 0; rg < nRanges; rg++) {
                    String[] rangeLine = lines.get(collectionLineNum + 1 + nLocations + rg).split(" ");
                    simulation.collections[col].ranges[rg][0] = Integer.parseInt(rangeLine[0]);
                    simulation.collections[col].ranges[rg][1] = Integer.parseInt(rangeLine[1]);
                }
                collectionLineNum += nLocations + nRanges + 1;
            }

            return simulation;
        } catch (Exception e) {
            throw new RuntimeException("Wrong input format", e);
        }
    }

    private static Satellite parseSatellite(String[] line) {
        int latitude = Integer.parseInt(line[0]);
        int longitude = Integer.parseInt(line[1]);
        int v0 = Integer.parseInt(line[2]);
        int maxOrientationChangePerTurn = Integer.parseInt(line[3]);
        int maxOrientationValue = Integer.parseInt(line[4]);
        return new Satellite(latitude, longitude, v0, maxOrientationChangePerTurn, maxOrientationValue);
    }
}
