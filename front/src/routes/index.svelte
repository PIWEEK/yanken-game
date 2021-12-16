<script lang="ts">
 import { State, initialState } from "$state";
 import store from "$store";
 import { StartWebsocket, Hello } from "$events";

 import PlayerScreen from "$screens/PlayerScreen.svelte";
 import AvatarScreen from "$screens/AvatarScreen.svelte";
 import JoinRoomScreen from "$screens/JoinRoomScreen.svelte";
 import GameScreen from "$screens/GameScreen.svelte"
 import MenuContainer from "$components/MenuContainer.svelte";

 const st = store.start(initialState);
 st.emit(new StartWebsocket());

 const screen = st.select(state => state.screen);
 const session = st.select(state => state.session);
 const room = st.select(state => state.room);

 $: if ($session) {
   localStorage.setItem("session", JSON.stringify($session));
 }

 // $: if ($session && $session.id && $session.avatar) {
 //   st.emit(new Hello($session.name, $session.avatar));
 // }

 $: if ($room) {
   localStorage.setItem("room", JSON.stringify($room));
 }

</script>

<div class="main">
  <div class="content">
    {#if $screen === "game"}
      <GameScreen/>
    {:else}
      <MenuContainer>
        {#if $screen === "player" || (!$screen && !$session?.id)}
          <PlayerScreen/>
        {:else if $screen === "join-room" || (!$screen && $session?.id)}
          <JoinRoomScreen/>
        {:else if $screen === "avatar"}
          <AvatarScreen/>
        {:else if $screen === "game"}
          <GameScreen/>
        {:else}
          <h1>Not found {$screen}</h1>
        {/if}
      </MenuContainer>
    {/if}
  </div>
</div>

<style lang="postcss">
 .main {
   width: 100%;
   height: 100%;
   overflow: hidden;
   background: black;
   display: flex;
   flex-direction: column;
   color: white;
 }

 .content {
   flex: 1;
   background: white;
   height: 100%;

   display: flex;
   justify-content: center;
   align-items: center;
   flex-direction: row;
 }
</style>
