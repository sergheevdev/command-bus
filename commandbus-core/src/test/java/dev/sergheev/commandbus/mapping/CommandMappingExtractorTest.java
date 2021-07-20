package dev.sergheev.commandbus.mapping;

import dev.sergheev.commandbus.Command;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CommandMappingExtractorTest {

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
        CommandMappingExtractor extractor = new CommandMappingExtractor();
        extractor.extractMappingsFrom(null);
    }

    @Test
    public void testExtractorGetsAllMappingsEvenInvalidInterface() {
        CommandMappingExtractor extractor = new CommandMappingExtractor();
        List<CommandMapping> mappings = extractor.extractMappingsFrom(FirstFakeCommandHandler.class);
        Assert.assertEquals(1, mappings.size());
        CommandMapping mapping = mappings.get(0);
        Assert.assertEquals(Command.class.getName(), mapping.value().getName());
    }

    @Test
    public void testMappingImplementingTheCommandInterfaceDoesGetExtracted() {
        CommandMappingExtractor extractor = new CommandMappingExtractor();
        List<CommandMapping> mappings = extractor.extractMappingsFrom(SecondFakeCommandHandler.class);
        Assert.assertEquals(1, mappings.size());
        CommandMapping mapping = mappings.get(0);
        Assert.assertEquals(FakeCommand.class.getName(), mapping.value().getName());
    }

    @Test
    public void testClassWithNoCommandMappingsReturnsEmptyExtractedList() {
        CommandMappingExtractor extractor = new CommandMappingExtractor();
        List<CommandMapping> mappings = extractor.extractMappingsFrom(ThirdFakeCommandHandler.class);
        Assert.assertTrue(mappings.isEmpty());
    }

    @Test
    public void testClassWithMultipleAnnotationsReturnsAndExtractsAllMappings() {
        CommandMappingExtractor extractor = new CommandMappingExtractor();
        List<CommandMapping> mappings = extractor.extractMappingsFrom(FourthRandomCommandHandler.class);
        Assert.assertEquals(3, mappings.size());
        Assert.assertTrue(mappings.stream().anyMatch(m -> m.value() == Command.class));
        Assert.assertTrue(mappings.stream().anyMatch(m -> m.value() == FakeCommand.class));
        Assert.assertTrue(mappings.stream().anyMatch(m -> m.value() == AnotherFakeCommand.class));
    }

}
