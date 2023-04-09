import type { Network, Node, Connection, NetworkDiscovery, Colour, Train, Carriage } from "./TrackGraphTypes";

export default class TrackMap {
  private canvas!: HTMLCanvasElement;
  private ctx!: CanvasRenderingContext2D;
  private scale: number = 1;
  private translateX: number = 0;
  private translateY: number = 0;
  private isPanning: boolean = false;
  private sensitivity: number = 0.1;

  private networks: Network[] = new Array();
  private trains: Train[] = new Array();

  constructor(canvas: HTMLCanvasElement) {
    this.canvas = canvas;
    this.ctx = canvas.getContext("2d") as CanvasRenderingContext2D;
    this.canvas.addEventListener("mousedown", this.handleMouseDown.bind(this));
    this.canvas.addEventListener("mousemove", this.handleMouseMove.bind(this));
    this.canvas.addEventListener("mouseup", this.handleMouseUp.bind(this));
    this.canvas.addEventListener("mouseleave", this.handleMouseLeave.bind(this));
    this.ctx.imageSmoothingQuality = 'high';

    setInterval(() => { 
      this.refreshData();
    }, 10000)
  }

  public async init() {
    this.ctx.fillStyle = "ffffff";
    await this.refreshData();
  }

  private async refreshData(): Promise<void> {
    const ids: NetworkDiscovery[] = await this.getNetworks();
    const networkDataPromises = ids.map(async (network: NetworkDiscovery) => {
      const nodes: Node[] = await this.getNodes(network.id);
      const connections: Connection[] = await this.getConnections(network.id);
      return {
         id: network.id, nodes: nodes, connections: connections, colour: network.colour };
    });
    this.networks = await Promise.all(networkDataPromises);
    this.trains = await this.getTrains();
    this.renderCanvas();
  }

  private async renderCanvas() {
    this.clearCanvas();
    this.networks.forEach(async (network: Network) => {
      this.ctx.fillStyle = this.colourToHex(network.colour);
      this.ctx.strokeStyle = this.colourToHex(network.colour);
      const nodes: Node[] = network.nodes;
      this.renderNodes(nodes);
      const connections: Connection[] = network.connections;
      this.renderConnections(connections);
    })

    this.ctx.fillStyle = '#ba4206';
    this.trains.forEach(async (train : Train) => this.renderTrain(train))
  }

  private async renderTrain(train: Train) {
    train.carriages.forEach(async (carriage: Carriage) => this.renderCarriage(carriage));
  }

  private async renderNodes(nodes: Node[]) {
    nodes.forEach(async (node: Node) => this.renderNode(node));
  }

  private async renderConnections(connections: Connection[]) {
    connections.forEach(async (connection: Connection) => this.renderConnection(connection));
  }

  private renderCarriage(carriage: Carriage) {
    this.ctx.beginPath();
    this.ctx.arc(carriage.leading.x * this.scale + this.translateX, carriage.leading.z * this.scale + this.translateY, 2, 0, 2 * Math.PI);
    this.ctx.fill();

    this.ctx.beginPath();
    this.ctx.arc(carriage.trailing.x * this.scale + this.translateX, carriage.trailing.z * this.scale + this.translateY, 2, 0, 2 * Math.PI);
    this.ctx.fill();
  }


  private renderNode(node: Node) {
    this.ctx.beginPath();
    this.ctx.arc(node.x * this.scale + this.translateX, node.z * this.scale + this.translateY, 2, 0, 2 * Math.PI);
    this.ctx.fill();
  }

  private renderConnection(connection: Connection) {
    this.ctx.beginPath();
    this.ctx.moveTo(connection.first.x * this.scale + this.translateX, connection.first.z * this.scale + this.translateY);
    this.ctx.lineTo(connection.second.x * this.scale + this.translateX, connection.second.z * this.scale + this.translateY);
    this.ctx.stroke();
  }

  private handleMouseDown(event: MouseEvent) {
    this.canvas.style.cursor = "grabbing";
    this.canvas.addEventListener("mousemove", this.handlePan.bind(this));
    this.isPanning = true;
  }

  private handleMouseMove(event: MouseEvent) {
    if (event.buttons !== 1) {
      this.canvas.style.cursor = "grab";
      this.canvas.removeEventListener("mousemove", this.handlePan.bind(this));
      this.isPanning = false;
    }
  }

  private handleMouseUp(event: MouseEvent) {
    this.canvas.style.cursor = "grab";
    this.canvas.removeEventListener("mousemove", this.handlePan.bind(this));
    this.isPanning = false;
  }

  private handleMouseLeave(event: MouseEvent) {
    this.canvas.style.cursor = "grab";
    this.canvas.removeEventListener("mousemove", this.handlePan.bind(this));
    this.isPanning = false;
  }

  private handlePan(event: MouseEvent) {
    if (!this.isPanning) {
      return;
    }
    const dX: number = event.movementX * this.sensitivity;
    const dY: number = event.movementY * this.sensitivity;

    this.translateX += dX;
    this.translateY += dY;
    this.ctx.translate(dX, dY);
    this.clearCanvas();
    this.renderCanvas();
  }

  private clearCanvas() {
    this.ctx.clearRect(-this.translateX / this.scale, -this.translateY / this.scale, this.canvas.width / this.scale, this.canvas.height / this.scale);
  }

  
  private colourToHex(colour: Colour): string {
    const toHex = (value: number) => {
      var hexadecimal = value.toString(16);
      return hexadecimal.length == 1 ? "0" + hexadecimal : hexadecimal;
    }
    return "#" + toHex(colour.r) + toHex(colour.g) + toHex(colour.b);
  }

  private async getNetworks(): Promise<NetworkDiscovery[]> {
    return fetch('http://127.0.0.1:8080/networks')
      .then(response => {
        if (response.status == 200) {
          return response.json()
        } else {
          throw new Error("Request failed")
        }
      })
      .then(data => {
        if (Array.isArray(data)) {
          return data;
        } else {
          throw new Error("Unexpected response")
        }
      })
      .catch(error => {
        throw new Error(`Failed to collect network data: ${error.message}`)
      });
  }

  private async getNodes(network: String): Promise<Node[]> {
    return fetch(`http://127.0.0.1:8080/nodes/:${network}:`)
      .then(response => {
        if (response.status == 200) {
          return response.json()
        } else {
          throw new Error("Request failed")
        }
      })
      .then(data => {
          return data.nodes;
      })
      .catch(error => {
        throw new Error(`Failed to collect node data for network ${network}: ${error.message}`)
      })
  }

  private async getConnections(network: String): Promise<Connection[]> {
    return fetch(`http://127.0.0.1:8080/connections/:${network}:`)
      .then(response => {
        if (response.status == 200) {
          return response.json()
        } else {
          throw new Error("Request failed")
        }
      })
      .then(data => {
        return data.connections;
      })
      .catch(error => {
        throw new Error(`Failed to collect connection data for network ${network}: ${error.message}`)
      })
  }

  private async getTrains(): Promise<Train[]> {
    return fetch(`http://127.0.0.1:8080/trains`)
    .then(response => {
        if (response.status == 200) {
          return response.json()
        } else {
          throw new Error("Request failed")
        }
      })
    .catch(error => {
      throw new Error(`Failed to collect trian data: ${error.message}`)
    })
  }
}
