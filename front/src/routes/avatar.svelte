<script lang="ts">
 import { goto } from "$app/navigation";
 import { base } from '$app/paths';
 import type { State } from "$state";
 import { Hello } from "$events";
 import store from "$store";
 import { page } from '$app/stores';
 import MenuContainer from "$components/MenuContainer.svelte";
 import PlayerCard from "$components/PlayerCard.svelte";
 import ColorPicker from "$components/ColorPicker.svelte";
 import HandPicker  from "$components/HandPicker.svelte";
 const st = store.get<State>();
 const session = st.select(state => state.session);

 let avatar = $session?.avatar || "red-rock";
 let color = avatar.split("-")[0];
 let hand = avatar.split("-")[1];

 function startGame() {
   const name = $page.query.get('name')
   if (name) {
     st.emit(new Hello(name, avatar));
   }
   goto(`${base}/join-room`);
 }

 function changeHand(event: CustomEvent<string>) {
   hand = event.detail;
   avatar = `${color}-${hand}`;
 }

 function changeColor(event: CustomEvent<string>) {
   color = event.detail;
   avatar = `${color}-${hand}`;
 }
</script>

<MenuContainer>
  <div class="container">
    <label for="name">Choose your fighter</label>
    <PlayerCard avatar={avatar} flipx={true} />
    <HandPicker on:change={changeHand} />
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
   text-align: center;
   font-size: 24px;
 }

 @media only screen and (min-width: 900px) {

  .container {
    max-height: 900px;
  }

   label[for=name] {
     font-size: 32px;
   }

   :global(button) {
     width: 300px;
     justify-self: center;
   }

 }
</style>
