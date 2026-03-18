import {Component, effect, inject, OnInit, signal} from '@angular/core';
import { CommonModule } from '@angular/common';
import { HistoryHeaderComponent } from './components/history-header-component/history-header-component';
import { HistoryTableComponent } from './components/history-table-component/history-table-component';
import { HistoryPaginationComponent } from './components/history-pagination-component/history-pagination-component';
import {AnalysisStore} from '../../../../core/store/analysis.store';

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
export class AnalysisHistoryComponent implements OnInit{

  readonly analysisStore = inject(AnalysisStore)

  constructor() {
    effect(() => {
      console.log(this.analysisStore.analyses())
    });
  }

  ngOnInit() {
    this.analysisStore.loadHistory(0)
  }

  analyses = signal<any[]>([
    {
      id: 'a1',
      date: 'Mar 18, 2026',
      time: '2:41 PM',
      resumeName: 'Frontend_CV_v2.pdf',
      resumeMeta: 'Library resume',
      jobTarget: 'Full Stack Java/Angular',
      jobMeta: 'OptimaCV target',
      score: 86,
    },
    {
      id: 'a2',
      date: 'Mar 12, 2026',
      time: '11:09 AM',
      resumeName: 'UI_Designer_CV.pdf',
      resumeMeta: 'Uploaded CV',
      jobTarget: 'Product Designer',
      jobMeta: 'Remote-friendly',
      score: 72,
    },
    {
      id: 'a3',
      date: 'Feb 27, 2026',
      time: '6:34 PM',
      resumeName: 'Junior_Dev_CV.pdf',
      resumeMeta: 'Library resume',
      jobTarget: 'Backend Java (Spring)',
      jobMeta: 'Entry-level',
      score: 41,
    },
  ]);

  searchTerm = signal('');
  currentPage = signal(1);
  pageSize = signal(10);
  totalItems = signal(48);

  onSearch(term: string) {
    this.searchTerm.set(term);
  }

  onViewDetails(id: string) {
    // Smart component hook for navigation/dialog later
    console.log('View details:', id);
  }

  onPageChange(page: number) {
    this.currentPage.set(page);
  }
}
