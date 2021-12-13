<script lang="ts">
 //import * as R from "ramda";
 import store from "$store";
 import type { State } from "$state";
 import { StartCounter } from "$events";
 import { GameManager } from "../services";
 
 const st = store.get<State>();

 // Global state
 const counter = st.select(state => state.counter);

 // Local state
 let clicked = false;

 function click() {
   clicked = true;
   st.emit(new StartCounter());
   GameManager.getInstance().createGame();
 }
</script>

<div class="test">
  <div class="counter">{$counter}</div>
  <button on:click={click} disabled={clicked}>CLICK</button>
</div>

<style lang="postcss">
 .test {
   border: 1px solid red;

   &:hover {
     border: 1px solid blue;
   }
 }
</style>
