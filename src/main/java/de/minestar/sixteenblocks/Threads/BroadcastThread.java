/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of SixteenBlocks.
 * 
 * SixteenBlocks is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * SixteenBlocks is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SixteenBlocks.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.sixteenblocks.Threads;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

import org.bukkit.Bukkit;

import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Manager.AreaManager;

public class BroadcastThread extends TimerTask {

    // the messages to broadcast shuffeld
    private List<String> messages;

    // when index == message.length shuffle the messages
    private int index;

    private AreaManager aManager;

    private BroadcastRunningThread subThread;

    public BroadcastThread(File dataFolder, AreaManager aManager) {
        loadMessages(dataFolder);
        this.aManager = aManager;
        initBrodacastThread();
    }

    private void loadMessages(File dataFolder) {
        File file = new File(dataFolder, "messages.txt");
        if (!file.exists()) {
            ConsoleUtils.printWarning(Core.NAME, "Can't find " + file + "! No messages for broadcast thread are loaded!");
            return;
        }
        // reading the file
        try {
            messages = new ArrayList<String>(32);
            BufferedReader bReader = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = bReader.readLine()) != null) {
                if (!line.isEmpty())
                    messages.add(line);
            }
            bReader.close();
            index = 0;
            // shuffle messages
            Collections.shuffle(messages);
        } catch (Exception e) {
            ConsoleUtils.printException(e, Core.NAME, "Error while loading " + file + " !");
        }
    }

    private void initBrodacastThread() {
        subThread = new BroadcastRunningThread(this.aManager);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), subThread, 20L, 20L);
    }

    @Override
    public void run() {
        // CREATE THE TASK
        if (messages == null)
            return;
        // SAY SUBTHREAD HE CAN BROADCAST
        subThread.setMessage(messages.get(index++));

        // shuffle the messages and reset index
        if (index == messages.size()) {
            Collections.shuffle(messages);
            index = 0;
        }
    }
}
