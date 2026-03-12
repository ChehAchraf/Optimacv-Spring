import { Component } from '@angular/core';
import {NavbarComponent} from '../../shared/components/navbar-component/navbar-component';
import {MainNavbarComponent} from '../../shared/components/main-navbar-component/main-navbar-component';
import {RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-main-layout',
  imports: [
    MainNavbarComponent,
    RouterOutlet
  ],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
})
export class MainLayout {}
