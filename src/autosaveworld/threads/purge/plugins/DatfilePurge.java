/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package autosaveworld.threads.purge.plugins;

import java.io.File;

import org.bukkit.Bukkit;

import autosaveworld.core.AutoSaveWorld;
import autosaveworld.threads.purge.ActivePlayersList;

public class DatfilePurge {

	private AutoSaveWorld plugin;

	public DatfilePurge(AutoSaveWorld plugin) {
		this.plugin = plugin;
	}

	public void doDelPlayerDatFileTask(ActivePlayersList pacheck) {

		plugin.debug("Playre .dat file purge started");

		int deleted = 0;
		String worldfoldername = Bukkit.getWorlds().get(0).getWorldFolder().getAbsolutePath();
		File playersdatfolder = new File(worldfoldername+ File.separator + "players"+ File.separator);
		for (File playerfile : playersdatfolder.listFiles()) {
			if (playerfile.getName().endsWith(".dat")) {
				String playername = playerfile.getName().substring(0, playerfile.getName().length() - 4);
				if (!pacheck.isActiveCS(playername)) {
					plugin.debug(playername + " is inactive. Removing dat file");
					playerfile.delete();
					deleted += 1;
				}
			}
		}

		plugin.debug("Player .dat purge finished, deleted "+deleted+" player .dat files");
	}

}
