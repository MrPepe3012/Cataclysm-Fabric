package team.heather.cataclysm.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class CataclysmCommand {

    protected abstract void defineSyntax(LiteralArgumentBuilder<CommandSourceStack> literal);

    public static CommandRegistration registration() {
        return new CommandRegistration();
    }

    public static final class CommandRegistration {

        private final List<Runnable> pendingRegistrations = new ArrayList<>();

        private CommandRegistration() {}

        public CommandRegistration add(int permission,
                                       String name,
                                       Supplier<? extends CataclysmCommand> commandSupplier) {
            this.pendingRegistrations.add(() -> CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                LiteralArgumentBuilder<CommandSourceStack> literal = Commands.literal(name)
                        .requires(source -> source.hasPermission(permission));

                commandSupplier.get().defineSyntax(literal);

                dispatcher.register(literal);
            }));

            return this;
        }

        public void done() {
            this.pendingRegistrations.forEach(Runnable::run);
            this.pendingRegistrations.clear();
        }
    }
}
