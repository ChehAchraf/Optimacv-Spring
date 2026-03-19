import { CommonModule } from '@angular/common';
import {Component, computed, effect, inject, OnInit, signal} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import { LucideAngularModule, FileText } from 'lucide-angular';
import {FullAnalysis, MockAnalysis} from '../../../../core/model/analysis.model';
import {ScoreCardComponent} from './components/score-card/score-card-component';
import {KeywordsCardComponent} from './components/keywords-card/keywords-card-component';
import {ActionPlanCardComponent} from './components/action-plan-card/action-plan-card-component';
import {AnalysisStore} from '../../../../core/store/analysis.store';



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

