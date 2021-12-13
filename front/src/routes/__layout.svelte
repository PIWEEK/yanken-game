<script lang="ts">
 import { start } from "$store"; 
 import { initialState } from "$state";
 import { GameManager } from "$services";
 import { onMount } from 'svelte';

  onMount(() => {
    const socket = new WebSocket(import.meta.env.VITE_BASE_URL);
    const gameManager = GameManager.getInstance();
    gameManager.setSocket(socket);
  });

 // Initializes the store
 start(initialState);
</script>

<main>
  <h1>HOLA</h1>
  <slot></slot>
</main>

<style lang="postcss">
 :global(body) {
   font-family: "Work Sans";
 }
</style>
