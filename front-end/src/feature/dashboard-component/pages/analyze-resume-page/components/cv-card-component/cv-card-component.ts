import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  LucideAngularModule,
  FileText,
  Clock,
} from 'lucide-angular';

@Component({
  selector: 'app-cv-card-component',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './cv-card-component.html',
})
export class CvCardComponent {
  readonly FileTextIcon = FileText;
  readonly ClockIcon = Clock;
}

