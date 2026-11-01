# Lemegeton

Reference: [TeamHeather/Cataclysm upgrade catalogue](https://github.com/TeamHeather/Cataclysm/blob/8eb70c62ab7fe258d394d6f76f41bb49948951ef/src/main/java/org/cataclysm/game/player/mechanics/upgrade/UpgradeCatalogue.java) and its ItemListener, Upgrades and UpgradeDefinition classes.

- Hold the book in the main hand and sneak + left click to open the 54-slot catalogue. The screen is a catalogue: its icons and inventory stacks cannot be moved through it.
- Hold the correct Cataclysm Upgrade in the off-hand and left click an upgrade to buy its next level. One upgrade item is consumed; creative mode follows the same cost as the reference.
- Right click activates the purchased effects for 600 ticks and starts a 6000-tick cooldown.
- The book stacks to one and glows only after at least one level has been purchased.

## Per-item state

The persistent, synchronized component is `cataclysm:lemegeton`:

```snbt
{upgrades:{speed:1,regeneration:2},cooldown_until:123456L,bonus_slots:0}
```

Levels are keyed by stable upgrade IDs, not player UUIDs. The component is immutable and validates every level against its upgrade's maximum. The cooldown uses the overworld's game time, so it follows the item through holders and dimensions and pauses while the server is stopped. Two books have independent levels, bonuses and cooldowns. Normal status effects are applied to the current holder upon activation.

## World progression

The first week is 1. `Cataclysm.getWeek()` is the existing world progression input; this feature does not introduce a separate calendar. The capacity is `2 * week + bonus_slots` purchased levels. Payment requires tier 1 in week 1, tier 2 in weeks 2–3, and tier 3 from week 4. The server rechecks the held book, payment, tier, capacity and maximum level on every purchase.

## Future integrations

Incursion rewards can update the book component without player data:

```java
var state = LemegetonItem.stateOf(stack);
stack.set(CataclysmDataComponents.LEMEGETON, state.withBonusSlots(state.bonusSlots() + 1));
```

Boss systems can register `LemegetonEvents.ALLOW_USE` and return false to deny activation, catalogue access and purchases. No boss or incursion system is simulated here.

The port fixes two reference bugs: Dolphin's Grace now grants water breathing for the full effect duration, and the extra Cleansing effects apply only at purchased level 2.

## Validation

`gradle test` runs 17 cases covering component serialization, networking, invalid data, immutable independent book states, cooldown preservation and week/tier boundaries. Server-side integration was also exercised in the isolated `Cataclysm Render QA` world: actual menu purchases, exact payment, maximum capacity, wrong tiers, all container click types, stale-book rejection, dynamic glint, full item serialization, effects and independent cooldowns.
