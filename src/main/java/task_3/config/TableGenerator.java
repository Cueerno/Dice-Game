package task_3.config;


import com.github.freva.asciitable.AsciiTable;
import task_3.model.Dice;

import java.util.List;

public class TableGenerator {


    public static String generateProbabilityTable(List<Dice> allDices) {
        List<String> diceToString = allDices.stream().map(Dice::toString).toList();

        List<String> dices = diceToString.stream()
                .map(s -> s.replaceAll("[\\[\\]\\s]", "")) // remove brackets and spaces
                .toList();

        String[] headers = new String[dices.size() + 1];

        headers[0] = "User Dice v";

        for (int i = 0; i < dices.size(); i++) {
            headers[i + 1] = dices.get(i);
        }

        String[][] data = new String[dices.size()][dices.size() + 1];

        for (int i = 0; i < dices.size(); i++) {
            String userDice = dices.get(i);
            data[i][0] = userDice;
            for (int j = 0; j < dices.size(); j++) {
                String opponentDice = dices.get(j);
                double probability = ProbabilityCalculator.calcUserWinProbability(userDice, opponentDice);
                data[i][j + 1] = probability + "";
            }
        }

        return AsciiTable.getTable(headers, data);
    }
}
