package cn.mumu.dbexplorer.provider.impl;

import cn.mumu.dbexplorer.dto.ConnectionRequest;
import cn.mumu.dbexplorer.dto.TableMetadata;
import cn.mumu.dbexplorer.provider.AbstractDatabaseMetadataProvider;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * SQL Server metadata provider
 */
@Component
public class SQLServerMetadataProvider extends AbstractDatabaseMetadataProvider {
    
    @Override
    public String getDatabaseType() {
        return "sqlserver";
    }
    
    @Override
    public String getDriverClassName() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }
    
    @Override
    public String buildUrl(ConnectionRequest request) {
        return String.format(
            "jdbc:sqlserver://%s:%d;databaseName=%s;trustServerCertificate=true",
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
        // Default to 'dbo' schema for SQL Server
        return "dbo";
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
        
        // SQL Server specific metadata implementation would go here
        
        return tableMetadata;
    }
}