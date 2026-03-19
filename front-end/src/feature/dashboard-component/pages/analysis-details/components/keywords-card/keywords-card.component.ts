import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule, Search } from 'lucide-angular';

@Component({
  selector: 'app-keywords-card',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './keywords-card.component.html',
})
export class KeywordsCardComponent {
  matchingSkills = input.required<string[]>();
  missingKeywords = input.required<string[]>();

  readonly SearchIcon = Search;
}

