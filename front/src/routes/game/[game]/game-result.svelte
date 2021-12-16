<script lang="ts">
 import { goto } from "$app/navigation";
 import { base } from '$app/paths';
 import type { State } from "$state";
 import { Join, JoinBots } from "$events";
 import store from "$store";

 import MenuContainer from "$components/MenuContainer.svelte";
 import PlayerCard from "$components/PlayerCard.svelte";

 const st = store.get<State>();
 const room = st.select(state => state.room);
 const winner = st.select(getWinner);
 const second = st.select(getSecond);
 const others = st.select(getOthers);


 function getWinner(state: State) {
   const room = state.room;
   const sessions = room?.sessions;
   const lastFight = room?.results[0][0];
   const winnerId = lastFight?.winner;

   if (!winnerId || !sessions) {
     return null;
   }
   
   return sessions[winnerId];
 }

 function getSecond(state: State) {
   const room = state.room;
   const sessions = room?.sessions;
   const lastFight = room?.results[0][0];
   const winnerId = lastFight?.winner;
   const secondId = lastFight?.players?.find(id => id !== winnerId);

   if (!sessions || !secondId) {
     return null;
   }
   return sessions[secondId];
 }

 function getOthers(state: State) {
   const room = state.room;
   if (!room) {
     return [];
   }
   const sessions = room.sessions;
   const lastFight = room.results[0][0]

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
   goto(`${base}/game/${roomId}/wait-players`);
 }
</script>

<MenuContainer>
  <div class="container">
    <div class="results">
      <div class="message">Winner</div>
      <PlayerCard cardType="full"
                  name={$winner?.name}
                  avatar={$winner?.avatar}/>
      <div class="message">Not winner</div>
      <PlayerCard cardType="full"
                  name={$second?.name}
                  avatar={$second?.avatar}/>

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
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
   overflow-y: scroll;
   height: 80%;
 }

 .players {
   display: flex;
 }

 button {
   margin-bottom: 16px;
   width: 300px;
 }

</style>
