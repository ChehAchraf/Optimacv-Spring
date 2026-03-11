import { Component } from '@angular/core';
import {NavbarComponent} from "../../shared/components/navbar-component/navbar-component";

@Component({
  selector: 'app-auth-layout',
    imports: [
        NavbarComponent
    ],
  templateUrl: './auth-layout.html',
  styleUrl: './auth-layout.css',
})
export class AuthLayout {}
