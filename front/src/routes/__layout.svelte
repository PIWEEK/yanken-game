<script lang="ts">
 import { start } from "$store";
 import { initialState } from "$state";

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
