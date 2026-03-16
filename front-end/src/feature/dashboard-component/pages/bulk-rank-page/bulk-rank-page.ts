import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  LucideAngularModule,
  Briefcase,
  Users,
  BarChart2,
  UploadCloud,
  Trash2,
} from 'lucide-angular';

type JobTargetOption = {
  id: number;
  name: string;
};

type RankedCandidate = {
  rank: number;
  name: string;
  matchScore: number;
  missingSkills: string;
};

@Component({
  selector: 'app-bulk-rank-page',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule],
  templateUrl: './bulk-rank-page.html',
  styleUrl: './bulk-rank-page.css',
})
export class BulkRankPage {
  readonly BriefcaseIcon = Briefcase;
  readonly UsersIcon = Users;
  readonly BarChartIcon = BarChart2;
  readonly UploadIcon = UploadCloud;
  readonly TrashIcon = Trash2;

  selectedFiles: File[] = [];
  isAnalyzing = false;
  showResults = false;

  step = 1;

  jobTargets: JobTargetOption[] = [
    { id: 1, name: 'Senior Angular Developer' },
    { id: 2, name: 'Frontend Tech Lead' },
    { id: 3, name: 'Product Designer' },
  ];

  selectedJobTargetId: number | null = null;

  rankedCandidates: RankedCandidate[] = [
    {
      rank: 1,
      name: 'Alex Johnson',
      matchScore: 93,
      missingSkills: 'Advanced RxJS, micro-frontend architecture',
    },
    {
      rank: 2,
      name: 'Maria Rodriguez',
      matchScore: 86,
      missingSkills: 'Design system documentation',
    },
    {
      rank: 3,
      name: 'Liam Chen',
      matchScore: 74,
      missingSkills: 'A/B testing, analytics tooling',
    },
  ];

  onFileSelect(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) {
      return;
    }
    this.selectedFiles = [...this.selectedFiles, ...Array.from(input.files)];
    if (this.selectedFiles.length && this.step < 2) {
      this.step = 2;
    }
  }

  removeFile(index: number): void {
    this.selectedFiles = this.selectedFiles.filter((_, i) => i !== index);
  }

  startAnalysis(): void {
    if (!this.selectedJobTargetId || !this.selectedFiles.length) {
      return;
    }
    this.isAnalyzing = true;
    this.showResults = false;
    this.step = 3;

    setTimeout(() => {
      this.isAnalyzing = false;
      this.showResults = true;
    }, 2000);
  }

  getMatchBadgeClass(score: number): string {
    if (score > 80) {
      return 'badge-success';
    }
    if (score > 60) {
      return 'badge-warning';
    }
    return 'badge-ghost';
  }
}

