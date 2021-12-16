<script lang="ts">
 import type { State, Room } from "$state";
 import type { Subscription } from "rxjs";
 import { goto } from "$app/navigation";
 import { base } from '$app/paths';
 import { onMount, onDestroy } from "svelte";
 import store from "$store";
 
 const st = store.get<State>();
 const room = st.select(state => state.room);

 let roomSub: Subscription;

 onMount(async () => {
   roomSub = room.subscribe((room?: Room) => {
     if (!room) {
       return;
     }

     const prefix = `${base}/game/${room.id}`;

     let state: string = room.status;
     if (state === "playing") {
       state = state + "/" + room.stage;
     }

     switch (state) {
       case "waiting":
         goto(`${prefix}/wait-players`);
         break;

       case "playing/pairing":
         goto(`${prefix}/turn-pairing`);
         break;

       case "playing/game":
       case "playing/gameEnd":
         goto(`${prefix}/turn`);
         break;

       case "playing/result":
         goto(`${prefix}/turn-result`);
         break;
         
       case "ended":
         goto(`${prefix}/game-result`);
         break;

       default:
         console.error("UNKNOWN:", state);
     }
   });
 });

 onDestroy(async () => {
   if (roomSub) {
     roomSub.unsubscribe();
   }
 });
</script>

<slot></slot>
