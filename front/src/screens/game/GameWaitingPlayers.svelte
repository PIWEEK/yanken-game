<script lang="ts">
 import store from "$store";
 import type { State } from "$state";
 import PlayerCard from "$components/PlayerCard.svelte";
 import clockIcon from "$lib/images/timer.png"
 import { StartGame, JoinBots } from "$events";

 const st = store.get<State>();
 const session = st.select(state => state.session);
 const room = st.select(state => state.room);

 function startGame() {
   st.emit(new StartGame());
 }

 function addBot() {
   if ($room?.id) {
     st.emit(new JoinBots($room.id));
   }
 }
</script>

<div class="container">
  <div class="message">Wait until the game starts...</div>
  <div class="clock"><img alt="Timer" src={clockIcon}/></div>
  {#if $room?.players}
    <div class="player-list">
      {#each $room.players as player}
	      <PlayerCard name={$room.sessions[player].name} avatar={$room.sessions[player].avatar || "red"} flipx={true} />
      {/each}
    </div>
  {/if}
  {#if $room?.owner && $session?.id && $room.owner == $session.id}
    <button on:click={startGame}>GO!</button>
    <button on:click={addBot}>+BOT</button>
  {/if}
</div>

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

 .clock {
   text-align: center;
   margin-bottom: 8px;
   & img {
     width: 21px;
   }
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
 }

 @media only screen and (min-width: 900px) {

   .message {
     font-size: 36px;
     margin-bottom: 16px;
   }

   .clock {
     margin-bottom: 16px;
     & img {
       width: 36px;
     }
   }

   .container :global(button) {
     width: 300px;
     justify-self: center;
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
