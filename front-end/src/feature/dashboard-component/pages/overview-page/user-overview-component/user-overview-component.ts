import { Component } from '@angular/core';
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

@Component({
  selector: 'app-user-overview-component',
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './user-overview-component.html',
  styleUrl: './user-overview-component.css',
})
export class UserOverviewComponent {}
