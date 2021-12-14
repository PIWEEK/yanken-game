<script>
 import PlayerCard from "$components/PlayerCard.svelte";
</script>

<div class="match-info">
  <PlayerCard name="Nombrelargodelcarajo"
              color="green"
              cardType="small"
              flipx={true}/>
  
  <PlayerCard name="Cletus"
              color="red"
              cardType="small"/>
  
</div>

<style lang="postcss">
 .match-info {
   display: grid;
   grid-template-columns: 1fr 1fr;
   justify-content: center;
   align-items: center;
 }
</style>
