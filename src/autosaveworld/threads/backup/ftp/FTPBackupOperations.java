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

package autosaveworld.threads.backup.ftp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

import autosaveworld.core.AutoSaveWorld;
import autosaveworld.threads.backup.BackupFileUtils;
import autosaveworld.threads.backup.ZipUtils;
import autosaveworldsrclibs.org.apache.commons.net.ftp.FTPClient;

public class FTPBackupOperations {

	private AutoSaveWorld plugin;
	final boolean zip;
	final List<String> excludefolders;
	private FTPClient ftp;
	public FTPBackupOperations(AutoSaveWorld plugin, FTPClient ftp, boolean zip,  List<String> excludefolders) {
		this.plugin = plugin;
		this.zip = zip;
		this.excludefolders = excludefolders;
		this.ftp = ftp;
	}

	public void backupWorld(World world) {
		plugin.debug("Backuping world "+world.getWorldFolder().getName());
		boolean savestaus = world.isAutoSave();
		world.setAutoSave(false);
		try {
			File worldfolder = world.getWorldFolder().getCanonicalFile();
			if (!zip) {
				BackupFileUtils.uploadDirectoryToFTP(ftp, worldfolder, excludefolders);
			} else {
				File tempzip = new File(plugin.constants.getBackupTempFolder(),worldfolder.getName()+".zip");
				ZipUtils.zipFolder(worldfolder, tempzip, excludefolders);
				BackupFileUtils.uploadDirectoryToFTP(ftp, tempzip, new ArrayList<String>());
				tempzip.delete();
				new File(plugin.constants.getBackupTempFolder()).delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			world.setAutoSave(savestaus);
		}

		plugin.debug("Backuped world "+world.getWorldFolder().getName());
	}


	public void backupPlugins() {
		try {
			File plfolder = plugin.getDataFolder().getParentFile().getCanonicalFile();
			if (!zip) {
				BackupFileUtils.uploadDirectoryToFTP(ftp, plfolder, excludefolders);
			} else  {
				File tempzip = new File(plugin.constants.getBackupTempFolder(), plfolder.getName()+".zip");
				List<String> excludefolderslist = new ArrayList<String>();
				excludefolderslist.addAll(excludefolders);
				excludefolderslist.add(plugin.constants.getBackupTempFolder());
				ZipUtils.zipFolder(plfolder, tempzip, excludefolderslist);
				BackupFileUtils.uploadDirectoryToFTP(ftp, tempzip, new ArrayList<String>());
				tempzip.delete();
				new File(plugin.constants.getBackupTempFolder()).delete();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
