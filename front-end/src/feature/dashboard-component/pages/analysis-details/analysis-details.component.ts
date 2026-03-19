import { CommonModule } from '@angular/common';
import { Component, computed, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { LucideAngularModule, FileText } from 'lucide-angular';
import { ActionPlanCardComponent } from './components/action-plan-card/action-plan-card.component';
import { KeywordsCardComponent } from './components/keywords-card/keywords-card.component';
import { ScoreCardComponent } from './components/score-card/score-card.component';
import { FullAnalysis, MockAnalysis } from './analysis-details.models';

const mockAnalysis: MockAnalysis = {
  id: '123',
  resumeFileName: 'mon_cv.pdf',
  jobTitle: 'Senior React/Java Developer',
  analyzedAt: '2026-03-18T13:40:34.876Z',
  fullAnalysis: {
    score: 78,
    verdict:
      'The candidate presents a strong profile with demonstrated experience in React, Python, Java, PostgreSQL, Git, and Docker. The primary gap is the explicit mention of Typescript, which significantly impacts the overall match.',
    matchingSkills: ['React', 'Java', 'Spring Boot', 'Python', 'PostgreSQL', 'Git', 'Docker'],
    missingKeywords: ['Typescript', 'Golang', 'Remote Collaboration'],
    actionPlan: [
      {
        title: 'Add Typescript Proficiency',
        description:
          "Explicitly add 'Typescript' to your 'Frontend & UI' skills section.",
      },
      {
        title: 'Address Soft Skills',
        description:
          "Integrate a 'Soft Skills' section or weave into project descriptions how you demonstrate autonomy and teamwork.",
      },
      {
        title: 'Clarify Internship Dates',
        description: "The 'Mai 2025- Juillet 2025' internship dates are in the future. Clarify this.",
      },
    ],
  } satisfies FullAnalysis,
};

@Component({
  selector: 'app-analysis-details-component',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    LucideAngularModule,
    ScoreCardComponent,
    KeywordsCardComponent,
    ActionPlanCardComponent,
  ],
  templateUrl: './analysis-details.component.html',
})
export class AnalysisDetailsComponent {
  readonly analysis = signal<MockAnalysis>(mockAnalysis);

  readonly score = computed(() => this.analysis().fullAnalysis.score);
  readonly verdict = computed(() => this.analysis().fullAnalysis.verdict);
  readonly matchingSkills = computed(() => this.analysis().fullAnalysis.matchingSkills);
  readonly missingKeywords = computed(() => this.analysis().fullAnalysis.missingKeywords);
  readonly actionPlan = computed(() => this.analysis().fullAnalysis.actionPlan);

  readonly FileTextIcon = FileText;
}

