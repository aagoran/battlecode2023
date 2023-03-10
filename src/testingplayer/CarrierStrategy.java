package testingplayer;

import battlecode.common.*;

public class CarrierStrategy {

    static MapLocation headquartersLocation;
    static MapLocation wellLocation;
    static MapLocation islandLocation;
    static ResourceType resource;

    /**
     * Run a single turn for a Carrier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runCarrier(RobotController rc) throws GameActionException {
        rc.setIndicatorString("Well Location " + wellLocation  + " " + rc.readSharedArray(0) + " " + rc.readSharedArray(1) + " " + rc.readSharedArray(11) + ", "+ rc.readSharedArray(10) + ", "+ rc.readSharedArray(9) + ", "+ rc.readSharedArray(8) + ", "+ rc.readSharedArray(15) + ", "+ rc.readSharedArray(14) + ", "+ rc.readSharedArray(13) + ", "+ rc.readSharedArray(12));

    if (rc.getHealth() < 40 && rc.getWeight() > 0)
    {
        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
		RobotInfo target = null;
        int lowestHealth = Integer.MAX_VALUE;
		// find and store lowest health enemy
		if (enemies.length > 0) {
            for (RobotInfo enemy: enemies) {
                if (enemy.getHealth() < lowestHealth && enemy.getType() != RobotType.HEADQUARTERS) {
                    lowestHealth = enemy.getHealth();
                    target = enemy;
                }
            }
            // Attack lowest-health enemy if possible
            if (target != null && rc.canAttack(target.getLocation())) {
                rc.attack(target.getLocation());
            }
        }
    }

    if (headquartersLocation == null) {
        scanHeadquarters(rc);
    }

    // check if hq has an anchor
    if (rc.canTakeAnchor(headquartersLocation, Anchor.STANDARD)) {
        rc.takeAnchor(headquartersLocation, Anchor.STANDARD);
    }

    if (rc.getAnchor() != null) {
        AnchorCarriers.runAnchorCarriers(rc);
    }

    else {
        // Scan for wells
        if (wellLocation == null) {
            scanWells(rc);
        }

        // Collect resource from well if possible
        if (wellLocation != null && rc.canCollectResource(wellLocation, -1)) {
            rc.collectResource(wellLocation, -1);
        }

        // Transfer resource to headquarters
        depositResource(rc, ResourceType.ADAMANTIUM);
        depositResource(rc, ResourceType.MANA);

        int total = getTotalResources(rc);

        if (total == 0)
        {
            if (wellLocation != null) {
                MapLocation me = rc.getLocation();
                Direction dir = me.directionTo(wellLocation);

                if (!rc.canCollectResource(me, 1)) {
                    BugZero.moveTowards(rc, wellLocation);
                }

            }

            else {
                RobotPlayer.moveRandom(rc);
            }
        }

        if (total == GameConstants.CARRIER_CAPACITY) {
            BugZero.moveTowards(rc, headquartersLocation);
        }
    }
}

    static void depositResource(RobotController rc, ResourceType type) throws GameActionException {
        int amount = rc.getResourceAmount(type);

        if (wellLocation != null && resource != null) {
            Communication.updateWellLocation(rc, resource, wellLocation);
        }

        if (amount > 0) {
            if (rc.canTransferResource(headquartersLocation, type, amount)) rc.transferResource(headquartersLocation, type, amount);

        }
    }

    static int getTotalResources(RobotController rc) throws GameActionException {
        return rc.getResourceAmount(ResourceType.ADAMANTIUM) + rc.getResourceAmount(ResourceType.MANA);
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

    static void scanWells(RobotController rc) throws GameActionException {
        WellInfo[] wells = rc.senseNearbyWells();
        if (wells.length > 0) {
            wellLocation = wells[0].getMapLocation();
            resource = wells[0].getResourceType();
        }

    }

}
