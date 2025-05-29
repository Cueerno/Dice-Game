package task_3;

import task_3.config.HmacCalculator;
import task_3.config.SecureKeyGenerator;
import task_3.config.TableGenerator;
import task_3.model.Dice;
import task_3.model.Player;

import java.util.*;

import static task_3.config.HmacCalculator.bytesToHex;

public class GameEngine {

    private final static Scanner SCANNER = new Scanner(System.in);
    private final static int DELAY = 65;

    private final List<Dice> allDices;
    private final Player user;
    private final Player computer;

    public GameEngine(String[] args) {
        this.allDices = new ArrayList<>();
        for (String dice : args) {
            this.allDices.add(new Dice(dice));
        }
        this.user = new Player("You");
        this.computer = new Player("Computer");
    }


    public void start() throws InterruptedException {
        boolean userStarts = determineOrderAndAssignDice();
        playRounds(userStarts);
    }


    private boolean determineOrderAndAssignDice() throws InterruptedException {
        int range = 2;
        int computerSelection = SecureKeyGenerator.generateSecureRandomInt(range);

        byte[] key = SecureKeyGenerator.generate256BitKey();
        String hmac = HmacCalculator.computeHMAC(key, computerSelection);

        printTypewriter(String.format("""
                Let's determine who makes the first move.
                %s selected a random value in the range 0..1
                (HMAC=%s)
                Try to guess my selection.
                0 - 0
                1 - 1
                X - exit
                """, computer.getName(), hmac));
        String userSelection = userInput(user.getName() + " selection: ");

        while (true) {
            switch (userSelection) {
                case "0", "1" -> {
                    printTypewriter(String.format(computer.getName() + " selection: " + computerSelection + "\n" + "(KEY=%s)", bytesToHex(key)) + "\n");

                    boolean userStarts = userSelection.equals(String.valueOf(computerSelection));

                    List<Dice> dices = new ArrayList<>(allDices);

                    if (userStarts) {
                        printTypewriter(user.getName() + " make the first move.\n");

                        user.setDice(chooseDice(dices));
                        dices.remove(user.getDice());

                        computer.setDice(selectRandomDice(dices));

                        printTypewriter(computer.getName() + " choose: " + computer.getDice() + "\n");
                    } else {
                        computer.setDice(selectRandomDice(dices));
                        dices.remove(computer.getDice());

                        printTypewriter(computer.getName() + " make the first move and choose: " + computer.getDice() + "\n");

                        user.setDice(chooseDice(dices));
                    }
                    return userStarts;
                }
                case "X" -> exit();
                case "?" -> help();
                default -> {
                    printTypewriter("Invalid input. Try again.\n");
                    userSelection = userInput("Your selection: ");
                }
            }
        }
    }

    private void playRounds(boolean userStarts) throws InterruptedException {
        int userResult;
        int computerResult;
        if (userStarts) {
            userResult = playSingleRound(user.getName() , user, computer);
            computerResult = playSingleRound(computer.getName(), computer, user);
        }
        else {
            computerResult = playSingleRound("Computer", computer, user);
            userResult = playSingleRound("User", user, computer);
        }

        determineWinner(computerResult, userResult);
    }

    private int playSingleRound(String current, Player p1, Player p2) throws InterruptedException {
        int range = p2.getDice().getLength();
        int computerIndex = SecureKeyGenerator.generateSecureRandomInt(range);
        byte[] key = SecureKeyGenerator.generate256BitKey();
        String hmac = HmacCalculator.computeHMAC(key, computerIndex);

        printTypewriter(String.format("""
                It's time for %s roll.
                %s selected a random value in the range 0..%d
                (HMAC=%s)
                """, current, computer.getName(), p1.getDice().getLength() - 1, hmac));

        int userIndex = selectDiceIndex(p1.getDice().getLength(), user.getName() + " selection: ");

        printTypewriter(String.format(computer.getName() + " number is: " + computerIndex + "\n" + "(KEY=%s)", bytesToHex(key)) + "\n");

        int result = p1.getRollResult(userIndex, computerIndex);

        printTypewriter(String.format("""
                The fair number generation result is %d + %d = %d (mod %d).
                %s roll result is %d.
                """, userIndex, computerIndex, (userIndex + computerIndex) % p1.getDice().getLength(),
                p1.getDice().getLength(), p1.getName(), result));

        return result;
    }

    private void determineWinner(int computerResult, int userResult) throws InterruptedException {
        if (userResult > computerResult) {
            printTypewriter(String.format("You win (%d > %d)!", userResult, computerResult));
        } else if (userResult < computerResult) {
            printTypewriter(String.format("You lose (%d < %d)!", userResult, computerResult));
        } else {
            printTypewriter(String.format("Draw (%d = %d)!", userResult, computerResult));
        }
    }

    private Dice chooseDice(List<Dice> dices) throws InterruptedException {
        while (true) {
            StringBuilder userDicesOutput = new StringBuilder();
            for (int i = 0; i < dices.size(); i++) {
                userDicesOutput.append("\n").append(i).append(" - ").append(dices.get(i));
            }

            printTypewriter(String.format("""
                Choose your dice: %s
                X - exit
                ? - help
                """, userDicesOutput));

            String choice = userInput("Choose your dice: ");

            switch (choice) {
                case "X" -> exit();
                case "?" -> help();
                default -> {
                    try {
                        int index = Integer.parseInt(choice);
                        if (index >= 0 && index < dices.size()) {
                            printTypewriter(user.getName() + " chose: " + dices.get(index) + "\n");
                            return dices.get(index);
                        }
                    } catch (NumberFormatException ignored) {}
                    printTypewriter("Invalid selection. Try again.\n");
                }
            }
        }
    }

    private Dice selectRandomDice(List<Dice> dices) {
        return dices.get(SecureKeyGenerator.generateSecureRandomInt(dices.size()));
    }

    private int selectDiceIndex(int length, String message) throws InterruptedException {
        while (true) {
            StringBuilder userDicesOutput = new StringBuilder();
            for (int i = 0; i < length; i++) {
                userDicesOutput.append("\n").append(i).append(" - ").append(i);
            }

            printTypewriter(String.format("""
                Choose your index: %s
                X - exit
                """, userDicesOutput));

            String choice = userInput(message);

            switch (choice) {
                case "X" -> exit();
                default -> {
                    try {
                        int index = Integer.parseInt(choice);
                        if (index >= 0 && index < length) {
                            printTypewriter("You number: " + choice + "\n");
                            return index;
                        }
                    } catch (NumberFormatException ignored) {}
                    printTypewriter("Invalid index. Try again.\n");
                }
            }
        }
    }


    private void exit() throws InterruptedException {
        printTypewriter("Exiting game. Goodbye!\n");
        System.exit(0);
    }

    private void help() throws InterruptedException {
        printTypewriter("""
                This is a fair dice game. Each player selects a dice and rolls it using a fair algorithm.
                
                Probability of the win for the user:
                """);
        System.out.println((TableGenerator.generateProbabilityTable(allDices) + "\n"));
    }


    private static String userInput(String message) throws InterruptedException {
        printTypewriter(message);
        return SCANNER.nextLine();
    }

    private static void printTypewriter(String text) throws InterruptedException {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            Thread.sleep(DELAY);
        }
    }
}
