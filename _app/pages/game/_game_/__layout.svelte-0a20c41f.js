import{S as g,i as p,s as _,D as m,F as d,G as $,H as b,x as y,u as k,A as h,a8 as j}from"../../../chunks/vendor-06d4fd3a.js";import{g as u}from"../../../chunks/navigation-51f4a605.js";import{s as v}from"../../../chunks/store-9ef01361.js";import"../../../chunks/singletons-12a22614.js";function w(n){let t;const r=n[1].default,e=m(r,n,n[0],null);return{c(){e&&e.c()},l(s){e&&e.l(s)},m(s,o){e&&e.m(s,o),t=!0},p(s,[o]){e&&e.p&&(!t||o&1)&&d(e,r,s,s[0],t?b(r,s[0],o,null):$(s[0]),null)},i(s){t||(y(e,s),t=!0)},o(s){k(e,s),t=!1},d(s){e&&e.d(s)}}}function N(n,t,r){let{$$slots:e={},$$scope:s}=t;const f=v.get().select(a=>a.room);let c;return h(async()=>{c=f.subscribe(a=>{if(!a)return;const i=`/game/${a.id}`;let l=a.status;switch(l==="playing"&&(l=l+"/"+a.stage),l){case"waiting":u(`${i}/wait-players`);break;case"playing/pairing":u(`${i}/turn-pairing`);break;case"playing/game":case"playing/gameEnd":u(`${i}/turn`);break;case"playing/result":u(`${i}/turn-result`);break;case"ended":u(`${i}/game-result`);break;default:console.error("UNKNOWN:",l)}})}),j(async()=>{c&&c.unsubscribe()}),n.$$set=a=>{"$$scope"in a&&r(0,s=a.$$scope)},[s,e]}class C extends g{constructor(t){super();p(this,t,N,w,_,{})}}export{C as default};
