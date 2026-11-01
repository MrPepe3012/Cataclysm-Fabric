package team.heather.cataclysm.datagen;

import team.heather.cataclysm.item.CataclysmItems;
import team.heather.cataclysm.item.totem.TotemItem;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class CataclysmModelProvider extends FabricModelProvider {
    public CataclysmModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        CataclysmItems.getContent().forEach(item -> {
            if (item instanceof TotemItem) {
                generator.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM);
            } else {
                generator.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
            }
        });
    }
}
