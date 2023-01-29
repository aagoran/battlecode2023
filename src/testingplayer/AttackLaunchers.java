package testingplayer;

import battlecode.common.*;
import java.util.ArrayList;

public class AttackLaunchers {

    static MapLocation headquartersLocation;
    static MapLocation targetLocation;
    static boolean randomMovement = false;
    // static boolean waitForGroup = true;

    static void runAttackLauncher(RobotController rc) throws GameActionException {
        if (headquartersLocation == null) {
            scanHeadquarters(rc);
            if (headquartersLocation != null) {
                //System.out.print("HQ Location Not Stored Somehow");
                targetLocation = Communication.getRotatedLocation(rc, headquartersLocation);
                // int random = RobotPlayer.rng.nextInt(4);
                // switch (random) {
                //     case 0:
                //         targetLocation = Communication.getHFlipLocation(rc, headquartersLocation);
                //         break;
                //     case 1:
                //         targetLocation = Communication.getVFlipLocation(rc, headquartersLocation);
                //         break;
                //     case 2:
                //     case 3:
                //         targetLocation = Communication.getRotatedLocation(rc, headquartersLocation);
                //         break;
                // }
            }
        }
        // RobotInfo[] robots = rc.senseNearbyRobots();
        // Team myTeam = rc.getTeam();
        // int count = 0;
        // for (RobotInfo r: robots) {
        //     if (r.getType() == RobotType.LAUNCHER && r.getTeam() == myTeam) {
        //         count++;
        //     }
        //     if (count == 10) {
        //         waitForGroup = true;
        //         break;
        //     }
        // }
        // if (!randomMovement && targetLocation != null && !waitForGroup) {
        if (!randomMovement && targetLocation != null) {
            if (!rc.canSenseLocation(targetLocation)) {
                BugZero.moveTowards(rc, targetLocation);
            }
            // if there isn't an HQ at targetLocation, switch to random movement
            else if (!rc.canSenseRobotAtLocation(targetLocation)) {
                // setting boolean to true makes sense but setting this to false works better
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