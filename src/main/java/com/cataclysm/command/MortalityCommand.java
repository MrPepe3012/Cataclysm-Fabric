package com.cataclysm.command;

import com.cataclysm.core.CataclysmComponents;
import com.cataclysm.registry.item.totem.mortality.MortalityComponent;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class MortalityCommand implements CommandRegistrationCallback {
    public static final String ALIAS = "mortality";
    private static final String KEY_PLAYER = "player";
    private static final String KEY_VALUE = "value";

    public static final MortalityCommand INSTANCE = new MortalityCommand();

    private MortalityCommand() {}

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal(ALIAS)
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument(KEY_PLAYER, EntityArgument.player())
                        .executes(this::get)
                        .then(Commands.argument(KEY_VALUE, FloatArgumentType.floatArg(0.0F, 1.0F))
                                .executes(this::set))));
    }

    private int get(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, KEY_PLAYER);
        float mortality = CataclysmComponents.MORTALITY.get(player).get();

        String username = player.getName().getString();
        context.getSource().sendSuccess(() -> Component.translatable("commands.cataclysm.mortality.get", username, mortality * 100), false);

        return Command.SINGLE_SUCCESS;
    }

    private int set(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, KEY_PLAYER);
        float value = FloatArgumentType.getFloat(context, KEY_VALUE);

        MortalityComponent component = CataclysmComponents.MORTALITY.get(player);
        float from = component.get();
        component.set(value);

        String username = player.getName().getString();
        context.getSource().sendSuccess(() -> Component.translatable("commands.cataclysm.mortality.set", username, from * 100, value * 100), false);

        return Command.SINGLE_SUCCESS;

    }
}