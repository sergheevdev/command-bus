package dev.sergheev.commandbus.container;

public class ConcurrentSimpleContainerTest extends AbstractSimpleContainerTest {

    @Override
    public SimpleContainer createSimpleContainer() {
        return SimpleContainer.newConcurrentInstance();
    }

}
