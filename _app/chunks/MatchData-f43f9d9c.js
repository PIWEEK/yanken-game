import{S as P,i as w,s as E,e as S,j as h,k as T,c as k,a as q,m as g,n as z,d as $,b as I,f as V,o as b,E as A,x as j,u as C,v as D}from"./vendor-a7bd5f25.js";import{P as M}from"./PlayerCard-8965b305.js";function B(l){var c,f,p,d;let e,t,m,n,s;return t=new M({props:{name:(c=l[0])==null?void 0:c.name,avatar:(f=l[0])==null?void 0:f.avatar,cardType:"small",flipx:!0}}),n=new M({props:{name:((p=l[1])==null?void 0:p.name)||"mr. nobody",avatar:((d=l[1])==null?void 0:d.avatar)||"bot",cardType:"small"}}),{c(){e=S("div"),h(t.$$.fragment),m=T(),h(n.$$.fragment),this.h()},l(a){e=k(a,"DIV",{class:!0});var r=q(e);g(t.$$.fragment,r),m=z(r),g(n.$$.fragment,r),r.forEach($),this.h()},h(){I(e,"class","match-info svelte-i5zt1j")},m(a,r){V(a,e,r),b(t,e,null),A(e,m),b(n,e,null),s=!0},p(a,[r]){var _,y,u,v;const o={};r&1&&(o.name=(_=a[0])==null?void 0:_.name),r&1&&(o.avatar=(y=a[0])==null?void 0:y.avatar),t.$set(o);const i={};r&2&&(i.name=((u=a[1])==null?void 0:u.name)||"mr. nobody"),r&2&&(i.avatar=((v=a[1])==null?void 0:v.avatar)||"bot"),n.$set(i)},i(a){s||(j(t.$$.fragment,a),j(n.$$.fragment,a),s=!0)},o(a){C(t.$$.fragment,a),C(n.$$.fragment,a),s=!1},d(a){a&&$(e),D(t),D(n)}}}function F(l,e,t){let{player1:m}=e,{player2:n}=e;return l.$$set=s=>{"player1"in s&&t(0,m=s.player1),"player2"in s&&t(1,n=s.player2)},[m,n]}class J extends P{constructor(e){super();w(this,e,F,B,E,{player1:0,player2:1})}}export{J as M};
