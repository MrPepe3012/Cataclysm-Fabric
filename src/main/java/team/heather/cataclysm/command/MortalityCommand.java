
package team.heather.cataclysm.command;

import team.heather.cataclysm.CataclysmCardinalComponents;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;

public final class MortalityCommand extends CataclysmCommand {

    public static final String ALIAS = "mortality";

    private static final Component MESSAGE_GET = Component.translatable("commands.cataclysm.mortality.get");
    private static final Component MESSAGE_SET = Component.translatable("commands.cataclysm.mortality.set");

    private static final String KEY_PLAYER = "player";
    private static final String KEY_VALUE = "value";

    @Override
    protected void defineSyntax(LiteralArgumentBuilder<CommandSourceStack> literal) {
        literal.then(Commands.argument(KEY_PLAYER, EntityArgument.player())
                .executes(this::get)
                .then(Commands.argument(KEY_VALUE, FloatArgumentType.floatArg(0.0F, 1.0F))
                        .executes(this::set)));
    }

    private int get(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = EntityArgument.getPlayer(context, KEY_PLAYER);
        var mortality = CataclysmCardinalComponents.MORTALITY.get(player).get();

        context.getSource().sendSuccess(() ->
                withArguments(MESSAGE_GET, player.getName().getString(), mortality * 100), false);

        return Command.SINGLE_SUCCESS;
    }

    private int set(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var player = EntityArgument.getPlayer(context, KEY_PLAYER);
        var value = FloatArgumentType.getFloat(context, KEY_VALUE);

        var mortality = CataclysmCardinalComponents.MORTALITY.get(player);
        var previous = mortality.get();
        mortality.set(value);

        context.getSource().sendSuccess(() ->
                withArguments(MESSAGE_SET, player.getName().getString(), previous * 100, value * 100), false);

        return Command.SINGLE_SUCCESS;
    }
}
