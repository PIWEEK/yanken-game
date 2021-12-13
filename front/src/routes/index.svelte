<script lang="ts">
 import store from "$store";
 import type { State } from "$state";
 import { goto } from '$app/navigation';
 import { StartCounter } from "$events";

 const st = store.get<State>();

 // Global state
 const counter = st.select(state => state.counter);

 // Local state
 let clicked = false;

 function click() {
   clicked = true;
   st.emit(new StartCounter());
 }

 let roomId = "";

 function createGame() {
   goto("/create-game");
 }

 function joinGame() {
   goto(`/join-game/${roomId}`);
 }

</script>

<div>
  <button on:click={createGame}>CREATE GAME</button>

  <button on:click={joinGame} disabled={roomId === ""}>JOIN GAME</button>
  <input type="text" placeholder="Room id" bind:value={roomId} />

  <!--
  <button on:click={click} disabled={clicked}>JOIN GAME</button>
  -->
</div>

<style lang="postcss">
</style>
