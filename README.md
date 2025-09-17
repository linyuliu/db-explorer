# 数据库探索器 (Database Explorer)

一个支持多种数据库的现代化Web数据库管理工具，提供直观的用户界面来浏览数据库结构和表元数据。

## 🚀 功能特性

### 🗄️ 多数据库支持
- **MySQL 5.x & 8.x** - 支持不同版本的MySQL数据库
- **PostgreSQL** - 完整的PostgreSQL支持，包括模式(Schema)管理
- **Oracle** - Enterprise级Oracle数据库支持
- **SQL Server** - Microsoft SQL Server支持
- **达梦 (Dameng)** - 国产达梦数据库支持
- **人大金仓 (KingbaseES)** - 基于PostgreSQL的国产数据库

### 🏗️ 架构特性
- **模块化驱动设计** - 每个数据库类型都有独立的驱动模块，支持版本隔离和专有数据库
- **Schema感知** - 智能处理支持Schema的数据库（PostgreSQL、Oracle、KingbaseES、Dameng）
- **版本兼容性** - 支持同一数据库的多个版本（如MySQL 5.x和8.x）
- **Shading支持** - MySQL模块包含Maven Shade插件，避免版本冲突
- **专有数据库支持** - 单独模块支持人大金仓、达梦等魔改数据库
- **可扩展性** - 易于添加新的数据库支持
- **现代化前端** - 基于Vue.js 3的响应式用户界面

### 📊 数据库功能
- **连接管理** - 安全的数据库连接配置
- **表浏览** - 实时获取数据库中的表和视图列表
- **表元数据** - 详细的表结构信息，包括：
  - 列信息（数据类型、约束、默认值等）
  - 索引信息
  - 外键关系
  - 主键信息
- **搜索过滤** - 快速搜索和过滤表名
- **Schema支持** - 针对支持Schema的数据库提供Schema选择

## 🛠️ 技术栈

### 后端
- **Java 17** - 现代Java开发
- **Spring Boot 3.1.2** - 企业级应用框架
- **Maven** - 项目管理和构建工具
- **多版本JDBC驱动** - 隔离不同版本的数据库驱动

### 前端
- **Vue.js 3** - 渐进式JavaScript框架
- **Vite** - 现代化构建工具
- **Tailwind CSS** - 实用优先的CSS框架
- **Axios** - HTTP客户端库

## 📁 项目结构

```
db-explorer/
├── db-explorer-app/           # 主应用模块
│   └── src/main/java/cn/mumu/dbexplorer/
│       ├── provider/          # 数据库元数据提供者
│       │   ├── impl/         # 各数据库的具体实现
│       │   └── DatabaseMetadataProvider.java
│       ├── service/          # 业务服务层
│       ├── controller/       # REST API控制器
│       └── dto/             # 数据传输对象
├── database-drivers/          # 数据库驱动模块（按数据库类型分离）
│   ├── mysql/               # MySQL驱动（支持5.x和8.x版本及shading）
│   ├── postgresql/          # PostgreSQL驱动
│   ├── oracle/              # Oracle驱动
│   ├── sqlserver/           # SQL Server驱动
│   ├── dameng/              # 达梦数据库驱动（专有）
│   └── kingbase/            # 人大金仓驱动（专有）
└── db-ui/                   # Vue.js 前端应用
    ├── src/
    │   ├── components/      # Vue组件
    │   ├── services/        # API服务
    │   └── assets/         # 静态资源
    └── dist/               # 构建输出
```

## 🚀 快速开始

### 环境要求
- Java 17 或更高版本
- Node.js 16+ 和 npm
- Maven 3.6+

### 1. 克隆项目
```bash
git clone https://github.com/linyuliu/db-explorer.git
cd db-explorer
```

### 2. 构建后端
```bash
# 编译所有模块
mvn clean compile

# 打包应用
mvn clean package
```

### 3. 构建前端
```bash
cd db-ui
npm install
npm run build
```

### 4. 运行应用

#### 后端服务
```bash
# 在项目根目录
java -jar db-explorer-app/target/db-explorer-app-1.0.0.jar
```

