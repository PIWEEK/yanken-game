<script lang="ts">
 import { goto } from "$app/navigation";
 import logo from "$lib/images/yanken.png";
 import MenuContainer from "$components/MenuContainer.svelte";
 
 let room: string;
 
 function joinRoom() {
   goto(`/game/${room}/player`);
 }
</script>

<MenuContainer>
  <div class="container">
    <img class="logo" src={logo} alt="Yanken Game"/>

    <div class="input-data">
      <button disabled={!room} on:click={joinRoom}>
        JOIN GAME
      </button>

      <input
        name="name"
        placeholder="Type room name..."
        type="text"
        pattern="[0-9]+"
        bind:value={room} />
    </div>
  </div>
</MenuContainer>

<style lang="postcss">
 .container {
   align-items: center;
   display: grid;
   grid-template-rows: 50% 50%;
   height: 100%;
   justify-content: center;
 }

 .logo {
   width: 100%;
 }

 .input-data {
   align-self: start;
   display: flex;
   flex-direction: column;
 }

 button {
   margin-bottom: 16px;
 }
</style>
