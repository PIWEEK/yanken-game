import{S as y,i as z,s as A,e as E,t as d,c as v,a as b,g as P,d as o,f as u,E as R,h as N,k as S,l as q,n as h,_ as C}from"./chunks/vendor-a7bd5f25.js";function H(r){let f,t=r[1].frame+"",a;return{c(){f=E("pre"),a=d(t)},l(l){f=v(l,"PRE",{});var s=b(f);a=P(s,t),s.forEach(o)},m(l,s){u(l,f,s),R(f,a)},p(l,s){s&2&&t!==(t=l[1].frame+"")&&N(a,t)},d(l){l&&o(f)}}}function w(r){let f,t=r[1].stack+"",a;return{c(){f=E("pre"),a=d(t)},l(l){f=v(l,"PRE",{});var s=b(f);a=P(s,t),s.forEach(o)},m(l,s){u(l,f,s),R(f,a)},p(l,s){s&2&&t!==(t=l[1].stack+"")&&N(a,t)},d(l){l&&o(f)}}}function B(r){let f,t,a,l,s=r[1].message+"",c,k,m,p,i=r[1].frame&&H(r),_=r[1].stack&&w(r);return{c(){f=E("h1"),t=d(r[0]),a=S(),l=E("pre"),c=d(s),k=S(),i&&i.c(),m=S(),_&&_.c(),p=q()},l(e){f=v(e,"H1",{});var n=b(f);t=P(n,r[0]),n.forEach(o),a=h(e),l=v(e,"PRE",{});var j=b(l);c=P(j,s),j.forEach(o),k=h(e),i&&i.l(e),m=h(e),_&&_.l(e),p=q()},m(e,n){u(e,f,n),R(f,t),u(e,a,n),u(e,l,n),R(l,c),u(e,k,n),i&&i.m(e,n),u(e,m,n),_&&_.m(e,n),u(e,p,n)},p(e,[n]){n&1&&N(t,e[0]),n&2&&s!==(s=e[1].message+"")&&N(c,s),e[1].frame?i?i.p(e,n):(i=H(e),i.c(),i.m(m.parentNode,m)):i&&(i.d(1),i=null),e[1].stack?_?_.p(e,n):(_=w(e),_.c(),_.m(p.parentNode,p)):_&&(_.d(1),_=null)},i:C,o:C,d(e){e&&o(f),e&&o(a),e&&o(l),e&&o(k),i&&i.d(e),e&&o(m),_&&_.d(e),e&&o(p)}}}function G({error:r,status:f}){return{props:{error:r,status:f}}}function D(r,f,t){let{status:a}=f,{error:l}=f;return r.$$set=s=>{"status"in s&&t(0,a=s.status),"error"in s&&t(1,l=s.error)},[a,l]}class I extends y{constructor(f){super();z(this,f,D,B,A,{status:0,error:1})}}export{I as default,G as load};
