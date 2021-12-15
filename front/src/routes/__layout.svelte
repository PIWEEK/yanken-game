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

 /*
    @media (orientation: portrait) {
    .content {
    flex-direction: column;
    }
    }

    @media only screen and (min-width: 641px) and (orientation: landscape) {
    .main {
    align-items: center;
    justify-content: center;
    }

    .content {
    flex: initial;
    width: 640px;
    height: 320px;
    transform: scale(2);
    }
    }

    @media only screen and (min-width: 1440) and (orientation: landscape) {
    .content {
    transform: scale(2.25);
    }
    }

    @media only screen and (min-width: 1600) and (orientation: landscape) {
    .content {
    transform: scale(2.5);
    }
    }

    @media only screen and (min-width: 1760) and (orientation: landscape) {
    .content {
    transform: scale(2.75);
    }
    }

    @media only screen and (min-width: 1920px) and (orientation: landscape) {
    .content {
    transform: scale(3);
    }
    }

    @media only screen and (min-width: 2080px) and (orientation: landscape) {
    .content {
    transform: scale(3.25);
    }
    }

    @media only screen and (min-width: 2240px) and (orientation: landscape) {
    .content {
    transform: scale(3.5);
    }
    }
  */

</style>
