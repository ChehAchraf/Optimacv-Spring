import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule, Sparkles } from 'lucide-angular';

@Component({
  selector: 'app-score-card',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './score-card.component.html',
})
export class ScoreCardComponent {
  score = input.required<number>();
  verdict = input.required<string>();

  readonly SparklesIcon = Sparkles;
}

