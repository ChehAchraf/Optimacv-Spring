import { Routes } from '@angular/router';
import {authGuard} from '../core/guard/auth-guard';

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
          .then((c)=>c.LoginPageComponent),
        //canActivate : [authGuard]
      },
      {
        path:'register',
        loadComponent : () => import('../feature/register-page-component/register-page-component')
          .then((c)=>c.RegisterPageComponent)
      }
    ]
  },
  {
    path: 'dashboard',
    loadComponent: () => import('../feature/dashboard-component/dashboard-component').then(c => c.DashboardComponent),
    children: [
      { path: '', loadComponent: () => import('../feature/dashboard-component/components/overview-component/overview-component')
          .then(c => c.OverviewComponent)
      },

    ]
  }
];
