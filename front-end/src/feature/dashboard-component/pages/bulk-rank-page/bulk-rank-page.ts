import {Component, effect, inject, OnInit} from '@angular/core';
import { Router } from '@angular/router';
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
import {CompanyStore} from '../../../../core/store/company.store';
import { TruncatePipe } from '../../../../core/pipes/truncate-pipe';

// removed Mock types

@Component({
  selector: 'app-bulk-rank-page',
  standalone: true,
  imports: [CommonModule, FormsModule, LucideAngularModule,TruncatePipe],
  templateUrl: './bulk-rank-page.html',
  styleUrl: './bulk-rank-page.css',
})
export class BulkRankPage implements OnInit{

  protected readonly companyStore = inject(CompanyStore);
  private readonly router = inject(Router);

  readonly BriefcaseIcon = Briefcase;
  readonly UsersIcon = Users;
  readonly BarChartIcon = BarChart2;
  readonly UploadIcon = UploadCloud;
  readonly TrashIcon = Trash2;

  constructor() {
    effect(() => {
      console.log(this.companyStore.jobs())
    });
  }

  selectedFiles: File[] = [];
  showResults = false;

  ngOnInit() {
    this.companyStore.loadCompanyJobs()
  }

  step = 1;

  selectedJobTargetId: string | null = null;
  selectedCandidateDetails: any = null;

  parseVerdict(aiFeedback: string | null): string {
    if (!aiFeedback) return 'Processing...';
    try {
      const parsed = JSON.parse(aiFeedback);
      return parsed.verdict || 'Analysis complete';
    } catch {
      return 'Invalid feedback format';
    }
  }

  openCandidateDetails(candidate: any): void {
    if (candidate.aiFeedback) {
      try {
        this.selectedCandidateDetails = JSON.parse(candidate.aiFeedback);
      } catch (e) {
        this.selectedCandidateDetails = { raw: candidate.aiFeedback };
      }
      this.selectedCandidateDetails.id = candidate.id;
      const modal = document.getElementById('feedback_modal') as HTMLDialogElement;
      if (modal) {
        modal.showModal();
      }
    }
  }

  MapsToFullDetails(candidateId: string): void {
    const modal = document.getElementById('feedback_modal') as HTMLDialogElement;
    if (modal) {
      modal.close();
    }
    this.router.navigate(['/dashboard/candidates/', candidateId]);
  }

  deleteCandidate(candidate: any): void {
    if (confirm('Are you sure you want to delete this candidate?')) {
      if (this.selectedJobTargetId) {
        this.companyStore.deleteCandidate({ candidateId: candidate.id, jobId: this.selectedJobTargetId });
      }
    }
  }

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

  onJobChange(): void {
    if (this.selectedJobTargetId) {
      // resetting page to 0 when job changes
      this.companyStore.changePage(0);
      this.companyStore.pollCandidates(this.selectedJobTargetId);
      this.showResults = true;
    }
  }

  changePage(page: number): void {
    if (page >= 0 && page < this.companyStore.totalPages() && page !== this.companyStore.currentPage()) {
      this.companyStore.changePage(page);
      if (this.selectedJobTargetId) {
        this.companyStore.pollCandidates(this.selectedJobTargetId);
      }
    }
  }

  startAnalysis(): void {
    if (!this.selectedJobTargetId || !this.selectedFiles.length) {
      return;
    }

    this.companyStore.uploadCandidates({
      jobId: this.selectedJobTargetId,
      files: this.selectedFiles
    });
    
    this.companyStore.pollCandidates(this.selectedJobTargetId);
    
    this.step = 3;
    this.showResults = true;
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

