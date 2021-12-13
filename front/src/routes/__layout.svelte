<script lang="ts">
 import store from "$store";
 import type { State } from "$state";
 import { initialState } from "$state";
 import { onMount } from 'svelte';
 import { SetWebSocket } from "$events";

 // Initializes the store
 store.start(initialState);

 const st = store.get<State>();

 onMount(() => {
  const socket = new WebSocket("ws://localhost:10000/");

  socket.addEventListener("open", (event: Event) => {
      console.log("WebSocket connection opened", event);
  });

  socket.addEventListener("message", (event: MessageEvent) => {
      console.log("WebSocket message received", event);
  });

  socket.addEventListener("error", (event) => {
      console.log("WebSocket error encountered", event);
  });

  socket.addEventListener("close", (event: CloseEvent) => {
      console.log("WebSocket connection closed", event);
  });

  st.emit(new SetWebSocket(socket));
 })
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
