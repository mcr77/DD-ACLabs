package de.dialogdata.aclabs.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import de.dialogdata.aclabs.entities.UserBE;

/**
 * 
 */
@Stateless
@Path("/userbes")
public class UserBEEndpoint
{
   @PersistenceContext(unitName = "forge-default")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(UserBE entity)
   {
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(UserBEEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      UserBE entity = em.find(UserBE.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      em.remove(entity);
      return Response.noContent().build();
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<UserBE> findByIdQuery = em.createQuery("SELECT DISTINCT u FROM UserBE u LEFT JOIN FETCH u.group LEFT JOIN FETCH u.attendances WHERE u.id = :entityId ORDER BY u.id", UserBE.class);
      findByIdQuery.setParameter("entityId", id);
      UserBE entity;
      try
      {
         entity = findByIdQuery.getSingleResult();
      }
      catch (NoResultException nre)
      {
         entity = null;
      }
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      return Response.ok(entity).build();
   }

   @GET
   @Produces("application/json")
   public List<UserBE> listAll()
   {
      final List<UserBE> results = em.createQuery("SELECT DISTINCT u FROM UserBE u LEFT JOIN FETCH u.group LEFT JOIN FETCH u.attendances ORDER BY u.id", UserBE.class).getResultList();
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(UserBE entity)
   {
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}