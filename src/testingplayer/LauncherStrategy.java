package testingplayer;

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
        int random = RobotPlayer.rng.nextInt(10);
        LauncherType type = null;
        if (random < 7) {
            type = LauncherType.ATTACK;
        }
        else if (random >= 7) {
            type = LauncherType.PATROL;
        }
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
        MapLocation[] clouds = rc.senseNearbyCloudLocations();
        if (clouds.length > 0) {
            int randCloud = RobotPlayer.rng.nextInt(clouds.length);
            if (rc.canAttack(clouds[randCloud])) {
                rc.attack(clouds[randCloud]);
            }
        }
        switch (type) {
            case PATROL:
                PatrolLaunchers.runPatrolLauncher(rc);
                break;
            case ATTACK:
                AttackLaunchers.runAttackLauncher(rc);
                break;
        }
    }
}
