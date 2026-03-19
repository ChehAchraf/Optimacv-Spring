import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { CvUploadZoneComponent } from './components/cv-upload-zone-component/cv-upload-zone-component';
import { CvCardComponent } from './components/cv-card-component/cv-card-component';
import { JobTargetSelectorComponent } from './components/job-target-selector-component/job-target-selector-component';
import {resumeStore} from '../../../../core/store/resume.store';
import {JobStore} from '../../../../core/store/job.store';
import {AnalysisStore} from '../../../../core/store/analysis.store';
import {toast} from 'ngx-sonner';

@Component({
  selector: 'app-analyze-resume-page',
  standalone: true,
  imports: [CommonModule, CvUploadZoneComponent, JobTargetSelectorComponent],
  templateUrl: './analyze-resume-page.html',
  styleUrl: './analyze-resume-page.css',
})
export class AnalyzeResumePage implements OnInit {

  protected readonly resumeStore = inject(resumeStore);
  protected readonly jobStore = inject(JobStore);
  private readonly analysisStore = inject(AnalysisStore);

  ngOnInit() {
    this.jobStore.getMyJobs({ page: 1, size: 5 });
    this.resumeStore.getMyResumes({ page: 1, size: 10 });
  }

  onResumePageChange(newPage: number) {
    if (newPage >= 1 && newPage <= this.resumeStore.totalPages()) {
      this.resumeStore.getMyResumes({ page: newPage, size: 10 });
    }
  }

  onJobPageChange(newPage: number) {
    if (newPage >= 1 && newPage <= this.jobStore.totalPages()) {
      this.jobStore.getMyJobs({ page: newPage, size: 5 });
    }
  }

  onStartAnalysis(){
    const resumeId = this.selectedResumeId();
    const jobId = this.selectedJobId();

    if (!resumeId || !jobId) {
      toast.warning("please make sure you're selecting a job and resume");
      return;
    }


    this.analysisStore.startAnalysis({ resumeId, jobId });
  }

  selectedResumeId() {
    return this.resumeStore.selectedResume();
  }

  selectedJobId() {
    return this.resumeStore.selectedJob();
  }

  handleJobSelection(jobId : string) {
    this.resumeStore.setSelectdJob(jobId)
  }

  handleResumeSelection(resumeId: string) {
    this.resumeStore.setSelectdResume(resumeId);
  }

}
