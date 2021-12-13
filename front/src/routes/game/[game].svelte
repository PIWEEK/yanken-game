<script context="module" lang="ts">
 import type { LoadInput } from '@sveltejs/kit';

 let gameId = "test";

 export async function load(data: LoadInput) {
   gameId = data.page.params.game;
   return true;
 }
</script>

<div>
  <div class="actions">
    <div class="action stone"></div>
    <div class="action paper"></div>
    <div class="action scissors"></div>
  </div>
</div>

<style>
 .actions {
   width: 120px;
   background: red;
   display: flex;
 }

 .action {
   height: 110px;
   width: 110px;
   background: blue;
 }
</style>
