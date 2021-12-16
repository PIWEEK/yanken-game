<script lang="ts">
 import store from "$store";
 import type { State } from "$state";
 import { initialState } from "$state";
 import { StartWebsocket } from "$events";

 // Initializes the store
 store.start(initialState);
 const st = store.get<State>();
 st.emit(new StartWebsocket());

 const session = st.select(state => state.session);
 session.subscribe(value => {
  if (value) {
    localStorage.setItem("session", JSON.stringify(value));
  }
 });

 const room = st.select(state => state.room);
 room.subscribe(value => {
  if (value) {
    localStorage.setItem("room", JSON.stringify(value));
  }
 });

</script>

<div class="main">
  <div class="content">
    <slot></slot>
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
