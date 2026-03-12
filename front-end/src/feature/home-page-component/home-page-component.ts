import { Component } from '@angular/core';
import {HeroSectionComponent} from './components/hero-section-component/hero-section-component';

@Component({
  selector: 'app-home-page-component',
  imports: [
    HeroSectionComponent
  ],
  templateUrl: './home-page-component.html',
  styleUrl: './home-page-component.css',
})
export class HomePageComponent {}
