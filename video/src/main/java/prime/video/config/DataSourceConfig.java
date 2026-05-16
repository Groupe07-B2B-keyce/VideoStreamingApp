package prime.video.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        // Chemin absolu pour éviter les problèmes de working directory
        config.setJdbcUrl("jdbc:sqlite:primevideo.db");
        config.setDriverClassName("org.sqlite.JDBC");
        config.setMaximumPoolSize(1);  // SQLite supporte 1 connexion à la fois
        config.setConnectionTestQuery("SELECT 1");
        // Désactiver les fonctionnalités non supportées par SQLite
        config.addDataSourceProperty("foreign_keys", "true");
        return new HikariDataSource(config);
    }
}
