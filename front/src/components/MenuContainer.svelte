<script lang="ts">
 import type { State } from "$state";
 import store from "$store";

 import background from "$lib/images/select-bg.png";

 const st = store.get<State>();
 const room = st.select(state => state.room);
</script>

{#if $room?.id}
  <div class="room-name">Room {$room?.id}</div>
{/if}

<div class="main" style="--background-image: url({background})">
  <slot></slot>
</div>

<style lang="postcss">
 @import "$styles/forms.css";

 .main {
   width: 100%;
   height: 100%;
   background-image: var(--background-image);
   background-size: cover;
   padding: 30px;
   display: flex;
   justify-content: center;
   align-items: center;
 }

 .room-name {
   color: #c27eca;
   font-size: 14px;
   padding: 8px 0;
   position: absolute;
   text-align: center;
   top: 0px;
   width: 100%;
 }

 @media only screen and (min-width: 900px) {
   .room-name {
     font-size: 24px;
     padding: 12px 0;
   }
 }
</style>
