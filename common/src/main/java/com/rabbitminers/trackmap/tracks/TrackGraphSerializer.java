package com.rabbitminers.trackmap.tracks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.trains.TrackEdge;
import com.simibubi.create.content.logistics.trains.TrackGraph;
import com.simibubi.create.content.logistics.trains.TrackNode;
import com.simibubi.create.content.logistics.trains.TrackNodeLocation;
import com.simibubi.create.content.logistics.trains.entity.Carriage;
import com.simibubi.create.content.logistics.trains.entity.Train;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

public class TrackGraphSerializer {
    public static JsonArray serializeTrains() {
        Set<Entry<UUID, Train>> trains = Create.RAILWAYS.trains.entrySet();
        return trains.stream().map(train -> serializeTrain(train.getValue()))
                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
    }

    @Nullable
    public static JsonObject serializeTrain(UUID uuid) {
        Map<UUID, Train> trains = Create.RAILWAYS.trains;
        Train train = trains.get(uuid);
        if (train == null) return null;
        return serializeTrain(train);
    }

    public static JsonObject serializeTrain(Train train) {
        JsonObject object = new JsonObject();
        object.addProperty("owner", train.owner.toString());
        object.addProperty("speed", train.speed);
        object.addProperty("id", train.speed);
        object.addProperty("name", train.name.toString());
        JsonArray carriages = train.carriages.stream().map(TrackGraphSerializer::serializeCarriage)
                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
        object.add("carriages", carriages);
        object.addProperty("distance", train.navigation.distanceToDestination);
        object.addProperty("passengers", train.countPlayerPassengers());
        return object;
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static JsonObject serializeCarriage(Carriage carriage) {
        JsonObject object = new JsonObject();
        object.addProperty("stalled", carriage.stalled);
        object.addProperty("id", carriage.id);
        object.addProperty("twobogeys", carriage.isOnTwoBogeys());
        JsonObject leading = new JsonObject();
        writePosition(leading, carriage.getLeadingPoint().getPosition());
        object.add("leading", leading);
        JsonObject trailing = new JsonObject();
        writePosition(trailing, carriage.getTrailingPoint().getPosition());
        object.add("trailing", trailing);
        return object;
    }

    public static JsonArray getAllNetworks() {
        Set<Entry<UUID, TrackGraph>> networks = Create.RAILWAYS.trackNetworks.entrySet();
        return networks.stream().map(network -> {
            JsonObject object = new JsonObject();
            object.addProperty("id", network.getKey().toString());
            object.add("colour", serializeColour(network.getValue().color));
            return object;
        }).collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
    }

    public static JsonObject serializeColour(Color color) {
        JsonObject object = new JsonObject();
        object.addProperty("r", color.getRed());
        object.addProperty("g", color.getGreen());
        object.addProperty("b", color.getBlue());
        object.addProperty("a", color.getAlpha());
        return object;
    }

    public static JsonArray serializeAllNodes() {
        Set<Entry<UUID, TrackGraph>> networks = Create.RAILWAYS.trackNetworks.entrySet();
        return networks.stream().map(network -> serializeNetworkNodes(network.getValue()))
                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
    }

    public static JsonArray serializeAllConnections() {
        Set<Entry<UUID, TrackGraph>> networks = Create.RAILWAYS.trackNetworks.entrySet();
        return networks.stream().map(network -> serializeNetworkConnections(network.getValue()))
                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
    }

    @Nullable
    public static JsonObject serializeNetworkConnections(UUID networkId) {
        JsonObject object = new JsonObject();
        TrackGraph graph = Create.RAILWAYS.trackNetworks.get(networkId);
        if (graph == null) return null;
        return serializeNetworkConnections(graph);
    }

    public static JsonObject serializeNetworkConnections(TrackGraph graph) {
        JsonObject object = new JsonObject();
        object.addProperty("id", graph.id.toString());
        JsonArray connections = serializeConnections(graph);
        object.add("connections", connections);
        return object;
    }

    @Nullable
    public static JsonObject serializeNetworkNodes(UUID networkId) {
        JsonObject object = new JsonObject();
        TrackGraph graph = Create.RAILWAYS.trackNetworks.get(networkId);
        if (graph == null) return null;
        return serializeNetworkNodes(graph);
    }

    public static JsonObject serializeNetworkNodes(TrackGraph graph) {
        Set<TrackNodeLocation> nodes = graph.getNodes();
        JsonObject object = new JsonObject();
        object.addProperty("id", graph.id.toString());
        JsonArray serializedNodes = nodes.stream().map(graph::locateNode)
                .map(TrackGraphSerializer::serializeNode)
                .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
        object.add("nodes", serializedNodes);
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
        final Set<Integer> existing = new HashSet<>();
        return graph.getNodes().stream()
                .map(graph::locateNode)
                .flatMap(node -> graph.getConnectionsFrom(node).values().stream())
                .filter(connection -> filterConnection(connection, existing))
                .collect(JsonArray::new, (array, edge) -> array.add(serializeConnection(edge)), JsonArray::addAll);
    }

    private static boolean filterConnection(TrackEdge connection, Set<Integer> existing) {
        int node1ID = connection.node1.getNetId(), node2ID = connection.node2.getNetId();
        int hashCode = (node1ID * 31) + node2ID;
        boolean exists = existing.contains(hashCode);
        if (!exists) existing.add(hashCode);
        return !exists;
    }

    private static JsonObject serializeConnection(TrackEdge edge) {
        JsonObject connection = new JsonObject();
        connection.addProperty("length", edge.getLength());
        connection.add("first", serializeNode(edge.node1));
        connection.add("second", serializeNode(edge.node2));
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
