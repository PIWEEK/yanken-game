import{S as Ce,i as Pe,s as Ze,e as M,t as K,c as V,a as Y,g as j,d,b as o,a3 as Ae,a6 as O,f as T,E as m,h as me,_ as ce,$ as L,a8 as Ne,x as z,j as ue,m as fe,o as ge,u as F,v as ve,w as _e,a4 as Ue,k as N,n as U,a5 as de,a1 as Ie,a2 as He,r as Se}from"../../chunks/vendor-a7bd5f25.js";import{s as Ge}from"../../chunks/store-344738a1.js";import{c as Oe}from"../../chunks/websockets-72c92e35.js";import{P as Qe,r as Le,p as Fe,s as Ke}from"../../chunks/PlayerCard-9b25eda5.js";import{M as je}from"../../chunks/MatchData-f4fffa53.js";var xe="/yanken-game/_app/assets/play-bg-c0f8ec31.png",$e="/yanken-game/_app/assets/hands-bg-f862f23e.png",es="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAPAAAADwCAYAAAA+VemSAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDcuMS1jMDAwIDc5LmRhYmFjYmIsIDIwMjEvMDQvMTQtMDA6Mzk6NDQgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCAyMy4wIChXaW5kb3dzKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpBMDk3NEZDNjVDQ0QxMUVDOTFCMEQ4MjczNTRGN0ZCRiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpBMDk3NEZDNzVDQ0QxMUVDOTFCMEQ4MjczNTRGN0ZCRiI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkEwOTc0RkM0NUNDRDExRUM5MUIwRDgyNzM1NEY3RkJGIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkEwOTc0RkM1NUNDRDExRUM5MUIwRDgyNzM1NEY3RkJGIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+y1vh5AAACMxJREFUeNrs3ctt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",ss="data:image/png;base64,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",ts="/yanken-game/_app/assets/timer-ff7c6b24.svg";function ls(s){let e,r,l;return{c(){e=M("div"),r=M("span"),l=K(s[0]),this.h()},l(t){e=V(t,"DIV",{class:!0,style:!0});var u=Y(e);r=V(u,"SPAN",{});var i=Y(r);l=j(i,s[0]),i.forEach(d),u.forEach(d),this.h()},h(){o(e,"class","clock svelte-k6kg0u"),Ae(e,"--clock-icon","url("+ts+")"),O(e,"low",s[0]<3)},m(t,u){T(t,e,u),m(e,r),m(r,l)},p(t,[u]){u&1&&me(l,t[0]),u&1&&O(e,"low",t[0]<3)},i:ce,o:ce,d(t){t&&d(e)}}}function rs(s,e,r){let l,t;const i=Ge.get().select(B=>B.room);L(s,i,B=>r(3,t=B));let n=(t==null?void 0:t.options.gameScreenTimeout)||0;const k=setInterval(()=>{r(2,n=Math.max(n-1e3,0))},1e3);return Ne(()=>{clearInterval(k)}),s.$$.update=()=>{s.$$.dirty&4&&r(0,l=Math.trunc(n/1e3))},[l,i,n]}class as extends Ce{constructor(e){super();Pe(this,e,rs,ls,Ze,{})}}function qe(s,e,r){const l=s.slice();return l[24]=e[r],l}function Je(s){let e,r,l=s[2],t=[];for(let i=0;i<l.length;i+=1)t[i]=We(qe(s,l,i));const u=i=>F(t[i],1,1,()=>{t[i]=null});return{c(){e=M("div");for(let i=0;i<t.length;i+=1)t[i].c();this.h()},l(i){e=V(i,"DIV",{class:!0});var c=Y(e);for(let n=0;n<t.length;n+=1)t[n].l(c);c.forEach(d),this.h()},h(){o(e,"class","players-info svelte-1383h37")},m(i,c){T(i,e,c);for(let n=0;n<t.length;n+=1)t[n].m(e,null);r=!0},p(i,c){if(c&4){l=i[2];let n;for(n=0;n<l.length;n+=1){const k=qe(i,l,n);t[n]?(t[n].p(k,c),z(t[n],1)):(t[n]=We(k),t[n].c(),z(t[n],1),t[n].m(e,null))}for(Se(),n=l.length;n<t.length;n+=1)u(n);_e()}},i(i){if(!r){for(let c=0;c<l.length;c+=1)z(t[c]);r=!0}},o(i){t=t.filter(Boolean);for(let c=0;c<t.length;c+=1)F(t[c]);r=!1},d(i){i&&d(e),Ue(t,i)}}}function We(s){let e,r;return e=new je({props:{player1:s[24][0],player2:s[24][1]}}),{c(){ue(e.$$.fragment)},l(l){fe(e.$$.fragment,l)},m(l,t){ge(e,l,t),r=!0},p(l,t){const u={};t&4&&(u.player1=l[24][0]),t&4&&(u.player2=l[24][1]),e.$set(u)},i(l){r||(z(e.$$.fragment,l),r=!0)},o(l){F(e.$$.fragment,l),r=!1},d(l){ve(e,l)}}}function is(s){let e,r;return{c(){e=M("div"),r=K("You Win!"),this.h()},l(l){e=V(l,"DIV",{class:!0});var t=Y(e);r=j(t,"You Win!"),t.forEach(d),this.h()},h(){o(e,"class","message svelte-1383h37")},m(l,t){T(l,e,t),m(e,r)},p:ce,d(l){l&&d(e)}}}function ns(s){let e,r;return{c(){e=M("div"),r=K("You Lose!"),this.h()},l(l){e=V(l,"DIV",{class:!0});var t=Y(e);r=j(t,"You Lose!"),t.forEach(d),this.h()},h(){o(e,"class","message svelte-1383h37")},m(l,t){T(l,e,t),m(e,r)},p:ce,d(l){l&&d(e)}}}function As(s){var u;let e,r,l=((u=s[4])==null?void 0:u.name)+"",t;return{c(){e=M("div"),r=K("Observing "),t=K(l),this.h()},l(i){e=V(i,"DIV",{class:!0});var c=Y(e);r=j(c,"Observing "),t=j(c,l),c.forEach(d),this.h()},h(){o(e,"class","message svelte-1383h37")},m(i,c){T(i,e,c),m(e,r),m(e,t)},p(i,c){var n;c&16&&l!==(l=((n=i[4])==null?void 0:n.name)+"")&&me(t,l)},d(i){i&&d(e)}}}function Te(s){let e,r,l,t,u,i,c,n,k,B,v,y,Q,b,X,S;return{c(){e=M("button"),r=M("img"),u=N(),i=M("button"),c=M("img"),B=N(),v=M("button"),y=M("img"),this.h()},l(f){e=V(f,"BUTTON",{class:!0});var I=Y(e);r=V(I,"IMG",{alt:!0,src:!0,class:!0}),I.forEach(d),u=U(f),i=V(f,"BUTTON",{class:!0});var w=Y(i);c=V(w,"IMG",{alt:!0,src:!0,class:!0}),w.forEach(d),B=U(f),v=V(f,"BUTTON",{class:!0});var D=Y(v);y=V(D,"IMG",{alt:!0,src:!0,class:!0}),D.forEach(d),this.h()},h(){var f,I,w;o(r,"alt","Rock"),de(r.src,l=Le)||o(r,"src",l),o(r,"class","svelte-1383h37"),o(e,"class","action rock svelte-1383h37"),e.disabled=t=!!((f=s[5])==null?void 0:f.myResult),O(e,"selected",s[0]==="rock"),o(c,"alt","Paper"),de(c.src,n=Fe)||o(c,"src",n),o(c,"class","svelte-1383h37"),o(i,"class","action paper svelte-1383h37"),i.disabled=k=!!((I=s[5])==null?void 0:I.myResult),O(i,"selected",s[0]==="paper"),o(y,"alt","Scissors"),de(y.src,Q=Ke)||o(y,"src",Q),o(y,"class","svelte-1383h37"),o(v,"class","action scissors svelte-1383h37"),v.disabled=b=!!((w=s[5])==null?void 0:w.myResult),O(v,"selected",s[0]==="scissors")},m(f,I){T(f,e,I),m(e,r),T(f,u,I),T(f,i,I),m(i,c),T(f,B,I),T(f,v,I),m(v,y),X||(S=[Ie(e,"click",s[16]),Ie(i,"click",s[17]),Ie(v,"click",s[18])],X=!0)},p(f,I){var w,D,C;I&32&&t!==(t=!!((w=f[5])==null?void 0:w.myResult))&&(e.disabled=t),I&1&&O(e,"selected",f[0]==="rock"),I&32&&k!==(k=!!((D=f[5])==null?void 0:D.myResult))&&(i.disabled=k),I&1&&O(i,"selected",f[0]==="paper"),I&32&&b!==(b=!!((C=f[5])==null?void 0:C.myResult))&&(v.disabled=b),I&1&&O(v,"selected",f[0]==="scissors")},d(f){f&&d(e),f&&d(u),f&&d(i),f&&d(B),f&&d(v),X=!1,He(S)}}}function cs(s){var q,Z,J,R,H,ee,se,te,ne,Ee,Me,Ve;let e,r,l,t,u=((q=s[1])==null?void 0:q.round)+"",i,c,n,k,B,v,y,Q,b,X,S,f,I,w,D,C,x,G,$;n=new as({});let E=s[2]&&Je(s);function pe(A,p){var P,_;if(A[3])return As;if(((P=A[5])==null?void 0:P.myResult)==="loss")return ns;if(((_=A[5])==null?void 0:_.myResult)==="win")return is}let a=pe(s),g=a&&a(s);b=new Qe({props:{cardType:"full",name:(Z=s[4])==null?void 0:Z.name,avatar:(J=s[4])==null?void 0:J.avatar,result:(R=s[5])==null?void 0:R.myResult,pick:(H=s[5])==null?void 0:H.myPick,flipx:!0,lastPlays:s[6]&&((ee=s[4])==null?void 0:ee.id)&&s[6][s[4].id]||[]}}),C=new Qe({props:{cardType:"full",name:((se=s[7])==null?void 0:se.name)||"MR. nobody",avatar:((te=s[7])==null?void 0:te.avatar)||"bot",result:(ne=s[5])==null?void 0:ne.rivalResult,pick:(Ee=s[5])==null?void 0:Ee.rivalPick,lastPlays:s[6]&&((Me=s[7])==null?void 0:Me.id)&&s[6][s[7].id]||[]}});let h=!s[3]&&((Ve=s[1])==null?void 0:Ve.stage)==="game"&&Te(s);return{c(){e=M("div"),r=M("div"),l=M("div"),t=K("Round "),i=K(u),c=N(),ue(n.$$.fragment),k=N(),E&&E.c(),B=N(),v=M("div"),g&&g.c(),y=N(),Q=M("div"),ue(b.$$.fragment),X=N(),S=M("div"),f=M("img"),w=N(),D=M("div"),ue(C.$$.fragment),x=N(),G=M("div"),h&&h.c(),this.h()},l(A){e=V(A,"DIV",{class:!0,style:!0});var p=Y(e);r=V(p,"DIV",{class:!0});var P=Y(r);l=V(P,"DIV",{class:!0});var _=Y(l);t=j(_,"Round "),i=j(_,u),_.forEach(d),c=U(P),fe(n.$$.fragment,P),P.forEach(d),k=U(p),E&&E.l(p),B=U(p),v=V(p,"DIV",{class:!0});var W=Y(v);g&&g.l(W),y=U(W),Q=V(W,"DIV",{class:!0});var le=Y(Q);fe(b.$$.fragment,le),le.forEach(d),X=U(W),S=V(W,"DIV",{class:!0});var re=Y(S);f=V(re,"IMG",{src:!0,alt:!0,class:!0}),re.forEach(d),w=U(W),D=V(W,"DIV",{class:!0});var ae=Y(D);fe(C.$$.fragment,ae),ae.forEach(d),W.forEach(d),x=U(p),G=V(p,"DIV",{class:!0});var ie=Y(G);h&&h.l(ie),ie.forEach(d),p.forEach(d),this.h()},h(){o(l,"class","turn-display svelte-1383h37"),o(r,"class","turn-info svelte-1383h37"),o(Q,"class","player-left svelte-1383h37"),de(f.src,I=ss)||o(f,"src",I),o(f,"alt","VS"),o(f,"class","svelte-1383h37"),o(S,"class","vs svelte-1383h37"),o(D,"class","player-right svelte-1383h37"),o(v,"class","play-area svelte-1383h37"),o(G,"class","actions svelte-1383h37"),o(e,"class","game svelte-1383h37"),Ae(e,"--background-image","url("+xe+")"),Ae(e,"--background-hands","url("+$e+")"),Ae(e,"--background-select","url("+es+")")},m(A,p){T(A,e,p),m(e,r),m(r,l),m(l,t),m(l,i),m(r,c),ge(n,r,null),m(e,k),E&&E.m(e,null),m(e,B),m(e,v),g&&g.m(v,null),m(v,y),m(v,Q),ge(b,Q,null),m(v,X),m(v,S),m(S,f),m(v,w),m(v,D),ge(C,D,null),m(e,x),m(e,G),h&&h.m(G,null),$=!0},p(A,[p]){var W,le,re,ae,ie,Be,Ye,ke,we,De,Re,ye;(!$||p&2)&&u!==(u=((W=A[1])==null?void 0:W.round)+"")&&me(i,u),A[2]?E?(E.p(A,p),p&4&&z(E,1)):(E=Je(A),E.c(),z(E,1),E.m(e,B)):E&&(Se(),F(E,1,1,()=>{E=null}),_e()),a===(a=pe(A))&&g?g.p(A,p):(g&&g.d(1),g=a&&a(A),g&&(g.c(),g.m(v,y)));const P={};p&16&&(P.name=(le=A[4])==null?void 0:le.name),p&16&&(P.avatar=(re=A[4])==null?void 0:re.avatar),p&32&&(P.result=(ae=A[5])==null?void 0:ae.myResult),p&32&&(P.pick=(ie=A[5])==null?void 0:ie.myPick),p&80&&(P.lastPlays=A[6]&&((Be=A[4])==null?void 0:Be.id)&&A[6][A[4].id]||[]),b.$set(P);const _={};p&128&&(_.name=((Ye=A[7])==null?void 0:Ye.name)||"MR. nobody"),p&128&&(_.avatar=((ke=A[7])==null?void 0:ke.avatar)||"bot"),p&32&&(_.result=(we=A[5])==null?void 0:we.rivalResult),p&32&&(_.pick=(De=A[5])==null?void 0:De.rivalPick),p&192&&(_.lastPlays=A[6]&&((Re=A[7])==null?void 0:Re.id)&&A[6][A[7].id]||[]),C.$set(_),!A[3]&&((ye=A[1])==null?void 0:ye.stage)==="game"?h?h.p(A,p):(h=Te(A),h.c(),h.m(G,null)):h&&(h.d(1),h=null)},i(A){$||(z(n.$$.fragment,A),z(E),z(b.$$.fragment,A),z(C.$$.fragment,A),$=!0)},o(A){F(n.$$.fragment,A),F(E),F(b.$$.fragment,A),F(C.$$.fragment,A),$=!1},d(A){A&&d(e),ve(n),E&&E.d(),g&&g.d(),ve(b),ve(C),h&&h.d()}}}function oe(s,e){return!!e&&s.players.includes(e)}function be(s){return(s==null?void 0:s.stage)=="game"&&(s==null?void 0:s.fights)?s==null?void 0:s.fights:(s==null?void 0:s.results)&&s.results[0]?s.results[0]:[]}function Xe(s,e){var r,l;return!e||((r=s==null?void 0:s.livePlayers)==null?void 0:r.includes(e))?!1:(s==null?void 0:s.stage)==="game"?(l=s==null?void 0:s.deadPlayers)==null?void 0:l.includes(e):(s==null?void 0:s.stage)==="gameEnd"?!be(s).find(i=>oe(i,e)):!1}function ze(s,e){const l=be(s).find(u=>oe(u,e));return l==null?void 0:l.players.find(u=>u!==e)}function us(s,e){const r=s==null?void 0:s.sessions;return!r||!s?[]:be(s).filter(t=>!oe(t,e)).map(t=>t.players.map(u=>r[u]))}function he(s){const e=s.room,r=s.session;let l=r==null?void 0:r.id;return(e==null?void 0:e.livePlayers)&&Xe(e,r==null?void 0:r.id)&&(l=e.livePlayers[0]),l}function fs(s,e,r){let l,t,u,i,c,n,k;const B={1:"rock",2:"paper",3:"scissors"},v={rock:1,paper:2,scissors:3};function y(a){var q;let g={};const h=(a==null?void 0:a.stage)==="gameEnd"?1:0;return(q=a==null?void 0:a.results)==null||q.slice(h).forEach(Z=>{Z.forEach(J=>{J.players.forEach(R=>{const H=J.responses[R];H&&(g[R]||(g[R]=[]),g[R].push(B[H]),g[R]=g[R].slice(0,2))})})}),g}function Q(a,g){if((a==null?void 0:a.stage)!=="gameEnd")return{};const h=ze(a,g);if(!g||!h)return{};let q=a.results[0],Z;if(q&&(Z=q.find(ne=>oe(ne,g))),!Z)return{};let J,R,H,ee;console.log(Z),Z.winner==="both"?(J="win",R="win"):Z.winner==="nobody"?(J="loss",R="loss"):Z.winner===g?(J="win",R="loss"):(J="loss",R="win");const se=Z.responses[g];se&&(H=B[se]);const te=Z.responses[h];return te&&(ee=B[te]),{myResult:J,myPick:H,rivalResult:R,rivalPick:ee}}const b=Ge.get(),X=b.select(a=>a.room);L(s,X,a=>r(1,l=a));const S=b.select(a=>us(a.room,he(a)));L(s,S,a=>r(2,t=a));const f=b.select(a=>Q(a.room,he(a)));L(s,f,a=>r(5,c=a));const I=b.select(({room:a,session:g})=>Xe(a,g==null?void 0:g.id));L(s,I,a=>r(3,u=a));const w=b.select(({room:a})=>y(a));L(s,w,a=>r(6,n=a));const D=b.select(a=>{var h;const g=he(a);return g?(h=a==null?void 0:a.room)==null?void 0:h.sessions[g]:null});L(s,D,a=>r(4,i=a));const C=b.select(a=>{var q;const g=he(a),h=ze(a==null?void 0:a.room,g);return h?(q=a==null?void 0:a.room)==null?void 0:q.sessions[h]:null});L(s,C,a=>r(7,k=a));let x=null;function G(a){r(0,x=a),b.emit(new Oe(v[a]))}return[x,l,t,u,i,c,n,k,X,S,f,I,w,D,C,G,()=>G("rock"),()=>G("paper"),()=>G("scissors")]}class ps extends Ce{constructor(e){super();Pe(this,e,fs,cs,Ze,{})}}export{ps as default};
