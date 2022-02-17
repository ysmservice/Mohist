/*
 * Minecraft Forge
 * Copyright (c) 2016-2022.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.fml.loading.targets;

import cpw.mods.modlauncher.api.ILaunchHandlerService;
import net.minecraftforge.fml.loading.LibraryFinder;
import net.minecraftforge.fml.loading.VersionInfo;

import java.nio.file.Path;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class ForgeServerLaunchHandler extends CommonServerLaunchHandler implements ILaunchHandlerService {
    @Override public String name() { return "forgeserver"; }

    @Override
    protected BiPredicate<String, String> processMCStream(VersionInfo versionInfo, Stream.Builder<Path> mc, BiPredicate<String, String> filter, Stream.Builder<List<Path>> mods) {
        var forgepatches = LibraryFinder.findPathForMaven(versionInfo.forgeGroup(), "forge", "", "server", versionInfo.mcAndForgeVersion());
        var forgejar = LibraryFinder.findPathForMaven(versionInfo.forgeGroup(), "forge", "", "universal", versionInfo.mcAndForgeVersion());
        mc.add(forgepatches);
        mods.add(List.of(forgejar));
        return filter;
    }
}
