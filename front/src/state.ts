export interface State {
  counter: number;
  socket?: WebSocket;
}

export const initialState = {
  counter: 0,
  socket: undefined
};
