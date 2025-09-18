package cn.mumu.dbexplorer;

import cn.mumu.dbexplorer.service.DatabaseConnectionStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 应用主入口
 * @author 18090
 */
@SpringBootApplication
@EnableJpaRepositories
public class DbExplorerApplication implements CommandLineRunner {
    
    @Autowired
    private DatabaseConnectionStorageService storageService;
    
    public static void main(String[] args) {
        SpringApplication.run(DbExplorerApplication.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize default connections on startup
        storageService.initializeDefaultConnections();
    }
}