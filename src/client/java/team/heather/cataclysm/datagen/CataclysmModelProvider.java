package team.heather.cataclysm.datagen;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.core.registries.BuiltInRegistries;
import team.heather.cataclysm.registry.item.CataclysmItems;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class CataclysmModelProvider extends FabricModelProvider {
    public CataclysmModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        // Decode the imported definitions so datagen validates animations and custom properties too.
        CataclysmItems.getContent().forEach(item -> {
            String path = "/cataclysm/datagen/items/" + BuiltInRegistries.ITEM.getKey(item).getPath() + ".json";
            try (var reader = new InputStreamReader(Objects.requireNonNull(
                    this.getClass().getResourceAsStream(path), "Missing item model definition: " + path), StandardCharsets.UTF_8)) {
                var json = JsonParser.parseReader(reader).getAsJsonObject().get("model");
                generator.itemModelOutput.accept(item, ItemModels.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow());
            } catch (IOException exception) {
                throw new UncheckedIOException("Could not read item model definition: " + path, exception);
            }
        });
    }
}
