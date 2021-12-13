import { interval } from "rxjs";
import * as rx from "rxjs/operators";
import { StoreEvent } from "$store";
import type { State } from "$state";

export class AddValue extends StoreEvent<State> {
  constructor(private value: number) {
    super();
  }

  public update(state: State) {
    state.counter += this.value;
  }
}

export class StartCounter extends StoreEvent<State> {
  public watch() {
    return interval(1000).pipe(rx.map(() => new AddValue(1)));
  }
}

export class SetWebSocket extends StoreEvent<State> {
  constructor(private socket: WebSocket) {
    super();
  }

  public update(state: State) {
    state.socket = this.socket;
  }
}

export class CreateGame extends StoreEvent<State> {
  constructor() {
    super();
  }

  public update(state: State) {
    state.socket?.send(JSON.stringify({ a: "b" }));
  }
}
