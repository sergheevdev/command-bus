package dev.sergheev.commandbus.container;

public class SimpleContainerTest extends AbstractSimpleContainerTest {

    @Override
    public SimpleContainer createSimpleContainer() {
        return SimpleContainer.newInstance();
    }

}
