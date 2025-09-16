package cn.mumu.dbexplorer.provider.impl;

import cn.mumu.dbexplorer.dto.ConnectionRequest;
import cn.mumu.dbexplorer.dto.TableMetadata;
import cn.mumu.dbexplorer.provider.AbstractDatabaseMetadataProvider;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * KingbaseES metadata provider
 * KingbaseES is based on PostgreSQL, so it inherits most PostgreSQL behavior
 */
@Component
public class KingbaseMetadataProvider extends AbstractDatabaseMetadataProvider {
    
    @Override
    public String getDatabaseType() {
        return "kingbase";
    }
    
    @Override
    public String getDriverClassName() {
        return "com.kingbase8.Driver";
    }
    
    @Override
    public String buildUrl(ConnectionRequest request) {
        return String.format(
            "jdbc:kingbase8://%s:%d/%s",
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
        // Default to 'public' schema for KingbaseES (like PostgreSQL)
        return "public";
    }
    
    @Override
    public TableMetadata getTableMetadata(Connection connection, ConnectionRequest request, String tableName) throws SQLException {
        // KingbaseES uses similar metadata structure as PostgreSQL
        TableMetadata tableMetadata = new TableMetadata();
        
        tableMetadata.setTableName(tableName);
        tableMetadata.setSchema(getEffectiveSchema(connection, request));
        tableMetadata.setCatalog(connection.getCatalog());
        tableMetadata.setColumns(new ArrayList<>());
        tableMetadata.setIndexes(new ArrayList<>());
        tableMetadata.setForeignKeys(new ArrayList<>());
        
        // KingbaseES specific implementation would be similar to PostgreSQL
        // but might have some vendor-specific features
        
        return tableMetadata;
    }
}