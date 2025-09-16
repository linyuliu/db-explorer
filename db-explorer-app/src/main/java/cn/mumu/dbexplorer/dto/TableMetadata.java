package cn.mumu.dbexplorer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Table metadata DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableMetadata {
    private String tableName;
    private String tableType;
    private String schema;
    private String catalog;
    private String remarks;
    private List<ColumnMetadata> columns;
    private List<IndexMetadata> indexes;
    private List<ForeignKeyMetadata> foreignKeys;
    private String primaryKey;
    private Long rowCount;
}

/**
 * Column metadata DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class ColumnMetadata {
    private String columnName;
    private String dataType;
    private int columnSize;
    private int decimalDigits;
    private boolean nullable;
    private String defaultValue;
    private boolean autoIncrement;
    private String remarks;
    private int position;
}

/**
 * Index metadata DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class IndexMetadata {
    private String indexName;
    private boolean unique;
    private List<String> columns;
    private String indexType;
}

/**
 * Foreign key metadata DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class ForeignKeyMetadata {
    private String fkName;
    private String fkColumn;
    private String pkTable;
    private String pkColumn;
    private String updateRule;
    private String deleteRule;
}