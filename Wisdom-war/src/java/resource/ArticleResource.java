/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resource;

import entity.ArticleEntity;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
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
        } catch (EntityNotFoundException e) { // article not found
            LOGGER.log(Level.WARNING, "1. article w ID: {0} not found - " + e.getMessage(), articleId);
            return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "2. article w ID: {0} not found - " + e.getMessage(), articleId);
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
        } catch (EntityNotFoundException e) { // reader not found
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

}
