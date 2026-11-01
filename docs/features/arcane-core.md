# Arcane Core block

Arcane Core is now a BlockItem for Cataclysm's registered HeavyCoreBlock. It inherits vanilla placement, waterlogging, collision and piston behavior. Its hardness (10), blast resistance (1200), metal map color and snare note-block instrument match Minecraft 1.21.5 Heavy Core. It is included in mineable/pickaxe and drops Arcane Core when broken.

Its SoundType reuses all five Heavy Core sound events and the same volume, with a **0.9 pitch multiplier** for placement, breaking, hitting, steps and falls. Engine-specific pitch variations remain intact.

The held/inventory model is preserved. The placed model derives from the pack's existing Arcane Core geometry and textures, uniformly scaled to an 8-model-unit height and translated to rest at Y=0. Bounds were calculated using rotated element vertices and checked to remain within the Heavy Core collision volume. No new bitmap textures were generated.

Resources: assets/cataclysm/models/block/arcane_core.json, assets/cataclysm/blockstates/arcane_core.json and data/cataclysm/loot_table/blocks/arcane_core.json.
