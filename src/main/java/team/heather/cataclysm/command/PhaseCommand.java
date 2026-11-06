package team.heather.cataclysm.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import team.heather.cataclysm.server.CataclysmServer;
import team.heather.cataclysm.component.level.phase.Phase;

public final class PhaseCommand extends CataclysmCommand {

    private static final String MESSAGE_GET = "Current phase: %s";
    private static final String MESSAGE_SET = "Phase changed from %s to %s";

    private static final SimpleCommandExceptionType ERROR_LAST_PHASE = new SimpleCommandExceptionType(Component.literal("Already at the last phase"));

    private static final String KEY_NEXT = "next";

    @Override
    protected void defineSyntax(LiteralArgumentBuilder<CommandSourceStack> literal) {
        literal.executes(this::get)
                .then(Commands.literal(KEY_NEXT)
                        .executes(this::next));

        for (var phase : Phase.values()) {
            literal.then(Commands.literal(phase.getSerializedName())
                    .executes(context -> this.set(context, phase)));
        }
    }

    private int get(CommandContext<CommandSourceStack> context) {
        var phase = CataclysmServer.getPhase();

        context.getSource().sendSuccess(() -> Component.literal(MESSAGE_GET.formatted(phase.getSerializedName())), false);

        return Command.SINGLE_SUCCESS;
    }

    private int next(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var phase = CataclysmServer.getPhase();
        if (phase == Phase.FINALE) {
            throw ERROR_LAST_PHASE.create();
        }

        return this.set(context, Phase.values()[phase.ordinal() + 1]);
    }

    private int set(CommandContext<CommandSourceStack> context,
                    Phase phase) {
        var previous = CataclysmServer.getPhase();
        CataclysmServer.setPhase(phase);

        context.getSource().sendSuccess(() ->
                Component.literal(MESSAGE_SET.formatted(previous.getSerializedName(), phase.getSerializedName())), true);

        return Command.SINGLE_SUCCESS;
    }
}
