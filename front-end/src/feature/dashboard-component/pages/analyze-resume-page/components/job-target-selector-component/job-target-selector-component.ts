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
  currentPage = input.required<number>();
  totalPages = input.required<number>();

  selectJob = output<string>();
  pageChange = output<number>();

  onJobClick(jobId : string){
    this.selectJob.emit(jobId)
  }

  onPageChange(newPage: number) {
    if (newPage >= 1 && newPage <= this.totalPages()) {
      this.pageChange.emit(newPage);
    }
  }

  onSearch(event: Event) {
    const query = (event.target as HTMLInputElement).value;
    this.jobStore.updateSearchQuery(query);
  }


}

