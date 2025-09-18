package cn.mumu.dbexplorer.controller;

import cn.mumu.dbexplorer.entity.DatabaseConnection;
import cn.mumu.dbexplorer.service.DatabaseConnectionStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing stored database connections
 */
@RestController
@RequestMapping("/api/connections")
@CrossOrigin(origins = "*")
public class DatabaseConnectionController {
    
    @Autowired
    private DatabaseConnectionStorageService storageService;
    
    /**
     * Get all stored connections
     */
    @GetMapping
    public List<DatabaseConnection> getAllConnections() {
        return storageService.getAllConnections();
    }
    
    /**
     * Get connection by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DatabaseConnection> getConnection(@PathVariable Long id) {
        Optional<DatabaseConnection> connection = storageService.findById(id);
        return connection.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Save a new connection
     */
    @PostMapping
    public ResponseEntity<DatabaseConnection> saveConnection(@RequestBody DatabaseConnection connection) {
        if (storageService.existsByName(connection.getName())) {
            return ResponseEntity.badRequest().build();
        }
        DatabaseConnection saved = storageService.saveConnection(connection);
        return ResponseEntity.ok(saved);
    }
    
    /**
     * Update an existing connection
     */
    @PutMapping("/{id}")
    public ResponseEntity<DatabaseConnection> updateConnection(@PathVariable Long id, @RequestBody DatabaseConnection connection) {
        if (!storageService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        connection.setId(id);
        DatabaseConnection updated = storageService.updateConnection(connection);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Delete a connection
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConnection(@PathVariable Long id) {
        if (!storageService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        storageService.deleteConnection(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Get connections by database type
     */
    @GetMapping("/type/{databaseType}")
    public List<DatabaseConnection> getConnectionsByType(@PathVariable String databaseType) {
        return storageService.findByDatabaseType(databaseType);
    }
}