package dev.sergheev.commandbus.container;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static java.util.Objects.isNull;

public abstract class AbstractSimpleContainerTest {

    @Test(expected = Exception.class)
    public void testEmptyConstructorThrowsException() throws Exception {
        final Constructor<SimpleContainer> constructor = SimpleContainer.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testContainerIsEmptyOnCreation() {
        final SimpleContainer container = createSimpleContainer();
        Assert.assertTrue(!isNull(container) && container.isEmpty() && container.size() == 0);
    }

    @Test
    public void testAddingToEmptyContainerReturnsNullValue() {
        final SimpleContainer container = createSimpleContainer();
        final String newValue = "Some original content";
        final String previousValue = container.put(String.class, newValue);
        Assert.assertNull(previousValue);
    }

    @Test
    public void testAddingToNonEmptyContainerReturnsPreviousValue() {
        final SimpleContainer container = createSimpleContainer();
        final String oldValue = "Some original content";
        container.put(String.class, oldValue);
        final String newValue = "More original content";
        final String previousValue = container.put(String.class, newValue);
        Assert.assertEquals(oldValue, previousValue);
    }

    @Test(expected = NullPointerException.class)
    public void testAddingNullKeyToContainerThrowsException() {
        final SimpleContainer container = createSimpleContainer();
        final String value = "Some original content";
        container.put(null, value);
    }

    @Test(expected = NullPointerException.class)
    public void testAddingNullValueToContainerThrowsException() {
        final SimpleContainer container = createSimpleContainer();
        container.put(String.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNonAssignableTypeInstanceToContainer() {
        final SimpleContainer container = createSimpleContainer();
        container.put(String.class, 5);
    }

    @Test(expected = NullPointerException.class)
    public void testAddingNullKeyAndValueThrowsException() {
        final SimpleContainer container = createSimpleContainer();
        container.put(null, null);
    }

    @Test
    public void testRemovingFromEmptyContainerReturnsNull() {
        final SimpleContainer container = createSimpleContainer();
        final String previousValue = container.remove(String.class);
        Assert.assertNull(previousValue);
    }

    @Test(expected = NullPointerException.class)
    public void testRemovingNullKeyFromEmptyContainerThrowsException() {
        final SimpleContainer container = createSimpleContainer();
        container.remove(null);
    }

    @Test
    public void testRemovingOneLeftValueFromContainerLeavesContainerEmpty() {
        final SimpleContainer container = createSimpleContainer();
        final String value = "Some original content";
        container.put(String.class, value);
        container.remove(String.class);
        Assert.assertEquals(0, container.size());
    }

    @Test(expected = NullPointerException.class)
    public void testGettingValueFromNullKeyThrowsException() {
        final SimpleContainer container = createSimpleContainer();
        container.get(null);
    }

    @Test
    public void testGettingValueFromKeyReturnsOriginalContent() {
        final SimpleContainer container = createSimpleContainer();
        final String original = "Some original content";
        container.put(String.class, original);
        final String fetched = container.get(String.class);
        Assert.assertEquals(fetched, original);
    }

    @Test
    public void testGettingValueFromContainerReturnsMostRecentValue() {
        final SimpleContainer container = createSimpleContainer();
        final String oldValue = "Some original content";
        container.put(String.class, oldValue);
        final String newValue = "More original content";
        container.put(String.class, newValue);
        final String fetched = container.get(String.class);
        Assert.assertEquals(fetched, newValue);
    }

    @Test(expected = NullPointerException.class)
    public void testContainsValueFromNullKeyThrowsException() {
        final SimpleContainer container = createSimpleContainer();
        container.contains(null);
    }

    @Test
    public void testEmptyContainerDoesNotContainAssociation() {
        final SimpleContainer container = createSimpleContainer();
        Assert.assertFalse(container.contains(String.class));
    }

    @Test
    public void testContainerValueFromGivenKeyReturnsTrue() {
        final SimpleContainer container = createSimpleContainer();
        final String value = "Some original content";
        container.put(String.class, value);
        Assert.assertTrue(container.contains(String.class));
    }

    @Test
    public void testEmptyContainerReturnsIsEmptyTrue() {
        final SimpleContainer container = createSimpleContainer();
        Assert.assertTrue(container.isEmpty());
    }

    @Test
    public void testNonEmptyContainerReturnsIsEmptyFalse() {
        final SimpleContainer container = createSimpleContainer();
        final String value = "Some original content";
        container.put(String.class, value);
        Assert.assertFalse(container.isEmpty());
    }

    @Test
    public void testAddingAndRemovingAllValuesFromContainerLeavesContainerEmpty() {
        final SimpleContainer container = createSimpleContainer();
        container.put(String.class, "Hello");
        container.put(Integer.class, 10000);
        container.put(Double.class, 7.95);
        container.remove(String.class);
        container.remove(Integer.class);
        container.remove(Double.class);
        Assert.assertTrue(container.isEmpty());
    }

    @Test
    public void testAddingValuesIncrementsContainerSize() {
        final SimpleContainer container = createSimpleContainer();
        container.put(String.class, "Hello");
        container.put(Integer.class, 10000);
        container.put(Double.class, 7.95);
        Assert.assertEquals(3, container.size());
    }

    @Test
    public void testRemovingValuesDecrementsContainerSize() {
        final SimpleContainer container = createSimpleContainer();
        container.put(String.class, "Hello");
        container.put(Integer.class, 10000);
        container.put(Double.class, 7.95);
        container.remove(String.class);
        Assert.assertEquals(2, container.size());
    }

    @Test
    public void testRemovingAllAssociationsFromContainerLeavesContainerWithZeroSize() {
        final SimpleContainer container = createSimpleContainer();
        container.put(String.class, "Hello");
        container.put(Integer.class, 10000);
        container.put(Double.class, 7.95);
        container.remove(String.class);
        container.remove(Integer.class);
        container.remove(Double.class);
        Assert.assertEquals(0, container.size());
    }

    public abstract SimpleContainer createSimpleContainer();

}
