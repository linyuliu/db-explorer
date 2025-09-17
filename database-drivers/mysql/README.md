# MySQL Drivers Module

This module provides MySQL database drivers for different versions with shading support.

## Included Drivers

- **MySQL 5.x**: `mysql:mysql-connector-java:5.1.49`
- **MySQL 8.x**: `com.mysql:mysql-connector-j:9.1.0`

## Shading Support

This module includes Maven Shade Plugin configuration to create isolated driver versions:

### mysql5-shaded
- Relocates MySQL 5.x driver classes to `com.yourcompany.dbexplorer.shaded.mysql5`
- Prevents conflicts when multiple MySQL versions are needed

### mysql8-shaded  
- Relocates MySQL 8.x driver classes to `com.yourcompany.dbexplorer.shaded.mysql8.cj`
- Allows coexistence with MySQL 5.x drivers

## Usage

### Standard Usage
```xml
<dependency>
    <groupId>com.yourcompany</groupId>
    <artifactId>mysql-drivers</artifactId>
    <version>1.0.0</version>
</dependency>
```

### With Shaded Drivers
Use the shaded classifiers when you need isolated driver versions:
```xml
<dependency>
    <groupId>com.yourcompany</groupId>
    <artifactId>mysql-drivers</artifactId>
    <version>1.0.0</version>
    <classifier>mysql5-shaded</classifier>
</dependency>
```

## Metadata Providers

This module supports specialized metadata providers:
- `MySQL5MetadataProvider` - Optimized for MySQL 5.x features and limitations
- `MySQL8MetadataProvider` - Supports MySQL 8.x specific features and improved metadata APIs