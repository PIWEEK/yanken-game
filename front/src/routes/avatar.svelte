<script lang="ts">
 import { goto } from "$app/navigation";

 import MenuContainer from "$components/MenuContainer.svelte";
 import PlayerCard from "$components/PlayerCard.svelte";
 import ColorPicker from "$components/ColorPicker.svelte";

 let color = "red";

 function startGame() {
   console.log("data", {color});
   goto(`/join-room`);
 }

 function changeColor(event: CustomEvent<string>) {
   color = event.detail;
 }
</script>

<MenuContainer>
  <div class="container">
    <label for="name">Choose your color</label>
    <PlayerCard color={color} flipx={true} />
    <ColorPicker on:change={changeColor} />
    <button on:click={startGame}>GO!</button>
  </div>
</MenuContainer>

<style lang="postcss">
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
