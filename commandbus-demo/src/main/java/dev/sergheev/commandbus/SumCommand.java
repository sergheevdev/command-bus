package dev.sergheev.commandbus;

/**
 * A simple DTO (Data Transfer Object) that contains the values to sum.
 */
public class SumCommand implements Command {

    private final int firstNumber;
    private final int secondNumber;

    public SumCommand(int firstNumber, int secondNumber) {
        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
    }

    public int getFirstNumber() {
        return firstNumber;
    }

    public int getSecondNumber() {
        return secondNumber;
    }

}