package org.social.controller;

import io.smallrye.mutiny.Uni;
import org.social.model.exception.ConflictException;
import org.social.model.exception.NotFoundException;
import org.social.model.response.BaseHttpContent;
import org.social.service.TokenUtils;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

public abstract class BaseController<T> extends BaseHttpContent {
    @Inject
    TokenUtils tokenUtils;

    protected abstract List<T> handleFindAll();

    protected abstract T handleFindBy(String query, Object object);

    protected abstract T handleCreate(T t);

    protected abstract T handleUpdate(T t);

    protected abstract void handleDelete(String t);

    protected abstract void handleDeleteId(String t);

    public Uni<Response> resFindAll(SecurityContext ctx) {
        return Uni.createFrom().voidItem()
                .onItem().invoke(x -> tokenUtils.verify(ctx))
                .onItem().transform(x -> this.handleFindAll())
                .onItem().transform(this::fetched)
                .onFailure().recoverWithItem(ex -> internal(ex.getMessage()));
    }

    public Uni<Response> resFindBy(SecurityContext ctx, String query, Object reqObject) {
        return Uni.createFrom().item(reqObject)
                .onItem().invoke(x -> tokenUtils.verify(ctx))
                .onItem().transform(data -> handleFindBy(query, data))
                .onItem().transform(this::fetched)
                .onFailure(NotFoundException.class).recoverWithItem(ex -> notFound(ex.getMessage()))
                .onFailure().recoverWithItem(ex -> internal(ex.getMessage()));
    }

    public Uni<Response> resCreate(SecurityContext ctx, T reqObject) {
        return Uni.createFrom().item(reqObject)
                .onItem().invoke(x -> tokenUtils.verify(ctx))
                .onItem().transform(this::handleCreate)
                .onItem().transform(this::created)
                .onFailure(ConflictException.class).recoverWithItem(ex -> conflict(ex.getMessage()))
                .onFailure(NotFoundException.class).recoverWithItem(ex -> notFound(ex.getMessage()))
                .onFailure().recoverWithItem(ex -> internal(ex.getMessage()));
    }

    public Uni<Response> resUpdate(SecurityContext ctx, T reqObject) {
        return Uni.createFrom().item(reqObject)
                .onItem().invoke(x -> tokenUtils.verify(ctx))
                .onItem().transform(this::handleUpdate)
                .onItem().transform(data -> Response.ok(data).build())
                .onFailure(NotFoundException.class).recoverWithItem(ex -> notFound(ex.getMessage()))
                .onFailure().recoverWithItem(ex -> internal(ex.getMessage()));
    }

    public Uni<Response> resDelete(SecurityContext ctx, String reqObject) {
        return Uni.createFrom().item(reqObject)
                .onItem().invoke(x -> tokenUtils.verify(ctx))
                .onItem().invoke(this::handleDelete)
                .onItem().transform(data -> Response.ok(data).build())
                .onFailure(NotFoundException.class).recoverWithItem(ex -> notFound(ex.getMessage()))
                .onFailure().recoverWithItem(ex -> internal(ex.getMessage()));
    }

    public Uni<Response> resDeleteId(SecurityContext ctx, String reqObject) {
        return Uni.createFrom().item(reqObject)
                .onItem().invoke(x -> tokenUtils.verify(ctx))
                .onItem().invoke(this::handleDeleteId)
                .onItem().transform(data -> Response.ok(data).build())
                .onFailure(NotFoundException.class).recoverWithItem(ex -> notFound(ex.getMessage()))
                .onFailure().recoverWithItem(ex -> internal(ex.getMessage()));
    }
}
