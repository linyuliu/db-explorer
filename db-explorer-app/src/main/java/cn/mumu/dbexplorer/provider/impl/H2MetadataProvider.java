package cn.mumu.dbexplorer.provider.impl;

import cn.mumu.dbexplorer.dto.ConnectionRequest;
import cn.mumu.dbexplorer.dto.TableMetadata;
import cn.mumu.dbexplorer.provider.AbstractDatabaseMetadataProvider;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * H2 embedded database metadata provider
 */
@Component
public class H2MetadataProvider extends AbstractDatabaseMetadataProvider {
    
    @Override
    public String getDatabaseType() {
        return "h2";
    }
    
    @Override
    public String getDriverClassName() {
        return "org.h2.Driver";
    }
    
    @Override
    public String buildUrl(ConnectionRequest request) {
        // Support both embedded and in-memory H2 databases
        if (request.getHost() == null || request.getHost().isEmpty()) {
            // Default to in-memory database for embedded usage
            return "jdbc:h2:mem:" + (request.getDatabase() != null ? request.getDatabase() : "dbexplorer");
        } else {
            // File-based H2 database
            return String.format(
                "jdbc:h2:tcp://%s:%d/%s",
                request.getHost(), request.getPort(), request.getDatabase()
            );
        }
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
        // H2 default schema is PUBLIC
        return "PUBLIC";
    }
    
    @Override
    public TableMetadata getTableMetadata(Connection connection, ConnectionRequest request, String tableName) throws SQLException {
        TableMetadata tableMetadata = new TableMetadata();
        
        tableMetadata.setTableName(tableName);
        tableMetadata.setSchema(getEffectiveSchema(connection, request));
        tableMetadata.setCatalog(connection.getCatalog());
        tableMetadata.setColumns(new ArrayList<>());
        tableMetadata.setIndexes(new ArrayList<>());
        tableMetadata.setForeignKeys(new ArrayList<>());
        
        // H2 specific metadata implementation would go here
        
        return tableMetadata;
    }
}