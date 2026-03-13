import { Routes } from '@angular/router';

export const routes: Routes = [

  {
    path : "",
    loadComponent : ()=> import('../layouts/main-layout/main-layout')
      .then((c)=>c.MainLayout),

    children : [
      {
        path : '',
        loadComponent :()=> import('../feature/home-page-component/home-page-component')
          .then((c)=>c.HomePageComponent)
      },
      {
        path:'login',
        loadComponent : () => import('../feature/login-page-component/login-page-component')
          .then((c)=>c.LoginPageComponent)
      },
      {
        path:'register',
        loadComponent : () => import('../feature/register-page-component/register-page-component')
          .then((c)=>c.RegisterPageComponent)
      }
    ]
  },
];
