import{S as t,i as o,s as i,j as d,m as b,o as c,_ as u,x as f,u as m,v as p}from"../../chunks/vendor-a7bd5f25.js";import{s as l}from"../../chunks/store-344738a1.js";import h from"../game/game-result.svelte-8a2d8f68.js";import"../../chunks/navigation-51f4a605.js";import"../../chunks/singletons-12a22614.js";import"../../chunks/paths-28a87002.js";import"../../chunks/websockets-72c92e35.js";import"../../chunks/MenuContainer-eb9e4ac9.js";import"../../chunks/PlayerCard-9b25eda5.js";function y(n){let e,s;return e=new h({}),{c(){d(e.$$.fragment)},l(a){b(e.$$.fragment,a)},m(a,r){c(e,a,r),s=!0},p:u,i(a){s||(f(e.$$.fragment,a),s=!0)},o(a){m(e.$$.fragment,a),s=!1},d(a){p(e,a)}}}function v(n){return l.start({session:{id:"180ee790-5e74-11ec-8be4-aa07de7f731b",avatar:"purple-scissors",isBot:!1,name:"eee"},room:{livePlayers:["00000000-0000-0064-0000-000000000001"],round:5,deadPlayers:["00000000-0000-0064-0000-000000000004","00000000-0000-0064-0000-000000000005","00000000-0000-0064-0000-000000000002","00000000-0000-0064-0000-000000000003","180ee790-5e74-11ec-8be4-aa07de7f731b"],status:"ended",id:"xxxx",players:["00000000-0000-0064-0000-000000000004","00000000-0000-0064-0000-000000000005","00000000-0000-0064-0000-000000000001","00000000-0000-0064-0000-000000000002","00000000-0000-0064-0000-000000000003","180ee790-5e74-11ec-8be4-aa07de7f731b"],sessions:{"00000000-0000-0064-0000-000000000004":{id:"00000000-0000-0064-0000-000000000004",isBot:!0,name:"bot-4",avatar:"bot"},"00000000-0000-0064-0000-000000000005":{id:"00000000-0000-0064-0000-000000000005",isBot:!0,name:"bot-5",avatar:"bot"},"00000000-0000-0064-0000-000000000001":{id:"00000000-0000-0064-0000-000000000001",isBot:!0,name:"bot-1",avatar:"bot"},"00000000-0000-0064-0000-000000000002":{id:"00000000-0000-0064-0000-000000000002",isBot:!0,name:"bot-2",avatar:"bot"},"00000000-0000-0064-0000-000000000003":{id:"00000000-0000-0064-0000-000000000003",isBot:!0,name:"bot-3",avatar:"bot"},"180ee790-5e74-11ec-8be4-aa07de7f731b":{id:"180ee790-5e74-11ec-8be4-aa07de7f731b",avatar:"purple-scissors",isBot:!1,name:"eee"}},options:{pairingScreenTimeout:3e3,gameScreenTimeout:5e3,gameEndScreenTimeout:3e3,resultScreenTimeout:3e3,roundTimeout:1e4,postRoundTimeout:3e3},owner:"180ee790-5e74-11ec-8be4-aa07de7f731b",results:[[{id:"3b88a170-5e74-11ec-8be4-aa07de7f731b",players:["00000000-0000-0064-0000-000000000004","00000000-0000-0064-0000-000000000001"],winner:"00000000-0000-0064-0000-000000000001",responses:{"00000000-0000-0064-0000-000000000004":3,"00000000-0000-0064-0000-000000000001":1},round:5}],[{id:"332f2df0-5e74-11ec-8be4-aa07de7f731b",players:["00000000-0000-0064-0000-000000000004","00000000-0000-0064-0000-000000000001"],winner:"both",responses:{"00000000-0000-0064-0000-000000000004":1,"00000000-0000-0064-0000-000000000001":1},round:4}],[{id:"2ad60890-5e74-11ec-8be4-aa07de7f731b",players:["00000000-0000-0064-0000-000000000001","00000000-0000-0064-0000-000000000002"],winner:"00000000-0000-0064-0000-000000000001",responses:{"00000000-0000-0064-0000-000000000001":3,"00000000-0000-0064-0000-000000000002":2},round:3},{id:"2ad60891-5e74-11ec-8be4-aa07de7f731b",players:["00000000-0000-0064-0000-000000000004","180ee790-5e74-11ec-8be4-aa07de7f731b"],winner:"00000000-0000-0064-0000-000000000004",responses:{"180ee790-5e74-11ec-8be4-aa07de7f731b":2,"00000000-0000-0064-0000-000000000004":3},round:3}],[{id:"227cbc20-5e74-11ec-8be4-aa07de7f731b",players:["00000000-0000-0064-0000-000000000004","00000000-0000-0064-0000-000000000002"],winner:"both",responses:{"00000000-0000-0064-0000-000000000004":1,"00000000-0000-0064-0000-000000000002":1},round:2},{id:"227cbc21-5e74-11ec-8be4-aa07de7f731b",players:["00000000-0000-0064-0000-000000000001","180ee790-5e74-11ec-8be4-aa07de7f731b"],winner:"both",responses:{"180ee790-5e74-11ec-8be4-aa07de7f731b":1,"00000000-0000-0064-0000-000000000001":1},round:2}],[{id:"1a236fb0-5e74-11ec-8be4-aa07de7f731b",players:["00000000-0000-0064-0000-000000000004","00000000-0000-0064-0000-000000000003"],winner:"00000000-0000-0064-0000-000000000004",responses:{"00000000-0000-0064-0000-000000000004":1,"00000000-0000-0064-0000-000000000003":3},round:1},{id:"1a236fb1-5e74-11ec-8be4-aa07de7f731b",players:["00000000-0000-0064-0000-000000000002","180ee790-5e74-11ec-8be4-aa07de7f731b"],winner:"both",responses:{"180ee790-5e74-11ec-8be4-aa07de7f731b":2,"00000000-0000-0064-0000-000000000002":2},round:1},{id:"1a236fb2-5e74-11ec-8be4-aa07de7f731b",players:["00000000-0000-0064-0000-000000000005","00000000-0000-0064-0000-000000000001"],winner:"00000000-0000-0064-0000-000000000001",responses:{"00000000-0000-0064-0000-000000000005":2,"00000000-0000-0064-0000-000000000001":3},round:1}]]}}),[]}class T extends t{constructor(e){super();o(this,e,v,y,i,{})}}export{T as default};
