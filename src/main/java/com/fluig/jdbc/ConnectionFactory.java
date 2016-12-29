package com.fluig.jdbc;


import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Created by eduardo.gabriel on 28/12/2016.
 */
public class ConnectionFactory {

    private Connection con;

    public Connection getConnection() {

        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("/jdbc/FluigDSRO");

            con = ds.getConnection();
            con.setAutoCommit(true);
            return con;
        } catch (SQLException e) {
            throw new RuntimeException();
        } catch (NamingException e) {
            throw new RuntimeException();
        }

    }
}
