package com.fluig.rest;

import com.fluig.dao.DatabaseManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Created by eduardo.gabriel on 28/12/2016.
 */


@Path("/alert")
public class Alert {

        private Logger log = LoggerFactory.getLogger(Alert.class);

        @POST
        @Path("/clean")
        @Produces(MediaType.APPLICATION_JSON)
        public Response cleanAlerts(int days){

            log.info("remove alerts before " + days);

            try {
                DatabaseManagement dm = new DatabaseManagement();
                dm.cleanAlerts(days);
                return Response.status(200).build();
            } catch (Exception e) {
                return Response.status(500).entity(e.toString()).build();
            }
        }


        @GET
        @Path("/{param}")
        public Response printMessage(@PathParam("param") String msg){

            String result = "Rest exemple: " + msg;

            return Response.status(200).entity(result).build();
        }
}
