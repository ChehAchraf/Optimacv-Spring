import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  LucideAngularModule,
  Target,
  Briefcase,
  Building2,
} from 'lucide-angular';

@Component({
  selector: 'app-job-target-selector-component',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './job-target-selector-component.html',
})
export class JobTargetSelectorComponent {
  readonly TargetIcon = Target;
  readonly BriefcaseIcon = Briefcase;
  readonly BuildingIcon = Building2;
}

