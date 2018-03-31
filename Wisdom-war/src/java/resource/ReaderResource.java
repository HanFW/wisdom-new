/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import entity.AuthorEntity;
import entity.ReaderEntity;
import exception.DuplicateEntityException;
import exception.NoSuchEntityException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import sessionBean.ReaderSessionBeanLocal;

/**
 * REST Web Service
 *
 * @author Chuck
 */
@Path("reader")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReaderResource {
    
    private static final Logger LOGGER 
            = Logger.getLogger(ReaderResource.class.getName());
    private static ConsoleHandler handler = null;

    @Context
    private UriInfo context;
    @EJB
    private ReaderSessionBeanLocal readerSessionBean;

    /**
     * Creates a new instance of ReaderResource
     */
    public ReaderResource() {
        handler = new ConsoleHandler();
        handler.setLevel(Level.FINEST);
        LOGGER.setLevel(Level.ALL);
        LOGGER.addHandler(handler);
    }
    
    @Path("login")
    @POST
    public Response logIn(final JsonObject credentials) {
        try {
            final String email = credentials.getString("email");
            final String pwd = credentials.getString("pwd");
            
            ReaderEntity reader 
                    = readerSessionBean.authenticateReader(email, pwd);
            
            if (reader != null) { // authentication success
                return Response.ok().entity(reader).build();
            } else { // authentication failed, OR unexpected exception
                return Response.status(Status.UNAUTHORIZED)
                        .entity("authentication failed.").build();
            }
        } catch (Exception e) { // invalid JSON OR unrecognised email
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @POST
    public Response registerNewReader(final ReaderEntity reader) {
        try {    
            ReaderEntity newReader 
                    = readerSessionBean.createReader(reader);
            
            if (newReader != null) { // creation success
                return Response
                        .created(URI.create(newReader.getId().toString()))
                        .entity(reader).build();
            } else { // missing fields
                return Response.status(Status.BAD_REQUEST)
                        .entity("missing data.").build();
            }
        } catch (DuplicateEntityException e) { // email conflict
            return Response.status(Status.CONFLICT)
                        .entity(e.getMessage()).build();
        }
    }
    
    @Path("conflict")
    @GET
    public Response checkEmailConflict(@QueryParam("email") final String email) {
        try {            
            if (!readerSessionBean.readerHasEmailConflict(email)) { // no conflict
                return Response.ok().build();
            } else { // email conflict OR unexpected exception
                return Response.status(Status.CONFLICT)
                        .entity("email already exists.").build();
            }
        } catch (Exception e) { // invalid JSON
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("{id}/topics")
    @PUT
    public Response updateInterestedTopics(final JsonArray topicsArr, 
            @PathParam("id") final Long readerId) {
        try {
            ArrayList<String> topicsList = new ArrayList<>();
            for (int i = 0; i < topicsArr.size(); i++) {
                topicsList.add(topicsArr.getString(i));
            }
            
            ReaderEntity reader 
                    = readerSessionBean.setInterestedTopics(topicsList, readerId);
            
            if (reader != null) { // success
                return Response.ok().entity(reader).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity("missing data").build();
            }
        } catch (NoSuchEntityException e) { // reader not found
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) { // invalid JsonArray, etc
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("{id}/follow/{authorId}")
    @PUT
    public Response followAuthor(
            @PathParam("id") final Long readerId,
            @PathParam("authorId") final Long authorId) {
        try {            
            ReaderEntity reader = readerSessionBean.followAuthor(readerId,authorId);
            
            if (reader != null) { // success
                return Response.ok().entity(reader).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity("missing data").build();
            }
        } catch (NoSuchEntityException e) { // reader not found
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) { // invalid JsonArray, etc
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("{id}/unfollow/{authorId}")
    @PUT
    public Response unfollowAuthor(
            @PathParam("id") final Long readerId,
            @PathParam("authorId") final Long authorId) {
        try {            
            ReaderEntity reader = readerSessionBean.unfollowAuthor(readerId,authorId);
            
            if (reader != null) { // success
                return Response.ok().entity(reader).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity("missing data").build();
            }
        } catch (NoSuchEntityException e) { // reader not found
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) { // invalid JsonArray, etc
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("{id}/checkFollow/{authorId}")
    @GET
    public Response checkFollow(
            @PathParam("id") final Long readerId,
            @PathParam("authorId") final Long authorId) {
        try {            
            Boolean result = readerSessionBean.checkFollow(readerId,authorId);
            
            if (result != null) { // success
                return Response.ok().build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity("missing data").build();
            }
        } catch (NoSuchEntityException e) { // reader not found
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) { // invalid JsonArray, etc
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("{id}/getFollowingAuthors")
    @GET
    public Response getFollowingAuthors(
            @PathParam("id") final Long readerId){
        try {
            List<AuthorEntity> authors = readerSessionBean.getAllFollowingAuthors(readerId);
            if (authors != null) {
                GenericEntity<List<AuthorEntity>> response 
                        = new GenericEntity<List<AuthorEntity>>(authors){};
                
                return Response.ok().entity(response).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity("missing data").build();
            }
        } catch (Exception e) { // invalid JsonArray, etc
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("{id}/topUp")
    @PUT
    public Response topUp(
            @PathParam("id") final Long readerId,
            final JsonObject request) {
        try {            
            
            Double amount = Double.valueOf(request.getString("amount"));
            ReaderEntity reader = readerSessionBean.topUpWallet(readerId,amount);
            
            if (reader != null) { // success
                return Response.ok().entity(reader).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity("reader entity not found").build();
            }
        } catch (Exception e) { // invalid JsonArray, etc
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
