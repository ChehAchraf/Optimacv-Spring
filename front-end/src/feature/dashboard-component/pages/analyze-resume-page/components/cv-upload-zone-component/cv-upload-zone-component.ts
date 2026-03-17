import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  LucideAngularModule,
  UploadCloud,
  FileText,
} from 'lucide-angular';

@Component({
  selector: 'app-cv-upload-zone-component',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './cv-upload-zone-component.html',
})
export class CvUploadZoneComponent {
  readonly UploadIcon = UploadCloud;
  readonly FileTextIcon = FileText;
}

