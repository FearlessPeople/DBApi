import{t as e}from"./index.959f545d.js";import{q as o}from"./index.4286c144.js";function a(s){return e.post("/api/role/list",o.stringify(s),{headers:{"Content-Type":"application/x-www-form-urlencoded"}})}function p(){return e.post("/api/role/all")}function l(s){return e.post("/api/role/add",s)}function u(s){return e.post("/api/role/edit",s)}function c(s){return e.post("/api/role/get-permissions-list",s)}function m(s,t){const i={...s,permissionIds:t};return e.post("/api/role/set-permissions",i)}function f(){return e.get("/api/role/all-permissions")}export{p as a,f as b,l as c,u as e,c as g,a as p,m as s};
