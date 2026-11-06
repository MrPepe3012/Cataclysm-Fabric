package team.heather.cataclysm.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import team.heather.cataclysm.server.CataclysmServer;

public final class RagnarokCommand extends CataclysmCommand {

    private static final String MESSAGE_ACTIVE = "Ragnarok is active";
    private static final String MESSAGE_INACTIVE = "Ragnarok is not active";

    @Override
    protected void defineSyntax(LiteralArgumentBuilder<CommandSourceStack> literal) {
        literal.executes(this::get)
                .then(Commands.literal("start")
                        .executes(context -> this.set(context, true)))
                .then(Commands.literal("stop")
                        .executes(context -> this.set(context, false)));
    }

    private int get(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Component.literal(describe(CataclysmServer.isRagnarok())), false);

        return Command.SINGLE_SUCCESS;
    }

    private int set(CommandContext<CommandSourceStack> context, boolean active) {
        CataclysmServer.setRagnarok(active);

        context.getSource().sendSuccess(() -> Component.literal(describe(active)), true);

        return Command.SINGLE_SUCCESS;
    }

    private static String describe(boolean active) {
        return active ? MESSAGE_ACTIVE : MESSAGE_INACTIVE;
    }
}
