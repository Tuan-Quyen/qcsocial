package org.social.controller;

import io.smallrye.mutiny.Uni;
import org.social.model.room.Room;
import org.social.service.RoomService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/rooms")
public class RoomController extends BaseController<Room> {
    @Inject
    RoomService roomService;

    @GET
    public Uni<Response> getAll() {
        return resFindAll();
    }

    @GET
    @Path("/search")
    public Uni<Response> search(@QueryParam("name") String name, @QueryParam("id") Long id) {
        if (id != null) {
            return resFindBy("id", id);
        }
        return resFindBy("name", name);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> create(Room room) {
        return resCreate(room);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> update(Room room) {
        return resUpdate(room);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(@QueryParam("name") String name, @QueryParam("id") String id) {
        if (id != null) {
            return resDeleteId(id);
        }
        return resDelete(name);
    }

    @Override
    protected List<Room> handleFindAll() {
        return roomService.findAll();
    }

    @Override
    protected Room handleFindBy(String query, Object object) {
        if (query.equals("id")) {
            return roomService.findById(String.valueOf(object));
        }
        return roomService.findByName(String.valueOf(object));
    }

    @Override
    protected Room handleCreate(Room room) {
        return roomService.register(room);
    }

    @Override
    protected Room handleUpdate(Room room) {
        return roomService.update(room);
    }

    @Override
    protected void handleDelete(String name) {
        roomService.deleteByName(name);
    }

    @Override
    protected void handleDeleteId(String id) {
        roomService.deleteById(id);
    }
}
