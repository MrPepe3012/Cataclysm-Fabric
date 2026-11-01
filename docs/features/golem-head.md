# Golem Head

Golem Head is a StandingAndWallBlockItem with the vanilla equippable-un-swappable head component. It uses Cataclysm's own registered floor and wall SkullBlocks, the `cataclysm:golem` skull type, and Fabric's supported-block extension for the vanilla SKULL block entity. There is no parallel custom block entity format.

Both blocks drop the same item and have vanilla floor/wall placement behavior (16 floor rotations, four horizontal wall directions). The inventory and held item use `minecraft:special`, `minecraft:head`, and `minecraft:item/template_skull`. Equipped rendering uses vanilla CustomHeadLayer, and placed rendering uses vanilla SkullBlockRenderer.

SkullBlockRendererMixin supplies the model and material for this skull type in all three paths. GolemSkullModel takes the actual `head` from ModelLayers.IRON_GOLEM without rebuilding cubes or UVs. A parent pivot centers its body in Z and places the nose's lowest vertex at Y=0. Rotations act around this normalized origin instead of the golem's torso attachment offset. The vanilla iron golem texture remains resource-pack compatible.

Regenerate Structure remains removed. Mirage Elytra keeps the 16 × 16 smooth purple gradient described in docs/assets/head-and-wing-refinement.md.

The item uses a geometry-free transform base inheriting template_skull. GUI and fixed translations are lowered by 1.5 model units to compensate for the taller vanilla golem head. Hand and ground transforms remain inherited.

Validation: datagen successfully resolved the custom skull type. A temporary in-engine check baked the real model and emitted exactly 48 vertices (the head and nose), verified centered X bounds and lowest Y=0, both block-to-item mappings, vanilla SKULL support for both blocks, 16/4 rotation properties, the HEAD equipment component and the shared skull material. Temporary verification hooks were removed afterward.
