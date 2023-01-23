package nullptrexception;

import battlecode.common.*;

public class BugTwo {

    static Direction currentDirection = null;
    static MapLocation curStart, curTarget;

    static void moveTowards(RobotController rc, MapLocation target) throws GameActionException {
        if (curTarget != target) {
            curTarget = target;
            curStart = rc.getLocation();
        }

        if (rc.getLocation().equals(target)) {
            return;
        }
        if (!rc.isMovementReady()) {
            return;
        }
        Direction d = rc.getLocation().directionTo(target);
        if (rc.canMove(d) && outside(rc.getLocation())) {
            rc.move(d);
        } else if (rc.canMove(d) && intersection(rc.getLocation())) {
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
                if (rc.canMove(currentDirection)) {
                    rc.move(currentDirection);
                    currentDirection = currentDirection.rotateRight();
                    break;
                } else {
                    currentDirection = currentDirection.rotateLeft();
                }
            }
        }
    }

    private static double calcY(float xval, double x0, double y0, double x1, double y1){
        if(x1 == x0) return Integer.MIN_VALUE;
        return y0 + (xval - x0)*(y1 - y0)/(x1 - x0);
    }

    private static double calcX(double yval, double x0, double y0, double x1, double y1) {
        if(x1 == x0) return Integer.MIN_VALUE;
        return x0 + (yval - y0)*(y1 - y0)/(x1 - x0);
    }

    private static boolean intersection(MapLocation loc) {
        int left = loc.x;
        int right = loc.x + 1;
        int bottom = loc.y;
        int top = loc.y + 1;

        double x0 = curStart.x + 0.5;
        double y0 = curStart.y + 0.5;
        double x1 = curTarget.x + 0.5;
        double y1 = curTarget.y + 0.5;

        int intersections = 0;
        if(calcX(bottom, x0, y0, x1, y1) < right && calcX(bottom, x0, y0, x1, y1) > left  ) intersections++;
        if(calcX(top   , x0, y0, x1, y1) < right && calcX(top   , x0, y0, x1, y1) > left  ) intersections++;
        if(calcY(left  , x0, y0, x1, y1) < top   && calcY(left  , x0, y0, x1, y1) > bottom) intersections++;
        if(calcY(right , x0, y0, x1, y1) < top   && calcY(right , x0, y0, x1, y1) > bottom) intersections++;
        return intersections > 0;
    }

    private static boolean outside(MapLocation loc) {
        int x = loc.x;
        int y = loc.y;

        if (x < curStart.x && x < curTarget.x) {
            return true;
        }
        if (x > curStart.x && x > curTarget.x) {
            return true;
        }
        if (y < curStart.y && y < curTarget.y) {
            return true;
        }
        if (y > curStart.y && y > curTarget.y) {
            return true;
        }
        return false;
    }
    
}
