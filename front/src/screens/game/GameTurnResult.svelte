<script lang="ts">
 import type { State } from "$state";

 import store from "$store";

 import PlayerCard from "$components/PlayerCard.svelte";
 import MenuContainer from "$components/MenuContainer.svelte";

 const st = store.get<State>();

 const room = st.select(state => state.room);
 const playersLeft = st.select(state => state.room?.livePlayers?.length);

 const deadPlayers = st.select(state => new Set(state.room?.deadPlayers));
</script>

<MenuContainer>
  <div class="container">
    <div class="message">Only {$playersLeft} Yankens left!</div>

    {#if $room?.players}
      <div class="player-list">
        {#each $room.players as player}
	        <PlayerCard name={$room.sessions[player].name}
                           avatar={$room.sessions[player].avatar || "red"}
                      result={$deadPlayers.has(player) ? "loss" : null}
                           flipx={true} />
        {/each}
      </div>
    {/if}
  </div>
</MenuContainer>

<style lang="postcss">
 .container {
   height: 100%;
   display: grid;
   justify-content: center;
   align-items: center;
   grid-template-rows: 115px auto 1fr 100px;
 }

 .message {
   text-align: center;
   font-size: 24px;
   align-self: end;
   margin-bottom: 8px;
 }

 .player-list {
   align-content: baseline;
   align-items: start;
   height: 100%;
   overflow-y: auto;
   width: 100%;
   display: grid;
   grid-template-columns: repeat(4, 25%);
   align-items: center;
   justify-content: center;

   & > :global(*) {
     margin: 8px;
   }

   & :global(.player-name) {
     font-size: 10px;
     padding-top: 0px;
   }

   & :global(.decoration .turn-result) {
     left: -21px;
     top: -12px;
   }
 }

 @media only screen and (min-width: 900px) {
   .message {
     font-size: 36px;
     margin-bottom: 16px;
   }
   .player-list {
     grid-template-columns: repeat(6, 16%);
     max-width: 1200px;
     margin-top: 100px;

     & :global(.player-name) {
       font-size: 24px;
     }
   }
 }
</style>
