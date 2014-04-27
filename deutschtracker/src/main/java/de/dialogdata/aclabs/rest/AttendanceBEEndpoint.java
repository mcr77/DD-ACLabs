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
import de.dialogdata.aclabs.entities.AttendanceBE;

/**
 * 
 */
@Stateless
@Path("/attendancebes")
public class AttendanceBEEndpoint
{
   @PersistenceContext(unitName = "forge-default")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(AttendanceBE entity)
   {
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(AttendanceBEEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      AttendanceBE entity = em.find(AttendanceBE.class, id);
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
      TypedQuery<AttendanceBE> findByIdQuery = em.createQuery("SELECT DISTINCT a FROM AttendanceBE a LEFT JOIN FETCH a.user LEFT JOIN FETCH a.groupcourse WHERE a.id = :entityId ORDER BY a.id", AttendanceBE.class);
      findByIdQuery.setParameter("entityId", id);
      AttendanceBE entity;
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
   public List<AttendanceBE> listAll()
   {
      final List<AttendanceBE> results = em.createQuery("SELECT DISTINCT a FROM AttendanceBE a LEFT JOIN FETCH a.user LEFT JOIN FETCH a.groupcourse ORDER BY a.id", AttendanceBE.class).getResultList();
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(AttendanceBE entity)
   {
      entity = em.merge(entity);
      return Response.noContent().build();
   }
}