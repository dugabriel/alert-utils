package com.fluig.rest;

import com.fluig.dao.DatabaseManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/alert")
public class Alert {

        private Logger log = LoggerFactory.getLogger(Alert.class);

        @POST
        @Path("/clean")
        @Produces(MediaType.APPLICATION_JSON)
        public Response cleanAlerts(int days){

            log.info("remove alerts before " + days);

            try {
                new DatabaseManagement().cleanAlerts(days);
                return Response.status(200).build();
            } catch (Exception e) {
                return Response.status(500).entity(e.toString()).build();
            }
        }

        @POST
        @Path("/optimize")
        @Produces(MediaType.APPLICATION_JSON)
        public Response optimizeTables(){

            log.info("opmize tables alerts");

            try {
                new DatabaseManagement().optimizeTable();
                return Response.status(200).build();
            } catch (Exception e) {
                return Response.status(500).entity(e.toString()).build();
            }
        }

}
