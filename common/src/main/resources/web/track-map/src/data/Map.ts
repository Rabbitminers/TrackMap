import { testData } from "./TestData";
import type { Network, Node, Connection, Vec2 } from "./TrackGraphTypes";

export default class TrackMap {
  private canvas!: HTMLCanvasElement;
  private ctx!: CanvasRenderingContext2D;
  private scale: number = 1;
  private translateX: number = 0;
  private translateY: number = 0;
  private isPanning: boolean = false; // new flag
  private sensitivity: number = 0.1;

  constructor(canvas: HTMLCanvasElement) {
    this.canvas = canvas;
    this.ctx = canvas.getContext("2d") as CanvasRenderingContext2D;
    this.canvas.addEventListener("mousedown", this.handleMouseDown.bind(this));
    this.canvas.addEventListener("mousemove", this.handleMouseMove.bind(this));
    this.canvas.addEventListener("mouseup", this.handleMouseUp.bind(this));
    this.canvas.addEventListener("mouseleave", this.handleMouseLeave.bind(this));
  }

  public init() {
    this.ctx.fillStyle = "ffffff";

    testData.forEach((network) => this.renderNetwork(network));
  }

  private renderNetwork(network: Network) {
    console.log("Rendered", network.id);
    network.nodes.forEach((node) => this.renderNode(node));
    network.connections.forEach((connection) => this.renderConnection(connection, network.nodes))
  }

  private renderNode(node: Node) {
    this.ctx.beginPath();
    this.ctx.fillStyle = "#ffffff"
    this.ctx.arc(node.x * this.scale + this.translateX, node.z * this.scale + this.translateY, 2, 0, 2 * Math.PI);
    this.ctx.fill();
  }

  private renderConnection(connection: Connection, nodes: Node[]) {
    console.log("Drew Connection For ", connection)
    let firstNodePos: Vec2 = {x: 0, y: 0};
    let secondNodePos: Vec2 = {x: 0, y: 0};
    for (const node of nodes) {
        if (node.id == connection.first) {
            console.log("Found node " + node)
            firstNodePos = {x: node.x, y: node.z}
        }
        if (node.id == connection.second) {
            console.log("Found Second Node " + node)
            secondNodePos = {x: node.x, y: node.z}
        }
        if (firstNodePos && secondNodePos) {
            break;
        }
    };

    /* DRAW A LINE FROM fristNodePos TO SecondNodePos*/
    this.ctx.beginPath();
    this.ctx.moveTo(firstNodePos.x * this.scale + this.translateX, firstNodePos.y * this.scale + this.translateY);
    this.ctx.lineTo(secondNodePos.x * this.scale + this.translateX, secondNodePos.y * this.scale + this.translateY);
    this.ctx.stroke();
  }

  private handleMouseDown(event: MouseEvent) {
    this.canvas.style.cursor = "grabbing";
    this.canvas.addEventListener("mousemove", this.handlePan.bind(this));
    this.isPanning = true; // set flag to true when mouse button is pressed
  }

  private handleMouseMove(event: MouseEvent) {
    if (event.buttons !== 1) {
      this.canvas.style.cursor = "grab";
      this.canvas.removeEventListener("mousemove", this.handlePan.bind(this));
      this.isPanning = false; // set flag to false when mouse button is released
    }
  }

  private handleMouseUp(event: MouseEvent) {
    this.canvas.style.cursor = "grab";
    this.canvas.removeEventListener("mousemove", this.handlePan.bind(this));
    this.isPanning = false; // set flag to false when mouse button is released
  }

  private handleMouseLeave(event: MouseEvent) {
    this.canvas.style.cursor = "grab";
    this.canvas.removeEventListener("mousemove", this.handlePan.bind(this));
    this.isPanning = false; // set flag to false when mouse leaves canvas
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
    testData.forEach((network) => this.renderNetwork(network));
  }

  private clearCanvas() {
    this.ctx.clearRect(-this.translateX / this.scale, -this.translateY / this.scale, this.canvas.width / this.scale, this.canvas.height / this.scale);
  }
}
