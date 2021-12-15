<script lang="ts">
 import store from "$store";
 import type { State } from "$state";
 import { StartGame, Hello, Join, SendTurn } from "$events";

 const st = store.get<State>();
 const sessionId = st.select(state => state.sessionId);
 const room = st.select(state => state.room);

 function hello() {
  st.emit(new Hello("fulano"));
 }

 function join() {
  st.emit(new Join("test"));
 }

 function startGame() {
  st.emit(new StartGame());
 }

 function sendTurn(turn: number) {
   st.emit(new SendTurn(turn));
 }

</script>

<div class="test">
  <button on:click={hello}>Hello</button>
  <button on:click={join}>Join</button>
  <button on:click={startGame}>START GAME</button>
  <button on:click={() => sendTurn(1)}>PIEDRA</button>
  <button on:click={() => sendTurn(2)}>PAPEL</button>
  <button on:click={() => sendTurn(3)}>TIJERA</button>

  <div>Session: {$sessionId}</div>

  {#if $room}
    <div>Round {$room.round}</div>
    <div>Status: {$room.status}</div>
    <div>Stage: {$room.stage}</div>
    <div>
      Players
      <ul>
        {#each $room.players as player}
          <li>{player}</li>
        {/each}
      </ul>
    </div>

    {#if $room.livePlayers}
    <div>
      Live Players
      <ul>
        {#each $room.livePlayers as player}
          <li>{player}</li>
        {/each}
      </ul>
    </div>
    {/if}

    {#if $room.deadPlayers}
    <div>
      Dead Players
      <ul>
        {#each $room.deadPlayers as player}
          <li>{player}</li>
        {/each}
      </ul>
    </div>
    {/if}

    {#if $room.results}
    <div>
      Results
      {#each $room.results as pairs}
        {#each pairs as result}
          <ul> Round: {result.round}
          {#each Object.entries(result.responses) as [user, result]}
            <li>{user} : {result}</li>
          {/each}
          <li>Winner: {result.winner}</li>
          </ul>
        {/each}
      {/each}
    </div>
    {/if}
  {/if}
</div>

<style lang="postcss">
.test {
  color: pink;
}
</style>
