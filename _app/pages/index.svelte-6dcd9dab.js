import{S as c,i as u,s as p,$ as m}from"../chunks/vendor-a7bd5f25.js";import{g as a}from"../chunks/navigation-51f4a605.js";import{b as r}from"../chunks/paths-28a87002.js";import{s as l}from"../chunks/store-344738a1.js";import"../chunks/singletons-12a22614.js";function f(t,e,i){let s;const n=l.get().select(o=>o.session);return m(t,n,o=>i(1,s=o)),s&&s.id?a(`${r}/join-room`):a(`${r}/player`),[n]}class k extends c{constructor(e){super();u(this,e,f,null,p,{})}}export{k as default};
