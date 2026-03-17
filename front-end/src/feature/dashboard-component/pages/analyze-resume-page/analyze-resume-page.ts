import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CvUploadZoneComponent } from './components/cv-upload-zone-component/cv-upload-zone-component';
import { CvCardComponent } from './components/cv-card-component/cv-card-component';
import { JobTargetSelectorComponent } from './components/job-target-selector-component/job-target-selector-component';

@Component({
  selector: 'app-analyze-resume-page',
  standalone: true,
  imports: [CommonModule, CvUploadZoneComponent, CvCardComponent, JobTargetSelectorComponent],
  templateUrl: './analyze-resume-page.html',
  styleUrl: './analyze-resume-page.css',
})
export class AnalyzeResumePage {}
