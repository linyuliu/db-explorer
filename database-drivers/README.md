# Database Drivers Setup

This module consolidates all database drivers for the DB Explorer application.

## Available Profiles

### Default Profile (all drivers)
Includes all database drivers including proprietary ones (Dameng, Kingbase).

### Open Source Profile (opensource-only)
Includes only open-source database drivers that are readily available from Maven Central.

To build with only open-source drivers:
```bash
mvn clean package -Popensource-only
```

## Troubleshooting

### Missing Proprietary Drivers
If you encounter errors about missing Dameng or Kingbase drivers:

1. **Option 1**: Install drivers manually (see README.md)
2. **Option 2**: Use the opensource-only profile
3. **Option 3**: Mark proprietary drivers as optional in your local build

### Common Issues
- **Build fails**: Check if all driver JARs are available
- **Runtime ClassNotFoundException**: Ensure the driver JAR is in the classpath
- **Connection failures**: Verify the driver version matches your database version