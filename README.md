# CommandBus

[![MIT License](https://img.shields.io/badge/license-MIT-brightgreen.svg)](https://github.com/sergheevdev/command-bus/blob/main/LICENSE)

## Introduction

**CommandBus** is a simple, lightweight and thread-safe, command bus (also known as
a **command dispatcher**). A command bus hands over commands to their handlers, and
may also perform other actions (i.e. validate data, wrap handler in a database 
transaction, provide queueing options, etc).

This library provides a unified entry point for your commands execution, promoting
best software design practices.

### Features
- Thread-safety (for concurrent operations when already serving, i.e. runtime loading of handlers).
- Custom annotations (to ease the handler-command association).
- Highly flexible and extensible library (the client can override default implementations).

### TODO
- Add tests and ensure code coverage (with JUnit and Mockito).
- Add middlewares (for multiple purposes, i.e. logging, units of work, etc).
- Add Spring Boot integration (handler-command provisioning, custom builder).
- Add async and multi-threaded command executors.

### Story

This project started as a coding kata for implementing a really simple command bus,
the problem raised other sub-problems, and the inclusion of fascinating concepts to
be implemented (i.e. heterogeneous containers).

In the process I started to design a more flexible API and I thought it was decent
enough to decide to open source it.

## Getting started

1. Package the library with [maven](https://maven.apache.org/) into a JAR file.
2. Add the packaged **JAR** as a provided dependency to your project.
3. Enjoy your new command bus library!

**NOTE**: make sure you do not include the demo submodule when compiling

## Usage

For shortness' sake and in order not to lengthen explanations, we are going to discuss
a really simple example.

In real-world applications those command classes may contain domain objects or the model
representation associated to the layer we're currently working in, instead of simple
primitive types, also the handlers may have dependencies upon other objects (i.e. services)
that may provide an entrypoint to your concrete use case execution.

You may skip this explanation (not recommended) and go straight to [check the demo submodule](commandbus-demo/src/main/java/dev/sergheev)

### Creating command classes

The first command class will be the ```SumCommand``` class which will simply contain 
two numbers which we want to sum.

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

Now that we have created the commands, we might notice that they are simple DTO's, they don't
carry any **logic** inside them, they are only _Data Transfer Objects_, to associate some logic
we need what is called a handler, a handler's purpose is to process the given command and 
return a response, so the handlers will contain the necessary logic to perform the necessary 
operation.

The first handler's job will be performing the sum of two numbers:

```java
    /**
     * A handler that performs the correspondent action for {@link SumCommand}.
     */
    @CommandMapping(SumCommand.class) // Associating the handler to the correspondent command (with the mapping)
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
    @CommandMapping(PrintLineCommand.class) // Associating the handler to the correspondent command (with the mapping)
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

In more complex scenarios, you may need to modify the registry of command handlers
in runtime to prevent any service downtime or any other reason, you can create and
manage the command registry yourself and pass it to the builder.

```java
    public class Main {

        public static void main(String[] args) {
            // Registry created and managed by the client
            CommandHandlerRegistry registry = CommandHandlerRegistryFactory.newRegistry();
            registry.registerHandler(SumCommandHandler.class, new SumCommandHandler());

            // Instantiating bus using client registry
            CommandBus commandBus = SimpleCommandBusBuilder.create().withRegistry(registry).build();

            // Creating and executing the summation command (provided before bus creation)
            int firstNumber = 15;
            int secondNumber = 5;
            Command sumCommand = new SumCommand(firstNumber, secondNumber);
            int sumResult = commandBus.execute(sumCommand);

            // Imagine we need to add another command and handler at runtime (we add it to our manually managed registry)
            registry.registerHandler(PrintLineCommandHandler.class, new PrintLineCommandHandler());

            // Creating and executing the print line command (provided after bus creation or at runtime)
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
