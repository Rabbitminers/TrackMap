export interface Network {
    id: String,
    connections: Connection[]
    nodes: Node[]
}

export interface Connection {
    length: number,
    first: number,
    second: number
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