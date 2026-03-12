package me.crylonz;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CCCommandExecutorLogicTest {

    @Test
    void canUseGetCommandWhenBasePermissionIsGranted() {
        assertTrue(CCCommandExecutor.canUseGetCommand(true, false));
    }

    @Test
    void canUseGetCommandWhenSpecificPermissionIsGranted() {
        assertTrue(CCCommandExecutor.canUseGetCommand(false, true));
    }

    @Test
    void canUseGetCommandWhenNoPermissionIsGranted() {
        assertFalse(CCCommandExecutor.canUseGetCommand(false, false));
    }

    @Test
    void buildTabCompletionsReturnsCommandsInExpectedOrder() {
        assertEquals(List.of("get", "reload"), CCCommandExecutor.buildTabCompletions(true, true));
    }

    @Test
    void buildTabCompletionsSkipsUnauthorizedCommands() {
        assertEquals(List.of("reload"), CCCommandExecutor.buildTabCompletions(false, true));
    }
}
