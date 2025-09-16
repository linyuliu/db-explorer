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
 * MySQL 5.x metadata provider
 */
@Component
public class MySQL5MetadataProvider extends AbstractDatabaseMetadataProvider {
    
    @Override
    public String getDatabaseType() {
        return "mysql5";
    }
    
    @Override
    public String getDriverClassName() {
        return "com.mysql.jdbc.Driver";
    }
    
    @Override
    public String buildUrl(ConnectionRequest request) {
        return String.format(
            "jdbc:mysql://%s:%d/%s?useSSL=false&characterEncoding=UTF-8",
            request.getHost(), request.getPort(), request.getDatabase()
        );
    }
    
    @Override
    protected boolean supportsSchema() {
        return false; // MySQL uses databases, not schemas
    }
    
    @Override
    public TableMetadata getTableMetadata(Connection connection, ConnectionRequest request, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        TableMetadata tableMetadata = new TableMetadata();
        
        tableMetadata.setTableName(tableName);
        tableMetadata.setSchema(request.getDatabase());
        tableMetadata.setCatalog(connection.getCatalog());
        tableMetadata.setColumns(new ArrayList<>());
        tableMetadata.setIndexes(new ArrayList<>());
        tableMetadata.setForeignKeys(new ArrayList<>());
        
        // Get table info
        try (ResultSet tableRs = metaData.getTables(connection.getCatalog(), null, tableName, null)) {
            if (tableRs.next()) {
                tableMetadata.setTableType(tableRs.getString("TABLE_TYPE"));
                tableMetadata.setRemarks(tableRs.getString("REMARKS"));
            }
        }
        
        return tableMetadata;
    }
}