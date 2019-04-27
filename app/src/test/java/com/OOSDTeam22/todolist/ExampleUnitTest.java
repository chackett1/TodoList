package com.OOSDTeam22.todolist;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void checkForMultipleLines_isCorrect() throws Exception {
        assertTrue(MainActivity.IsOneLine("This is a short list title"));

        assertFalse(MainActivity.IsOneLine("This is a long\n list title"));
    }

    @Test
    public void lessThan50Characters_isCorrect() throws Exception {
        assertTrue(ListDetailsActivity.LessThan10Characters("Small"));

        assertFalse(ListDetailsActivity.LessThan10Characters("Longer list entry"));
    }
}