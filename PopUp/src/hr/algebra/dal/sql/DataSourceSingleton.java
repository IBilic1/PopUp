/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import javax.sql.DataSource;

/**
 *
 * @author HT-ICT
 */
public enum DataSourceSingleton {

    INSTANCE;

    private DataSourceSingleton() {
        instance = createInstance();
    }

    private final DataSource instance;

    public DataSource getInstance() {
        return instance;
        
    }

    private DataSource createInstance() {
        SQLServerDataSource dataSource = new SQLServerDataSource();

        dataSource.setServerName("xx");
        dataSource.setDatabaseName("");
        dataSource.setUser("");
        dataSource.setPassword("");
        return dataSource;
    }

}
