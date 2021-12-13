<script lang="ts">
 import { start } from "$store"; 
 import { initialState } from "$state";
 import { GameManager } from "$services";
 import { onMount } from 'svelte';

  onMount(() => {
    const socket = new WebSocket(import.meta.env.VITE_BASE_URL || "ws://localhost:3000");
    const gameManager = GameManager.getInstance();
    gameManager.setSocket(socket);
  });

 // Initializes the store
 start(initialState);
</script>

<div class="main">
  <div class="content">
    <slot></slot>
  </div>
</div>

<style lang="postcss">
 .main {
   width: 100%;
   height: 100%;
   overflow: hidden;
   background: black;
   display: flex;
   flex-direction: column;
 }

 .content {
   flex: 1;
   padding: 1rem;
   background: white;
   border: 1px solid red;
   height: 100%;

   display: flex;
   justify-content: center;
   align-items: center;
   flex-direction: row;
 }

 @media (orientation: portrait) {
   .content {
     flex-direction: column;
   }
 }

 @media only screen and (min-width: 641px) and (orientation: landscape) {
   .main {
     align-items: center;
     justify-content: center;
   }

   .content {
     flex: initial;
     width: 640px;
     height: 320px;
     transform: scale(2);
   }
 }

 @media only screen and (min-width: 1440) and (orientation: landscape) {
   .content {
     transform: scale(2.25);
   }
 }

 @media only screen and (min-width: 1600) and (orientation: landscape) {
   .content {
     transform: scale(2.5);
   }
 }

 @media only screen and (min-width: 1760) and (orientation: landscape) {
   .content {
     transform: scale(2.75);
   }
 }

 @media only screen and (min-width: 1920px) and (orientation: landscape) {
   .content {
     transform: scale(3);
   }
 }

 /*
 @media only screen and (min-width: 2080px) and (orientation: landscape) {
   .content {
     transform: scale(3.25);
   }
 }

 @media only screen and (min-width: 2240px) and (orientation: landscape) {
   .content {
     transform: scale(3.5);
   }
 }
 */

</style>
