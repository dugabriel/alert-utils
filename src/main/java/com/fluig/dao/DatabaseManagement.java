package com.fluig.dao;

import com.fluig.jdbc.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by eduardo.gabriel on 28/12/2016.
 */
public class DatabaseManagement {

    private Logger log = LoggerFactory.getLogger(DatabaseManagement.class);

    private Connection con = null;

    private PreparedStatement statement;

    private ResultSet rs;


    public DatabaseManagement(){
        ConnectionFactory cf = new ConnectionFactory();
        this.con = cf.getConnection();
    }


    public void cleanAlerts(int limitDays){

        try {
            log.info("get list of allerts");

            List<Long> alertIds = new ArrayList<>();
            List<Long> alertObjectIds = new ArrayList<Long>();

            this.statement = this.con.prepareStatement(Query.FIND_ALERTS_ID);
            this.statement.setInt(1,limitDays);

            if (this.statement != null) {

                this.rs = this.statement.executeQuery();

                while (rs.next()){
                    alertIds.add(rs.getLong(1));
                    Long objId = rs.getLong(2);
                    if (!rs.wasNull()) {
                        alertObjectIds.add(objId);
                    }
                    Long placeId = rs.getLong(3);
                    if (!rs.wasNull()) {
                        alertObjectIds.add(placeId);
                    }
                }

            }

            log.info("#size alert: " + alertIds.size() + " size objects: " + alertObjectIds.size());

            if (!alertIds.isEmpty()) {

                log.info("#delete FDN_ALERTSENDER");
                this.tryExecuteBatch(Query.REMOVE_FDN_ALERTSENDER,alertIds,1000);

                log.info("#delete FDN_AlertAction");
                this.tryExecuteBatch(Query.REMOVE_FDN_AlertAction,alertIds,1000);

                log.info("#delete FDN_AlertMetadata");
                this.tryExecuteBatch(Query.REMOVE_FDN_AlertMetadata,alertIds,1000);

                log.info("#delete FDN_Alert");
                this.tryExecuteBatch(Query.REMOVE_FDN_Alert,alertIds,1000);

                log.info("#delete FDN_AlertObject");
                this.tryExecuteBatch(Query.REMOVE_FDN_AlertObject,alertObjectIds,1000);

            }

        } catch (SQLException e){
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            log.info("#close connections...");
            this.closeConnection(this.rs, this.statement, this.con);
        }
    }



    private synchronized void tryExecuteBatch(String SQL, List<Long> entities, int offset){

        try{
            this.statement = this.con.prepareStatement(SQL);

            System.out.println("Executando UPDATE: " + SQL);

            if(this.statement != null){
                final int limit = entities.size();

                for (int i = 0; i < limit; i += offset) {
                    for (Long id : entities.subList(i, Math.min(limit, i + offset))) {

                        this.statement.setLong(1, id);
                        this.statement.addBatch();
                        this.statement.clearParameters();
                    }
                    this.statement.executeBatch();
                    this.statement.clearBatch();
                }
            }
        } catch (SQLException e) {
            log.error("Erro ao realizar update: " + e.getMessage());
        }
    }


    private void closeConnection(ResultSet rs, PreparedStatement stmt, Connection con){

        try {
            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            log.error("Erro ao fechar conexões: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
