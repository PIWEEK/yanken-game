import adapter from '@sveltejs/adapter-static';
import preprocess from 'svelte-preprocess';
import { resolve } from 'path';

/** @type {import('@sveltejs/kit').Config} */
const config = {
	preprocess: preprocess({
    postcss: true
  }),

	kit: {
		adapter: adapter({
			pages: 'build',
			assets: 'build',
			fallback: null
		}),
    ssr: false,
		target: '#app',
    vite: {
      resolve: {
        alias: {
          $state: resolve("./src/state.ts"),
          $store: resolve("./src/store.ts"),
          $events: resolve("./src/events"),
          $styles: resolve("./src/styles"),
          $components: resolve("./src/components"),
        }
      }
    }
	}
};

// Uncomment before deploying to github pages
// config.kit.paths = {
//   base: "/yanken-game"
// }

export default config;
