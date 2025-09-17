# Kingbase Drivers Module

This module provides KingBase database drivers for different versions.

## About KingBase

KingBase is a domestic database based on PostgreSQL. This module provides drivers for various KingBase versions and enables specialized metadata handling for modified database features.

## Included Drivers

- **KingBase 8.x**: `cn.com.kingbase:kingbase8:8.6.0`

## Installation

Since KingBase drivers are proprietary, you may need to install them manually:

```bash
mvn install:install-file \
  -Dfile=kingbase8.jar \
  -DgroupId=cn.com.kingbase \
  -DartifactId=kingbase8 \
  -Dversion=8.6.0 \
  -Dpackaging=jar
```

## Usage

```xml
<dependency>
    <groupId>com.yourcompany</groupId>
    <artifactId>kingbase-drivers</artifactId>
    <version>1.0.0</version>
    <optional>true</optional>
</dependency>
```

## Metadata Provider

This module supports the specialized `KingbaseMetadataProvider` which:
- Inherits PostgreSQL-like behavior
- Handles KingBase-specific modifications
- Provides optimized metadata acquisition for domestic database features
- Supports schema-aware operations

## Version Compatibility

Different KingBase versions may have different driver requirements. This modular approach allows:
- Easy addition of new KingBase versions
- Version-specific metadata handling
- Isolated driver dependencies per version