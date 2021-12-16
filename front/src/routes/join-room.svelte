<script lang="ts">
 import { goto } from "$app/navigation";
 import type { State } from "$state";
 import { Join, JoinBots } from "$events";
 import store from "$store";
 import MenuContainer from "$components/MenuContainer.svelte";
 import PlayerCard from "$components/PlayerCard.svelte";
 
 const st = store.get<State>();
 const session = st.select(state => state.session);
 let room: string;

 function joinRoom() {
   st.emit(new Join(room));
   st.emit(new JoinBots(room));
   goto(`/game/${room}/wait-players`);
 }

 function editPlayer() {
   goto(`/player`);
 }
</script>

<MenuContainer>
  <div class="container">
    <!--img class="logo" src={logo} alt="Yanken Game"/-->
    <div class="input-data">
      <label for="name">ENTER GAME AS...</label>
      <PlayerCard avatar={$session?.avatar} flipx={true} />
      <label for="name">{$session?.name}</label>
      <button class="no-background" on:click={editPlayer}>
        EDIT
      </button>
      <input
        name="name"
        placeholder="Type room name..."
        type="text"
        autocomplete="off"
        bind:value={room} />
      <button class="join" disabled={!room} on:click={joinRoom}>
        JOIN GAME
      </button>
    </div>
  </div>
</MenuContainer>

<style lang="postcss">
 label[for=name] {
   margin-bottom: 16px;
   text-align: center;
   font-size: 24px;
 }

 .container {
 }

 .input-data {
   align-self: start;
   display: flex;
   flex-direction: column;
   align-items: center;
 }

 .join {
  margin-top: 16px;
  width: 300px;
 }

 input {
  max-width: 300px;
  margin-top: 32px;
 }

 button {
   margin-bottom: 16px;
   padding-right:20px;
   padding-left:20px;
 }

 @media only screen and (min-width: 900px) {

   label[for=name] {
     font-size: 32px;
   }

 }

</style>
