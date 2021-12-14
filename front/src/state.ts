import type { boolean } from "yup";

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
  sessions: { [name: string]: Session };
}

export interface Session {
  id: string;
  isBot: boolean;
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
