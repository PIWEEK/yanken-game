<script lang="ts">
 import type { State } from "$state";
 import store from "$store";

 import GameResult from "$screens/game/GameResult.svelte";
 import GameTurn from "$screens/game/GameTurn.svelte";
 import GameTurnPairing from "$screens/game/GameTurnPairing.svelte";
 import GameTurnResult from "$screens/game/GameTurnResult.svelte";
 import GameWaitingPlayers from "$screens/game/GameWaitingPlayers.svelte";
 import MenuContainer from "$components/MenuContainer.svelte";

 const st = store.get<State>();
 const room = st.select(state => state.room);
</script>

{#if $room.status === "playing" && ($room.stage === "game" || $room.stage === "gameEnd") }
  <GameTurn/>
{:else}
  <MenuContainer>
    {#if $room.status === "waiting"}
      <GameWaitingPlayers/>
    {:else if $room.status === "playing" && $room.stage === "pairing"}
      <GameTurnPairing/>
    {:else if $room.status === "playing" && $room.stage === "result"}
      <GameTurnResult/>
    {:else if $room.status === "ended"}
      <GameResult/>
    {/if}
  </MenuContainer>
{/if}

