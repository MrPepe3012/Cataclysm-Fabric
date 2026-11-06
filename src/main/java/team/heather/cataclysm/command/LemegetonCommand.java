package team.heather.cataclysm.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import team.heather.cataclysm.item.data.CataclysmDataComponents;
import team.heather.cataclysm.item.data.LemegetonComponent;
import team.heather.cataclysm.item.feature.lemegeton.LemegetonProcessor;
import team.heather.cataclysm.item.feature.lemegeton.LemegetonItem;
import team.heather.cataclysm.server.CataclysmServer;

import java.util.Arrays;
import java.util.stream.Collectors;

/** Edits the book in the player's main hand, or else the first one in their inventory. */
public final class LemegetonCommand extends CataclysmCommand {

    private static final String MESSAGE_GET = "%s's Lemegeton: %s/%s levels, %s bonus slots. Upgrades: %s";
    private static final String MESSAGE_NONE = "none";
    private static final String MESSAGE_UPGRADE = "%s's Lemegeton: %s changed from level %s to %s";
    private static final String MESSAGE_BONUS_SLOTS = "%s's Lemegeton bonus slots changed from %s to %s";
    private static final String MESSAGE_RESET = "%s's Lemegeton was reset";

    private static final SimpleCommandExceptionType ERROR_NO_BOOK = new SimpleCommandExceptionType(Component.literal("That player has no Lemegeton"));

    private static final String KEY_PLAYER = "player";
    private static final String KEY_UPGRADE = "upgrade";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_BONUS_SLOTS = "bonus_slots";
    private static final String KEY_VALUE = "value";
    private static final String KEY_RESET = "reset";

    @Override
    protected void defineSyntax(LiteralArgumentBuilder<CommandSourceStack> literal) {
        var upgrades = Commands.literal(KEY_UPGRADE);
        for (var upgrade : LemegetonComponent.Upgrade.values()) {
            upgrades.then(Commands.literal(upgrade.getSerializedName())
                    .then(Commands.argument(KEY_LEVEL, IntegerArgumentType.integer(0, upgrade.maxLevel()))
                            .executes(context -> this.setUpgrade(context, upgrade))));
        }

        literal.then(Commands.argument(KEY_PLAYER, EntityArgument.player())
                .executes(this::get)
                .then(upgrades)
                .then(Commands.literal(KEY_BONUS_SLOTS)
                        .then(Commands.argument(KEY_VALUE, IntegerArgumentType.integer(0))
                                .executes(this::setBonusSlots)))
                .then(Commands.literal(KEY_RESET)
                        .executes(this::reset)));
    }

    private int get(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = EntityArgument.getPlayer(context, KEY_PLAYER);
        var state = LemegetonItem.stateOf(findBook(player));
        int capacity = LemegetonProcessor.capacity(CataclysmServer.getPhase(), state);

        context.getSource().sendSuccess(() -> Component.literal(MESSAGE_GET.formatted(player.getName().getString(),
                state.totalLevels(), capacity, state.bonusSlots(), describeUpgrades(state))), false);

        return Command.SINGLE_SUCCESS;
    }

    private int setUpgrade(CommandContext<CommandSourceStack> context,
                           LemegetonComponent.Upgrade upgrade) throws CommandSyntaxException {
        var player = EntityArgument.getPlayer(context, KEY_PLAYER);
        var level = IntegerArgumentType.getInteger(context, KEY_LEVEL);

        var book = findBook(player);
        var state = LemegetonItem.stateOf(book);
        var previous = state.level(upgrade);
        book.set(CataclysmDataComponents.LEMEGETON, state.withLevel(upgrade, level));

        context.getSource().sendSuccess(() -> Component.literal(MESSAGE_UPGRADE.formatted(player.getName().getString(),
                upgrade.getSerializedName(), previous, level)), false);

        return Command.SINGLE_SUCCESS;
    }

    private int setBonusSlots(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = EntityArgument.getPlayer(context, KEY_PLAYER);
        var value = IntegerArgumentType.getInteger(context, KEY_VALUE);

        var book = findBook(player);
        var state = LemegetonItem.stateOf(book);
        var previous = state.bonusSlots();
        book.set(CataclysmDataComponents.LEMEGETON, state.withBonusSlots(value));

        context.getSource().sendSuccess(() ->
                Component.literal(MESSAGE_BONUS_SLOTS.formatted(player.getName().getString(), previous, value)), false);

        return Command.SINGLE_SUCCESS;
    }

    private int reset(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = EntityArgument.getPlayer(context, KEY_PLAYER);

        findBook(player).set(CataclysmDataComponents.LEMEGETON, LemegetonComponent.EMPTY);

        context.getSource().sendSuccess(() -> Component.literal(MESSAGE_RESET.formatted(player.getName().getString())), false);

        return Command.SINGLE_SUCCESS;
    }

    private static ItemStack findBook(ServerPlayer player) throws CommandSyntaxException {
        if (player.getMainHandItem().getItem() instanceof LemegetonItem) {
            return player.getMainHandItem();
        }
        var inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            if (inventory.getItem(slot).getItem() instanceof LemegetonItem) {
                return inventory.getItem(slot);
            }
        }
        throw ERROR_NO_BOOK.create();
    }

    private static String describeUpgrades(LemegetonComponent state) {
        var upgrades = Arrays.stream(LemegetonComponent.Upgrade.values())
                .filter(upgrade -> state.level(upgrade) > 0)
                .map(upgrade -> upgrade.getSerializedName() + " " + state.level(upgrade))
                .collect(Collectors.joining(", "));
        return upgrades.isEmpty() ? MESSAGE_NONE : upgrades;
    }
}
