# CommandBus

## Introduction

**CommandBus** is a lightweight, thread-safe, command bus (a.k.a **command dispatcher**),
this library provides a unified entry point for your command execution promoting best
design patterns practices.

### Features
- Thread-safety
- Custom annotations
- Highly flexibly library

### TODO
- Add tests and ensure code coverage
- Add middlewares (logging, units of work, etc.)
- Add Spring Boot integration

### Story

This project started as a coding kata for implementing a really simple command bus,
the problem raised other sub-problems, and the inclusion of fascinating concepts to
be implemented (i.e. heterogeneous containers).

In the process I started to design a more flexible API and I thought it was decent
enough to decide to open source it.

## Getting started

1. Compile the library with [maven](https://maven.apache.org/)
2. Add the packaged **JAR** as a provided dependency to your project

**NOTE**: make sure you do not include the demo submodule when compiling

## Usage

For shortness' sake and in order not to lengthen explanations we are going to discuss
a really simple example, but in real-world applications those command DTO's contain
domain objects or the domain model representation correspondent to the layer we're 
currently working in instead of simple primitive types, also the handlers may have 
dependencies upon  other objects that provide an entrypoint to the use case execution.

You may skip this explanation and [check the demo submodule](commandbus-demo/src/main/java/dev/sergheev)

### Creating command classes

The first one will be the ```SumCommand``` class which will simply contain two numbers
which we want to sum.

```java
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
```

The second one will be the ```PrintLineCommand``` class which will simply contain the
message to print.

```java
    /**
     * A simple DTO (Data Transfer Object) that contains the message to be printed.
     */
    public class PrintLineCommand implements Command {

        private final String message;

        public PrintLineCommand(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

    }
```

### Creating the handlers

Now that we have created the commands, we might notice that they are simple DTO's they don't
carry **logic** inside them, they are only _Data Transfer Objects_, to associate some logic
we need what is called a handler, a handler's purpose is to process the given command and 
return a response, so the handlers will contain the necessary logic to perform the necessary 
operation.

The first handler's job will be performing the sum of two numbers:

```java
    /**
     * A handler that performs the correspondent action for {@link SumCommand}.
     */
    @CommandMapping(SumCommand.class)
    public class SumCommandHandler implements CommandHandler<SumCommand, Integer> {

        public Integer handle(SumCommand command) {
            return command.getFirstNumber() + command.getSecondNumber();
        }

    }
```

The second handler's job will be printing the provided message:

```java
    /**
     * A handler that performs the correspondent action for {@link PrintLineCommand}.
     */
    @CommandMapping(PrintLineCommand.class)
    public class PrintLineCommandHandler implements CommandHandler<PrintLineCommand, Boolean> {

        public Boolean handle(PrintLineCommand command) {
            System.out.println(command.getMessage());
            return true;
        }

    }
```

Note that we're using annotations to associate a handler to its correspondent command.

### Assembling everything

Putting all together, we'll have a unified execution entry point for all our commands
execution, actual and future one's, making it easy to extend our commands.

```java
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
```

### Complex scenarios

In more complex scenarios you may need to modify the registry of commands and handlers
in runtime to prevent any service downtime or any other reason, you can create and
manage the registry yourself and pass it to the builder.

```java
    public class Main {

        public static void main(String[] args) {
            // Manual instantiation management of the registry
            CommandHandlerRegistry registry = CommandHandlerRegistryFactory.newRegistry();
            registry.registerHandler(SumCommandHandler.class, new SumCommandHandler());

            // Instantiation of the bus using the given registry
            CommandBus commandBus = SimpleCommandBusBuilder.create().withRegistry(registry).build();

            // Creating and executing the summation command
            int firstNumber = 15;
            int secondNumber = 5;
            Command sumCommand = new SumCommand(firstNumber, secondNumber);
            int sumResult = commandBus.execute(sumCommand);

            // Now imagine that we need to add a new command and handler in runtime or after instantiation
            registry.registerHandler(PrintLineCommandHandler.class, new PrintLineCommandHandler());

            // Creating and executing the print line command added after bus instantiation
            String message = String.format("Result of %d + %d is %d %n", firstNumber, secondNumber, sumResult);
            Command printLineCommand = new PrintLineCommand(message);
            commandBus.execute(printLineCommand);
        }

    }
```

The additional documentation for individual features can be found in the source code
javadocs. For additional help, you can create an issue, and I will try to answer as
fast as possible.

## License

[MIT](LICENSE) &copy; Serghei Sergheev
