package suza.project.crazyballs.state;

import suza.project.crazyballs.R;
import suza.project.crazyballs.util.Util;

public class LevelConfig {
        public int getNormalBallSpawnPeriod() {
                return normalBallSpawnPeriod;
        }

        public int getLifeBallSpawnPeriod() {
                return lifeBallSpawnPeriod;
        }

        public int getBadBallSpawnPeriod() {
                return badBallSpawnPeriod;
        }

        private int normalBallSpawnPeriod = 2000; //ms
        private int lifeBallSpawnPeriod = 10000; // ms
        private int badBallSpawnPeriod = 5000;  // ms

        private double multiplier;

        LevelConfig(double multiplier) {
                this.multiplier = multiplier;
                randomizeNormalPeriod();
                randomizeBadPeriod();
                randomizeLifePeriod();
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

        public void randomizeNormalPeriod() {
                normalBallSpawnPeriod = (int) (Util.randomInteger(3, 5) * 1000 * multiplier);
        }

        public void randomizeBadPeriod() {
                badBallSpawnPeriod = (int) (Util.randomInteger(7, 9) * 1000 * multiplier);
        }

        public void randomizeLifePeriod() {
                lifeBallSpawnPeriod = (int) (Util.randomInteger(7, 12) * 1000 * multiplier);
        }
}
