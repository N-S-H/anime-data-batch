package com.example.anime.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;

@Profile({ "dev" })
@Configuration
@Getter
@Setter
@PropertySource(value = "file:${credentialsPath}", factory = YamlPropertySourceFactory.class)
public class PostgresConfig {

	@Value("${postgresql.url}")
	private String url;

	@Value("${postgresusername}")
	private String username;

	@Value("${postgrespassword}")
	private String password;

	private String driverName = "org.postgresql.Driver";

	@Value("${postgresql.schemaName}")
	private String schemaName;

	/**
	 * Returns DataSource instance
	 * 
	 * @return
	 * @throws IOException
	 */
	@Bean(name = "dataSourceConfig")
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

}
