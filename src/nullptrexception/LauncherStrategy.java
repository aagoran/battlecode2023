package nullptrexception;

import battlecode.common.*;

public class LauncherStrategy {
    
    enum LauncherType {
        ATTACK,
        PATROL,
        ISLAND
    }
    
    /**
     * Run a single turn for a Launcher.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runLauncher(RobotController rc) throws GameActionException {
        // starting with all Launchers as Patrol for now
        LauncherType type = LauncherType.PATROL;
        int radius = rc.getType().actionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);
		RobotInfo target = null;
        int lowestHealth = Integer.MAX_VALUE;
		// find and store lowest health enemy
		if (enemies.length > 0) {
            for (RobotInfo enemy: enemies) {
                if (enemy.getHealth() < lowestHealth) {
                    lowestHealth = enemy.getHealth();
                    target = enemy;
                }
            }
            // Attack lowest-health enemy if possible
            if (rc.canAttack(target.getLocation())) {
                rc.attack(target.getLocation());
            }
        }
        switch (type) {
            case PATROL:
                PatrolLaunchers.runPatrolLauncher(rc);
                break; 
        }
    }
}
