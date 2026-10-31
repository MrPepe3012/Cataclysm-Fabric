package team.heather.cataclysm;

import team.heather.cataclysm.datagen.CataclysmLanguageProvider;
import team.heather.cataclysm.datagen.CataclysmModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class CataclysmDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(CataclysmLanguageProvider::new);
        pack.addProvider(CataclysmModelProvider::new);
    }
}
