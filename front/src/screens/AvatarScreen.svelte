<script lang="ts">
 import type { State } from "$state";
 import { Hello, ChangeScreen } from "$events";
 import store from "$store";

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
   if ($session.name) {
     st.emit(new Hello($session.name, avatar));
     st.emit(new ChangeScreen("join-room"));
   }
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

<div class="container">
  <label for="name">Get pretty!</label>
  <PlayerCard avatar={avatar} flipx={false} />
  <HandPicker on:change={changeHand} />
  <ColorPicker on:change={changeColor} />
  <button on:click={startGame}>GO!</button>
</div>

<style lang="postcss">
 .container {
   height: 100%;
   display: grid;
   justify-content: center;
   align-items: center;
   max-width: 300px;

   & > :global(.suc-data) {
     max-width: 74%;
     justify-self: center;
   }
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
