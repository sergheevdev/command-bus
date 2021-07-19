package dev.sergheev.commandbus.container;

public class DefaultSimpleContainerTest extends AbstractSimpleContainerTest {

    @Override
    public SimpleContainer createSimpleContainer() {
        return SimpleContainer.newInstance();
    }

}
