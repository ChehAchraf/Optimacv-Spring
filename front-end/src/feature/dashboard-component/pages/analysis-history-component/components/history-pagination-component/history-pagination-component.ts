import { Component, computed, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-history-pagination',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './history-pagination-component.html',
})
export class HistoryPaginationComponent {
  totalItems = input(0);
  pageSize = input(10);
  currentPage = input(1);

  pageChange = output<number>();

  totalPages = computed(() => {
    const size = Math.max(1, this.pageSize());
    return Math.max(1, Math.ceil(this.totalItems() / size));
  });

  startItem = computed(() => {
    if (this.totalItems() === 0) return 0;
    return (this.currentPage() - 1) * this.pageSize() + 1;
  });

  endItem = computed(() => {
    if (this.totalItems() === 0) return 0;
    return Math.min(this.totalItems(), this.currentPage() * this.pageSize());
  });

  goTo(page: number) {
    const clamped = Math.min(Math.max(1, page), this.totalPages());
    this.pageChange.emit(clamped);
  }
}

