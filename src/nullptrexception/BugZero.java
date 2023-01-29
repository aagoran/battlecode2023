package nullptrexception;

import battlecode.common.*;

public class BugZero {
    // Basic bug nav - Bug 0

    static Direction currentDirection = null;

    static void moveTowards(RobotController rc, MapLocation target) throws GameActionException {
        if (rc.getLocation().equals(target)) {
            return;
        }
        if (!rc.isMovementReady()) {
            return;
        }
        Direction d = rc.getLocation().directionTo(target);
        MapInfo info = rc.senseMapInfo(rc.getLocation().add(d));

        boolean against = info.getMapLocation().add(info.getCurrentDirection()).equals(rc.getLocation());

        if (rc.canMove(d) && !against) {
            rc.move(d);
            currentDirection = null; // there is no obstacle we're going around
        } else {
            // Going around some obstacle: can't move towards d because there's an obstacle there
            // Idea: keep the obstacle on our right hand

            if (currentDirection == null) {
                currentDirection = d;
            }
            // Try to move in a way that keeps the obstacle on our right
            for (int i = 0; i < 8; i++) {
                try {  // make sure robot doesn't try to go off the map
                    info = rc.senseMapInfo(rc.getLocation().add(currentDirection));

                    against = info.getMapLocation().add(info.getCurrentDirection()).equals(rc.getLocation());

                    if (rc.canMove(currentDirection) && !against) {
                        rc.move(currentDirection);
                        currentDirection = currentDirection.rotateRight();
                        break;
                    } else {
                        currentDirection = currentDirection.rotateLeft();
                    }
                } catch(Exception ignored) {}
            }
        }
    }
}