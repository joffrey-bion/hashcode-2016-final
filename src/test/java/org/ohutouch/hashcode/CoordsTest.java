package org.ohutouch.hashcode;

import org.junit.Assert;
import org.junit.Test;

import static org.ohutouch.hashcode.Simulation.*;

public class CoordsTest {

    @Test
    public void testDifference() {
        int[] pos1 = new int[2];
        int[] pos2 = new int[2];
        int[] diff;

        pos1[LATITUDE] = 33;
        pos2[LATITUDE] = 53;

        pos1[LONGITUDE] = 100;
        pos2[LONGITUDE] = 150;

        diff = Coord.difference(pos1, pos2);
        Assert.assertEquals(-20, diff[LATITUDE]);
        Assert.assertEquals(-50, diff[LONGITUDE]);

        diff = Coord.difference(pos2, pos1);
        Assert.assertEquals(20, diff[LATITUDE]);
        Assert.assertEquals(50, diff[LONGITUDE]);

        pos1[LATITUDE] = 50 * 3600;
        pos2[LATITUDE] = 75 * 3600;

        pos1[LONGITUDE] = 0;
        pos2[LONGITUDE] = 180 * 3600;

        diff = Coord.difference(pos1, pos2);
        Assert.assertEquals(-25 * 3600, diff[LATITUDE]);
        Assert.assertEquals(-180 * 3600, diff[LONGITUDE]);

        diff = Coord.difference(pos2, pos1);
        Assert.assertEquals(25 * 3600, diff[LATITUDE]);
        Assert.assertEquals(-180 * 3600, diff[LONGITUDE]);

        pos1[LATITUDE] = 50 * 3600;
        pos2[LATITUDE] = 50 * 3600;

        pos1[LONGITUDE] = -179 * 3600;
        pos2[LONGITUDE] = 179 * 3600;

        diff = Coord.difference(pos1, pos2);
        Assert.assertEquals(0, diff[LATITUDE]);
        Assert.assertEquals(2 * 3600, diff[LONGITUDE]);

        diff = Coord.difference(pos2, pos1);
        Assert.assertEquals(0, diff[LATITUDE]);
        Assert.assertEquals(-2 * 3600, diff[LONGITUDE]);
    }

    @Test
    public void testDistance() {
        int[] pos1 = new int[2];
        int[] pos2 = new int[2];
        int[] diff;

        pos1[LATITUDE] = 33;
        pos2[LATITUDE] = 53;

        pos1[LONGITUDE] = 100;
        pos2[LONGITUDE] = 150;

        diff = Coord.distance(pos1, pos2);
        Assert.assertEquals(20, diff[LATITUDE]);
        Assert.assertEquals(50, diff[LONGITUDE]);

        diff = Coord.distance(pos2, pos1);
        Assert.assertEquals(20, diff[LATITUDE]);
        Assert.assertEquals(50, diff[LONGITUDE]);

        pos1[LATITUDE] = 50 * 3600;
        pos2[LATITUDE] = 75 * 3600;

        pos1[LONGITUDE] = 0;
        pos2[LONGITUDE] = 180 * 3600;

        diff = Coord.distance(pos1, pos2);
        Assert.assertEquals(25 * 3600, diff[LATITUDE]);
        Assert.assertEquals(180 * 3600, diff[LONGITUDE]);

        diff = Coord.distance(pos2, pos1);
        Assert.assertEquals(25 * 3600, diff[LATITUDE]);
        Assert.assertEquals(180 * 3600, diff[LONGITUDE]);

        pos1[LATITUDE] = 50 * 3600;
        pos2[LATITUDE] = 50 * 3600;

        pos1[LONGITUDE] = -179 * 3600;
        pos2[LONGITUDE] = 179 * 3600;

        diff = Coord.distance(pos1, pos2);
        Assert.assertEquals(0, diff[LATITUDE]);
        Assert.assertEquals(2 * 3600, diff[LONGITUDE]);

        diff = Coord.distance(pos2, pos1);
        Assert.assertEquals(0, diff[LATITUDE]);
        Assert.assertEquals(2 * 3600, diff[LONGITUDE]);
    }
}
