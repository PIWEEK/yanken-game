<script lang="ts">
 import { goto } from "$app/navigation";
 import background from "$lib/images/select-bg.png"
 import clockIcon from "$lib/images/timer.png"
 
 import PlayerCard from "$components/PlayerCard.svelte";
 
 let room = "apothecary";
 let players = new Array(100).fill({name: "Uno", color: "red"});

 function startGame() {
   goto(`/game/${room}/turn`);
 }
 
</script>

<div class="room-name">Room {room}</div>
<div class="main" style="--background-image: url({background})">
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
</div>

<style lang="postcss">
 @import "$styles/forms.css";
 
 .main {
   width: 100%;
   height: 100%;
   background-image: var(--background-image);
   background-size: cover;
   padding: 30px;
 }

 .room-name {
   position: absolute;
   width: 100%;
   top: 0px;
   color: #c27eca;
   font-size: 14px;
   text-align: center;
   padding: 8px 0;
 }

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


