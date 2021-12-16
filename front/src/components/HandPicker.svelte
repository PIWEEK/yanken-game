<script lang="ts">
 import { createEventDispatcher } from 'svelte';
 import rock from "$lib/images/hands-rock.png";
 import paper from "$lib/images/hands-paper.png";
 import scissors from "$lib/images/hands-scissors.png";

 const dispatch = createEventDispatcher();

 const hands = [
   { value: "rock", image: rock},
   { value: "paper", image: paper},
   { value: "scissors", image: scissors}
 ];

 function clickHand(hand: string) {
   dispatch("change", hand);
 }
</script>

<div class="hand-picker">
  {#each hands as hand}
    <img
      alt="{hand.value}"
      src="{hand.image}"
      class="hand"
      on:click={() => clickHand(hand.value)}/>
  {/each}
</div>

<style lang="postcss">
 .hand-picker {
   width: 74%;
   display: flex;
   justify-content: space-between;
   justify-self: center;
   margin: 16px 0;
 }

 .hand {
   background-color: #45283c;
   border: 2px solid black;
   width: 56px;
   height: 56px;

   &:hover {
     border: 2px solid #de8eee;
   }
 }

 @media only screen and (min-width: 400px) {

   .hand {
     width: 84px;
     height: 84px;
   }

 }

</style>
