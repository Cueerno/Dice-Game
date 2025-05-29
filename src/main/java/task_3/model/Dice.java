package task_3.model;

import java.util.Arrays;

public class Dice {
    private final int[] faces;

    public Dice(String diceString) {
        this.faces = Arrays.stream(diceString.split(",")).mapToInt(Integer::parseInt).toArray();
    }

    public int roll(int index) {
        return faces[index % faces.length];
    }

    public int getLength() {
        return faces.length;
    }

    public int[] getFaces() {
        return faces;
    }

    @Override
    public String toString() {
        return Arrays.toString(faces);
    }
}
