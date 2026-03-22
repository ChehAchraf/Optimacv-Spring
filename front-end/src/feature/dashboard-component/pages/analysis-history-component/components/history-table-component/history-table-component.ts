import {Component, inject, input, output} from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router, RouterLink} from '@angular/router';

import {LucideAngularModule, TrashIcon} from 'lucide-angular';

@Component({
  selector: 'app-history-table',
  standalone: true,
  imports: [CommonModule, RouterLink, LucideAngularModule],
  templateUrl: './history-table-component.html',
})
export class HistoryTableComponent {
  private router = inject(Router)
  readonly TrashIcon = TrashIcon;
  analyses = input.required<any[]>();
  viewDetails = output<string>();
  delete = output<string>();

  constructor() {
    console.log(this.router.routerState)
  }

  onViewDetails(id: string) {
    this.viewDetails.emit(id);
  }

  onDelete(id: string) {
    this.delete.emit(id);
  }

  scoreBadgeClass(score: number) {
    if (score >= 80) return 'badge-success';
    if (score >= 50) return 'badge-warning';
    return 'badge-error';
  }
}

