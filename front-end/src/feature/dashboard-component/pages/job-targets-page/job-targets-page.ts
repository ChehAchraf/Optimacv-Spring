import {Component, effect, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  LucideAngularModule,
  Briefcase,
  Plus,
  Target,
  Pencil,
  Trash2,
  Clock,
  FileText,
  Inbox,
} from 'lucide-angular';
import { AuthStore } from '../../../../core/store/auth.store';
import {JobStore} from '../../../../core/store/job.store';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {on} from '@ngrx/signals/events';
import {email} from '@angular/forms/signals';

@Component({
  selector: 'app-job-targets-page',
  standalone: true,
  imports: [CommonModule, LucideAngularModule, ReactiveFormsModule],
  templateUrl: './job-targets-page.html',
  styleUrl: './job-targets-page.css',
})
export class JobTargetsPage implements OnInit{

  protected readonly authStore = inject(AuthStore);
  protected readonly store = inject(JobStore);
  private readonly fb = inject(FormBuilder);

  constructor() {
    effect(() => {
      console.log(this.store.Jobs())
    });
  }

  createJobFrom = this.fb.group({
    title : ['',[Validators.required,Validators.minLength(3)]],
    company : ['',[Validators.required,Validators.minLength(3)]],
    description : ['', [Validators.required, Validators.minLength(10)]]
  })

  readonly BriefcaseIcon = Briefcase;
  readonly PlusIcon = Plus;
  readonly TargetIcon = Target;
  readonly PencilIcon = Pencil;
  readonly TrashIcon = Trash2;
  readonly ClockIcon = Clock;
  readonly FileTextIcon = FileText;
  readonly InboxIcon = Inbox;
  protected readonly on = on;


  readonly totalTargets = 0;
  readonly jobTargets: Array<{
    id: number;
    title: string;
    status: 'Draft' | 'Open' | 'Closed';
    description: string;
    createdAt: string;
  }> = [];

  onSubmit(){
    console.log(this.createJobFrom.value)
    if (this.createJobFrom.valid){
      this.store.createJob({
        title : this.createJobFrom.value.title!,
        company : this.createJobFrom.value.company!,
        description : this.createJobFrom.value.description!
      })
    }
  }

  deleteJob(jobId : string ){
    this.store.deleteMyJob(jobId);
  }

  ngOnInit(): void {
    this.store.getMyJobs({ page: 1, size: 5 });
  }

  onPageChange(newPage: number) {
    if (newPage >= 1 && newPage <= this.store.totalPages()) {
      this.store.getMyJobs({ page: newPage, size: 5 });
    }
  }



  // getters for the form
  get title(){
    return this.createJobFrom.get('title')
  }

  get company (){
    return this.createJobFrom.get('company')
  }

  get description(){
    return this.createJobFrom.get('description')
  }

  protected readonly email = email;
}
