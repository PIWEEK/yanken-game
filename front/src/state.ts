export interface State {
  counter: number;
  session?: Session;
  room?: Room;
}

export interface Room {
  id: string;
  createdAt: string;
  status: "waiting" | "playing" | "ended";
  stage?: "pairing" | "game" | "gameEnd" | "result";
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
  avatar: string;
  name: string;
}

export interface Fight {
  id: string;
  players: string[];
  responses: { [name: string]: 1 | 2 | 3 };
  round: number;
  winner: string;
}

function getLocalStorage(key: string) {
  if (localStorage) {
    return localStorage.getItem(key);
  }
  return null;
}

export const initialState = {
  counter: 0,
  session: JSON.parse(getLocalStorage("session") || "{}"),
  room: JSON.parse(getLocalStorage("room") || "{}")
};

