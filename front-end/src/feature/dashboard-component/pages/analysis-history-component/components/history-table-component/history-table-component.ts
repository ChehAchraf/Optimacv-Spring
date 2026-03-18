import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-history-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './history-table-component.html',
})
export class HistoryTableComponent {
  analyses = input.required<any[]>();
  viewDetails = output<string>();

  onViewDetails(id: string) {
    this.viewDetails.emit(id);
  }

  scoreBadgeClass(score: number) {
    if (score >= 80) return 'badge-success';
    if (score >= 50) return 'badge-warning';
    return 'badge-error';
  }
}

