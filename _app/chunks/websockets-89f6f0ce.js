import{S as h}from"./store-9ef01361.js";import{K as x,X as l,U as v,Y as i,Q as p,Z as O}from"./vendor-06d4fd3a.js";class c extends h{constructor(e){super();this.requestId=e}toJSON(){return{}}}class f extends c{constructor(e,s,n,a){super(n);this.name=e,this.avatar=s,this.sessionId=a}toJSON(){return{type:"request",name:"hello",requestId:this.requestId,sessionId:this.sessionId,playerName:this.name,playerAvatar:this.avatar}}}class g extends c{constructor(e,s){super(s);this.roomId=e}toJSON(){return{type:"request",name:"joinRoom",requestId:this.requestId,roomId:this.roomId}}}class y extends c{constructor(e,s){super(s);this.roomId=e}toJSON(){return{type:"notification",name:"joinBots",requestId:this.requestId,roomId:this.roomId,botNum:5,botJoinTimeout:100}}}class b extends c{toJSON(){return{type:"request",name:"startGame",options:{roundTimeout:1e4,postRoundTimeout:3e3}}}}class k extends c{constructor(e,s){super(s);this.turn=e}toJSON(){return{type:"request",name:"sendTurn",result:this.turn}}}class S extends h{constructor(e){super();this.event=e}update(e){console.log(`Socket event received: ${this.constructor.name}`)}}class R extends S{}class T extends S{}class j extends S{}class w extends S{constructor(e){super(e);const s=JSON.parse(e.data);this.requestId=s.requestId,this.type=s.type}}class E extends h{constructor(e,s){super();this.session=e,this.room=s}update(e){this.session&&(e.session=this.session,console.log("Updating session:",this.session)),this.room&&(e.room=this.room,console.log("Updating room:",this.room.status,this.room.stage,this.room))}}class m extends h{constructor(){super();this.requestId=Date.now().toString()}watch(e,s){const n=this.requestId;return s.pipe(l(r=>r instanceof w&&r.requestId===n),O(1),v(r=>{const u=JSON.parse(r.event.data),q=u.session,I=u.room;return new E(q,I)}))}}class _ extends m{constructor(e,s,n){super();this.name=e,this.avatar=s,this.sessionId=n}watch(e,s){return i(super.watch(e,s),p(new f(this.name,this.avatar,this.requestId,this.sessionId)))}}class A extends m{constructor(e){super();this.roomId=e}watch(e,s){return i(super.watch(e,s),p(new g(this.roomId,this.requestId)))}}class B extends m{watch(e,s){return i(super.watch(e,s),p(new b(this.requestId)))}}class D extends m{constructor(e){super();this.turn=e}watch(e,s){return i(super.watch(e,s),p(new k(this.turn,this.requestId)))}}class G extends m{constructor(e){super();this.roomId=e}watch(e,s){return i(super.watch(e,s),p(new y(this.roomId,this.requestId)))}}class H extends h{watch(e,s){let n;typeof window!="undefined"?n=window.YANKEN_SERVER:n={}.VITE_BASE_URL;const a=new WebSocket(n),r=new x,u=new x;a.addEventListener("open",t=>{r.next(new R(t)),e.session&&u.next(new f(e.session.name,e.session.avatar,Date.now().toString(),e.session.id))}),a.addEventListener("message",t=>{console.log("[WS]<<",JSON.parse(t.data)),r.next(new w(t))}),a.addEventListener("close",t=>{r.next(new T(t))}),a.addEventListener("Error",t=>{r.next(new j(t))}),s.pipe(l(t=>t instanceof c)).subscribe(t=>{const d=t.toJSON();console.log("[WS]>>",d),a.send(JSON.stringify(d))});const I=s.pipe(l(t=>t instanceof w&&t.type==="notification"),v(t=>{const d=JSON.parse(t.event.data),J=d.session,N=d.room;return new E(J,N)}));return s.pipe(l(t=>t instanceof w&&t.type==="error")).subscribe(t=>{console.error("TODO: Error,  do something with this",JSON.parse(t.event.data).error)}),i(I,r,u)}}export{_ as H,A as J,H as S,G as a,B as b,D as c};