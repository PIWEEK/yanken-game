<script lang="ts">
 import { onMount, onDestroy } from "svelte";
 import store from "$store";
 import type { State } from "$state";

 import MatchData from "$components/MatchData.svelte";
 import ProgressBar from "$components/ProgressBar.svelte";
 import PlayerCard from "$components/PlayerCard.svelte";
 import vsLetters from "$lib/images/versus-live.gif";

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
 	   clearInterval(interval);
   }
 });

 onDestroy(() => {
 });
</script>

<div class="container">
  <div class="round">
    {#if $pairings.length == 1}
      Last round
    {:else}
      Round {$room?.round}
    {/if}
  </div>
  <div class="message">
    {#if $pairings.length == 1}
      Who will win?
    {:else}
      Ready to fight?
    {/if}
  </div>
  <ProgressBar progress={progress} />

  {#if $pairings.length == 1}
    <div class="play-area">
      <div class="player-left">
        <PlayerCard cardType="full"
                    name={$pairings[0][0].name}
                    avatar={$pairings[0][0].avatar}
                    flipx={true}/>
      </div>
      <div class="vs">
        {#if drawGifs}
          <img src={vsLetters + "?ts=" + new Date().getTime()} alt="VS" />
        {/if}
      </div>
      <div class="player-right">
        <PlayerCard cardType="full"
                    name={$pairings[0][1].name}
                    avatar={$pairings[0][1].avatar}/>
      </div>
    </div>
  {:else}
  <div class="matches-list">
    {#each $pairings as pairing}
      <MatchData player1={pairing[0]} player2={pairing[1]} />
    {/each}
  </div>
  {/if}
</div>

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
 
 .play-area {
   display: grid;
   grid-template-areas: "message message message" "player-left vs player-right";
   grid-template-columns: 1fr 0px 1fr;
   grid-template-rows: auto 1fr;
   padding: 16px 16px 0 16px;
   position: relative;

   & .player-left {
     grid-area: player-left;
     padding-right: 28px;
   }

   & .player-right {
     grid-area: player-right;
     padding-right: 28px;
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
       left: -56px;
       bottom: 50%;
       margin-bottom: -30px;
     }
   }
 }

 @media only screen and (min-width: 900px) {
   .play-area {
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
