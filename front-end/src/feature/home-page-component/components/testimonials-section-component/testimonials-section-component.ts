import { Component } from '@angular/core';
import { LucideAngularModule, Star } from 'lucide-angular';

@Component({
  selector: 'app-testimonials-section-component',
  standalone: true,
  imports: [LucideAngularModule],
  templateUrl: './testimonials-section-component.html',
  styleUrl: './testimonials-section-component.css',
})
export class TestimonialsSectionComponent {
  readonly StarIcon = Star;
}
