package task_3;


import task_3.config.Validation;

public class Main {

    public static void main(String[] args) {
        try {
            Validation.validateArgs(args);
            GameEngine engine = new GameEngine(args);
            engine.start();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
