package me.crylonz;

import org.bukkit.ChatColor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreatureCaptureLogicTest {

    @Test
    void buildCaptureBowLoreUsesConfiguredDurability() {
        assertEquals(List.of("10/10"), CreatureCapture.buildCaptureBowLore(10));
    }

    @Test
    void buildCaptureBowLoreUsesInfinityForUnlimitedDurability() {
        assertEquals(List.of("\u221e"), CreatureCapture.buildCaptureBowLore(-1));
    }

    @Test
    void getCaptureBowDisplayNameMatchesPluginFormatting() {
        assertEquals(
                ChatColor.RED + "" + ChatColor.BOLD + "Capture Bow" + ChatColor.GOLD,
                CreatureCapture.getCaptureBowDisplayName()
        );
    }
}
