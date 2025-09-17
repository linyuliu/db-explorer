# Database Drivers Setup

This module provides modular database drivers separated by database type and version for the DB Explorer application.

## Modular Structure

The database drivers are now organized by database type in separate modules:

```
database-drivers/
├── mysql/          # MySQL drivers (5.x and 8.x with shading support)
├── postgresql/     # PostgreSQL drivers
├── oracle/         # Oracle drivers
├── sqlserver/      # SQL Server drivers
├── dameng/         # Dameng drivers (proprietary)
└── kingbase/       # Kingbase drivers (proprietary)
```

## Benefits of Modular Structure

1. **Individual Metadata Providers**: Each database type can have specialized metadata acquisition logic
2. **Version Management**: Easy to handle different versions of the same database (e.g., MySQL 5 vs 8)
3. **Shading Support**: MySQL module includes Maven Shade Plugin for creating isolated driver versions
4. **Proprietary Database Support**: Separate modules for modified databases like KingBase and Dameng
5. **Flexible Dependencies**: Applications can include only the drivers they need

## Available Profiles

### Default Profile (all drivers)
Includes all database drivers including proprietary ones (Dameng, Kingbase).

### Open Source Profile (opensource-only)
Includes only open-source database drivers that are readily available from Maven Central.

To build with only open-source drivers:
```bash
mvn clean package -Popensource-only
```

## MySQL Driver Shading

The MySQL module includes Maven Shade Plugin configuration to create shaded versions:
- `mysql5-shaded`: Relocates MySQL 5.x driver to `com.yourcompany.dbexplorer.shaded.mysql5`
- `mysql8-shaded`: Relocates MySQL 8.x driver to `com.yourcompany.dbexplorer.shaded.mysql8.cj`

This allows running multiple MySQL driver versions without conflicts.

## Usage in Applications

To use specific driver modules in your application:

```xml
<!-- Include only MySQL drivers -->
<dependency>
    <groupId>com.yourcompany</groupId>
    <artifactId>mysql-drivers</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- Include PostgreSQL drivers -->
<dependency>
    <groupId>com.yourcompany</groupId>
    <artifactId>postgresql-drivers</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- Include proprietary drivers (optional) -->
<dependency>
    <groupId>com.yourcompany</groupId>
    <artifactId>kingbase-drivers</artifactId>
    <version>1.0.0</version>
    <optional>true</optional>
</dependency>
```

## Troubleshooting

### Missing Proprietary Drivers
If you encounter errors about missing Dameng or Kingbase drivers:

1. **Option 1**: Install drivers manually (see individual module README.md)
2. **Option 2**: Use the opensource-only profile
3. **Option 3**: Mark proprietary drivers as optional in your local build

### Common Issues
- **Build fails**: Check if all driver JARs are available in the specific module
- **Runtime ClassNotFoundException**: Ensure the correct driver module is in the classpath
- **Connection failures**: Verify the driver version matches your database version
- **Shading conflicts**: Use the shaded versions for MySQL when needed