<script lang="ts">
 import type { State } from "$state";
 import { Join, ChangeScreen } from "$events";
 import store from "$store";
 import PlayerCard from "$components/PlayerCard.svelte";

 const st = store.get<State>();
 const session = st.select(state => state.session);
 let room: string;

 function joinRoom() {
   st.emit(new Join(room));
   st.emit(new ChangeScreen("game"));
 }

 function editPlayer() {
   st.emit(new ChangeScreen("player"));
 }
</script>

<div class="container">
  <!--img class="logo" src={logo} alt="Yanken Game"/-->
  <div class="input-data">
    <label for="name">ENTERING GAME AS...</label>
    <PlayerCard avatar={$session?.avatar} flipx={false} />
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
    <div>
      <button class="join" disabled={!room} on:click={joinRoom}>
        JOIN GAME
      </button>
    </div>
  </div>
</div>

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
