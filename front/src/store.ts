import { getContext, setContext } from "svelte";
import { Observable, Subject, EMPTY, concat, of } from "rxjs";
import * as rx from "rxjs/operators";
import produce from "immer";

const STORE_CONTEXT_KEY = "store";

export abstract class StoreEvent<State> {
  update(_state: State): void {}

  watch(_state: State, _events: Observable<StoreEvent<State>>): Observable<StoreEvent<State>> {
    return EMPTY;
  }
}

export class Store<State> {
  private state$: Observable<State>;
  private event$ = new Subject<StoreEvent<State>>();

  constructor(initialState: State) {
    this.state$ = concat(
      of(initialState),
      this.event$.pipe(
        rx.scan((state, e) => produce(state, e.update.bind(e)), initialState),
        rx.catchError((err) => {
          console.error("Error", err);
          return this.state$;
        }),
        rx.shareReplay(1)
      )
    );

    const watch$: Observable<StoreEvent<State>> = this.event$.pipe(
      rx.withLatestFrom(this.state$),
      rx.mergeMap(([e, state]) => e.watch(state, this.event$)),
      rx.catchError((err) => {
        console.error("Error", err);
        return watch$;
      })
    );

    watch$.subscribe(this.event$);
  }

  get state(): Observable<State> {
    return this.state$;
  }

  // Send a new event to the event bus
  emit(event: StoreEvent<State>) {
    this.event$.next(event);
  }

  // Retrieves a stream with the values
  select<T>(selector: (st: State) => T): Observable<T> {
    return this.state$.pipe(
      rx.map(selector),
      rx.distinct()
    );
  }
}

export function start<State>(initialState: State) {
  setContext(STORE_CONTEXT_KEY, new Store(initialState));
}

export function get<State>(): Store<State> {
  return getContext(STORE_CONTEXT_KEY);
}

export default {
  StoreEvent,
  Store,
  start,
  get
};
