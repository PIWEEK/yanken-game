import{S as pe,i as he,s as we,j as q,m as C,o as Q,x as G,u as W,v as Z,$ as S,e as h,t as z,k as R,c as w,a as $,g as X,d as _,n as k,b as v,a5 as me,f as _e,E as i,a1 as Ae,r as $e,w as je,a4 as ye}from"../../chunks/vendor-a7bd5f25.js";import{g as Ve}from"../../chunks/navigation-d8ceaada.js";import{b as Ie}from"../../chunks/paths-c3a241ee.js";import{s as Re}from"../../chunks/store-344738a1.js";import{J as ke,a as Ee}from"../../chunks/websockets-72c92e35.js";import{M as be}from"../../chunks/MenuContainer-eb9e4ac9.js";import{P as re}from"../../chunks/PlayerCard-9b25eda5.js";var Ne="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADcAAABVCAYAAADt53wXAAAAAXNSR0IArs4c6QAAAkNJREFUeJztnDtLA1EQhXd1iRrFFxg1aUIqQcEm2GoliCDa+APS2fgPRMTGRmxT2Wuhoo2WFgaRNIIhIBhtYiMoqHkQE2PvGWHcjcIM85WHvZs9GfYw9+7edRPxpOOXwtoRJTeZw93vQmJz0f+1PGRBa/N9NgGYOamYOamYOamYOal43ANn03ugHT99gvZY4/1fbaV3HLu0AVr0cJ11PvI3fI8UgJmTimpz7EDJvraDtjD0AdObdLGDdb7Q/R2MrUeGYRoUBNWVM3NSUW2OHSjPdbzXueFBUZ2YBC2cu/R9PgrVlTNzUlFtzqOmMhTnL398JY7jjBxsQdfyPLUMSVaNjePgnXmQVFfOzElFtTmPmsokextwY08P1OG4fBnHBoFaQ3GIp0adxRyETJR4WqW6cmZOKqrNedOjOG3Zvm2A1kzFcPRZAaQg0yAug1f7EDJvYzMQMqorZ+akotqcVyqV4eZsphJw4AoRHq2GWlfpvLkGjepk+i5OQFNdOTMnFdXm2IuyFP/RjVCETzFkuitZCEbVlTNzUlFtLlCgtBqqGwmC6sqZOamoNudRXUZ6twgaNQ0KsobS6vCgUF05MycVMycVMycVMycVNxFPwtrDD5v+WATpPKi1EYr+ygFotfaYPeVRg5mTihuZWwWxJ59h7Rj+4b0RFlR4UEFBQYVHqIHTNNWVM3NSUW3O68lnKJ3aU8MKGW6X8QtY4WGfL9CEmZMKuRmP+kZKhXjdvauY4377hAW386DCg0J15cycVFSb+wKXKonGfL3FBgAAAABJRU5ErkJggg==",Pe="/yanken-game/_app/assets/winner-live-7fb33d66.gif";function de(l,e,s){const t=l.slice();return t[10]=e[s],t}function ge(l){var t,c;let e,s;return e=new re({props:{cardType:"small",name:(t=l[10])==null?void 0:t.name,avatar:(c=l[10])==null?void 0:c.avatar}}),{c(){q(e.$$.fragment)},l(n){C(e.$$.fragment,n)},m(n,f){Q(e,n,f),s=!0},p(n,f){var j,A;const r={};f&4&&(r.name=(j=n[10])==null?void 0:j.name),f&4&&(r.avatar=(A=n[10])==null?void 0:A.avatar),e.$set(r)},i(n){s||(G(e.$$.fragment,n),s=!0)},o(n){W(e.$$.fragment,n),s=!1},d(n){Z(e,n)}}}function De(l){var ce,ie,ue,fe;let e,s,t,c,n,f,r,j,A,p,O,y,g,H,V,E,le,K,I,L,B,x,ee,b,te,N,se,Y,ae,oe;p=new re({props:{cardType:"full",name:(ce=l[0])==null?void 0:ce.name,avatar:(ie=l[0])==null?void 0:ie.avatar}}),I=new re({props:{cardType:"full",name:(ue=l[1])==null?void 0:ue.name,avatar:(fe=l[1])==null?void 0:fe.avatar}});let P=l[2],o=[];for(let a=0;a<P.length;a+=1)o[a]=ge(de(l,P,a));const ve=a=>W(o[a],1,1,()=>{o[a]=null});return{c(){e=h("div"),s=h("div"),t=h("div"),c=z("Winner"),n=R(),f=h("div"),r=h("img"),A=R(),q(p.$$.fragment),O=R(),y=h("div"),g=z("Not winner"),H=R(),V=h("div"),E=h("img"),K=R(),q(I.$$.fragment),L=R(),B=h("div"),x=z("Bunch of losers"),ee=R(),b=h("div");for(let a=0;a<o.length;a+=1)o[a].c();te=R(),N=h("button"),se=z("Play again"),this.h()},l(a){e=w(a,"DIV",{class:!0});var u=$(e);s=w(u,"DIV",{class:!0});var m=$(s);t=w(m,"DIV",{class:!0});var T=$(t);c=X(T,"Winner"),T.forEach(_),n=k(m),f=w(m,"DIV",{class:!0});var D=$(f);r=w(D,"IMG",{src:!0,alt:!0,class:!0}),A=k(D),C(p.$$.fragment,D),D.forEach(_),O=k(m),y=w(m,"DIV",{class:!0});var F=$(y);g=X(F,"Not winner"),F.forEach(_),H=k(m),V=w(m,"DIV",{class:!0});var M=$(V);E=w(M,"IMG",{src:!0,alt:!0,class:!0}),K=k(M),C(I.$$.fragment,M),M.forEach(_),L=k(m),B=w(m,"DIV",{class:!0});var J=$(B);x=X(J,"Bunch of losers"),J.forEach(_),ee=k(m),b=w(m,"DIV",{class:!0});var d=$(b);for(let ne=0;ne<o.length;ne+=1)o[ne].l(d);d.forEach(_),m.forEach(_),te=k(u),N=w(u,"BUTTON",{class:!0});var U=$(N);se=X(U,"Play again"),U.forEach(_),u.forEach(_),this.h()},h(){v(t,"class","message svelte-19jwow9"),me(r.src,j=Pe)||v(r,"src",j),v(r,"alt","champion"),v(r,"class","svelte-19jwow9"),v(f,"class","winner svelte-19jwow9"),v(y,"class","message svelte-19jwow9"),me(E.src,le=Ne)||v(E,"src",le),v(E,"alt","drop"),v(E,"class","svelte-19jwow9"),v(V,"class","second svelte-19jwow9"),v(B,"class","message svelte-19jwow9"),v(b,"class","players svelte-19jwow9"),v(s,"class","results svelte-19jwow9"),v(N,"class","svelte-19jwow9"),v(e,"class","container svelte-19jwow9")},m(a,u){_e(a,e,u),i(e,s),i(s,t),i(t,c),i(s,n),i(s,f),i(f,r),i(f,A),Q(p,f,null),i(s,O),i(s,y),i(y,g),i(s,H),i(s,V),i(V,E),i(V,K),Q(I,V,null),i(s,L),i(s,B),i(B,x),i(s,ee),i(s,b);for(let m=0;m<o.length;m+=1)o[m].m(b,null);i(e,te),i(e,N),i(N,se),Y=!0,ae||(oe=Ae(N,"click",l[7]),ae=!0)},p(a,u){var D,F,M,J;const m={};u&1&&(m.name=(D=a[0])==null?void 0:D.name),u&1&&(m.avatar=(F=a[0])==null?void 0:F.avatar),p.$set(m);const T={};if(u&2&&(T.name=(M=a[1])==null?void 0:M.name),u&2&&(T.avatar=(J=a[1])==null?void 0:J.avatar),I.$set(T),u&4){P=a[2];let d;for(d=0;d<P.length;d+=1){const U=de(a,P,d);o[d]?(o[d].p(U,u),G(o[d],1)):(o[d]=ge(U),o[d].c(),G(o[d],1),o[d].m(b,null))}for($e(),d=P.length;d<o.length;d+=1)ve(d);je()}},i(a){if(!Y){G(p.$$.fragment,a),G(I.$$.fragment,a);for(let u=0;u<P.length;u+=1)G(o[u]);Y=!0}},o(a){W(p.$$.fragment,a),W(I.$$.fragment,a),o=o.filter(Boolean);for(let u=0;u<o.length;u+=1)W(o[u]);Y=!1},d(a){a&&_(e),Z(p),Z(I),ye(o,a),ae=!1,oe()}}}function Me(l){let e,s;return e=new be({props:{$$slots:{default:[De]},$$scope:{ctx:l}}}),{c(){q(e.$$.fragment)},l(t){C(e.$$.fragment,t)},m(t,c){Q(e,t,c),s=!0},p(t,[c]){const n={};c&8199&&(n.$$scope={dirty:c,ctx:t}),e.$set(n)},i(t){s||(G(e.$$.fragment,t),s=!0)},o(t){W(e.$$.fragment,t),s=!1},d(t){Z(e,t)}}}function Ge(l){const e=l.room,s=e==null?void 0:e.sessions;if(!e||!s||!e.results||!e.results[0]||!e.results[0][0])return null;const t=e==null?void 0:e.results[0][0],c=t==null?void 0:t.winner;return c?s[c]:null}function Oe(l){var f;const e=l.room,s=e==null?void 0:e.sessions;if(!e||!s||!e.results||!e.results[0]||!e.results[0][0])return null;const t=e==null?void 0:e.results[0][0],c=t==null?void 0:t.winner,n=(f=t==null?void 0:t.players)==null?void 0:f.find(r=>r!==c);return n?s[n]:null}function Be(l){const e=l.room,s=e==null?void 0:e.sessions;if(!e||!s||!e.results||!e.results[0]||!e.results[0][0])return[];const t=e.results[0][0],c=t.winner,n=t.players.find(r=>r!==c);return e.players.filter(r=>r!==c&&r!==n).map(r=>s[r])}function Te(l,e,s){let t,c,n,f;const r=Re.get(),j=r.select(g=>g.room);S(l,j,g=>s(8,t=g));const A=r.select(Ge);S(l,A,g=>s(0,c=g));const p=r.select(Oe);S(l,p,g=>s(1,n=g));const O=r.select(Be);S(l,O,g=>s(2,f=g));function y(){const g=t==null?void 0:t.id;!g||(r.emit(new ke(g)),r.emit(new Ee(g)),Ve(`${Ie}/game/wait-players`))}return[c,n,f,j,A,p,O,y]}class Qe extends pe{constructor(e){super();he(this,e,Te,Me,we,{})}}export{Qe as default};
