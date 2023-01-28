package testingplayer;

import battlecode.common.*;
import java.util.ArrayList;

public class AttackLaunchers {

    static MapLocation headquartersLocation;
    static MapLocation targetLocation;
    static boolean randomMovement = false;

    static void runAttackLauncher(RobotController rc) throws GameActionException {
        if (headquartersLocation == null) {
            scanHeadquarters(rc);
            if (headquartersLocation != null) {
                targetLocation = Communication.getRotatedLocation(rc, headquartersLocation);
            }
        }
        if (!randomMovement && targetLocation != null) {
            if (!rc.canSenseLocation(targetLocation)) {
                BugZero.moveTowards(rc, targetLocation);
            }
            // if there isn't an HQ at targetLocation, switch to random movement
            else if (!rc.canSenseRobotAtLocation(targetLocation)) {
                randomMovement = false;
            }
            // if there is an HQ at targetLocation, don't move because it is well placed
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