package cn.mumu.dbexplorer.provider.impl;

import cn.mumu.dbexplorer.dto.ConnectionRequest;
import cn.mumu.dbexplorer.dto.TableMetadata;
import cn.mumu.dbexplorer.provider.AbstractDatabaseMetadataProvider;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * PostgreSQL metadata provider
 */
@Component
public class PostgreSQLMetadataProvider extends AbstractDatabaseMetadataProvider {
    
    @Override
    public String getDatabaseType() {
        return "postgresql";
    }
    
    @Override
    public String getDriverClassName() {
        return "org.postgresql.Driver";
    }
    
    @Override
    public String buildUrl(ConnectionRequest request) {
        return String.format(
            "jdbc:postgresql://%s:%d/%s",
            request.getHost(), request.getPort(), request.getDatabase()
        );
    }
    
    @Override
    protected boolean supportsSchema() {
        return true;
    }
    
    @Override
    protected String getEffectiveSchema(Connection connection, ConnectionRequest request) throws SQLException {
        if (request.getSchema() != null && !request.getSchema().trim().isEmpty()) {
            return request.getSchema();
        }
        // Default to 'public' schema for PostgreSQL
        return "public";
    }
    
    @Override
    public TableMetadata getTableMetadata(Connection connection, ConnectionRequest request, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        TableMetadata tableMetadata = new TableMetadata();
        
        tableMetadata.setTableName(tableName);
        tableMetadata.setSchema(getEffectiveSchema(connection, request));
        tableMetadata.setCatalog(connection.getCatalog());
        tableMetadata.setColumns(new ArrayList<>());
        tableMetadata.setIndexes(new ArrayList<>());
        tableMetadata.setForeignKeys(new ArrayList<>());
        
        String schema = getEffectiveSchema(connection, request);
        
        // Get table info
        try (ResultSet tableRs = metaData.getTables(null, schema, tableName, null)) {
            if (tableRs.next()) {
                tableMetadata.setTableType(tableRs.getString("TABLE_TYPE"));
                tableMetadata.setRemarks(tableRs.getString("REMARKS"));
            }
        }
        
        // PostgreSQL specific column metadata implementation would go here
        
        return tableMetadata;
    }
}