<script lang="ts">
 import { goto } from "$app/navigation";
 import logo from "$lib/images/yanken.png";
 import MenuContainer from "$components/MenuContainer.svelte";

 let name = "Kidd";

 function setupName() {
   goto(`/avatar?name=${name}`);
 }
</script>

<MenuContainer>
  <div class="container">
    <img class="logo" src={logo} alt="Yanken Game"/>

    <div class="chose-name">
      <label for="name">Choose your name</label>
      <div><input name="name" type="text" bind:value={name} autoComplete="off"/></div>
      <div><button disabled={!name} on:click={setupName}>GO!</button></div>
    </div>
  </div>
</MenuContainer>

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
</style>
