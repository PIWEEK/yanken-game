<script lang="ts">
 import { goto } from "$app/navigation";
 import { page } from '$app/stores';

 import PlayerCard from "$components/PlayerCard.svelte";

 import MenuContainer from "$components/MenuContainer.svelte";
 import clockIcon from "$lib/images/timer.png"

 let players = new Array(100).fill({name: "Uno", color: "red"});

 function startGame() {
   const room = $page.params.game;
   goto(`/game/${room}/turn`);
 }
 
</script>

<MenuContainer>
  <div class="container">
    <label for="name">Wait until the game starts...</label>
    <div class="clock"><img alt="Timer" src={clockIcon}/></div>
    <div class="player-list">
      {#each players as player}
	      <PlayerCard name={player.name} color={player.color} flipx={true} />
      {/each}
    </div>
    <button on:click={startGame}>GO!</button>
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

 label[for=name] {
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
   overflow-y: scroll;
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
</style>


