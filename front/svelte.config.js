import adapter from '@sveltejs/adapter-auto';
import preprocess from 'svelte-preprocess';
import { resolve } from 'path';

/** @type {import('@sveltejs/kit').Config} */
const config = {
	preprocess: preprocess({
    postcss: true
  }),

	kit: {
		adapter: adapter(),
		target: '#svelte',
    vite: {
      resolve: {
        alias: {
          $state: resolve("./src/state.ts"),
          $store: resolve("./src/store.ts"),
          $events: resolve("./src/events"),
          $services: resolve("./src/services")
        }
      }
    }
	}
};

export default config;