#### 前端开发服务器
```bash
cd db-ui
npm run dev
```

应用将在以下地址可用：
- 后端API: http://localhost:8080
- 前端界面: http://localhost:5173

## 🔧 配置说明

### 数据库连接配置
每种数据库都有特定的连接参数：

- **Host/端口**: 数据库服务器地址和端口
- **数据库名**: 要连接的数据库/服务名
- **Schema**: 对于支持Schema的数据库（PostgreSQL、Oracle、KingbaseES、Dameng）
- **用户名/密码**: 数据库认证信息

### Schema支持
- **PostgreSQL**: 默认使用`public` schema，可指定其他schema
- **KingbaseES**: 兼容PostgreSQL，默认使用`public` schema
- **Oracle**: 通常使用用户名作为schema名（大写）
- **Dameng**: 类似Oracle，支持用户定义的schema
- **MySQL/SQL Server**: 使用数据库概念，不支持schema

## 🏗️ 开发指南

### 安装私有数据库驱动

某些数据库驱动（如达梦、人大金仓）可能不在 Maven 中央仓库中。如果遇到构建错误，请手动安装这些驱动：

```bash
# 达梦数据库驱动
mvn install:install-file -Dfile=DmJdbcDriver18.jar \
    -DgroupId=com.dameng -DartifactId=DmJdbcDriver18 \
    -Dversion=8.1.3.140 -Dpackaging=jar

# 人大金仓驱动  
mvn install:install-file -Dfile=kingbase8.jar \
    -DgroupId=cn.com.kingbase -DartifactId=kingbase8 \
    -Dversion=8.6.0 -Dpackaging=jar
```

### 添加新数据库支持

1. **创建专用驱动模块**
```bash
# 在 database-drivers/ 下创建新的数据库类型目录
mkdir database-drivers/newdb
```

2. **配置驱动模块 POM**
```xml
<!-- 在 database-drivers/newdb/pom.xml 中添加驱动依赖 -->
<dependency>
    <groupId>com.example</groupId>
    <artifactId>new-database-driver</artifactId>
    <version>1.0.0</version>
</dependency>
```

3. **更新父模块**
```xml
<!-- 在 database-drivers/pom.xml 中添加新模块 -->
<module>newdb</module>
```

4. **实现元数据提供者**
```java
@Component
public class NewDatabaseMetadataProvider extends AbstractDatabaseMetadataProvider {
    @Override
    public String getDatabaseType() {
        return "newdb";
    }
    
    @Override
    public String getDriverClassName() {
        return "com.example.newdb.Driver";
    }
    
    @Override
    public String buildUrl(ConnectionRequest request) {
        return String.format("jdbc:newdb://%s:%d/%s", 
            request.getHost(), request.getPort(), request.getDatabase());
    }
    
    // 实现其他必要方法...
}
```

5. **更新应用依赖**
```xml
<!-- 在 db-explorer-app/pom.xml 中添加新驱动模块依赖 -->
<dependency>
    <groupId>com.yourcompany</groupId>
    <artifactId>newdb-drivers</artifactId>
    <version>1.0.0</version>
</dependency>
```
```

3. **更新前端配置**
在 `ConnectionPanel.vue` 中添加新的数据库类型到 `dbTypes` 数组。

### API端点

- `POST /api/connect` - 连接数据库并获取表列表
- `POST /api/table-metadata?tableName={name}` - 获取表的详细元数据
- `POST /api/schemas` - 获取数据库的Schema列表（仅支持Schema的数据库）

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📝 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 🙏 致谢

- Spring Boot 团队提供的优秀框架
- Vue.js 团队的现代化前端框架
- 各数据库厂商提供的JDBC驱动
- Tailwind CSS 的美观UI组件

## 📞 支持

如果您遇到问题或有建议，请：
1. 查看现有的 [Issues](https://github.com/linyuliu/db-explorer/issues)
2. 创建新的 Issue 描述您的问题
3. 联系项目维护者

---

**数据库探索器** - 让数据库管理变得简单而高效！ 🚀