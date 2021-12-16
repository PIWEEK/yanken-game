import { StoreEvent } from "$store";
import type { State } from "$state";

export * from "./websockets";

export class ChangeScreen extends StoreEvent<State> {
  constructor(private screen: string) {
    super();
  }

  public update(state: State) {
    state.screen = this.screen;
  }
}

export class SetSessionName extends StoreEvent<State> {
  constructor(private name: string) {
    super();
  }

  public update(state: State) {
    if (!state.session) {
      state.session = {};
    }
    state.session.name = this.name;
  }
}
