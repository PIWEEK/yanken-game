<script lang="ts">
 import { createEventDispatcher } from 'svelte';

 const dispatch = createEventDispatcher();

 const colors = [
   { value: "white", hex: "#ffffff"},
   { value: "blue", hex: "#5fcde4"},
   { value: "green", hex: "#65d380"},
   { value: "red", hex: "#d95763"},
   { value: "orange", hex: "#df7126"},
   { value: "yellow", hex: "#fbf236"},
   { value: "purple", hex: "#c27eca"},
   { value: "pink", hex: "#e997cf"},
 ];

 function clickColor(color: string) {
   dispatch("change", color);
 }
</script>

<div class="color-picker">
  {#each colors as color}
    <button
      class="color"
      style="--button-color: {color.hex};"
      on:click={() => clickColor(color.value)}></button>
  {/each}
</div>

<style lang="postcss">
 .color-picker {
   width: 100%;
   display: grid;
   grid-template-columns: repeat(4, 1fr);
   grid-gap: 12px;
 }

 button {
   background-color: var(--button-color);
   border: 2px solid black;
   width: 48px;
   height: 48px;
   justify-self: center;

   &:hover {
     background-color: var(--button-color);
     border: 2px solid #de8eee;
   }
 }

 @media only screen and (min-width: 900px) {

   button {
     width: 66px;
     height: 66px;
     border: 4px solid black;
   }

  :global(.suc-data) {
    max-width: 100%;
    justify-self: center;
  }

 }
</style>
