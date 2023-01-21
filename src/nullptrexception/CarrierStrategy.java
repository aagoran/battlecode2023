package nullptrexception;

import battlecode.common.*;

public class CarrierStrategy {

    static MapLocation headquartersLocation;
    static MapLocation wellLocation;
    static MapLocation islandLocation;

    /**
     * Run a single turn for a Carrier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runCarrier(RobotController rc) throws GameActionException {
        // Scan for headquarters
        if (headquartersLocation == null) {
            scanHeadquarters(rc);
        }

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
                    RobotPlayer.moveTowards(rc, wellLocation);
                }

            }

            else {
                RobotPlayer.moveRandom(rc);
            }
        }

        if (total == GameConstants.CARRIER_CAPACITY) {
            RobotPlayer.moveTowards(rc, headquartersLocation);
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

    static void scanWells(RobotController rc) throws GameActionException {
        WellInfo[] wells = rc.senseNearbyWells();
        if (wells.length > 0) wellLocation = wells[0].getMapLocation();
    }

    static void depositResource(RobotController rc, ResourceType type) throws GameActionException {
        int amount = rc.getResourceAmount(type);

        if (amount > 0) {
            if (rc.canTransferResource(headquartersLocation, type, amount)) rc.transferResource(headquartersLocation, type, amount);
        }
    }

    static int getTotalResources(RobotController rc) throws GameActionException {
        return rc.getResourceAmount(ResourceType.ADAMANTIUM) + rc.getResourceAmount(ResourceType.MANA);
    }
}
