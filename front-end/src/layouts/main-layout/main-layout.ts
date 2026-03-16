import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from '../../feature/footer-component/footer-component';
import { MainNavbarComponent } from '../../shared/components/main-navbar-component/main-navbar-component';

@Component({
  selector: 'app-main-layout',
  imports: [
    MainNavbarComponent,
    RouterOutlet,
    FooterComponent,
  ],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
})
export class MainLayout {}
