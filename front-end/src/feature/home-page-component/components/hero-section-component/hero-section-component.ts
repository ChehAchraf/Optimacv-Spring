import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import {
  LucideAngularModule,
  Sparkles,
  ArrowRight,
  PlayCircle,
} from 'lucide-angular';

@Component({
  selector: 'app-hero-section-component',
  standalone: true,
  imports: [LucideAngularModule, RouterLink],
  templateUrl: './hero-section-component.html',
  styleUrl: './hero-section-component.css',
})
export class HeroSectionComponent {
  readonly SparklesIcon = Sparkles;
  readonly ArrowRightIcon = ArrowRight;
  readonly PlayCircleIcon = PlayCircle;
}
