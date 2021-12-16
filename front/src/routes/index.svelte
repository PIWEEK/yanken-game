<script lang="ts">
 import { goto } from "$app/navigation";
 import { base } from '$app/paths';
 import type { State } from "$state";
 import store from "$store";

 const st = store.get<State>();
 const session = st.select(state => state.session);

 if ($session && $session.id) {
  goto(`${base}/join-room`);
 } else {
  goto(`${base}/player`);
 }
 </script>
