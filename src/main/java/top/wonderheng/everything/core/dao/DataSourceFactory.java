package top.wonderheng.everything.core.dao;

import com.alibaba.druid.pool.DruidDataSource;
import top.wonderheng.everything.config.EverythingConfig;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.core.dao.impl
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:16
 */
public class DataSourceFactory {

    private static DruidDataSource dataSource;

    public static DataSource dataSource(EverythingConfig everythingConfig) {
        if (dataSource == null) {
            synchronized(top.wonderheng.everything.core.dao.DataSourceFactory.class) {
                if (dataSource == null) {
                    dataSource = new DruidDataSource();
                    dataSource.setUrl("jdbc:h2:" + everythingConfig.getH2IndexFile());
                    dataSource.setDriverClassName("org.h2.Driver");
                    dataSource.setTestWhileIdle(false);
                }
            }
        }
        return dataSource;
    }


    /**
     * 初始化数据库
     */
    public static void databaseInit() {
        DataSource dataSource = top.wonderheng.everything.core.dao.DataSourceFactory.dataSource(EverythingConfig.defaultConfig());
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
        ) {
            try (InputStream is = top.wonderheng.everything.core.dao.DataSourceFactory.class.getClassLoader().getResourceAsStream("database.sql");
                 InputStreamReader stream = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(stream);
            ) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                System.out.println(statement.execute(sb.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}