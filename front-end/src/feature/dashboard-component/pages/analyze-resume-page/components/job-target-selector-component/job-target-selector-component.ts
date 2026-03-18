import {Component, effect, inject, input, OnInit, output} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  LucideAngularModule,
  Target,
  Briefcase,
  Building2,
} from 'lucide-angular';
import {JobStore} from '../../../../../../core/store/job.store';
import {resumeStore} from '../../../../../../core/store/resume.store';
import {IJobResponse} from '../../../../../../core/model/job.model';

@Component({
  selector: 'app-job-target-selector-component',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './job-target-selector-component.html',
})
export class JobTargetSelectorComponent {

  protected readonly jobStore = inject(JobStore)




  readonly TargetIcon = Target;
  readonly BriefcaseIcon = Briefcase;
  readonly BuildingIcon = Building2;

  jobs = input.required<IJobResponse[]>();

  selectJob = output<string>();

  onJobClick(jobId : string){
    this.selectJob.emit(jobId)
  }


}

