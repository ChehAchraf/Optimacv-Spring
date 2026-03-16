import { Component } from '@angular/core';
import {
  LucideAngularModule,
  UploadCloud,
  Sparkles,
  Trophy,
} from 'lucide-angular';
import { LottieComponent, AnimationOptions } from 'ngx-lottie';

@Component({
  selector: 'app-how-it-works-section',
  standalone: true,
  imports: [LucideAngularModule, LottieComponent],
  templateUrl: './how-it-works-section.html',
  styleUrl: './how-it-works-section.css',
})
export class HowItWorksSection {
  readonly UploadCloudIcon = UploadCloud;
  readonly SparklesIcon = Sparkles;
  readonly TrophyIcon = Trophy;

  /**
   * Lottie animation options.
   * Configure path, loop, autoplay, etc. here.
   * Animation file: public/lotties/ai-analysis.json
   */
  readonly lottieOptions: AnimationOptions = {
    path: '/lottie/ai-analysis.json',
    loop: true,
    autoplay: true,
  };
}
