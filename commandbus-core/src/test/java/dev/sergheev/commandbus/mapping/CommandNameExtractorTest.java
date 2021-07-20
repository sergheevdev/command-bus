package dev.sergheev.commandbus.mapping;

import dev.sergheev.commandbus.Command;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CommandNameExtractorTest {

    public static class FakeCommand implements Command {}

    public static class AnotherFakeCommand implements Command {}

    @CommandMapping(Command.class)
    public static class FirstFakeCommandHandler {}

    @CommandMapping(FakeCommand.class)
    public static class SecondFakeCommandHandler {}

    public static class ThirdFakeCommandHandler {}

    @CommandMapping(Command.class)
    @CommandMapping(FakeCommand.class)
    @CommandMapping(AnotherFakeCommand.class)
    public static class FourthRandomCommandHandler {}

    @Test(expected = NullPointerException.class)
    public void testMappingExtractionThrowsExceptionWhenPassingNullClass() {
        CommandNameExtractor extractor = new CommandNameExtractor();
        extractor.extractCommandNamesFor(null);
    }

    @Test
    public void testMappingAssociatedToAnInterfaceInsteadOfImplementationDoesNotGetExtracted() {
        CommandNameExtractor extractor = new CommandNameExtractor();
        List<String> mappings = extractor.extractCommandNamesFor(FirstFakeCommandHandler.class);
        Assert.assertTrue(mappings.isEmpty());
    }

    @Test
    public void testMappingImplementingTheCommandInterfaceDoesGetExtracted() {
        CommandNameExtractor extractor = new CommandNameExtractor();
        List<String> mappings = extractor.extractCommandNamesFor(SecondFakeCommandHandler.class);
        Assert.assertEquals(1, mappings.size());
        String className = mappings.get(0);
        Assert.assertEquals(FakeCommand.class.getName(), className);
    }

    @Test
    public void testClassWithNoCommandMappingsReturnsEmptyExtractedList() {
        CommandNameExtractor extractor = new CommandNameExtractor();
        List<String> mappings = extractor.extractCommandNamesFor(ThirdFakeCommandHandler.class);
        Assert.assertTrue(mappings.isEmpty());
    }

    @Test
    public void testClassWithMultipleAnnotationsReturnsValidAnnotationsAndIgnoresTheRawInterface() {
        CommandNameExtractor extractor = new CommandNameExtractor();
        List<String> mappings = extractor.extractCommandNamesFor(FourthRandomCommandHandler.class);
        Assert.assertEquals(2, mappings.size());
        Assert.assertTrue(mappings.contains(FakeCommand.class.getName()));
        Assert.assertTrue(mappings.contains(AnotherFakeCommand.class.getName()));
    }

}
