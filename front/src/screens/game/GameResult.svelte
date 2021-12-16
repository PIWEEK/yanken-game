<script lang="ts">
 import { goto } from "$app/navigation";
 import { base } from '$app/paths';
 import type { State, Session } from "$state";
 import { Join, JoinBots } from "$events";
 import store from "$store";

 import MenuContainer from "$components/MenuContainer.svelte";
 import PlayerCard from "$components/PlayerCard.svelte";

 import dropDecoration from "$lib/images/drop.png";
 import winnerDecoration from "$lib/images/winner-live.gif";

 const st = store.get<State>();
 const room = st.select(state => state.room);
 const winner = st.select(getWinner);
 const second = st.select(getSecond);
 const others = st.select(getOthers);

 function getWinner(state: State) {
   const room = state.room;
   const sessions = room?.sessions;

   if (!room || !sessions || !room.results || !room.results[0] || !room.results[0][0]) {
     return null;
   }

   const lastFight = room?.results[0][0];
   const winnerId = lastFight?.winner;

   if (!winnerId) {
     return null;
   }

   return sessions[winnerId];
 }

 function getSecond(state: State) {
   const room = state.room;
   const sessions = room?.sessions;

   if (!room || !sessions || !room.results || !room.results[0] || !room.results[0][0]) {
     return null;
   }

   const lastFight = room?.results[0][0];
   const winnerId = lastFight?.winner;
   const secondId = lastFight?.players?.find(id => id !== winnerId);

   if (!secondId) {
     return null;
   }
   return sessions[secondId];
 }

 function getOthers(state: State): Session[] {
   const room = state.room;
   const sessions = room?.sessions;

   if (!room || !sessions || !room.results || !room.results[0] || !room.results[0][0]) {
     return [];
   }

   const lastFight = room.results[0][0];
   const winnerId = lastFight.winner;
   const secondId = lastFight.players.find(id => id !== winnerId);

   const result = room.players
   .filter((p) => p !== winnerId && p !== secondId)
   .map((p) => sessions[p]);

   return result;
 }

 function goBack() {
   const roomId = $room?.id;

   if (!roomId) {
     return;
   }

   st.emit(new Join(roomId));
   st.emit(new JoinBots(roomId));
   goto(`${base}/game/wait-players`);
 }
</script>

<MenuContainer>
  <div class="container">
    <div class="results">
      <div class="message">Winner</div>
      <div class="winner">
        <img src={winnerDecoration} alt="champion" />
        <PlayerCard cardType="full"
                    name={$winner?.name}
                              avatar={$winner?.avatar}/>
      </div>
      <div class="message">Not winner</div>
      <div class="second">
        <img src={dropDecoration} alt="drop" />
        <PlayerCard cardType="full"
                    name={$second?.name}
                              avatar={$second?.avatar}/>
      </div>

      <div class="message">Bunch of losers</div>
      <div class="players">
        {#each $others as player}
          <PlayerCard cardType="small"
                      name={player?.name}
                                avatar={player?.avatar}/>
        {/each}
      </div>
    </div>
    <button on:click={goBack}>Play again</button>
  </div>
</MenuContainer>

<style lang="postcss">
 .container {
   align-items: center;
   display: grid;
   grid-template-rows: 1fr auto;
   height: 100%;
   justify-content: center;
 }

 .message {
   color: #c27eca;
   font-size: 28px;
   text-align: center;
   margin-top: 20px;
   margin-bottom: 10px;
 }

 .results {
   align-items: center;
   display: flex;
   flex-direction: column;
   height: 92%;
   justify-content: flex-start;
   overflow-y: auto;
 }

 .players {
   display: flex;
 }

 .winner {
   position: relative;
   & > img {
     position: absolute;
     top: -10%;
     left: -10%;
     width: 120%;
     z-index: 200;
   }
 }

 .second {
   position: relative;

   & > img {
     position: absolute;
     z-index: 200;
     right: -10%;
     width: 25%;
     top: -5%;
   }
 }

 button {
   margin-bottom: 0;
   width: 300px;
 }

 @media only screen and (min-width: 900px) {
   .winner, .second {
     margin-top: 16px;
   }
   .winner {
     margin-bottom: 32px;
   }
   .container :global(.results .winner .suc-data.full),
   .container :global(.results .second .suc-data.full) {
     width: 200px;
   }

   button {
     justify-self: center;
   }

 }

</style>
