package org.geysermc.floodgate.module;

import cloud.commandframework.meta.CommandMeta;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ForgeCommandMetaBuilder {

    private ForgeCommandMetaBuilder() {
    }

    /**
     * Create a new builder
     *
     * @return Builder instance
     */
    public static @NonNull Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String description;

        private Builder() {
        }

        /**
         * Set the command description
         *
         * @param description Command description
         * @return Builder instance
         */
        public @NonNull Builder withDescription(final @NonNull String description) {
            this.description = description;
            return this;
        }

        /**
         * Build the command meta instance
         *
         * @return Meta instance
         */
        public @NonNull CommandMeta build() {
            // Use Floodgate's CommandMeta builder to create the meta instance
            return CommandMeta.simple().with(CommandMeta.DESCRIPTION, this.description).build();
        }

    }

}