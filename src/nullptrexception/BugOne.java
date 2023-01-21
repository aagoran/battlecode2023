package nullptrexception;

import battlecode.common.*;

public class BugOne {
    static Direction currentDirection = null;
    static boolean obstacle = false;
    static MapLocation orig = null;  // original location in which obstacle was identified
    static MapLocation closest = null;
    static int closestDist = Integer.MAX_VALUE;
    static boolean identified = false;

    static void moveTowards(RobotController rc, MapLocation target) throws GameActionException {
        if (rc.getLocation().equals(target)) {
            return;
        }
        if (!rc.isMovementReady()) {
            return;
        }
        Direction d = rc.getLocation().directionTo(target);

        if (!obstacle && !rc.canMove(d)) {
            if (rc.senseRobotAtLocation(rc.adjacentLocation(d)) != null) {
                return;
            }
            orig = rc.getLocation();
            if (currentDirection == null) {
                currentDirection = d;
            }
            obstacle = true;
        }
        if (obstacle) {
            // Try to move in a way that keeps the obstacle on our right
            for (int i = 0; i < 8; i++) {
                if (rc.canMove(currentDirection)) {
                    rc.move(currentDirection);
                    currentDirection = currentDirection.rotateRight();
                    break;
                } else {
                    currentDirection = currentDirection.rotateLeft();
                }
            }
            // update min dist if necessary
            if (!identified) {
                int dist = rc.getLocation().distanceSquaredTo(target);
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = rc.getLocation();
                }
            }

            // check for if next stage should occur
            if (identified) {
                if (rc.getLocation().equals(closest)) {
                    obstacle = false;
                    closest = null;
                    closestDist = Integer.MAX_VALUE;
                    identified = false;
                }
            } else {
                if (rc.getLocation().equals(orig)) {
                    identified = true;
                }
            }
        } else {
            rc.move(d);
            currentDirection = null;
        }
    }
}
