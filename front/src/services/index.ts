export class GameManager {
  private static instance: GameManager = new GameManager();
  private socket?: WebSocket;

  constructor() {
    if (GameManager.instance) {
      throw new Error("Error: Instance failed: Use GameManager.getInstance() instead of new.");
    }
    GameManager.instance = this;
  }

  static getInstance() {
    return GameManager.instance;
  }

  setSocket(socket: WebSocket) {
    this.socket = socket;
    this.socket.addEventListener("open", (event: Event) => {
      console.log("WebSocket connection opened", event);
    });

    this.socket.addEventListener("message", (event: MessageEvent) => {
      console.log("WebSocket message received", event);
    });

    this.socket.addEventListener("error", (event) => {
      console.log("WebSocket error encountered", event);
    });

    this.socket.addEventListener("close", (event: CloseEvent) => {
      console.log("WebSocket connection closed", event);
    });
  }

  createGame() {
    console.log("Sending create game message");
    this.socket?.send(JSON.stringify({ a: "b" }));
  }
}
