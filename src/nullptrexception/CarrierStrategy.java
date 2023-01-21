package nullptrexception;

import battlecode.common.*;

public class CarrierStrategy {

    static MapLocation headquartersLocation;
    static MapLocation wellLocation;
    static MapLocation islandLocation;
    static ResourceType resource;
    static boolean attemptedWellUpdate = false;

    /**
     * Run a single turn for a Carrier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runCarrier(RobotController rc) throws GameActionException {
        rc.setIndicatorString("well location: " + rc.readSharedArray(14));
        // Scan for headquarters
        if (headquartersLocation == null) {
            Communication.scanHeadquarters(rc, headquartersLocation);
        }

        // Scan for wells
        if (wellLocation == null) {
            Communication.scanWells(rc, wellLocation);
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

    static void depositResource(RobotController rc, ResourceType type) throws GameActionException {
        int amount = rc.getResourceAmount(type);
        if(!attemptedWellUpdate){ //if robot has not checked with shared array, check for update
            Communication.updateWellLocation(rc, resource, wellLocation);
            attemptedWellUpdate = true;
        }
        if (amount > 0) {
            if (rc.canTransferResource(headquartersLocation, type, amount)) rc.transferResource(headquartersLocation, type, amount);
        }
    }

    static int getTotalResources(RobotController rc) throws GameActionException {
        return rc.getResourceAmount(ResourceType.ADAMANTIUM) + rc.getResourceAmount(ResourceType.MANA);
    }


}
