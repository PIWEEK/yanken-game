<script lang="ts">
 import store from "$store";
 import type { State } from "$state";

 import MatchData from "$components/MatchData.svelte";
 import MenuContainer from "$components/MenuContainer.svelte";
 import ProgressBar from "$components/ProgressBar.svelte";
 import { onDestroy } from 'svelte';

 function getPairings(state: State) {
   const room = state.room;

   if (!room || !room.fights) {
     return [];
   }

   const sessions = room.sessions;

   return room.fights.map((f) => f.players.map(p => sessions[p]));
 }

 const st = store.get<State>();
 const room = st.select(st => st.room);
 const pairings = st.select(getPairings);

 let time = 0;
 const interval = setInterval(() => {
   time += 100;
 }, 100);

 const pairingScreenTimeout = $room?.options.pairingScreenTimeout;

 $: progress = pairingScreenTimeout ? time * 100 / pairingScreenTimeout : 100;

 onDestroy(() => {
 	 clearInterval(interval);
 });
</script>

<MenuContainer hideRoom>
  <div class="container">
    <div class="round">Round {$room?.round}</div>
    <div class="message">Ready to fight?</div>
    <ProgressBar progress={progress} />

    <div class="matches-list">
      {#each $pairings as pairing}
        <MatchData player1={pairing[0]} player2={pairing[1]} />
      {/each}
    </div>
  </div>
</MenuContainer>

<style lang="postcss">
 .container {
   height: 100%;
   display: grid;
   justify-content: center;
   justify-items: center;
   align-items: center;
   grid-template-rows: 50px auto 75px 1fr;
   grid-template-columns: 1fr;
   padding-bottom: 30px;

   & :global(.progress) {
     margin-bottom: 25px;
   }
 }

 .round {
   color: #c27eca;
   font-size: 36px;
 }

 .message {
   font-size: 24px;
 }

 .matches-list {
   overflow-y: auto;
   display: grid;
   grid-template-columns: 1fr 1fr;
   grid-gap: 16px;
   align-self: start;
 }

 @media only screen and (min-width: 900px) {

   .message {
     font-size: 56px;
   }

   .matches-list {
     display: flex;
     flex-direction: row;
     flex-wrap: wrap;
     justify-content: center;
     padding: 40px 40px 0;
   }

 }

</style>
