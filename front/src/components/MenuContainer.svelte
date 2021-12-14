<script lang="ts">
 import { page } from '$app/stores';
 import background from "$lib/images/select-bg.png";
</script>

{#if $page.params.game}
  <div class="room-name">Room {$page.params.game}</div>
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
</style>
