export interface State {
  counter: number;
  sessionId?: string;
}

export const initialState = {
  counter: 0,
  sessionId: undefined
};
