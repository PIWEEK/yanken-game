<script lang="ts">
 import store from "$store";
 import type { State } from "$state";
 import { StartGame, Hello } from "$events";

 const st = store.get<State>();

 function startGame() {
  st.emit(new StartGame());
 }

 function hello() {
  st.emit(new Hello("fulano"));
 }

</script>

<div>
  <button on:click={startGame}>START GAME</button>
  <button on:click={hello}>Hello</button>
</div>

<style lang="postcss">
</style>
