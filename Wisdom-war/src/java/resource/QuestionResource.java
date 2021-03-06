/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import entity.QuestionEntity;
import entity.ReaderEntity;
import exception.InsufficientBalanceException;
import exception.NoSuchEntityException;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.json.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import sessionBean.QuestionSessionBeanLocal;

/**
 * REST Web Service
 *
 * @author Chuck
 */
@Path("question")
public class QuestionResource {
    private static final Logger LOGGER 
            = Logger.getLogger(QuestionResource.class.getName());
    private static ConsoleHandler handler = null;

    @Context
    private UriInfo context;
    @EJB
    private QuestionSessionBeanLocal questionSessionBean;

    /**
     * Creates a new instance of QuestionResource
     */
    public QuestionResource() {
        handler = new ConsoleHandler();
        handler.setLevel(Level.FINEST);
        LOGGER.setLevel(Level.ALL);
        LOGGER.addHandler(handler);
    }
    
    @GET
    public Response getQuestionsByReader(@QueryParam("readerId") final Long readerId) {
        try {
            List<QuestionEntity> questions = questionSessionBean.getQuestionsByReader(readerId);
            if (questions != null) { // success
                // response wrapper for list data type
                GenericEntity<List<QuestionEntity>> response = new GenericEntity<List<QuestionEntity>>(questions) {};
                return Response.ok().entity(response).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity("missing data").build();
            }
        } catch (NoSuchEntityException e) { // reader not found
            LOGGER.log(Level.SEVERE, "1. reader {0} not found", readerId);
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
    
    @Path("{id}")
    @GET
    public Response getQuestionById(@PathParam("id") final Long questionId) {
        try {
            QuestionEntity question = questionSessionBean.getQuestionById(questionId);
            if (question != null) { // success
                return Response.ok().entity(question).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity("missing data").build();
            }
        } catch (NoSuchEntityException e) { // question not found
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
    
    @Path("{id}/ask/{authorId}")
    @POST
    public Response askQuestion(@PathParam("id") final Long readerId,
            @PathParam("authorId") final Long authorId,
            final QuestionEntity question) {

        try {
            ReaderEntity reader = questionSessionBean.askQuestion(readerId, authorId, question);
            if (reader != null) { // success
                return Response.ok().entity(reader).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity("missing data").build();
            }
        } catch(InsufficientBalanceException e){
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }catch (NoSuchEntityException e) { // question not found
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

}
