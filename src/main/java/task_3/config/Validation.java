package task_3.config;

public class Validation {
    public static void validateArgs(String[] args) {
        if (args.length <= 2) {
            throw new IllegalArgumentException("Error! The number of dices must be more than two!");
        }

        for (String dice : args) {
            for (String num : dice.split(",")) {
                if (!num.matches("-?\\d+")) {
                    throw new IllegalArgumentException("Error! Invalid number: " + num);
                }
            }
        }
    }
}
