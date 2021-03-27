package dev.sergheev.commandbus;

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