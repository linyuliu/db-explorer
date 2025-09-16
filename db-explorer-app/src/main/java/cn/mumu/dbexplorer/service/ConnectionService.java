package cn.mumu.dbexplorer.service;

import cn.mumu.dbexplorer.dto.ConnectionRequest;
import cn.mumu.dbexplorer.dto.DatabaseInfo;
import cn.mumu.dbexplorer.dto.TableMetadata;
import cn.mumu.dbexplorer.provider.DatabaseMetadataProvider;
import cn.mumu.dbexplorer.provider.DatabaseMetadataProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Enhanced database connection service using the new provider architecture
 */
@Service
public class ConnectionService {

    private static final Logger log = LoggerFactory.getLogger(ConnectionService.class);
    
    private final DatabaseMetadataProviderFactory providerFactory;

    public ConnectionService(DatabaseMetadataProviderFactory providerFactory) {
        this.providerFactory = providerFactory;
    }

    /**
     * Connect to database and get basic database information with table list
     */
    public DatabaseInfo connectAndFetchTables(ConnectionRequest request) throws Exception {
        DatabaseMetadataProvider provider = providerFactory.getProvider(request.getDatabaseType());
        
        // Load driver
        log.info("Loading driver: {}", provider.getDriverClassName());
        Class.forName(provider.getDriverClassName());
        
        // Build URL and connection properties
        String url = provider.buildUrl(request);
        Properties props = new Properties();
        props.setProperty("user", request.getUsername());
        props.setProperty("password", request.getPassword());
        
        log.info("Attempting to connect: {}", url);
        
        try (Connection conn = DriverManager.getConnection(url, props)) {
            log.info("Successfully connected to {} database!", request.getDatabaseType());
            log.info("Driver version: {}", conn.getMetaData().getDriverVersion());
            
            // Configure connection for specific database
            provider.configureConnection(conn);
            
            // Get database information
            DatabaseInfo databaseInfo = provider.getDatabaseInfo(conn, request);
            
            // Get table list
            List<String> tables = provider.getTables(conn, request);
            databaseInfo.setTables(tables);
            
            log.info("Found {} tables/views in database '{}'", tables.size(), request.getDatabase());
            return databaseInfo;
            
        } catch (SQLException e) {
            log.error("Database operation failed: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Get detailed metadata for a specific table
     */
    public TableMetadata getTableMetadata(ConnectionRequest request, String tableName) throws Exception {
        DatabaseMetadataProvider provider = providerFactory.getProvider(request.getDatabaseType());
        
        // Load driver
        Class.forName(provider.getDriverClassName());
        
        // Build URL and connection properties
        String url = provider.buildUrl(request);
        Properties props = new Properties();
        props.setProperty("user", request.getUsername());
        props.setProperty("password", request.getPassword());
        
        try (Connection conn = DriverManager.getConnection(url, props)) {
            provider.configureConnection(conn);
            return provider.getTableMetadata(conn, request, tableName);
        } catch (SQLException e) {
            log.error("Failed to get table metadata for {}: {}", tableName, e.getMessage());
            throw e;
        }
    }
    
    /**
     * Get list of schemas for databases that support them
     */
    public List<String> getSchemas(ConnectionRequest request) throws Exception {
        DatabaseMetadataProvider provider = providerFactory.getProvider(request.getDatabaseType());
        
        // Load driver
        Class.forName(provider.getDriverClassName());
        
        // Build URL and connection properties
        String url = provider.buildUrl(request);
        Properties props = new Properties();
        props.setProperty("user", request.getUsername());
        props.setProperty("password", request.getPassword());
        
        try (Connection conn = DriverManager.getConnection(url, props)) {
            provider.configureConnection(conn);
            return provider.getSchemas(conn, request);
        } catch (SQLException e) {
            log.error("Failed to get schemas: {}", e.getMessage());
            throw e;
        }
    }
}
