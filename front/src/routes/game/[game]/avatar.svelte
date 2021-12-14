<script lang="ts">
 import { goto } from "$app/navigation";
 import background from "$lib/images/select-bg.png"

 import PlayerCard from "$components/PlayerCard.svelte";
 import ColorPicker from "$components/ColorPicker.svelte";
 
 let room = "apothecary";
 let color = "red";

 function startGame() {
   console.log("data", {color});
   goto(`/game/${room}/wait-players`);
 }
 
</script>

<div class="room-name">Room {room}</div>
<div class="main" style="--background-image: url({background})">
  <div class="container">
    <label for="name">Choose your color</label>
    <PlayerCard color="blue" flipx={true} />
    <ColorPicker/>
    <button on:click={startGame}>GO!</button>
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
   justify-content: center;
   align-items: center;
 }

 label[for=name] {
   margin-top: 30px;
   text-align: center;
   font-size: 24px;
 }

</style>

