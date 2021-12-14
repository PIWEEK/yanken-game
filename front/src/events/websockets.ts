import type { State } from "$state";
import { StoreEvent } from "$store";
import { Observable, Subject, of, merge } from "rxjs";
import { filter, take, map } from "rxjs/operators";
import type { Room } from "$state";

export class WebSocketRequest extends StoreEvent<State> {
  public requestId: string;
  constructor(requestId: string) {
    super();
    this.requestId = requestId;
  }
  public toJSON() {
    return {};
  }
}

export class HelloRequest extends WebSocketRequest {
  private name: string;

  constructor(name: string, requestId: string) {
    super(requestId);
    this.name = name;
  }
  public toJSON() {
    return {
      type: "request",
      name: "hello",
      requestId: this.requestId,
      playerName: this.name
    };
  }
}

export class JoinRequest extends WebSocketRequest {
  private roomId: string;

  constructor(roomId: string, requestId: string) {
    super(requestId);
    this.roomId = roomId;
  }
  public toJSON() {
    return {
      type: "request",
      name: "joinRoom",
      requestId: this.requestId,
      roomId: this.roomId
    };
  }
}

export class CreateGameRequest extends WebSocketRequest {
  public toJSON() {
    return {
      type: "request",
      name: "startGame"
    };
  }
}

export class SocketEvent extends StoreEvent<State> {
  public event: Event;
  constructor(event: Event) {
    super();
    this.event = event;
  }
  public update(_state: State) {
    console.log(`Socket event received: ${this.constructor.name}`);
  }
}

export class OpenSocketEvent extends SocketEvent {}

export class CloseSocketEvent extends SocketEvent {}

export class ErrorSocketEvent extends SocketEvent {}

export class MessageSocketEvent extends SocketEvent {
  requestId: string;
  type: string;
  constructor(event: Event) {
    super(event);
    const data = JSON.parse((event as MessageEvent).data);
    this.requestId = data.requestId;
    this.type = data.type;
  }
}

export class Update extends StoreEvent<State> {
  public sessionId?: string;
  public room?: Room;
  constructor(sessionId?: string, room?: Room) {
    super();
    this.sessionId = sessionId;
    this.room = room;
  }

  public update(state: State) {
    if (this.sessionId) {
      state.sessionId = this.sessionId;
      console.log("Updating sessionId:", this.sessionId);
    }
    if (this.room) {
      state.room = this.room;
      console.log("Updating room:", this.room.status, this.room.stage, this.room);
    }
  }
}

export class Action extends StoreEvent<State> {
  public requestId: string;
  constructor() {
    super();
    this.requestId = Date.now().toString();
  }
  public watch(state: State, stream: Observable<StoreEvent<State>>) {
    const requestId = this.requestId;
    const updateStream = stream.pipe(
      filter((ev: StoreEvent<State>) => ev instanceof MessageSocketEvent && ev.requestId === requestId),
      take(1),
      map((e) => {
        const data = JSON.parse(((e as MessageSocketEvent).event as MessageEvent).data);
        const sessionId = data.sessionId;
        const room = data.room;
        return new Update(sessionId, room);
      })
    );
    return updateStream;
  }
}

export class Hello extends Action {
  private name: string;
  constructor(name: string) {
    super();
    this.name = name;
  }
  public watch(state: State, stream: Observable<StoreEvent<State>>) {
    return merge(super.watch(state, stream), of(new HelloRequest(this.name, this.requestId)));
  }
}

export class Join extends Action {
  private roomId: string;
  constructor(roomId: string) {
    super();
    this.roomId = roomId;
  }
  public watch(state: State, stream: Observable<StoreEvent<State>>) {
    return merge(super.watch(state, stream), of(new JoinRequest(this.roomId, this.requestId)));
  }
}

export class StartGame extends Action {
  public watch(state: State, stream: Observable<StoreEvent<State>>) {
    console.log("StartGame", this.requestId);
    return merge(super.watch(state, stream), of(new CreateGameRequest(this.requestId)));
  }
}

export class StartWebsocket extends StoreEvent<State> {
  public watch(_state: State, stream: Observable<StoreEvent<State>>) {
    const ws = new WebSocket(import.meta.env.VITE_BASE_URL);

    const socketEvents = new Subject<SocketEvent>();

    ws.addEventListener("open", (event: Event) => {
      socketEvents.next(new OpenSocketEvent(event));
    });

    ws.addEventListener("message", (event: Event) => {
      socketEvents.next(new MessageSocketEvent(event));
    });

    ws.addEventListener("close", (event: Event) => {
      socketEvents.next(new CloseSocketEvent(event));
    });

    ws.addEventListener("Error", (event: Event) => {
      socketEvents.next(new ErrorSocketEvent(event));
    });

    const requests = stream.pipe(filter((ev: StoreEvent<State>) => ev instanceof WebSocketRequest));
    requests.subscribe((ev) => {
      ws.send(JSON.stringify((ev as WebSocketRequest).toJSON()));
    });

    // Notifications
    const notifications = stream.pipe(
      filter((ev: StoreEvent<State>) => ev instanceof MessageSocketEvent && ev.type === "notification"),
      map((e) => {
        const data = JSON.parse(((e as MessageSocketEvent).event as MessageEvent).data);
        const sessionId = data.sessionId;
        const room = data.room;
        return new Update(sessionId, room);
      })
    );

    // Errors
    const errors = stream.pipe(filter((ev: StoreEvent<State>) => ev instanceof MessageSocketEvent && ev.type === "error"));
    errors.subscribe((e) => {
      console.error("TODO: Error,  do something with this", e);
    });

    return merge(notifications, socketEvents);
  }
}
