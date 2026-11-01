# Vanilla golem head and wing gradient

Golem Head now uses the vanilla skull pipeline for inventory, equipped and placed rendering. See ../features/golem-head.md for current implementation details.

Mirage Elytra retains its **16 × 16 smooth purple gradient**, generated with the built-in image generation tool and sampled using nearest-neighbor pixel selection. UV rows track geometric height across the membrane and tips to avoid restarting the gradient per segment.

Wing asset: `src/main/resources/assets/cataclysm/textures/entity/equipment/wings/mirage_elytra.png`.

## Final wing generation prompt

A completely flat Minecraft texture: smooth vertical color gradient ONLY from dark deep plum purple at top to muted luminous amethyst purple at bottom. Whole canvas filled opaque. No veins, no stripes, no alternating bands, no horizontal lines, no crystals, no noise, no pixel decoration, no objects, no highlights, no text. Subtle gradual smooth uniform interpolation of purple hues, upper color #281236 lower color #a25bb5. Intended to sample to a 16x16 wing material texture, so each successive row should progress gently in brightness without abrupt changes. Square canvas, evenly smooth color field.

Regenerate Structure has been removed from registration and datagen. Existing saved copies of that removed registry item are no longer supported.

