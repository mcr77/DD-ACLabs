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
import de.dialogdata.aclabs.entities.GroupBE;

/**
 * 
 */
@Stateless
@Path("/groupbes")
public class GroupBEEndpoint
{
   @PersistenceContext(unitName = "forge-default")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(GroupBE entity)
   {
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(GroupBEEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      GroupBE entity = em.find(GroupBE.class, id);
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
      TypedQuery<GroupBE> findByIdQuery = em.createQuery("SELECT DISTINCT g FROM GroupBE g LEFT JOIN FETCH g.courses WHERE g.id = :entityId ORDER BY g.id", GroupBE.class);
      findByIdQuery.setParameter("entityId", id);
      GroupBE entity;
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
   public List<GroupBE> listAll()
   {
      final List<GroupBE> results = em.createQuery("SELECT DISTINCT g FROM GroupBE g LEFT JOIN FETCH g.courses ORDER BY g.id", GroupBE.class).getResultList();
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(GroupBE entity)
   {
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}