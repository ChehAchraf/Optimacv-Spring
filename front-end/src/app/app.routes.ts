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

      {
        path : '',
        loadComponent : ()=> import('../feature/dashboard-component/pages/overview-page/overview-page')
          .then(c => c.OverviewPage)
      },

      { path: 'jobs', loadComponent: () => import('../feature/dashboard-component/pages/job-targets-page/job-targets-page')
        .then(c => c.JobTargetsPage)
      },

      {
        path: 'bulk-rank',
        loadComponent : ()=> import('../feature/dashboard-component/pages/bulk-rank-page/bulk-rank-page')
          .then((c) => c.BulkRankPage)
      },

      {
        path: 'resumes',
        loadComponent : ()=> import('../feature/dashboard-component/pages/analyze-resume-page/analyze-resume-page')
          .then((c) => c.AnalyzeResumePage)
      }

    ]
  }
];
