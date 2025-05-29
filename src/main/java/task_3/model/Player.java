package task_3.model;

public class Player {

    private final String name;
    private Dice dice;

    public Player(String name) {
        this.name = name;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public Dice getDice() {
        return dice;
    }

    public String getName() {
        return name;
    }

    public int getRollResult(int userValue, int computerValue) {
        int index = (userValue + computerValue) % dice.getLength();
        return dice.roll(index);
    }
}
