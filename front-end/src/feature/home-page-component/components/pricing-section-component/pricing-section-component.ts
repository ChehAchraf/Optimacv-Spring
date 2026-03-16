import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LucideAngularModule, Check } from 'lucide-angular';

@Component({
  selector: 'app-pricing-section-component',
  standalone: true,
  imports: [LucideAngularModule, RouterLink],
  templateUrl: './pricing-section-component.html',
  styleUrl: './pricing-section-component.css',
})
export class PricingSectionComponent {
  readonly CheckIcon = Check;
  billingPeriod: 'monthly' | 'yearly' = 'monthly';
}
