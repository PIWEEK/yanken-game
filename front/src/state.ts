export interface State {
  screen?: string;
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
  options: {
    gameEndScreenTimeout: number;
    gameScreenTimeout: number;
    pairingScreenTimeout: number;
    postRoundTimeout: number;
    resultScreenTimeout: number;
    roundTimeout: number;
  };
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
  if (typeof window !== "undefined") {
    return localStorage.getItem(key);
  }
  return null;
}

export const initialState = {
  session: JSON.parse(getLocalStorage("session") || "{}"),
  room: JSON.parse(getLocalStorage("room") || "{}")
};
