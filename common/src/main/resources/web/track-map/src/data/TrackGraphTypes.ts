export interface Network {
    id: String,
    connections: Connection[],
    nodes: Node[],
    colour: Colour
}

export interface Connection {
    length: number,
    first: Node,
    second: Node
}

export interface Node {
    dimension: String,
    x: number,
    y: number,
    z: number,
    id: number
}

export interface Vec2 {
    x: number,
    y: number
}

export interface Vec3 {
    x: number,
    y: number,
    z: number
}

export interface NetworkDiscovery {
    id: String,
    colour: Colour
}

export interface Colour {
    r: number,
    g: number,
    b: number,
    a: number
}

export interface Train {
    owner: String,
    speed: number,
    id: number,
    name: String,
    carriages: Carriage[],
    distance: number,
    passengers: number
}

export interface Carriage {
    stalled: boolean,
    id: number,
    twobogeys: boolean,
    leading: Vec3,
    trailing: Vec3
}