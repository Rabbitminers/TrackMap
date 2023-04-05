package com.rabbitminers.trackmap.http.routes.base;

import com.rabbitminers.trackmap.http.util.HttpHelper;
import com.rabbitminers.trackmap.http.util.HttpMethod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.OutputStream;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public abstract class HttpHandler {
    protected final Map<HttpMethod, Consumer<OutputStream>> handlers = new EnumMap<>(HttpMethod.class);

    public void handle(OutputStream out, HttpMethod method) {
        Consumer<OutputStream> handler = this.getHandlerFromMethod(method);
        handler.accept(out);
    }

    @NotNull
    private Consumer<OutputStream> getHandlerFromMethod(HttpMethod method) {
        Consumer<OutputStream> handler = handlers.get(method);
        return handler == null ? HttpHandler::handleMissingMethod : handler;
    }

    public Collection<HttpMethod> validMethods() {
        return handlers.keySet();
    }

    private static void handleMissingMethod(OutputStream out) {
        HttpHelper.writeToOutputStream(out, "HTTP/1.1 405 Method Not Allowed\r\n\r\n");
    }

    public abstract String getPath();
}
