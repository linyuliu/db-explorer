package cn.mumu.dbexplorer.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity for storing database connection configurations
 */
@Entity
@Table(name = "db_connections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseConnection {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, length = 50)
    private String databaseType;
    
    @Column(length = 255)
    private String host;
    
    private Integer port;
    
    @Column(length = 100)
    private String database;
    
    @Column(length = 100)
    private String username;
    
    @Column(length = 255)
    private String password;
    
    @Column(length = 100)
    private String schema;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}