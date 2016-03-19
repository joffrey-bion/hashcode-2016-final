package org.ohutouch.hashcode;

import org.junit.Assert;
import org.junit.Test;

import static org.ohutouch.hashcode.Simulation.*;

public class CoordsTest {

    @Test
    public void testMove() {
        int[] pos = new int[2];
        boolean invertedVelocity;

        pos[LATITUDE] = 1000;
        pos[LONGITUDE] = 2000;

        invertedVelocity = Coord.move(pos, 100, 150);
        Assert.assertEquals(1100, pos[LATITUDE]);
        Assert.assertEquals(2150, pos[LONGITUDE]);
        Assert.assertFalse(invertedVelocity);

        pos[LATITUDE] = 1000;
        pos[LONGITUDE] = 2000;

        invertedVelocity = Coord.move(pos, -100, -150);
        Assert.assertEquals(900, pos[LATITUDE]);
        Assert.assertEquals(1850, pos[LONGITUDE]);
        Assert.assertFalse(invertedVelocity);

        pos[LATITUDE] = 1000;
        pos[LONGITUDE] = 179 * 3600;

        invertedVelocity = Coord.move(pos, 0, 3600);
        Assert.assertEquals(1000, pos[LATITUDE]);
        Assert.assertEquals(-180 * 3600, pos[LONGITUDE]);
        Assert.assertFalse(invertedVelocity);

        invertedVelocity = Coord.move(pos, 0, 3600);
        Assert.assertEquals(1000, pos[LATITUDE]);
        Assert.assertEquals(-179 * 3600, pos[LONGITUDE]);
        Assert.assertFalse(invertedVelocity);
    }

    @Test
    public void testMoveAtPole() {
        int[] pos = new int[2];
        boolean invertedVelocity;

        pos[LATITUDE] = 89 * 3600;
        pos[LONGITUDE] = 10 * 3600;

        invertedVelocity = Coord.move(pos, 2 * 3600, 0);
        Assert.assertEquals(89 * 3600, pos[LATITUDE]);
        Assert.assertEquals(-170 * 3600, pos[LONGITUDE]);
        Assert.assertTrue(invertedVelocity);
    }

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
