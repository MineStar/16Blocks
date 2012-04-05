package de.minestar.sixteenblocks.Manager;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import de.minestar.minestarlibrary.database.AbstractDatabaseHandler;
import de.minestar.minestarlibrary.database.DatabaseConnection;
import de.minestar.minestarlibrary.database.DatabaseType;
import de.minestar.minestarlibrary.database.DatabaseUtils;
import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Units.ZoneXZ;

public class AreaDatabaseManager extends AbstractDatabaseHandler {

    private PreparedStatement loadAreas, insertArea, updateArea, checkExistance;

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

    public ArrayList<SkinArea> createNotExistingAreas() {
        List<SkinArea> currentZones = this.loadZones();

        // GET MAXIMUM ROW
        ZoneXZ thisZone;
        int maxRow = Integer.MIN_VALUE;
        for (SkinArea thisArea : currentZones) {
            thisZone = thisArea.getZoneXZ();
            if (thisZone.getZ() > maxRow)
                maxRow = thisZone.getZ();
        }

        // INSERT AREAS INTO DATABASE, IF THEY DO NOT EXIST
        ArrayList<SkinArea> newSkins = new ArrayList<SkinArea>();
        for (int row = 0; row <= maxRow; row++) {
            System.out.println("Extending row in DB: " + row);
            for (int x = -Settings.getSkinsRight() + (row % 2 == 0 ? 0 : 1); x <= Settings.getSkinsLeft(); x++) {
                if (!this.areaExists(x, row)) {
                    SkinArea newArea = new SkinArea(x, row, "");
                    this.saveZone(newArea);
                    newSkins.add(newArea);
                }
            }
        }
        return newSkins;
    }

    private boolean areaExists(int x, int z) {
        try {
            this.checkExistance.setInt(1, x);
            this.checkExistance.setInt(2, z);
            ResultSet result = this.checkExistance.executeQuery();
            return result != null && result.next();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    @Override
    protected void createStatements(String pluginName, Connection connection) throws Exception {
        this.loadAreas = dbConnection.getConnection().prepareStatement("SELECT areaOwner, x, z FROM zones");
        this.insertArea = dbConnection.getConnection().prepareStatement("INSERT INTO zones (areaOwner, x, z) VALUES(?, ?, ?)");
        this.updateArea = dbConnection.getConnection().prepareStatement("UPDATE zones SET areaOwner = ? , creationDate = ? WHERE x = ? AND z = ?");
        this.checkExistance = dbConnection.getConnection().prepareStatement("SELECT 1 FROM zones WHERE x = ? AND z = ? LIMIT 1");
    }

    @Override
    protected void createStructure(String pluginName, Connection connection) throws Exception {
        DatabaseUtils.createStructure(this.getClass().getResourceAsStream("/structure.sql"), connection, Core.getInstance().getDescription().getName());
    }

    public List<SkinArea> loadZones() {
        List<SkinArea> thisList = new LinkedList<SkinArea>();
        try {
            ResultSet result = this.loadAreas.executeQuery();
            while (result.next())
                thisList.add(new SkinArea(result.getInt(2), result.getInt(3), result.getString(1)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return thisList;
    }

    public void saveZone(SkinArea thisArea) {
        try {
            insertArea.setString(1, thisArea.getAreaOwner());
            insertArea.setInt(2, thisArea.getZoneXZ().getX());
            insertArea.setInt(3, thisArea.getZoneXZ().getZ());
            insertArea.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAreaOwner(SkinArea thisArea) {
        try {
            updateArea.setString(1, thisArea.getAreaOwner());
            updateArea.setLong(2, System.currentTimeMillis());
            updateArea.setInt(3, thisArea.getZoneXZ().getX());
            updateArea.setInt(4, thisArea.getZoneXZ().getZ());
            updateArea.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAreaOwner(ZoneXZ thisZone) {
        try {
            updateArea.setString(1, "");
            updateArea.setInt(2, thisZone.getX());
            updateArea.setInt(3, thisZone.getZ());
            updateArea.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
