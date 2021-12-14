export interface State {
  counter: number;
  sessionId?: string;
  room?: Room;
}

export interface Room {
  id: string;
  status: string;
  players: string[];
  owner: string;
  sessions: Session[];
}

export interface Session {
  id: string;
  avatarId: number;
  name: string;
  connectionId: string;
  roomId: string;
}

export const initialState = {
  counter: 0,
  sessionId: undefined,
  room: undefined
};
