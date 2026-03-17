import {Component, computed, effect, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  LucideAngularModule,
  LayoutDashboard,
  Target,
  FileText,
  Activity,
  Plus,
  Search,
} from 'lucide-angular'
import {overviewStore} from '../../../../../core/store/overview.store';

@Component({
  selector: 'app-user-overview-component',
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './user-overview-component.html',
  styleUrl: './user-overview-component.css',
})
export class UserOverviewComponent implements OnInit{

  protected readonly store = inject(overviewStore)

  isEmpty = computed(()=>{
    const recetJobsCount = this.store.overview()?.recentTargets.length
    return recetJobsCount === 0;

  })

  constructor() {
    effect(() => {
      console.log(this.store.overview())
    });
  }

  // icons
  readonly LayoutDashboardIcon = LayoutDashboard;
  readonly TargetIcon = Target;
  readonly FileTextIcon = FileText;
  readonly ActivityIcon = Activity;
  readonly PlusIcon = Plus;
  readonly SearchIcon = Search;

  ngOnInit(): void {
    this.store.getMyOverview();
  }


}
