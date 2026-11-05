package team.heather.cataclysm.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import team.heather.cataclysm.datagen.resources.CataclysmLanguageProvider;
import team.heather.cataclysm.datagen.resources.CataclysmModelProvider;

public class CataclysmDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(CataclysmLanguageProvider::new);
        pack.addProvider(CataclysmModelProvider::new);
    }
}
