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
import de.dialogdata.aclabs.entities.GroupCourseBE;

/**
 * 
 */
@Stateless
@Path("/groupcoursebes")
public class GroupCourseBEEndpoint
{
   @PersistenceContext(unitName = "forge-default")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(GroupCourseBE entity)
   {
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(GroupCourseBEEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      GroupCourseBE entity = em.find(GroupCourseBE.class, id);
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
      TypedQuery<GroupCourseBE> findByIdQuery = em.createQuery("SELECT DISTINCT g FROM GroupCourseBE g LEFT JOIN FETCH g.group LEFT JOIN FETCH g.userAttendances WHERE g.id = :entityId ORDER BY g.id", GroupCourseBE.class);
      findByIdQuery.setParameter("entityId", id);
      GroupCourseBE entity;
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
   public List<GroupCourseBE> listAll()
   {
      final List<GroupCourseBE> results = em.createQuery("SELECT DISTINCT g FROM GroupCourseBE g LEFT JOIN FETCH g.group LEFT JOIN FETCH g.userAttendances ORDER BY g.id", GroupCourseBE.class).getResultList();
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(GroupCourseBE entity)
   {
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}