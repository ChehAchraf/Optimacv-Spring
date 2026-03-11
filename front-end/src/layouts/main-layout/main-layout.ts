import { Component } from '@angular/core';
import {NavbarComponent} from '../../shared/components/navbar-component/navbar-component';

@Component({
  selector: 'app-main-layout',
  imports: [
    NavbarComponent
  ],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
})
export class MainLayout {}
