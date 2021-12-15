import type { number } from "yup";

export interface State {
  counter: number;
  sessionId?: string;
  room?: Room;
}

export interface Room {
  id: string;
  createdAt: string;
  status: "waiting" | "playing" | "ended";
  stage?: "waitResponses" | "turnEnded";
  round?: number;
  owner: string;
  players: string[];
  fights: Fight[];
  livePlayers: string[];
  deadPlayers: string[];
  results: Fight[][];
  lastResult?: Fight[];
  sessions: { [name: string]: Session };
}

export interface Session {
  id: string;
  isBot: boolean;
  avatarId: number;
  name: string;
}

export interface Fight {
  id: string;
  players: string[];
  responses: { [name: string]: 1 | 2 | 3 };
  round: number;
  winner: string;
}

export const initialState = {
  counter: 0,
  sessionId: undefined,
  room: undefined
};
