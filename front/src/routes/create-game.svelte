<script lang="ts">
 import { goto } from '$app/navigation';
 import { writable } from 'svelte/store';

 const game = writable({
   name: "YankenGame"
 });

 function createGame(event: Event) {
   event.preventDefault();
   console.log(JSON.stringify($game, null, 2));
   const roomKey = "test";
   goto(`game/${roomKey}`);
 }
 
</script>

<h2>Create Game</h2>

<form class="content">
  <label for="name">Room name</label>
  <input name="name" type="text" bind:value={$game.name} />
  <button on:click={createGame} type="submit">Create</button>
</form>

<style>
 @import "$styles/forms.css";
 
 .content {
   display: grid;
   grid-template-columns: 20% 80%;
   grid-column-gap: 10px;
 }
</style>

