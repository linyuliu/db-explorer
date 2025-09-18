package cn.mumu.dbexplorer.repository;

import cn.mumu.dbexplorer.entity.DatabaseConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing database connection configurations
 */
@Repository
public interface DatabaseConnectionRepository extends JpaRepository<DatabaseConnection, Long> {
    
    /**
     * Find connection by name
     */
    Optional<DatabaseConnection> findByName(String name);
    
    /**
     * Find connections by database type
     */
    List<DatabaseConnection> findByDatabaseType(String databaseType);
    
    /**
     * Find connections by host
     */
    List<DatabaseConnection> findByHost(String host);
    
    /**
     * Check if connection name already exists
     */
    boolean existsByName(String name);
    
    /**
     * Find connections with similar configuration
     */
    @Query("SELECT dc FROM DatabaseConnection dc WHERE dc.host = :host AND dc.port = :port AND dc.database = :database")
    List<DatabaseConnection> findSimilarConnections(@Param("host") String host, @Param("port") Integer port, @Param("database") String database);
}