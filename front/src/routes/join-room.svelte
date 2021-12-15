<script lang="ts">
 import { goto } from "$app/navigation";
 import type { State } from "$state";
 import { Join, Hello } from "$events";
 import store from "$store";
 import logo from "$lib/images/yanken.png";
 import MenuContainer from "$components/MenuContainer.svelte";

 const st = store.get<State>();
 const session = st.select(state => state.session);
 let room: string;

 function joinRoom() {
   st.emit(new Join(room));
   goto(`/game/${room}/wait-players`);
 }
</script>

<MenuContainer>
  <div class="container">
    <img class="logo" src={logo} alt="Yanken Game"/>

    <div class="input-data">
      <button disabled={!room} on:click={joinRoom}>
        JOIN GAME
      </button>

      <input
        name="name"
        placeholder="Type room name..."
        type="text"
        pattern="[0-9]+"
        bind:value={room} />
    </div>
  </div>
</MenuContainer>

<style lang="postcss">
 .container {
   align-items: center;
   display: grid;
   grid-template-rows: 50% 50%;
   height: 100%;
   justify-content: center;
 }

 .logo {
   width: 100%;
   max-width: 760px;
 }

 .input-data {
   align-self: start;
   display: flex;
   flex-direction: column;
   align-items: center;
 }

 button {
  margin-bottom: 16px;
  width: 300px;
 }
 input {
  max-width: 300px;
 }
</style>
