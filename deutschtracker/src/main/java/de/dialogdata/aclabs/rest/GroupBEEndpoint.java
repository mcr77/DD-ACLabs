package de.dialogdata.aclabs.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import de.dialogdata.aclabs.entities.GroupBE;
import de.dialogdata.aclabs.service.IGroupService;

/**
 * 
 */
@Stateless
@Path("/groups")
public class GroupBEEndpoint {

	@EJB
	private IGroupService groupService;

	@POST
	@Consumes("application/json")
	public Response create(GroupBE entity) {
		groupService.createOrUpdate(entity);
		return Response.created(
				UriBuilder.fromResource(GroupBEEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		GroupBE entity = groupService.findGroup(id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		groupService.deleteGroup(id);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		GroupBE entity = groupService.findGroup(id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	@GET
	@Produces("application/json")
	public List<GroupBE> listAll() {
		return groupService.findAll();
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(GroupBE entity) {
		groupService.createOrUpdate(entity);
		return Response.noContent().build();
	}
}