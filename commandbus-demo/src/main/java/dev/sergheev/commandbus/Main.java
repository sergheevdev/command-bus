package dev.sergheev.commandbus;

public class Main {

    public static void main(String[] args) {
        // Instantiation of the bus (handlers registration, bus type selection, etc.)
        CommandBus commandBus = SimpleCommandBusBuilder.create()
                .registerHandler(PrintLineCommandHandler.class, new PrintLineCommandHandler())
                .registerHandler(SumCommandHandler.class, new SumCommandHandler())
            .build();

        // Creating and executing the summation command
        int firstNumber = 15;
        int secondNumber = 5;
        Command sumCommand = new SumCommand(firstNumber, secondNumber);
        int sumResult = commandBus.execute(sumCommand);

        // Creating and executing the print line command
        String message = String.format("Result of %d + %d is %d %n", firstNumber, secondNumber, sumResult);
        Command printLineCommand = new PrintLineCommand(message);
        commandBus.execute(printLineCommand);
    }

}
