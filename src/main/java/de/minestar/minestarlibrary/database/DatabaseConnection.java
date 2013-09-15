/*
 * Copyright (C) 2011 MineStar.de 
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

package de.minestar.minestarlibrary.database;

import java.sql.Connection;
import java.sql.DriverManager;

import org.bukkit.configuration.file.YamlConfiguration;

import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class DatabaseConnection {

    private Connection con;

    private final String pluginName;

    private DatabaseConnection(String pluginName) {
        this.pluginName = pluginName;
    }

    /**
     * Creates a connection to a MySQL Connection.
     * 
     * @param pluginName
     *            Name of the plugin which uses the class
     * @param host
     *            Hosting the MySQL Database
     * @param port
     *            Port for MySQL Client
     * @param database
     *            Name of the database
     * @param userName
     *            User with enough permission to access the database
     * @param password
     *            Password for the user. It will deleted by this
     */
    public DatabaseConnection(String pluginName, String host, String port, String database, String userName, String password) {
        this(pluginName);
        createMySQLConnection(host, port, database, userName, password);
    }

    /**
     * Creates a connection to a SQLLite database. When the database is not
     * existing in moment of creating a connection to it, a new database will be
     * created
     * 
     * @param pluginName
     *            Name of the plugin which uses the class
     * @param folder
     *            Where the database will be stored
     * @param databaseName
     *            Name of the file. Do not use the file ending '.db', it will
     *            added automatically
     */
    public DatabaseConnection(String pluginName, String folder, String databaseName) {
        this(pluginName);
        createSQLLiteConnection(folder, databaseName);
    }

    /**
     * Creates a connection to database specificed by <code>type</code>. The
     * config must contain the setting nodes given by
     * {@link DatabaseUtils#createDatabaseConfig(DatabaseType, File, String)}
     * 
     * @param pluginName
     *            Name of the plugin which uses the class
     * @param type
     *            The type of the Database , see {@link DatabaseType} for
     *            possible values
     * @param config
     *            The configuration. Must contain the same setting nods as
     *            created as by this method
     *            {@link DatabaseUtils#createDatabaseConfig(DatabaseType, File, String)}
     * <br>
     *            The config must be loaded!
     */
    public DatabaseConnection(String pluginName, DatabaseType type, YamlConfiguration config) {
        this(pluginName);
        switch (type) {
            case MySQL :
                createMySQLConnection(config.getString("Host"), config.getString("Port"), config.getString("Database"), config.getString("User"), config.getString("Password"));
                break;
            case SQLLite :
                createSQLLiteConnection(config.getString("Folder"), config.getString("FileName"));
                break;
            default :
                ConsoleUtils.printError(pluginName, "Unsupported DatabaseType '" + type + "'!");
        }
    }

    /**
     * Help method to create a connection to a MySQL database.
     * 
     * @param host
     *            Hosting the MySQL Database
     * @param port
     *            Port for MySQL Client
     * @param database
     *            Name of the database
     * @param userName
     *            User with enough permission to access the database
     * @param password
     *            Password for the user. It will deleted by this
     */
    private void createMySQLConnection(String host, String port, String database, String userName, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, userName, password);
        } catch (Exception e) {
            ConsoleUtils.printException(e, pluginName, "Can't create a MySQL connection! Please check your connection information in the sql.config and your database connection!");
        }
        userName = null;
        password = null;
        System.gc();
    }

    /**
     * Help method to create a connection to a SQLLite database
     * 
     * @param folder
     *            Where the database will be stored
     * @param databaseName
     *            Name of the file. Do not use the file ending '.db', it will
     *            added automatically
     */
    private void createSQLLiteConnection(String folder, String databaseName) {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + folder + "/" + databaseName + ".db");
        } catch (Exception e) {
            ConsoleUtils.printException(e, pluginName, "Can't create a SQLLite connection to " + folder + "/" + databaseName + ".db! Please check your system rights!");
        }
    }

    /**
     * @return Connection to the database to create statements etc.
     */
    public Connection getConnection() {
        return con;
    }

    /**
     * @return True, when a connection to the database is existing.
     */
    public boolean hasConnection() {
        return con != null;
    }

    /**
     * Call this method always before deleting the DatabaseConnection object
     */
    public void closeConnection() {
        try {
            if (con != null) {
                con.close();
                con = null;
            }
        } catch (Exception e) {
            ConsoleUtils.printException(e, "Can't close connection!", pluginName);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        // Try to close connection in every case!
        closeConnection();
    }
}
