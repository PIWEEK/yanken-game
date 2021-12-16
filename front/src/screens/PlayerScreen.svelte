<script lang="ts">
 import type { State } from "$state";
 import store from "$store";
 import { SetSessionName, ChangeScreen } from "$events";

 import logo from "$lib/images/yanken-live.gif";

 const st = store.get<State>();
 const session = st.select(state => state.session);

 let name = $session?.name;

 function setupName() {
   if (name) {
     st.emit(new SetSessionName(name));
     st.emit(new ChangeScreen("avatar"));
   }
 }
</script>

<div class="container">
  <img class="logo" src={logo} alt="Yanken Game"/>

  <div class="chose-name">
    <label for="name">Choose your name</label>
    <div><input name="name" type="text" bind:value={name} autoComplete="off"/></div>
    <div><button disabled={!name} on:click={setupName}>GO!</button></div>
  </div>
</div>

<style lang="postcss">
 .container {
   height: 100%;
   display: grid;
   grid-template-rows: 50% 50%;
   justify-content: center;
   align-items: center;
 }

 .logo {
   width: 100%;
   max-width: 760px;
 }

 .chose-name {
   display: grid;
   align-self: start;
   grid-gap: 32px;
   align-items: center;
   & div {
     text-align: center;
   }
 }

 label[for=name] {
   text-align: center;
   font-size: 24px;
 }
 button {
   margin-bottom: 16px;
   width: 300px;
 }
 input {
   max-width: 300px;
 }

 @media only screen and (min-width: 900px) {

   label[for=name] {
     font-size: 32px;
   }

 }
</style>
