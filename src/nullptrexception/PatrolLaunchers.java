package nullptrexception;

import battlecode.common.*;
import java.util.ArrayList;

public class PatrolLaunchers {

    static MapLocation headquartersLocation;
    static MapLocation wellLocation;
    static boolean towardsHeadquarters = false;

    static void runPatrolLauncher(RobotController rc) throws GameActionException {
        if (headquartersLocation == null) {
            Communication.scanHeadquarters(rc, headquartersLocation);
        }
        if (wellLocation == null) {
            int wellType = RobotPlayer.rng.nextInt(2);
            ArrayList<MapLocation> wells = new ArrayList<>();
            if (wellType == 0) {
                for (int i = 8; i < 12; i++) {
                    int val = rc.readSharedArray(i);
                    if (val > 0) {
                        MapLocation loc = Communication.intToLocation(rc, val);
                        wells.add(loc);
                    }
                }
            }
            else if (wellType == 1) {
                for (int i = 12; i < 16; i++) {
                    int val = rc.readSharedArray(i);
                    if (val > 0) {
                        MapLocation loc = Communication.intToLocation(rc, val);
                        wells.add(loc);
                    }
                }
            }
            int random = RobotPlayer.rng.nextInt(wells.size());
            wellLocation = wells.get(random);
        }
        if (!towardsHeadquarters && wellLocation != null) {
            BugZero.moveTowards(rc, wellLocation);
            if (rc.canSenseLocation(wellLocation)) {
                towardsHeadquarters = true;
            }
        }
        else if (towardsHeadquarters) {
            BugZero.moveTowards(rc, headquartersLocation);
            if (rc.canSenseLocation(headquartersLocation)) {
                towardsHeadquarters = false;
            }
        }
        else {
            RobotPlayer.moveRandom(rc);
        }
    }

}