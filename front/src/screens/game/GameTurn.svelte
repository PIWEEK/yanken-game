<script lang="ts">
 import { onMount, onDestroy } from "svelte";
 import store from "$store";
 import type { Room, Session, Fight, State } from "$state";
 import { SendTurn } from "$events";

 import rock from "$lib/images/rock.png";
 import paper from "$lib/images/paper.png";
 import scissors from "$lib/images/scissors.png";
 import background from "$lib/images/play-bg.png";
 import handsBg from "$lib/images/hands-bg.png";
 import selectBg from "$lib/images/select.png";
 import vsLetters from "$lib/images/versus-live.gif";

 import MatchData from "$components/MatchData.svelte";
 import PlayerCard from "$components/PlayerCard.svelte";
 import Timer from "$components/Timer.svelte"

 const RESPONSES = {
   1: "rock",
   2: "paper",
   3: "scissors",
 };

 const TURNS = {
   "rock": 1,
   "paper": 2,
   "scissors": 3,
 };

 function isObservedFight(fight: Fight, observedId?: string): boolean {
   return !!observedId && fight.players.includes(observedId);
 }

 function getPairings(room?: Room): Fight[] {
   if (room?.stage == "game" && room?.fights) {
     return room?.fights;
   }

   if (room?.results && room.results[0]) {
     return room.results[0];
   }

   return [];
 }

 function isPlayerDead(room?: Room, sessionId?: string) {
   if (!sessionId || room?.livePlayers?.includes(sessionId)) {
     return false;
   }

   if (room?.stage === "game") {
     return room?.deadPlayers?.includes(sessionId);
   }

   if (room?.stage === "gameEnd") {
     // If it's dead we need to check if has died this round. The player is only dead if it began the round dead
     const pairings = getPairings(room);
     const currentFight = pairings.find((f) => isObservedFight(f, sessionId));

     // We haven't found a fight so the player was dead before starting the round
     return !currentFight;
   }

   return false
 }

 function getRivalSessionId(room?: Room, sessionId?: string): string | undefined {
   const pairings = getPairings(room);
   const currentFight = pairings.find((f) => isObservedFight(f, sessionId));
   const otherSessionId = currentFight?.players.find((id) => id !== sessionId);
   return otherSessionId;
 }

 function getOthersPairings(room?: Room, sessionId?: string): Session[][] {
   const sessions = room?.sessions;

   if (!sessions || !room) {
     return [];
   }

   const pairings = getPairings(room);

   return pairings
   .filter((f) => !isObservedFight(f, sessionId))
   .map((f) => f.players.map((p) => sessions[p]));
 }

 function getLastPlays(room?: Room): Record<string, string[]> {
   let newLastPlays = {} as Record<string, string[]>;

   // We skip the first result if the game has ended
   const fromIdx = (room?.stage === "gameEnd") ? 1 : 0;
   room?.results?.slice(fromIdx).forEach((round) =>{
     round.forEach((fight) => {
       fight.players.forEach((player) => {
         const r = fight.responses[player];
         if (r) {
           if (!newLastPlays[player]) {
             newLastPlays[player] = [];
           }
           newLastPlays[player].push(RESPONSES[r]);
           newLastPlays[player] = newLastPlays[player].slice(0, 2);
         }
       });
     })
   });

   return newLastPlays;
 }

 interface ResultType {
   myResult?: "win" | "loss";
   myPick?: "rock" | "paper" | "scissors";
   rivalResult?: "win" | "loss";
   rivalPick?: "rock" | "paper" | "scissors";
 }

 function getResults(room?: Room, sessionId?: string): ResultType {
   // Save current round
   if (room?.stage !== "gameEnd") {
     return {};
   }

   // const observeId = isPlayerDead(room, currentSessionId) ? (room?.livePlayers && room.livePlayers[0]) : currentSessionId;
   const rivalSessionId = getRivalSessionId(room, sessionId);

   if (!sessionId || !rivalSessionId) {
     return {};
   }

   let pairings = room.results[0];
   let result : Fight | undefined;
   if (pairings) {
     result = pairings.find(f => isObservedFight(f, sessionId));
   }

   if (!result) {
     return {};
   }

   let myResult: ResultType['myResult'];
   let rivalResult: ResultType['rivalResult'];
   let myPick: ResultType['myPick']
   let rivalPick: ResultType['rivalPick'];

   console.log(result);
   if (result.winner === "both") {
     myResult = "win";
     rivalResult = "win";
   } else if (result.winner === "nobody") {
     myResult = "loss";
     rivalResult = "loss";
   } else if (result.winner === sessionId) {
     myResult = "win";
     rivalResult = "loss";
   } else {
     myResult = "loss";
     rivalResult = "win";
   }

   const observeResponse = result.responses[sessionId] as 1 | 2 | 3 | undefined;
   if (observeResponse) {
     myPick = RESPONSES[observeResponse] as ResultType['myPick'];
   }

   const rivalResponse = result.responses[rivalSessionId] as 1 | 2 | 3 | undefined;
   if (rivalResponse) {
     rivalPick = RESPONSES[rivalResponse] as ResultType['rivalPick'];
   }

   return {
     myResult,
     myPick,
     rivalResult,
     rivalPick
   };
 }

 function getObserveId(state: State) {
   const room = state.room;
   const session = state.session;
   let observeId = session?.id;

   if (room?.livePlayers && isPlayerDead(room, session?.id)) {
     observeId = room.livePlayers[0];
   }
   return observeId;
 }

 const st = store.get<State>();
 // const session = st.select(state => state.session);
 const room = st.select(state => state.room);
 //  const roomSession = room.pipe(rx.withLatestFrom(session));

 const otherPairings = st.select(state => getOthersPairings(state.room, getObserveId(state)));


 const results = st.select(state => getResults(state.room, getObserveId(state)));

 const playerDead = st.select(({room, session}) => isPlayerDead(room, session?.id));
 const lastPlays = st.select(({room}) => getLastPlays(room));

 const player = st.select(state => {
   const observeId = getObserveId(state);
   if (!observeId) {
     return null;
   }
   return state?.room?.sessions[observeId];
 });

 const rival = st.select(state => {
   const observeId = getObserveId(state);
   const rivalId = getRivalSessionId(state?.room, observeId);
   if (!rivalId) {
     return null;
   }
   return state?.room?.sessions[rivalId];
 });

 let selectedPick: string | null = null;

 function pick(value: "rock" | "paper" | "scissors") {
   selectedPick = value;
   st.emit(new SendTurn(TURNS[value]))
 }

 let drawGifs = false;
 let timeoutId: ReturnType<typeof setTimeout> | undefined = undefined;

 onMount(() => {
   if (typeof window !== "undefined") {
     timeoutId = setTimeout(() => {
       drawGifs = true;
     }, 100);
   }
 });

 onDestroy(() => {
   if (typeof window !== "undefined" && timeoutId) {
     clearTimeout(timeoutId);
   }
 });

