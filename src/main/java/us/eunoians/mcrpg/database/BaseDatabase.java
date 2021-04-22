package us.eunoians.mcrpg.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.util.DriverDataSource;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jline.utils.Log;

import javax.sql.DataSource;
import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;


/**
 * Singleton used for easy database access.
 * Can be initialised with a mysql/mariadb or sqlite connection.
 *
 * @author OxKitsune
 */
public class BaseDatabase {

    /**
     * Singleton instance
     */
    private static BaseDatabase instance;

    /**
     * Reference to the plugin that created this database object
     */
    private final Plugin plugin;

    /**
     * The HikariCP configuration for this connection
     */
    private final HikariConfig config;

    /**
     * The HikariCP datasource for this database
     */
    private HikariDataSource dataSource;

    /**
     * Construct a new {@link BaseDatabase}.
     * Private constructor because this class shouldn't be instantiated outside of this class
     *
     * @param plugin - the plugin that's instantiating this object
     */
    private BaseDatabase(Plugin plugin) {
        this.plugin = plugin;
        this.config = new HikariConfig();
    }

    /**
     * Initialise the {@link BaseDatabase}.
     *
     * @param plugin - the plugin
     */
    public static void init(@NotNull Plugin plugin) {

        if (instance != null) throw new IllegalStateException("BaseDatabase has already been initialised!");
        instance = new BaseDatabase(plugin);

        DatabaseType databaseType = DatabaseType.fromString(plugin.getConfig().getString("database.type")).orElse(null);

        if (databaseType == null) {
            throw new IllegalArgumentException("[mcRPG] Invalid database type specified! Please make sure it's either \"SQLITE\" or \"MYSQL\" ");
        }


        if (databaseType == DatabaseType.SQLITE) {
            File file = new File(plugin.getDataFolder(), "mcrpg.db");

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            DataSource dataSource = new DriverDataSource("jdbc:sqlite:" + file.getAbsolutePath(), "org.sqlite.JDBC", new Properties(), null, null);
            instance.config.setDataSource(dataSource);
            instance.config.setPoolName("SQLiteConnectionPool");
            instance.config.setDataSourceClassName("org.sqlite.SQLiteDataSource");


            plugin.getLogger().info("Initialising database connection to sqlite database...");
        }
        else {

            String host = plugin.getConfig().getString("database.mysql.host");
            String database = plugin.getConfig().getString("database.mysql.database");
            String username = plugin.getConfig().getString("database.mysql.username");
            String password = plugin.getConfig().getString("database.mysql.password");

            instance.config.setJdbcUrl("jdbc:mysql://" + host + "/" + database);
            instance.config.setUsername(username);
            instance.config.setPassword(password);

            plugin.getLogger().info("Initialising database connection to " + host + " using database \"" + database + "\"");
        }


        instance.config.setAllowPoolSuspension(false);

        // Set time-out to 10 seconds
        instance.config.setConnectionTimeout(10000);
        instance.config.setMaximumPoolSize(6);

        // Create database
        instance.connect();
    }

    /**
     * Attempts a {@link Connection} from the HikariCP connection pool.
     *
     * @return - the connection or else {@code null}
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /***
     * Create the database connection
     */
    private void connect() {
        dataSource = new HikariDataSource(config);
    }

    /**
     * Get the singleton instance.
     *
     * @return - the singleton instance
     */
    public static BaseDatabase getInstance() {
        return instance;
    }

    /**
     * Closes the database connection.
     */
    public void close() {
        dataSource.close();
    }

    public enum DatabaseType {
        SQLITE, MYSQL;


        /**
         * Attempt to get a {@link DatabaseType} from a string.
         *
         * @param str the string
         *
         * @return an {@link Optional} containing the database type
         */
        public static Optional<DatabaseType> fromString (String str) {
            try {
                return Optional.of(DatabaseType.valueOf(str.toUpperCase()));
            }
            catch (IllegalArgumentException e){
                return Optional.empty();
            }

        }
    }
}