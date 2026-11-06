package team.heather.cataclysm.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import team.heather.cataclysm.item.behavior.PersistentItemCooldowns;

public final class ItemCooldownCommand extends CataclysmCommand {

    private static final String MESSAGE_RESET = "%s's item cooldowns were reset";

    private static final String KEY_PLAYER = "player";
    private static final String KEY_RESET = "reset";

    @Override
    protected void defineSyntax(LiteralArgumentBuilder<CommandSourceStack> literal) {
        literal.then(Commands.argument(KEY_PLAYER, EntityArgument.player())
                .then(Commands.literal(KEY_RESET)
                        .executes(this::reset)));
    }

    private int reset(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = EntityArgument.getPlayer(context, KEY_PLAYER);

        PersistentItemCooldowns.reset(player);

        context.getSource().sendSuccess(() -> Component.literal(MESSAGE_RESET.formatted(player.getName().getString())), false);

        return Command.SINGLE_SUCCESS;
    }
}
