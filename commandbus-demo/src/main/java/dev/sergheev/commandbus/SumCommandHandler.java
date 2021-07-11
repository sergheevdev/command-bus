package dev.sergheev.commandbus;

import dev.sergheev.commandbus.mapping.CommandMapping;

/**
 * A handler that performs the correspondent action for {@link SumCommand}.
 */
@CommandMapping(SumCommand.class)
public class SumCommandHandler implements CommandHandler<SumCommand, Integer> {

    public Integer handle(SumCommand command) {
        return command.getFirstNumber() + command.getSecondNumber();
    }

}