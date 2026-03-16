import { Component } from '@angular/core';
import {LucideAngularModule} from 'lucide-angular';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-cta-section-component',
  imports: [
    LucideAngularModule,
    RouterLink
  ],
  templateUrl: './cta-section-component.html',
  styleUrl: './cta-section-component.css',
})
export class CtaSectionComponent {}
