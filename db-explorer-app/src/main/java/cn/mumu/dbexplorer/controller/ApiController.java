package cn.mumu.dbexplorer.controller;

import cn.mumu.dbexplorer.dto.ConnectionRequest;
import cn.mumu.dbexplorer.dto.DatabaseInfo;
import cn.mumu.dbexplorer.dto.TableMetadata;
import cn.mumu.dbexplorer.service.ConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Enhanced API controller with metadata endpoints
 * @author 18090
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);
    private final ConnectionService connectionService;

    public ApiController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    /**
     * Connect to database and get basic information with table list
     */
    @PostMapping("/connect")
    public ResponseEntity<?> connectAndFetchTables(@RequestBody ConnectionRequest request) {
        log.info("Received connection request: {}", request.getDatabaseType());
        try {
            DatabaseInfo databaseInfo = connectionService.connectAndFetchTables(request);
            return ResponseEntity.ok(databaseInfo);
        } catch (Exception e) {
            log.error("Connection failed for request: {}", request, e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getClass().getName() + ": " + e.getMessage()));
        }
    }

    /**
     * Get detailed metadata for a specific table
     */
    @PostMapping("/table-metadata")
    public ResponseEntity<?> getTableMetadata(@RequestBody ConnectionRequest request, @RequestParam String tableName) {
        log.info("Getting metadata for table: {} in database: {}", tableName, request.getDatabaseType());
        try {
            TableMetadata metadata = connectionService.getTableMetadata(request, tableName);
            return ResponseEntity.ok(metadata);
        } catch (Exception e) {
            log.error("Failed to get table metadata for {}: {}", tableName, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getClass().getName() + ": " + e.getMessage()));
        }
    }

    /**
     * Get list of schemas for databases that support them
     */
    @PostMapping("/schemas")
    public ResponseEntity<?> getSchemas(@RequestBody ConnectionRequest request) {
        log.info("Getting schemas for database: {}", request.getDatabaseType());
        try {
            List<String> schemas = connectionService.getSchemas(request);
            return ResponseEntity.ok(Map.of("schemas", schemas));
        } catch (Exception e) {
            log.error("Failed to get schemas: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getClass().getName() + ": " + e.getMessage()));
        }
    }
}
