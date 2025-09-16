package cn.mumu.dbexplorer.controller;


import cn.mumu.dbexplorer.dto.ConnectionRequest;
import cn.mumu.dbexplorer.service.ConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * API 控制器，处理前端请求
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
     * 连接数据库并获取表结构
     * @param request 连接请求参数
     * @return 成功时返回表列表，失败时返回错误信息
     */
    @PostMapping("/connect")
    public ResponseEntity<?> connectAndFetchTables(@RequestBody ConnectionRequest request) {
        log.info("收到连接请求: {}", request.getDatabaseType());
        try {
            List<String> tables = connectionService.connectAndFetchTables(request);
            return ResponseEntity.ok(Map.of("tables", tables));
        } catch (Exception e) {
            log.error("连接失败，请求详情: {}", request, e);
            // 返回具体的错误信息给前端
            return ResponseEntity.badRequest().body(Map.of("error", e.getClass().getName() + ": " + e.getMessage()));
        }
    }
}
