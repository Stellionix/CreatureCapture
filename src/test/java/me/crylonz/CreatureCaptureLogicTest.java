package me.crylonz;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void calculateCollectionProgressReturnsRoundedPercentage() {
        assertEquals(25, CreatureCapture.calculateCollectionProgress(1, 4));
    }

    @Test
    void formatEntityTypeNameConvertsEnumNameToReadableText() {
        assertEquals("Mushroom Cow", CreatureCapture.formatEntityTypeName("MUSHROOM_COW"));
    }

    @Test
    void getCollectibleEntityTypesContainsCommonCreatures() {
        assertTrue(CreatureCapture.getCollectibleEntityTypes().contains(EntityType.ZOMBIE));
        assertTrue(CreatureCapture.getCollectibleEntityTypes().contains(EntityType.ENDERMAN));
        assertTrue(CreatureCapture.getCollectibleEntityTypes().contains(EntityType.ALLAY));
        assertTrue(CreatureCapture.getCollectibleEntityTypes().contains(EntityType.BREEZE));
    }
}
