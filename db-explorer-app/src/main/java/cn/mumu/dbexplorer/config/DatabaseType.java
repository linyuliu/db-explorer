package cn.mumu.dbexplorer.config;


import cn.mumu.dbexplorer.dto.ConnectionRequest;
import lombok.Getter;

import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 数据库类型枚举
 * 这是一个核心优化点，它将每种数据库的连接策略（驱动、URL格式、特定属性）都封装在一起。
 * 这种设计使得 ConnectionService 的逻辑变得非常简洁，并且扩展新的数据库类型时，只需在此处添加一个新的枚举成员。
 * @author yulin
 */
@Getter
@SuppressWarnings("all")
public enum DatabaseType {

    MYSQL8(
            "mysql8",
            "com.mysql.cj.jdbc.Driver", // Standard MySQL 8.x driver
            "jdbc:mysql://%s:%d/%s?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true"
    ),

    MYSQL5(
            "mysql5",
            "com.mysql.jdbc.Driver", // Standard MySQL 5.x driver
            "jdbc:mysql://%s:%d/%s?useSSL=false&characterEncoding=UTF-8"
    ),

    POSTGRESQL(
            "postgresql",
            "org.postgresql.Driver",
            "jdbc:postgresql://%s:%d/%s"
    ),

    ORACLE(
            "oracle",
            "oracle.jdbc.driver.OracleDriver",
            "jdbc:oracle:thin:@//%s:%d/%s", // 使用 Service Name 格式
            props -> props.setProperty("remarks", "true") // Oracle 需要此属性来获取注释
    ),

    SQLSERVER(
            "sqlserver",
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            "jdbc:sqlserver://%s:%d;databaseName=%s;trustServerCertificate=true"
    ),

    DAMENG(
            "dameng",
            "dm.jdbc.driver.DmDriver",
            "jdbc:dm://%s:%d/%s"
    ),

    KINGBASE(
            "kingbase",
            "com.kingbase8.Driver",
            "jdbc:kingbase8://%s:%d/%s"
    );

    private final String typeName;
    private final String driverClassName;
    private final Function<ConnectionRequest, String> urlFormatter;
    private final Consumer<Properties> propertyConfigurer;

    DatabaseType(String typeName, String driverClassName, String urlTemplate) {
        this(typeName, driverClassName, (req) -> String.format(urlTemplate, req.getHost(), req.getPort(), req.getDatabase()), props -> {});
    }

    // 构造函数 for databases with special property needs (like Oracle)
    DatabaseType(String typeName, String driverClassName, String urlTemplate, Consumer<Properties> propertyConfigurer) {
        this(typeName, driverClassName, (req) -> String.format(urlTemplate, req.getHost(), req.getPort(), req.getDatabase()), propertyConfigurer);
    }
    
    // 主构造函数
    DatabaseType(String typeName, String driverClassName, Function<ConnectionRequest, String> urlFormatter, Consumer<Properties> propertyConfigurer) {
        this.typeName = typeName;
        this.driverClassName = driverClassName;
        this.urlFormatter = urlFormatter;
        this.propertyConfigurer = propertyConfigurer;
    }

    /**
     * 根据请求信息构建JDBC URL
     * @param request 连接请求
     * @return 格式化后的JDBC URL
     */
    public String buildUrl(ConnectionRequest request) {
        return this.urlFormatter.apply(request);
    }
    
    /**
     * 为特定数据库配置额外的连接属性
     * @param props 属性对象
     */
    public void configureProperties(Properties props) {
        this.propertyConfigurer.accept(props);
    }

    /**
     * 根据类型名称字符串查找对应的枚举实例
     * @param typeName 类型名称 (e.g., "mysql8")
     * @return 对应的 DatabaseType 枚举
     * @throws IllegalArgumentException 如果找不到对应的类型
     */
    public static DatabaseType fromTypeName(String typeName) {
        for (DatabaseType type : values()) {
            if (type.getTypeName().equalsIgnoreCase(typeName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("不支持的数据库类型: " + typeName);
    }
}
