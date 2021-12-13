<script context="module" lang="ts">
 import type { LoadInput } from '@sveltejs/kit';
 import rock from "$lib/images/rock.png";

 let gameId = "test";

 export async function load(data: LoadInput) {
   gameId = data.page.params.game;
   return true;
 }
</script>

<div class="game">
  <div class="game-area">
    
  </div>
  <div class="actions">
    <div class="action rock">
      <img alt="Rock" src={rock}/>
    </div>
    <div class="action paper">
      <img alt="Paper"src={rock}/>
    </div>
    <div class="action scissors">
      <img alt="Scissors" src={rock}/>
    </div>
  </div>
</div>

<style lang="postcss">
 .game {
   height: 100%;
   width: 100%;
   flex: 1;
   display: grid;
   grid-template-areas: "game buttons" ;
   grid-template-columns: 1fr 20%;
   grid-template-rows: 100%;
 }

 @media (orientation: portrait) {
   .game {
     grid-template-areas: "game" "buttons";
     grid-template-columns: initial;
     grid-template-rows: 1fr 20%;
     grid-template-columns: 100%;
   }
 }

 .game-area {
   grid-area: game;
 }

 .actions {
   grid-area: buttons;
   display: flex;
   flex-direction: column;

   @media (orientation: portrait) {
     flex-direction: row;
   }

   & .action {
     flex: 1;
     display: flex;
     justify-content: center;
     align-items: center;
     position: relative;

     & img {
       position: absolute;
       height: 100%;

       @media (orientation: portrait) {
         & {
           height: auto;
           width: 100%;
         }
       }

     }
   }

 }
 
</style>
