package cn.mumu.dbexplorer.provider;

import cn.mumu.dbexplorer.provider.impl.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory for creating database metadata providers
 */
@Component
public class DatabaseMetadataProviderFactory {
    
    private final Map<String, DatabaseMetadataProvider> providers = new HashMap<>();
    
    public DatabaseMetadataProviderFactory(List<DatabaseMetadataProvider> providerList) {
        for (DatabaseMetadataProvider provider : providerList) {
            providers.put(provider.getDatabaseType(), provider);
        }
    }
    
    /**
     * Get provider for specified database type
     */
    public DatabaseMetadataProvider getProvider(String databaseType) {
        DatabaseMetadataProvider provider = providers.get(databaseType);
        if (provider == null) {
            throw new IllegalArgumentException("Unsupported database type: " + databaseType);
        }
        return provider;
    }
    
    /**
     * Get all supported database types
     */
    public String[] getSupportedTypes() {
        return providers.keySet().toArray(new String[0]);
    }
}