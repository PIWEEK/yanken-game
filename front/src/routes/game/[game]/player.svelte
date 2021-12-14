<script lang="ts">
 import { goto } from "$app/navigation";
 import logo from "$lib/images/yanken.png" 
 import background from "$lib/images/select-bg.png"

 let room = "apothecary";
 let name = "Kidd";

 function setupName() {
   goto(`/game/${room}/avatar`);
 }
 
</script>

<div class="room-name">Room {room}</div>
<div class="main" style="--background-image: url({background})">
  <div class="container">
    <img class="logo" src={logo} alt="Yanken Game"/>

    <div class="chose-name">
      <label for="name">Choose your name</label>
      <input name="name" type="text" bind:value={name} />
      <button disabled={!name} on:click={setupName}>GO!</button>
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

 .room-name {
   position: absolute;
   width: 100%;
   top: 0px;
   color: #c27eca;
   font-size: 14px;
   text-align: center;
   padding: 8px 0;
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

 .chose-name {
   display: flex;
   flex-direction: column;
   align-self: start;

   & label[for=name] {
     text-align: center;
     font-size: 24px;
   }

   & > * {
     margin-bottom: 32px;
   }
 }
</style>
