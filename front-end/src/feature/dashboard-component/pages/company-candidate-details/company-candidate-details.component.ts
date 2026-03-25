import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import {CompanyService} from '../../../../core/service/company/company.service';
import { LucideAngularModule, ArrowLeft, BarChart2, Briefcase, Users, FileText } from 'lucide-angular';

@Component({
  selector: 'app-company-candidate-details',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './company-candidate-details.component.html',
  styleUrl: './company-candidate-details.component.css'
})
export class CompanyCandidateDetailsComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly companyService = inject(CompanyService);
  private readonly location = inject(Location);

  readonly ArrowLeftIcon = ArrowLeft;
  readonly BarChartIcon = BarChart2;
  readonly BriefcaseIcon = Briefcase;
  readonly UsersIcon = Users;
  readonly FileTextIcon = FileText;

  candidate = signal<any>(null);
  loading = signal<boolean>(true);
  error = signal<string | null>(null);

  parsedInsights = computed(() => {
    const currentCandidate = this.candidate();
    if (!currentCandidate || !currentCandidate.aiFeedback) return null;
    
    const feedbackStr = currentCandidate.aiFeedback;
    try {
      let parsed = JSON.parse(feedbackStr);
      if (typeof parsed === 'string') {
        let cleanedStr = parsed;
        if (cleanedStr.startsWith('```json')) cleanedStr = cleanedStr.replace(/```json/g, '');
        if (cleanedStr.endsWith('```')) cleanedStr = cleanedStr.replace(/```/g, '');
        parsed = JSON.parse(cleanedStr);
      }
      return parsed;
    } catch (e) {
      return { raw: feedbackStr };
    }
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.fetchCandidateDetails(id);
    } else {
      this.error.set('No candidate ID provided.');
      this.loading.set(false);
    }
  }

  fetchCandidateDetails(id: string): void {
    this.companyService.getCandidateDetails(id).subscribe({
      next: (data) => {
        this.candidate.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.error(err);
        this.error.set('Failed to load candidate details.');
        this.loading.set(false);
      }
    });
  }

  goBack(): void {
    this.location.back();
  }
}
