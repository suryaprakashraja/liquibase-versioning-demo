package com.helppo.dbindexing.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/demo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DemoResource {

    @GET
    @Path("/stringList")
    public Response getAllStrings() {
        List<String> myFavMovies = List.of("Golden Eye", "Casino Royale", "Tomorrow Never Dies", "Goldfinger");
        Response response = Response.ok(myFavMovies).build();
        return response;
    }
}
