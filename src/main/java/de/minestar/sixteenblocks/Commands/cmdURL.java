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

package de.minestar.sixteenblocks.Commands;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.bukkit.entity.Player;

import de.minestar.minestarlibrary.commands.AbstractExtendedCommand;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.SkinArea;

public class cmdURL extends AbstractExtendedCommand {

    private static final String LINK_HEAD = "http://server.youareminecraft.com/map/?worldname=world&mapname=surface&zoom=7&x=";
    private static final String LINK_MID = ".0&y=64&z=";

    private AreaManager aManager;

    public cmdURL(String syntax, String arguments, String node, AreaManager aManager) {
        super(Core.NAME, syntax, arguments, node);
        this.aManager = aManager;

        this.description = "Creates a link for the dynmap";
    }

    @Override
    public void execute(String[] args, Player player) {

        String url = "";

        if (args.length == 0) {
            TextUtils.sendInfo(player, "The clickable URL for your position on the livemap is");
            url = LINK_HEAD + player.getLocation().getBlockX() + LINK_MID + player.getLocation().getBlockZ() + ".0";
            url = getTinyUrl(url);
            if (url != null)
                player.sendMessage(url);
            else
                TextUtils.sendError(player, "An error occured! Please contact an admin or supporter!");

        } else {
            SkinArea skin = aManager.getPlayerArea(args[1]);
            if (skin == null)
                TextUtils.sendError(player, "Player '" + args[0] + "' has no skin or doesn't exist");
            else {
                TextUtils.sendInfo(player, "The clickable URL for '" + args[0] + "'s skin location is");
                url = LINK_HEAD + skin.getZoneXZ().getX() + LINK_MID + skin.getZoneXZ().getZ() + ".0";
                url = getTinyUrl(url);
                if (url != null)
                    player.sendMessage(url);
                else
                    TextUtils.sendError(player, "An error occured! Please contact an admin or supporter!");
            }
        }
    }

    private String getTinyUrl(String fullUrl) {
        try {
            HttpClient httpclient = new HttpClient();

            // Prepare a request object
            HttpMethod method = new GetMethod("http://tinyurl.com/api-create.php");
            method.setQueryString(new NameValuePair[]{new NameValuePair("url", fullUrl)});
            httpclient.executeMethod(method);
            String tinyUrl = method.getResponseBodyAsString();
            method.releaseConnection();
            return tinyUrl;
        } catch (Exception e) {
            ConsoleUtils.printException(e, pluginName, "Can't tiny the URL " + fullUrl + " !");
            return null;
        }
    }
}
