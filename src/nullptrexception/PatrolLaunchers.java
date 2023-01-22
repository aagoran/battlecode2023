package nullptrexception;

import battlecode.common.*;
import java.util.ArrayList;

public class PatrolLaunchers {

    static MapLocation headquartersLocation;
    static MapLocation wellLocation;
    static boolean towardsHeadquarters = false;

    static void runPatrolLauncher(RobotController rc) throws GameActionException {
        if (headquartersLocation == null) {
            scanHeadquarters(rc);
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
            if (wells.size() > 0) {
                int random = RobotPlayer.rng.nextInt(wells.size());
                wellLocation = wells.get(random);
            }
        }
        if (!towardsHeadquarters && wellLocation != null) {
            BugZero.moveTowards(rc, wellLocation);
            rc.setIndicatorString("Destination: " + Communication.locationToInt(rc, wellLocation));
            if (rc.canSenseLocation(wellLocation)) {
                towardsHeadquarters = true;
            }
        }
        else if (towardsHeadquarters) {
            BugZero.moveTowards(rc, headquartersLocation);
            rc.setIndicatorString("Destination: " + Communication.locationToInt(rc, headquartersLocation));
            if (rc.canSenseLocation(headquartersLocation)) {
                towardsHeadquarters = false;
            }
        }
        else {
            RobotPlayer.moveRandom(rc);
        }
    }

    static void scanHeadquarters(RobotController rc) throws GameActionException {
        RobotInfo[] robots = rc.senseNearbyRobots();
        for (RobotInfo robot : robots) {
            if (robot.getTeam() == rc.getTeam() && robot.getType() == RobotType.HEADQUARTERS) {
                headquartersLocation = robot.getLocation();
                break;
            }
        }
    }

}