package cn.mumu.dbexplorer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Database information DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseInfo {
    private String databaseName;
    private String schema;
    private String catalog;
    private List<String> availableSchemas;
    private String databaseType;
    private String version;
    private boolean supportsSchema;
    private List<String> tables;
}