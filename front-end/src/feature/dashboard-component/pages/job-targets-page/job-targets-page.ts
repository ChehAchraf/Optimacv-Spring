import { Component, inject } from '@angular/core';
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

@Component({
  selector: 'app-job-targets-page',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './job-targets-page.html',
  styleUrl: './job-targets-page.css',
})
export class JobTargetsPage {
  protected readonly authStore = inject(AuthStore);

  readonly BriefcaseIcon = Briefcase;
  readonly PlusIcon = Plus;
  readonly TargetIcon = Target;
  readonly PencilIcon = Pencil;
  readonly TrashIcon = Trash2;
  readonly ClockIcon = Clock;
  readonly FileTextIcon = FileText;
  readonly InboxIcon = Inbox;

  readonly totalTargets = 0;
  readonly jobTargets: Array<{
    id: number;
    title: string;
    status: 'Draft' | 'Open' | 'Closed';
    description: string;
    createdAt: string;
  }> = [];
}
