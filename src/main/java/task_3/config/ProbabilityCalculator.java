package task_3.config;

import java.util.Arrays;

class ProbabilityCalculator {

    public static double calcUserWinProbability(String user1, String user2) {
        int[] userDice = Arrays.stream(user1.split(",")).mapToInt(Integer::parseInt).toArray();
        int[] oppDice = Arrays.stream(user2.split(",")).mapToInt(Integer::parseInt).toArray();

        int n = userDice.length;
        int totalCombinations = (n - 1) * (n - 1);

        double[] p = new double[n];

        for (int a = 1; a < n; a++) {
            for (int b = 1; b < n; b++) {
                int r = (a + b) % n;
                p[r] += 1;
            }
        }

        for (int i = 0; i < n; i++) {
            p[i] /= totalCombinations;
        }

        double winProb = 0.0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (userDice[i] > oppDice[j]) {
                    winProb += p[i] * p[j];
                }
            }
        }

        return Math.round(winProb * 100.0) / 100.0;
    }
}
