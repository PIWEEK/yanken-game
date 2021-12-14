<script lang="ts">
 import { goto } from "$app/navigation";
 
 import logo from "$lib/images/yanken.png";
 import background from "$lib/images/select-bg.png";

 let room: string;

 function joinRoom() {
   goto(`/game/${room}/player`);
 }
</script>

<div class="main" style="--background-image: url({background})">
  <div class="container">
    <img class="logo" src={logo} alt="Yanken Game"/>

    <div class="input-data">
      <button disabled={!room} on:click={joinRoom}>JOIN GAME</button>
      <input name="name" placeholder="Type room name..." type="text" bind:value={room} />
    </div>
  </div>
</div>

<style lang="postcss">
 @import "$styles/forms.css";
 
 .main {
   width: 100%;
   height: 100%;
   background-image: var(--background-image);
   background-size: cover;
   padding: 30px;
 }

 .container {
   height: 100%;
   display: grid;
   grid-template-rows: 50% 50%;
   justify-content: center;
   align-items: center;
 }

 .logo {
   width: 100%;
 }

 .input-data {
   display: flex;
   flex-direction: column;
   align-self: start;

   & > * {
     margin-bottom: 16px;
   }
 }
</style>
