<script lang="ts">
 import PlayerCard from "$components/PlayerCard.svelte";

 interface Player {
   name: string;
   avatar: string;
 }
 export let player1: Player;
 export let player2: Player;
</script>

<div class="match-info">
  <PlayerCard name={player1?.name}
              avatar={player1?.avatar}
              cardType="small"
              flipx={true}/>

  <PlayerCard name={player2?.name || "mr. nobody"}
              avatar={player2?.avatar || "bot"}
              cardType="small"/>

</div>

<style lang="postcss">
 .match-info {
   display: grid;
   grid-template-columns: 1fr 1fr;
   justify-content: center;
   align-items: center;
 }

 @media only screen and (min-width: 900px) {

   .match-info {
    margin: 0 80px;
   }

 }
</style>
