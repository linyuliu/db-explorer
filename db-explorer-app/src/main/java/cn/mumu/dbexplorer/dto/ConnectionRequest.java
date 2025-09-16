package cn.mumu.dbexplorer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * API请求的数据传输
 * @author yulin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
public class ConnectionRequest {
    private String databaseType;
    private String host;
    private int port;
    private String database;
    private String schema;
    private String username;
    private String password;
}
