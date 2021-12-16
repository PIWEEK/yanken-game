<script lang="ts">
 import type { Session } from "$state";
 import PlayerCard from "$components/PlayerCard.svelte";

 export let player1: Session;
 export let player2: Session;
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
