<script lang="ts">
 import { goto } from "$app/navigation";
 import logo from "$lib/images/yanken.png";
 import MenuContainer from "$components/MenuContainer.svelte";

 let name = "Kidd";

 function setupName() {
   goto(`/avatar`);
 }
</script>

<MenuContainer>
  <div class="container">
    <img class="logo" src={logo} alt="Yanken Game"/>

    <div class="chose-name">
      <label for="name">Choose your name</label>
      <input name="name" type="text" bind:value={name} />
      <button disabled={!name} on:click={setupName}>GO!</button>
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
 }

 .chose-name {
   display: grid;
   align-self: start;
   grid-gap: 32px;
 }

 label[for=name] {
   text-align: center;
   font-size: 24px;
 }
</style>
