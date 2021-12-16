<script lang="ts">
 import store from "$store";
 import type { State } from "$state";
 import clockIcon from "$lib/images/timer.svg";
 import { onDestroy } from 'svelte';

 const st = store.get<State>();
 const room = st.select(st => st.room);
 const gameScreenTimeout = $room?.options.gameScreenTimeout;

 let time = gameScreenTimeout || 0;
 const interval = setInterval(() => {
   time = Math.max(time - 1000, 0);
 }, 1000);

 $: timeLeft = Math.trunc(time / 1000);

 onDestroy(() => {
  clearInterval(interval);
 });

</script>

<div class="clock"
     class:low={timeLeft < 3}
     style="--clock-icon: url({clockIcon})">
  <span>{timeLeft}</span>
</div>

<style lang="postcss">
 .clock {
   width: 50px;
   padding: 4px 8px;
   text-align: right;
   background-color: #222034;
   position: relative;

   &.low {
     background-color: #e03333;
   }

   &::before {
     content: '';
     width: 10px;
     height: 15px;
     position: absolute;
     left: 10px;
     top: 5px;
     background-image: var(--clock-icon);
     background-size: cover;
   }
 }

 @media only screen and (min-width: 900px) {
   .clock {
     width: 90px;
     padding: 4px 8px;

     &::before {
       width: 20px;
       height: 30px;
       top: 7px;
     }
   }
 }

</style>
