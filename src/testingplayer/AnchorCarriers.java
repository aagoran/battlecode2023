package testingplayer;

import battlecode.common.*;

public class AnchorCarriers {

    static MapLocation islandLocation;
    static int islandIndex;

    static void runAnchorCarriers(RobotController rc) throws GameActionException {
        if (islandLocation == null) {
            scanIslands(rc);
        }
        if (islandLocation != null && rc.getLocation().equals(islandLocation)) {
            if (rc.canPlaceAnchor() && rc.senseTeamOccupyingIsland(islandIndex) != rc.getTeam()) {
                rc.placeAnchor();
            }
        }
        else if (islandLocation != null) {
            BugZero.moveTowards(rc, islandLocation);
        }
        else {
            RobotPlayer.moveRandom(rc);
        }
    }

    static void scanIslands(RobotController rc) throws GameActionException {
        int[] islandIndices = rc.senseNearbyIslands();
        if (islandIndices.length > 0) {
            MapLocation[] islands = rc.senseNearbyIslandLocations(-1, islandIndices[0]);
            if (islands.length > 0) {
                if (rc.senseTeamOccupyingIsland(islandIndices[0]) != rc.getTeam()) {
                    islandLocation = islands[0];
                    islandIndex = islandIndices[0];
                }
            }
        }
    }

}