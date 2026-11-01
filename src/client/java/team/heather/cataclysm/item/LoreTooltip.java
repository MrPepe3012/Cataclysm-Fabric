package team.heather.cataclysm.item;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import team.heather.cataclysm.Cataclysm;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class LoreTooltip {
    public static final int LORE_MAX_CHARACTERS = 40;

    private LoreTooltip() {}

    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, context, flags, lines) -> {
            var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (!id.getNamespace().equals(Cataclysm.MOD_ID)) return;
            String key = id.toLanguageKey("item", "description");
            for (int i = lines.size() - 1; i >= 0; i--) {
                Component line = lines.get(i);
                if (line.getContents() instanceof TranslatableContents translation
                        && translation.getKey().equals(key)) {
                    lines.remove(i);
                    List<Component> wrapped = wrap(line);
                    if (wrapped.isEmpty() && i > 0 && lines.get(i - 1).getString().isEmpty()) {
                        lines.remove(i - 1);
                    } else {
                        if (!wrapped.isEmpty() && (i == lines.size() || !lines.get(i).getString().isEmpty())) {
                            wrapped.add(Component.empty());
                        }
                        lines.addAll(i, wrapped);
                    }
                }
            }
        });
    }

    /** Resolve the current language on each tooltip and carry each character's inherited style. */
    public static List<Component> wrap(Component text) {
        var characters = new ArrayList<String>();
        var styles = new ArrayList<Style>();
        text.visit((style, segment) -> {
            segment.codePoints().forEach(codePoint -> {
                characters.add(new String(Character.toChars(codePoint)));
                styles.add(style);
            });
            return Optional.empty();
        }, Style.EMPTY);
        var lines = new ArrayList<Component>();
        int start = 0;
        while (start < characters.size()) {
            int end = start;
            int lastSpace = -1;
            while (end < characters.size() && end - start < LORE_MAX_CHARACTERS
                    && !characters.get(end).equals("\n")) {
                if (characters.get(end).isBlank()) lastSpace = end;
                end++;
            }
            if (end < characters.size() && !characters.get(end).equals("\n")
                    && !characters.get(end).isBlank() && lastSpace > start) end = lastSpace;
            var line = Component.empty();
            for (int i = start; i < end; i++) {
                line.append(Component.literal(characters.get(i)).withStyle(styles.get(i)));
            }
            lines.add(line);
            start = end;
            if (start < characters.size() && characters.get(start).equals("\n")) start++;
            else while (start < characters.size() && characters.get(start).isBlank()
                    && !characters.get(start).equals("\n")) start++;
        }
        return lines;
    }
}
