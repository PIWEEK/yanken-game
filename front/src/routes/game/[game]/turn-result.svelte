<script lang="ts">
 import MatchData from "$components/MatchData.svelte";
 import MenuContainer from "$components/MenuContainer.svelte";
 import ProgressBar from "$components/ProgressBar.svelte";

 let matches = new Array(20).fill(
   {
     player1: {name: "Uno", color: "red"},
     player2: {name: "Dos", color: "green"}
   }
 );

</script>

<MenuContainer hideRoom>
  <div class="container">
    <div class="round">Round 1</div>
    <div class="message">Ready to fight?</div>
    <ProgressBar progress={100} />

    <div class="matches-list">
      {#each matches as _match}
        <MatchData />
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
   height: 100%;
   display: grid;
   grid-template-columns: 1fr 1fr;
   grid-gap: 16px;
   justify-items: center;
   align-items: center;
   justify-self: stretch;
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
