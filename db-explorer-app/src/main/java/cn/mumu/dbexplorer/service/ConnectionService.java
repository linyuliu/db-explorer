package cn.mumu.dbexplorer.service;


import cn.mumu.dbexplorer.config.DatabaseType;
import cn.mumu.dbexplorer.dto.ConnectionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 负责处理数据库连接和元数据获取的核心服务（重构版）
 */
@Service
public class ConnectionService {

    private static final Logger log = LoggerFactory.getLogger(ConnectionService.class);

    /**
     * 根据请求动态选择驱动并连接数据库
     *
     * @param request 前端发送的连接请求
     * @return 数据库中的表名列表
     * @throws Exception 连接或查询失败时抛出异常
     */
    public List<String> connectAndFetchTables(ConnectionRequest request) throws Exception {
        // 1. 从枚举中获取数据库连接策略
        DatabaseType dbType = DatabaseType.fromTypeName(request.getDatabaseType());
        // 2. 加载驱动
        log.info("加载驱动: {}", dbType.getDriverClassName());
        Class.forName(dbType.getDriverClassName());
        // 3. 构建URL和连接属性
        String url = dbType.buildUrl(request);
        Properties props = new Properties();
        props.setProperty("user", request.getUsername());
        props.setProperty("password", request.getPassword());
        dbType.configureProperties(props);
        log.info("尝试连接: {}", url);
        List<String> tableNames = new ArrayList<>();
        // 4. 建立连接并获取元数据
        try (Connection conn = DriverManager.getConnection(url, props)) {
            log.info("数据库 '{}' 连接成功!", request.getDatabaseType());
            log.info("驱动版本: {}", conn.getMetaData().getDriverVersion());
            DatabaseMetaData metaData = conn.getMetaData();
            // 兼容不同数据库获取 Catalog 和 Schema 的方式
            String catalog = conn.getCatalog();
            // Oracle 的 Schema 通常是用户名的大写形式
            String schema = "oracle".equals(request.getDatabaseType()) ? request.getUsername().toUpperCase() : conn.getSchema();
            log.info("使用 Catalog: '{}', Schema: '{}' 进行查询", catalog, schema);
            try (ResultSet rs = metaData.getTables(catalog, schema, "%", new String[]{"TABLE", "VIEW"})) {
                while (rs.next()) {
                    tableNames.add(rs.getString("TABLE_NAME"));
                }
            }
            log.info("在数据库 '{}' 中找到 {} 个表/视图.", request.getDatabase(), tableNames.size());
        } catch (SQLException e) {
            log.error("数据库操作失败: {}", e.getMessage());
            throw e;
        }
        return tableNames;
    }
}
