package team.heather.cataclysm.command;

import lombok.Getter;

public enum Permission {

        /** All players. */
        EVERYONE(0),

        /** Basic operators. Can bypass spawn protection. */
        BASIC_OPERATOR(1),

        /** Game masters. Can use commands like /give and /gamemode. */
        GAME_MASTER(2),

        /** Administrators. Can manage players using /ban and /kick. */
        ADMIN(3),

        /** Server owners. Can control the server using /stop. */
        SERVER_OWNER(4);

        @Getter private final int level;

        Permission(int level) {
            this.level = level;
        }
    }