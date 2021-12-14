<script lang="ts">
 import store from "$store";
 import type { State } from "$state";
 import { StartGame, Hello, Join } from "$events";

 const st = store.get<State>();

 function hello() {
  st.emit(new Hello("fulano"));
 }

 function join() {
  st.emit(new Join(Date.now().toString()));
 }

 function startGame() {
  st.emit(new StartGame());
 }

</script>

<div>
  <button on:click={hello}>Hello</button>
  <button on:click={join}>Join</button>
  <button on:click={startGame}>START GAME</button>
</div>

<style lang="postcss">
</style>
