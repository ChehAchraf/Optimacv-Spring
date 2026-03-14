import { Component } from '@angular/core';
import { HeroSectionComponent } from './components/hero-section-component/hero-section-component';
import { ServiceSectionComponent } from './components/service-section-component/service-section-component';
import {HowItWorksSection} from './components/how-it-works-section/how-it-works-section';
import {PricingSectionComponent} from './components/pricing-section-component/pricing-section-component';
import {TestimonialsSectionComponent} from './components/testimonials-section-component/testimonials-section-component';
import {CtaSectionComponent} from './components/cta-section-component/cta-section-component';

@Component({
  selector: 'app-home-page-component',
  imports: [
    HeroSectionComponent,
    ServiceSectionComponent,
    HowItWorksSection,
    PricingSectionComponent,
    TestimonialsSectionComponent,
    CtaSectionComponent
  ],
  templateUrl: './home-page-component.html',
  styleUrl: './home-page-component.css',
})
export class HomePageComponent {}
