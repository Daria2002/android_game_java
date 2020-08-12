package suza.project.crazyballs.state;

import suza.project.crazyballs.util.Util;

public class LevelConfig {

        public int good_ball_score = 10;
        public int bad_ball_score = -5;
        private static int threshold = 2;

        // randomize spawn period
        public int getNormalBallSpawnPeriod() {
                return (int) (Util.randomInteger(normalBallSpawnPeriod - threshold, normalBallSpawnPeriod + threshold) * 1000 * multiplier);
        }

        public int getLifeBallSpawnPeriod() {
                return (int) (Util.randomInteger(lifeBallSpawnPeriod - threshold, lifeBallSpawnPeriod + threshold) * 1000 * multiplier);
        }

        public int getBadBallSpawnPeriod() {
                return (int) (Util.randomInteger(badBallSpawnPeriod - threshold, badBallSpawnPeriod + threshold) * 1000 * multiplier);
        }

        public int getStarSpawnPeriod() {
                return (int) (Util.randomInteger(starSpawnPeriod - threshold, starSpawnPeriod + threshold) * 1000 * multiplier);
        }

        public int getLifeSaverSpawnPeriod() {
                return (int) (Util.randomInteger(lifeSaverSpawnPeriod - threshold, lifeSaverSpawnPeriod + threshold) * 1000 * multiplier);
        }

        private int normalBallSpawnPeriod = 5; // s
        private int lifeBallSpawnPeriod = 20; // s
        private int starSpawnPeriod = 20; // s
        private int lifeSaverSpawnPeriod = 20; // s
        private int badBallSpawnPeriod = 5;  // s

        private double multiplier;

        LevelConfig(double multiplier) {
                this.multiplier = multiplier;
                good_ball_score /= multiplier;
        }

        public static LevelConfig generate_hard() {
                return new LevelConfig(0.7);
        }

        public static LevelConfig generate_medium() {
                return new LevelConfig(1);
        }

        public static LevelConfig generate_easy() {
                return new LevelConfig(1.3);
        }
}
