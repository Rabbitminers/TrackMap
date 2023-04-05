package com.rabbitminers.trackmap.tracks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.trains.TrackEdge;
import com.simibubi.create.content.logistics.trains.TrackGraph;
import com.simibubi.create.content.logistics.trains.TrackNode;
import com.simibubi.create.content.logistics.trains.TrackNodeLocation;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.Map.Entry;

public class TrackGraphSerializer {
    public static JsonArray serializeNetworks() {
        Set<Entry<UUID, TrackGraph>> networks = Create.RAILWAYS.trackNetworks.entrySet();
        return networks.stream().map(network -> serializeNetwork(network.getValue()))
                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
    }

    private static JsonObject serializeNetwork(TrackGraph graph) {
        Set<TrackNodeLocation> nodes = graph.getNodes();
        JsonObject object = new JsonObject();
        object.addProperty("id", graph.id.toString());
        JsonArray serializedNodes = nodes.stream().map(graph::locateNode)
                .map(TrackGraphSerializer::serializeNode)
                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
        object.add("nodes", serializedNodes);
        JsonArray connections = serializeConnections(graph);
        object.add("connections", connections);
        return object;
    }

    private static JsonObject serializeNode(TrackNode node) {
        TrackNodeLocation location = node.getLocation();
        JsonObject object = new JsonObject();
        TrackGraphSerializer.writePosition(object, location);
        object.addProperty("id", node.getNetId());
        object.addProperty("dimension", location.getDimension().registry().toString());
        return object;
    }

    private static JsonArray serializeConnections(TrackGraph graph) {
        return graph.getNodes().stream()
                .map(graph::locateNode)
                .flatMap(node -> graph.getConnectionsFrom(node).values().stream())
                .collect(JsonArray::new, (array, edge) -> array.add(serializeConnection(edge)), JsonArray::addAll);
    }

    private static JsonObject serializeConnection(TrackEdge edge) {
        JsonObject connection = new JsonObject();
        connection.addProperty("length", edge.getLength());
        connection.addProperty("first", edge.node1.getNetId());
        connection.addProperty("second", edge.node2.getNetId());
        return connection;
    }

    private static void writePosition(JsonObject object, TrackNodeLocation location) {
        writePosition(object, location.getLocation());
    }

    private static void writePosition(JsonObject object, Vec3 pos) {
        object.addProperty("x", pos.x);
        object.addProperty("y", pos.y);
        object.addProperty("z", pos.z);
    }

    private static boolean areCouplesEqual(Couple<?> first, Couple<?> second) {
        List<?> a = first.stream().sorted().toList(), b = second.stream().sorted().toList();
        return a.get(0) == b.get(0) && a.get(1) == b.get(1);
    }
}
