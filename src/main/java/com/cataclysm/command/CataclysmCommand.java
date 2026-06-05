package com.cataclysm.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.cataclysm.core.CataclysmComponents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class CataclysmCommand {
    private CataclysmCommand() {}

    public static void initialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(Commands.literal("cataclysm").requires(source -> source.hasPermission(2))
                        .then(Commands.literal("player")
                                .then(Commands.literal("mortality")
                                        .then(Commands.argument("target", EntityArgument.player())
                                                .executes(CataclysmCommand::getMortality)
                                                .then(Commands.argument("value", FloatArgumentType.floatArg(0.0F, 1.0F))
                                                        .executes(CataclysmCommand::setMortality)
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static int getMortality(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(context, "target");
        float mortality = CataclysmComponents.MORTALITY.get(target).getMortality();
        context.getSource().sendSuccess(() -> Component.literal(target.getName().getString() + "'s mortality: " + mortality), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int setMortality(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(context, "target");
        float value = FloatArgumentType.getFloat(context, "value");
        CataclysmComponents.MORTALITY.get(target).setMortality(value);
        context.getSource().sendSuccess(() -> Component.literal("Set " + target.getName().getString() + "'s mortality to " + value), false);
        return Command.SINGLE_SUCCESS;
    }
}