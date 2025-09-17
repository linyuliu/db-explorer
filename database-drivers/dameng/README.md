# Dameng Drivers Module

This module provides Dameng database drivers for different versions.

## About Dameng

Dameng is a domestic database system. This module provides drivers for various Dameng versions and enables specialized metadata handling for modified database features.

## Included Drivers

- **Dameng 8.x**: `com.dameng:DmJdbcDriver18:8.1.3.140`

## Installation

Since Dameng drivers are proprietary, you may need to install them manually:

```bash
mvn install:install-file \
  -Dfile=DmJdbcDriver18.jar \
  -DgroupId=com.dameng \
  -DartifactId=DmJdbcDriver18 \
  -Dversion=8.1.3.140 \
  -Dpackaging=jar
```

## Usage

```xml
<dependency>
    <groupId>com.yourcompany</groupId>
    <artifactId>dameng-drivers</artifactId>
    <version>1.0.0</version>
    <optional>true</optional>
</dependency>
```

## Metadata Provider

This module supports the specialized `DamengMetadataProvider` which:
- Handles Dameng-specific SQL dialects
- Provides optimized metadata acquisition for domestic database features
- Supports schema-aware operations (similar to Oracle)
- Manages Dameng-specific connection properties

## Version Compatibility

Different Dameng versions may have different driver requirements. This modular approach allows:
- Easy addition of new Dameng versions
- Version-specific metadata handling
- Isolated driver dependencies per version
- Support for different driver architectures (32-bit vs 64-bit)