<script lang="ts">
 import { goto } from "$app/navigation";
 import type { State } from "$state";
 import { Hello } from "$events";
 import store from "$store";
 import { page } from '$app/stores';
 import MenuContainer from "$components/MenuContainer.svelte";
 import PlayerCard from "$components/PlayerCard.svelte";
 import ColorPicker from "$components/ColorPicker.svelte";

 let color = "red";
 const st = store.get<State>();

 function startGame() {
   const name = $page.query.get('name')
   if (name) {
     st.emit(new Hello(name, color));
   }
   goto(`/join-room`);
 }

 function changeColor(event: CustomEvent<string>) {
   color = event.detail;
 }
</script>

<MenuContainer>
  <div class="container">
    <label for="name">Choose your color</label>
    <PlayerCard avatar={color} flipx={true} />
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

 @media only screen and (min-width: 900px) {

  .container {
    height: 800px;
  }

   label[for=name] {
     font-size: 32px;
   }

 }
</style>
