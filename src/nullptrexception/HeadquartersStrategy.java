package nullptrexception;

import battlecode.common.*;

public class HeadquartersStrategy {
    /**
     * Run a single turn for a Headquarters.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runHeadquarters(RobotController rc) throws GameActionException {
        // Write the location of a team headquarter on the first round.
        if (rc.getRoundNum() == 1) {
            Communication.addHeadquarter(rc);
        }

        // Pick a direction to build in.
        Direction dir = RobotPlayer.directions[RobotPlayer.rng.nextInt(RobotPlayer.directions.length)];
        MapLocation newLoc = rc.getLocation().add(dir);

        // Try to build an anchor.
        rc.setIndicatorString("Trying to build an anchor");
        if (rc.canBuildAnchor(Anchor.STANDARD) && rc.getResourceAmount(ResourceType.ADAMANTIUM) > 1000) {
            rc.buildAnchor(Anchor.STANDARD);
        }

        if (RobotPlayer.rng.nextBoolean()) {
            // Try to build a carrier.
            rc.setIndicatorString("Trying to build a carrier");
            if (rc.canBuildRobot(RobotType.CARRIER, newLoc)) {
                rc.buildRobot(RobotType.CARRIER, newLoc);
            }
        }

        else {
            // Try to build a launcher.
            rc.setIndicatorString("Trying to build a launcher");
            if (rc.canBuildRobot(RobotType.LAUNCHER, newLoc)) {
                rc.buildRobot(RobotType.LAUNCHER, newLoc);
            }
        }
    }
}
