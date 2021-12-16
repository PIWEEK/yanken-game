<script lang="ts">
 export let progress = 0;

 const totalBullets = 14;

 $: lighted = Math.floor(totalBullets * progress / 100);
 $: notLighted = totalBullets - lighted;

 $: progressBullets = [
   ...(new Array(lighted).fill(true)),
   ...(new Array(notLighted).fill(false))
 ];
</script>

<div class="progress" style="--bullet-count: {totalBullets}">
  {#each progressBullets as bullet}
    <div class="bullet" class:filled={!bullet}></div>
  {/each}
</div>

<style lang="postcss">
 .progress {
   display: grid;
   grid-template-columns: repeat(var(--bullet-count), 1fr);
   grid-gap: 6px;
 }

 .bullet {
   width: 9px;
   height: 9px;
   background-color: white;
 }

 .bullet.filled {
   opacity: 40%;
 }

  @media only screen and (min-width: 900px) {

   .progress {
     grid-gap: 8px;
   }
  .bullet {
    width: 16px;
    height: 16px;
  }

}
</style>
