import { CommonModule } from '@angular/common';
import {Component, computed, effect, inject, OnInit, signal} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import { LucideAngularModule, FileText } from 'lucide-angular';
import {FullAnalysis, MockAnalysis} from '../../../../core/model/analysis.model';
import {ScoreCardComponent} from './components/score-card/score-card-component';
import {KeywordsCardComponent} from './components/keywords-card/keywords-card-component';
import {ActionPlanCardComponent} from './components/action-plan-card/action-plan-card-component';
import {AnalysisStore} from '../../../../core/store/analysis.store';


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
  templateUrl: './analysis-details-component.html',
})
export class AnalysisDetailsComponent implements OnInit {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  readonly store = inject(AnalysisStore);

  analysisId = this.route.snapshot.paramMap.get('id');

  readonly rawAnalysis = computed(() => {
    return this.store.analyses().find((a: any) => a.id === this.analysisId);
  });

  readonly parsedDetails = computed(() => {
    const analysis = this.rawAnalysis();
    return analysis ? analysis.fullAnalysis : null;
  });

  readonly score = computed(() => this.parsedDetails()?.score || 0);
  readonly verdict = computed(() => this.parsedDetails()?.verdict || '');
  readonly matchingSkills = computed(() => this.parsedDetails()?.matchingSkills || []);
  readonly missingKeywords = computed(() => this.parsedDetails()?.missingKeywords || []);
  readonly actionPlan = computed(() => this.parsedDetails()?.actionPlan || []);

  ngOnInit() {
    if (this.analysisId) {
      this.store.getAnalysisById({ analysisId: this.analysisId });
    } else {
      this.router.navigate(['/dashboard/history']);
    }
  }
  constructor() {
    effect(() => {
      console.log(this.store.analyses())
    });
  }





  readonly FileTextIcon = FileText;

  scoreBadgeClass(score: number): string {
    if (score >= 80) return 'badge-success';
    if (score >= 60) return 'badge-warning';
    return 'badge-error';
  }
}

