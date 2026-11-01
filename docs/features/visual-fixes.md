# Visual fixes and client runtime

- Minecraft 1.21.5 Item.Properties.buildAndValidateComponents overwrites ITEM_NAME. ItemPropertiesMixin preserves the explicitly configured component for cataclysm items before that assignment. Materials without a palette retain an uncolored name; custom names still follow normal ItemStack behavior.
- Mirage Elytra selects custom equipped geometry and keeps vanilla wing animation. Model instances are initialized lazily per renderer, including baby variants.
- All four generated textures in this change are 16 × 16. See docs/assets/missing-item-textures.md and docs/assets/mirage-elytra.md for final paths and generation prompts.
- IntelliJ Minecraft Client and Gradle JavaExec tasks now launch with Java 21. The original failed-close log used Java 26 and contained LWJGL's unsupported-JNI warning. This is a compatibility mitigation, not a proven root-cause diagnosis: the original process was ended before a thread dump could be captured.

## Manual validation

In Cataclysm Render QA: loaded all three newly textured items, confirmed the upgrade's palette-colored hotbar name, switched to third person and inspected the equipped segmented wings. The final run had no missing-texture or unsupported-JNI warnings. Closing directly from the world with Alt+F4 saved all dimensions and exited successfully (Gradle process exit 0), without ending the process forcibly.

A renderer initialization failure found during development was corrected before this final run. The temporary crash report remains only in the ignored run directory.
