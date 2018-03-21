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
            log.info("#get list of allerts...");

            List<Long> alertIds = new ArrayList<>();
            List<Long> alertObjectIds = new ArrayList<>();

            String database =  this.con.getMetaData().getClientInfoProperties().toString();

            if (database.contains("SQLServer")) {
                this.statement = this.con.prepareStatement(Query.FIND_ALERTS_ID_SQLSERVER);
            } else if (database.contains("MySQL")) {
                this.statement = this.con.prepareStatement(Query.FIND_ALERTS_ID_MYSQL);
            } else if (database.contains("Oracle")) {
                this.statement = this.con.prepareStatement(Query.FIND_ALERTS_ID_ORACLE);
            }

            if (this.statement == null ) {
                log.error("statement error");
                return;
            }

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
                final int OFFSET = 1000;
                this.tryExecuteBatch(Query.REMOVE_FDN_ALERTSENDER,alertIds,OFFSET);
                this.tryExecuteBatch(Query.REMOVE_FDN_ALERTACTION,alertIds,OFFSET);
                this.tryExecuteBatch(Query.REMOVE_FDN_ALERTMETADATA,alertIds,100);
                this.tryExecuteBatch(Query.REMOVE_FDN_ALERT,alertIds,100);
                this.tryExecuteBatch(Query.REMOVE_FDN_ALERTOBJECT,alertObjectIds,OFFSET);
            }

        } catch (SQLException e){
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            log.info("#close connections...");
            this.closeConnection(this.rs, this.statement, this.con);
        }
    }



    public void optimizeTable(){

        try {
            log.info("#Executando as otimizacoes das tabelas: " + Query.TABLES.toString());


            for (String table : Query.TABLES){

                String query = Query.OPTIMIZE_TABLE + table;

                log.info("#"+query);

                this.statement = this.con.prepareStatement(query);
                this.statement.execute();
            }

        } catch (SQLException e) {
            log.error("optimize error: " + e.getMessage());
        }finally {
            log.info("#close connections...");
            this.closeConnection(this.rs, this.statement, this.con);
        }

    }



    private void tryExecuteBatch(String SQL, List<Long> entities, int offset){

        try{
            float complete;
            this.statement = this.con.prepareStatement(SQL);

            log.info("#executando UPDATE: " + SQL);

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
                    complete = (float) (((i+offset > limit ? limit : i+offset)*100)/limit);
                    log.info("#processando exclusao dos alerts. Status: " + complete + "%");
                }


            }
        } catch (SQLException e) {
            log.error("update error: " + e.getMessage());
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
            log.error("error on close connections: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
