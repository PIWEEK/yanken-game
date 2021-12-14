<script lang="ts">
 import store from "$store";
 import type { State } from "$state";
 import { StartGame, Join } from "$events";

 const st = store.get<State>();

 function startGame() {
  st.emit(new StartGame());
 }

 function join() {
  st.emit(new Join("fulano"));
 }

</script>

<div>
  <button on:click={startGame}>START GAME</button>
  <button on:click={join}>JOIN</button>
</div>

<style lang="postcss">
</style>
