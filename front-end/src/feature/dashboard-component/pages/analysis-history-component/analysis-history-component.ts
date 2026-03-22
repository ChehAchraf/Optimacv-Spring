import {Component, effect, inject, OnInit, OnDestroy} from '@angular/core';
import { CommonModule } from '@angular/common';
import { HistoryHeaderComponent } from './components/history-header-component/history-header-component';
import { HistoryTableComponent } from './components/history-table-component/history-table-component';
import { HistoryPaginationComponent } from './components/history-pagination-component/history-pagination-component';
import {AnalysisStore} from '../../../../core/store/analysis.store';
import {Router} from '@angular/router';
import { FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged, Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-analysis-history-component',
  standalone: true,
  imports: [
    CommonModule,
    HistoryHeaderComponent,
    HistoryTableComponent,
    HistoryPaginationComponent,
  ],
  templateUrl: './analysis-history-component.html',
})
export class AnalysisHistoryComponent implements OnInit, OnDestroy {

  readonly analysisStore = inject(AnalysisStore)
  private router = inject(Router);

  searchControl = new FormControl('');
  private destroy$ = new Subject<void>();

  ngOnInit() {
    this.analysisStore.loadHistory(0);

    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(value => {
      this.analysisStore.updateSearchQuery(value || '');
    });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onSearch(term: string) {
    this.searchControl.setValue(term);
  }

  onDelete(id: string) {
    this.analysisStore.deleteAnalysis(id);
  }

  onPageChange(page: number) {

    this.analysisStore.loadHistory(page - 1);
  }
}
