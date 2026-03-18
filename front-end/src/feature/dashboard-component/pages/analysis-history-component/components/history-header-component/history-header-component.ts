import { Component, output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-history-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './history-header-component.html',
})
export class HistoryHeaderComponent {
  search = output<string>();

  onSearchInput(value: string) {
    this.search.emit(value);
  }
}