</script>

<div class="game"
     style="--background-image: url({background});
            --background-hands: url({handsBg});
            --background-select: url({selectBg});">
  <div class="turn-info">
    <div class="turn-display">Round {$room?.round}</div>
    <Timer/>
  </div>

  {#if $otherPairings}
    <div class="players-info">
      {#each $otherPairings as pairing}
        <MatchData player1={pairing[0]} player2={pairing[1]} />
      {/each}
    </div>
  {/if}


  <div class="play-area">
    {#if $playerDead}
      <div class="message">Observing {$player?.name}</div>
    {:else if $results?.myResult === "loss"}
      <div class="message">You Lose!</div>
    {:else if $results?.myResult === "win"}
      <div class="message">You Win!</div>
    {/if}

    <div class="player-left">
      <PlayerCard cardType="full"
                  name={$player?.name}
                            avatar={$player?.avatar}
                  result={$results?.myResult}
                            pick={$results?.myPick}
                  flipx={true}
                            lastPlays={$lastPlays && $player?.id && $lastPlays[$player.id] || []}/>
    </div>
    <div class="vs">
      {#if drawGifs}
        <img src={vsLetters + "?ts=" + new Date().getTime()} alt="VS" />
      {/if}
    </div>
    <div class="player-right">
      <PlayerCard cardType="full"
                  name={$rival?.name || "MR. nobody"}
                            avatar={$rival?.avatar || "bot"}
                  result={$results?.rivalResult}
                            pick={$results?.rivalPick}
                  lastPlays={$lastPlays && $rival?.id && $lastPlays[$rival.id] || []}/>
    </div>
  </div>

  <div class="actions">
    {#if !$playerDead && $room?.stage === "game"}
      <button class="action rock"
              on:click={() => pick("rock")}
                     class:selected={selectedPick === "rock"}
              disabled={!!$results?.myResult}>
        <img width="100%" alt="Rock" src={rock}/>
      </button>
      <button class="action paper"
              on:click={() => pick("paper")}
                     class:selected={selectedPick === "paper"}
              disabled={!!$results?.myResult}>
        <img width="100%" alt="Paper"src={paper}/>
      </button>
      <button class="action scissors"
              on:click={() => pick("scissors")}
                     class:selected={selectedPick === "scissors"}
              disabled={!!$results?.myResult}>
        <img width="100%" alt="Scissors" src={scissors}/>
      </button>
    {/if}
  </div>
</div>

<style lang="postcss">
 .game {
   background-image: var(--background-image);
   background-size: cover;
   flex: 1;
   height: 100%;
   width: 100%;

   &::before {
     background-image: var(--background-hands);
     background-size: cover;
     bottom: 0;
     content: '';
     height: 100%;
     pointer-events: none;
     position: absolute;
     width: 100%;
     opacity: 0.5;
   }

   & > * {
     z-index: 1;
   }
 }

 .game {
   display: grid;
   grid-template-areas:
     "turn-info"
     "players-info"
     "play-area"
     "actions";

   grid-template-columns: initial;
   grid-template-rows: auto 170px 1fr auto;
   grid-template-columns: 100%;

   & .turn-info {
     grid-area: turn-info;
   }

   & .players-info {
     grid-area: players-info;
   }

   & .play-area {
     grid-area: play-area;
   }

   & .actions {
     grid-area: actions;
   }
 }

 .turn-info {
   padding: 1rem;
   display: flex;
   flex-direction: row;
   justify-content: space-between;

   & .turn-display {
     padding: 4px 8px;
     background-color: #222034;
   }
 }

 .players-info {
   display: flex;
   flex-direction: column;
   flex-wrap: wrap;
   margin: 0 1rem;
   overflow-x: scroll;

   & > :global(*) {
     width: 120px;
     margin-top: 0.5rem;
     margin-right: 1.5rem;
   }
 }

 .play-area {
   display: grid;
   grid-template-areas: "message message message" "player-left vs player-right";
   grid-template-columns: 1fr 0px 1fr;
   grid-template-rows: auto 1fr;
   padding: 16px 16px 0 16px;
   position: relative;

   & .message {
     grid-area: message;
     text-align: center;
     text-shadow: 8px 8px rgba(0,0,0,40%);
     font-size: 32px;
     position: absolute;
     top: -16px;
     left: 0;
     width: 100%;
   }

   & .player-left {
     grid-area: player-left;
   }

   & .player-right {
     grid-area: player-right;
   }

   & .player-right, & .player-left {
     display: flex;
     align-items: center;
     justify-content: center;
   }

   & .vs {
     grid-area: vs;
     position: relative;

     & img {
       width: 85px;
       position: absolute;
       z-index: 200;
       left: -47px;bottom: 50%;
       margin-bottom: -10px;
     }
   }
 }

 .actions {
   grid-area: buttons;
   border-top: 2px dashed #8e489c;
   padding-top: 1rem;
   margin: 0 1.5rem 0.5rem 1.5rem;
   display: grid;
   grid-template-columns: repeat(3, 70px);
   grid-template-rows: 114px;
   grid-gap: 50px;
   justify-content: space-evenly;

   & .action {
     padding: 0;
     border: none;
     background: none;
     position: relative;
     width: 100%;

     &.selected::before {
       content: '';
       width: 122px;
       height: 122px;
       position: absolute;
       left: -30px;
       top: -6px;
       z-index: -1;
       background-image: var(--background-select);
       background-size: cover;
     }

     & img {
       width: 100%;
     }
   }
 }

 @media only screen and (min-width: 900px) {
   .game {
     grid-template-areas:
       "turn-info turn-info"
       "players-info play-area"
       "players-info actions";

     grid-template-columns: initial;
     grid-template-rows: auto 1fr 0fr auto;
     grid-template-columns: 1fr 1fr;

   }

   .turn-info {
     font-size: 32px;
     padding: 3rem;
   }

   .play-area {
     & .message {
       font-size: 56px;
     }

     & .player-left {
       justify-content: flex-end;
       padding-right: 40px;
     }

     & .player-right {
       justify-content: flex-start;
       padding-right: 40px;
     }

     & .vs {

       & img {
         width: 142px;
         left: -104px;
         bottom: 50%;
         margin-bottom: -40px;
       }
     }
   }

   .players-info {
     flex-direction: row;
     margin: 4rem;
     overflow-x: auto;
     align-content: flex-start;
     justify-content: center;

     & > :global(*) {
       width: 240px;
       margin-top: 3rem;
       margin-right: 3rem;
     }
   }
   .actions {
     justify-content: center;
     margin: 0 3rem 1rem 3rem;
     display: grid;
     grid-template-columns: repeat(3, 100px);
     grid-template-rows: 180px;
     grid-column-gap: 140px;

     & .action {

       &.selected::before {
         content: '';
         width: 160px;
         height: 160px;
         position: absolute;
         left: -30px;
         top: -2px;
         z-index: -1;
         background-image: var(--background-select);
         background-size: cover;
       }

       & img {
         width: 100%;
       }
     }
   }

 }

</style>
