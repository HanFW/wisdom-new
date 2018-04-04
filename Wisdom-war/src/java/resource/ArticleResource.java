/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import entity.ArticleEntity;
import entity.AuthorEntity;
import entity.ReaderEntity;
import exception.InsufficientBalanceException;
import exception.NoSuchEntityException;
import exception.RepeatActionException;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import sessionBean.ArticleSessionBeanLocal;

/**
 * REST Web Service
 *
 * @author Chuck
 */
@Path("article")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ArticleResource {

    private static final Logger LOGGER
            = Logger.getLogger(ArticleResource.class.getName());
    private static ConsoleHandler handler = null;

    @Context
    private UriInfo context;
    @EJB
    private ArticleSessionBeanLocal articleSessionBean;

    /**
     * Creates a new instance of ArticleResource
     */
    public ArticleResource() {
        handler = new ConsoleHandler();
        handler.setLevel(Level.FINEST);
        LOGGER.setLevel(Level.ALL);
        LOGGER.addHandler(handler);
    }

    @Path("{id}")
    @GET
    public Response getArticleById(@PathParam("id") final Long articleId) {
        try {
            ArticleEntity article = articleSessionBean.getArticleById(articleId);
            if (article != null) { // success
                return Response.ok().entity(article).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity("missing data").build();
            }
        } catch (NoSuchEntityException e) { // article not found
            LOGGER.log(Level.FINEST, "1. article w ID: {0} not found - " + e.getMessage(), articleId);
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            LOGGER.log(Level.FINEST, "2. article w ID: {0} not found - " + e.getMessage(), articleId);
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @Path("newest")
    @GET
    public Response getNewestArticlesFromFollowedAuthors(
            @QueryParam("readerId") final Long readerId) {
        try {
            List<ArticleEntity> articles
                    = articleSessionBean.getNewestArticlesOfFollowedAuthors(readerId);
            if (articles != null) { // success
                GenericEntity<List<ArticleEntity>> response = new GenericEntity<List<ArticleEntity>>(articles) {
                };
                return Response.ok().entity(articles).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity("missing data").build();
            }
        } catch (NoSuchEntityException e) { // reader not found
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @Path("mostLiked")
    @GET
    public Response getMostLikedArticlesOfTopic(
            @QueryParam("topic") final String topic) {
        List<ArticleEntity> articles
                = articleSessionBean.getMostLikedArticlesOfTopic(topic);
        if (articles != null) { // success
            GenericEntity<List<ArticleEntity>> response = new GenericEntity<List<ArticleEntity>>(articles) {
            };
            return Response.ok().entity(articles).build();
        } else {
            return Response.status(Status.BAD_REQUEST).entity("missing data").build();
        }
    }
    
    
    @Path("{id}/save/{articleId}")
    @PUT
    public Response saveArticle(
            @PathParam("id") final Long readerId,
            @PathParam("articleId") final Long articleId) {
        try {            
            ReaderEntity reader = articleSessionBean.saveArticle(readerId,articleId);
            
            if (reader != null) { // success
                return Response.ok().entity(reader).build();
            } else {
                return Response.status(Status.NOT_FOUND).entity("entity not found").build();
            }
        } catch (RepeatActionException e) { // article already saved
            return Response.status(Status.CONFLICT).entity(e.getMessage()).build();
        } catch (Exception e) { // invalid JsonArray, etc
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("{id}/unsave/{articleId}")
    @PUT
    public Response unsaveArticle(
            @PathParam("id") final Long readerId,
            @PathParam("articleId") final Long articleId) {
        try {            
            ReaderEntity reader = articleSessionBean.unsaveArticle(readerId,articleId);
            
            if (reader != null) { // success
                return Response.ok().entity(reader).build();
            } else {
                return Response.status(Status.NOT_FOUND).entity("entity not found").build();
            }
        } catch (RepeatActionException e) { // article has not been saved
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) { // invalid JsonArray, etc
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("{id}/checkSaved/{articleId}")
    @GET
    public Response checkSaved(
            @PathParam("id") final Long readerId,
            @PathParam("articleId") final Long articleId) {
        try {            
            Boolean result = articleSessionBean.checkArticleSaved(readerId,articleId);
            
            if (result != null) { // success
                 JsonObject json = Json.createObjectBuilder().add("result", result).build();
                return Response.ok().entity(json).build();
            } else {
                return Response.status(Status.NOT_FOUND).entity("reader or article not found").build();
            }
        } catch (Exception e) { // invalid JsonArray, etc
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("{id}/getSavedArticles")
    @GET
    public Response getSavedArticles(
            @PathParam("id") final Long readerId){
        try {
            List<ArticleEntity> articles = articleSessionBean.getAllSavedArticles(readerId);
            if (articles != null) {
                GenericEntity<List<ArticleEntity>> response 
                        = new GenericEntity<List<ArticleEntity>>(articles){};
                
                return Response.ok().entity(response).build();
            } else {
                return Response.status(Status.NOT_FOUND).entity("reader or article not found").build();
            }
        } catch (Exception e) { // invalid JsonArray, etc
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @Path("{id}/tip/{articleId}")
    @PUT
    public Response tip(
            @PathParam("id") final Long readerId,
            @PathParam("articleId") final Long articleId,
            final JsonObject request) {
        try {            
            
            Double amount = Double.valueOf(request.getString("amount"));
            ReaderEntity reader = articleSessionBean.tipArticle(readerId, articleId, amount);
            
            if (reader != null) { // success
                return Response.ok().entity(reader).build();
            } else {
                return Response.status(Status.BAD_REQUEST).entity("reader entity not found").build();
            }
        } catch(InsufficientBalanceException e){// ..should check at front-end
            return Response.status(Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        } catch (Exception e) { // invalid JsonArray, etc
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}
