package cn.mumu.dbexplorer.provider;

import cn.mumu.dbexplorer.dto.ConnectionRequest;
import cn.mumu.dbexplorer.dto.DatabaseInfo;
import cn.mumu.dbexplorer.dto.TableMetadata;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Database metadata provider interface
 * Each database type should implement this interface to provide specific metadata logic
 */
public interface DatabaseMetadataProvider {
    
    /**
     * Get database type name
     */
    String getDatabaseType();
    
    /**
     * Get JDBC driver class name
     */
    String getDriverClassName();
    
    /**
     * Build JDBC URL from connection request
     */
    String buildUrl(ConnectionRequest request);
    
    /**
     * Configure additional connection properties if needed
     */
    void configureConnection(Connection connection) throws SQLException;
    
    /**
     * Get database information including schemas, catalogs, etc.
     */
    DatabaseInfo getDatabaseInfo(Connection connection, ConnectionRequest request) throws SQLException;
    
    /**
     * Get list of tables/views in the database
     */
    List<String> getTables(Connection connection, ConnectionRequest request) throws SQLException;
    
    /**
     * Get detailed metadata for a specific table
     */
    TableMetadata getTableMetadata(Connection connection, ConnectionRequest request, String tableName) throws SQLException;
    
    /**
     * Get list of schemas in the database (for databases that support schemas)
     */
    List<String> getSchemas(Connection connection, ConnectionRequest request) throws SQLException;
}