package cn.mumu.dbexplorer.provider;

import cn.mumu.dbexplorer.dto.ConnectionRequest;
import cn.mumu.dbexplorer.dto.DatabaseInfo;
import cn.mumu.dbexplorer.dto.TableMetadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base implementation for database metadata providers
 */
public abstract class AbstractDatabaseMetadataProvider implements DatabaseMetadataProvider {
    
    @Override
    public void configureConnection(Connection connection) throws SQLException {
        // Default implementation - do nothing
        // Subclasses can override for specific configuration
    }
    
    @Override
    public DatabaseInfo getDatabaseInfo(Connection connection, ConnectionRequest request) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        DatabaseInfo info = new DatabaseInfo();
        
        info.setDatabaseName(request.getDatabase());
        info.setDatabaseType(getDatabaseType());
        info.setVersion(metaData.getDatabaseProductVersion());
        info.setSupportsSchema(supportsSchema());
        
        // Get catalog and schema information
        info.setCatalog(connection.getCatalog());
        info.setSchema(getEffectiveSchema(connection, request));
        
        if (supportsSchema()) {
            info.setAvailableSchemas(getSchemas(connection, request));
        }
        
        return info;
    }
    
    @Override
    public List<String> getTables(Connection connection, ConnectionRequest request) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        List<String> tableNames = new ArrayList<>();
        
        String catalog = getEffectiveCatalog(connection, request);
        String schema = getEffectiveSchema(connection, request);
        
        try (ResultSet rs = metaData.getTables(catalog, schema, "%", new String[]{"TABLE", "VIEW"})) {
            while (rs.next()) {
                tableNames.add(rs.getString("TABLE_NAME"));
            }
        }
        
        return tableNames;
    }
    
    @Override
    public List<String> getSchemas(Connection connection, ConnectionRequest request) throws SQLException {
        if (!supportsSchema()) {
            return new ArrayList<>();
        }
        
        DatabaseMetaData metaData = connection.getMetaData();
        List<String> schemas = new ArrayList<>();
        
        try (ResultSet rs = metaData.getSchemas()) {
            while (rs.next()) {
                schemas.add(rs.getString("TABLE_SCHEM"));
            }
        }
        
        return schemas;
    }
    
    /**
     * Whether this database supports schemas
     */
    protected abstract boolean supportsSchema();
    
    /**
     * Get the effective catalog to use for metadata queries
     */
    protected String getEffectiveCatalog(Connection connection, ConnectionRequest request) throws SQLException {
        return connection.getCatalog();
    }
    
    /**
     * Get the effective schema to use for metadata queries
     */
    protected String getEffectiveSchema(Connection connection, ConnectionRequest request) throws SQLException {
        return request.getSchema() != null ? request.getSchema() : connection.getSchema();
    }
}