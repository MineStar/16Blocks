/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of MineStarLibrary.
 * 
 * MineStarLibrary is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * MineStarLibrary is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MineStarLibrary.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.minestarlibrary.utils;

public class ConsoleUtils {

    /**
     * Prints an information to the console
     * 
     * @param message
     *            The information
     */
    public static void printInfo(String message) {
        System.out.println(message);
    }

    /**
     * Prints an information to the console with the prefix <br>
     * <code>[ PLUGIN ] :</code>
     * 
     * @param pluginName
     *            The name of the plugin
     * @param message
     *            The information
     */
    public static void printInfo(String pluginName, String message) {
        printInfo("[ " + pluginName + " ] : " + message);
    }

    /**
     * Prints an warning to the console with the prefix <br>
     * <code>Warning! </code>
     * 
     * @param message
     *            The warning message
     */
    public static void printWarning(String message) {
        printInfo("Warning! " + message);
    }

    /**
     * Prints an warning to the console with the prefix<br>
     * <code>[ PLUGIN ] : Warning! </code>
     * 
     * @param pluginName
     *            The name of the plugin
     * @param message
     *            The warning message
     */
    public static void printWarning(String pluginName, String message) {
        printInfo(pluginName, "Warning! " + message);
    }

    /**
     * Prints an error to the console with the prefix <br>
     * <code>ERROR </code>
     * 
     * @param message
     *            The error message
     */
    public static void printError(String message) {
        printInfo("ERROR! " + message);
    }

    /**
     * Prints an error to the console with the prefix <br>
     * <code>[ PLUGIN ] : ERROR</code>
     * 
     * @param pluginName
     *            The name of the plugin
     * @param message
     *            The error message
     */
    public static void printError(String pluginName, String message) {
        printInfo(pluginName, "ERROR! " + message);
    }

    /**
     * Prints an error message with the exception stacktrace to the console with
     * the prefix <br>
     * <code>EXCEPTION! </code>
     * 
     * @param e
     *            The catched exeption
     * @param message
     *            Helpful hints why exception was thrown
     */
    public static void printException(Exception e, String message) {
        printInfo("EXCEPTION! " + message);
    }

    /**
     * Prints an error message with the exception stacktrace to the console with
     * the prefix <br>
     * <code>[ PLUGIN ] : EXCEPTION! </code>
     * 
     * @param e
     *            The catched exeption
     * @param pluginName
     *            The name of the plugin
     * @param message
     *            Helpful hints why exception was thrown
     */
    public static void printException(Exception e, String pluginName, String message) {
        printInfo(pluginName, "EXCEPTION! " + message);
        e.printStackTrace();
    }
}
