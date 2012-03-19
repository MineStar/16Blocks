package de.minestar.sixteenblocks.Manager;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.bukkit.configuration.file.YamlConfiguration;

import de.minestar.minestarlibrary.database.AbstractDatabaseHandler;
import de.minestar.minestarlibrary.database.DatabaseConnection;
import de.minestar.minestarlibrary.database.DatabaseType;
import de.minestar.minestarlibrary.database.DatabaseUtils;
import de.minestar.sixteenblocks.Core.Core;

public class AreaDatabaseManager extends AbstractDatabaseHandler {

    private PreparedStatement loadAreas, saveArea, updateArea;

    public AreaDatabaseManager(String pluginName, File dataFolder) {
        super(pluginName, dataFolder);
    }

    @Override
    protected DatabaseConnection createConnection(String pluginName, File dataFolder) throws Exception {
        File configFile = new File(dataFolder, "area_sqlconfig.yml");
        YamlConfiguration config = new YamlConfiguration();

        if (!configFile.exists()) {
            DatabaseUtils.createDatabaseConfig(DatabaseType.MySQL, configFile, Core.getInstance().getDescription().getName());
            return null;
        }

        config.load(configFile);
        return new DatabaseConnection(pluginName, config.getString("Host"), config.getString("Port"), config.getString("Database"), config.getString("User"), config.getString("Password"));

    }

    @Override
    protected void createStatements(String pluginName, Connection connection) throws Exception {
        this.loadAreas = dbConnection.getConnection().prepareStatement("SELECT areaOwner, x, z FROM zones");
        this.saveArea = dbConnection.getConnection().prepareStatement("INSERT INTO zones (areaOwner, x, z) VALUES(?, ?, ?)");
        this.updateArea = dbConnection.getConnection().prepareStatement("UPDATE zones SET areaOwner = ? WHERE x = ? AND z = ?");
    }

    @Override
    protected void createStructure(String pluginName, Connection connection) throws Exception {
        DatabaseUtils.createStructure(this.getClass().getResourceAsStream("/structure.sql"), connection, Core.getInstance().getDescription().getName());
    }

    public ArrayList<SkinArea> loadZones() {
        ArrayList<SkinArea> thisList = new ArrayList<SkinArea>();
        try {
            ResultSet result = this.loadAreas.executeQuery();
            while (result != null && result.next()) {
                thisList.add(new SkinArea(result.getInt(2), result.getInt(3), result.getString(1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thisList;
    }

    public void saveZone(SkinArea thisArea) {
        try {
            saveArea.setString(1, thisArea.getAreaOwner());
            saveArea.setInt(2, thisArea.getZoneXZ().getX());
            saveArea.setInt(3, thisArea.getZoneXZ().getZ());
            saveArea.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAreaOwner(SkinArea thisArea) {
        try {
            updateArea.setString(1, thisArea.getAreaOwner());
            updateArea.setInt(2, thisArea.getZoneXZ().getX());
            updateArea.setInt(3, thisArea.getZoneXZ().getZ());
            updateArea.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
