package de.dialogdata.aclabs.rest;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import de.dialogdata.aclabs.entities.UserBE;
import de.dialogdata.aclabs.service.IUserService;

@Stateless
@Path("/users")
public class UserBEEndpoint {

	@EJB
	private IUserService userService;

	@POST
	@Consumes("application/json")
	public Response create(UserBE entity) {
		userService.createOrUpdate(entity);
		return Response.created(
				UriBuilder.fromResource(UserBEEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		UserBE entity = userService.findUser(id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		UserBE entity = userService.findUser(id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	@GET
	@Produces("application/json")
	public Response listAll(@QueryParam("groupid")String groupId) {
		if(groupId!=null){
			Long id = null;
			try{
				id = Long.parseLong(groupId);
				return Response.ok(userService.getUsersForGroup(id)).build();
			}catch(Exception e){
				return Response.status(Status.BAD_REQUEST).build();
			}
		}
		return Response.ok(userService.findAll()).build();
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(UserBE entity) {
		userService.createOrUpdate(entity);
		return Response.noContent().build();
	}
}