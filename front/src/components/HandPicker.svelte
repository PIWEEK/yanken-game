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
   width: 100%;
   height: 120px;
   display: flex;
   justify-content: space-between;
 }

 .hand {
   background-color: #45283c;
   border: 2px solid black;
   margin: 16px;

   &:hover {
     border: 2px solid #de8eee;
   }
 }
</style>
