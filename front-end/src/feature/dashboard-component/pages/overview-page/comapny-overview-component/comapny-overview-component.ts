import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import {
  LucideAngularModule,
  Briefcase,
  Users,
  BarChart2,
  TrendingUp,
  Plus,
  FileText,
  Award,
} from 'lucide-angular';

type TopCandidate = {
  name: string;
  appliedFor: string;
  matchScore: number;
  matchBadgeClass: string;
  date: string;
};

@Component({
  selector: 'app-comapny-overview-component',
  standalone: true,
  imports: [CommonModule, RouterLink, LucideAngularModule],
  templateUrl: './comapny-overview-component.html',
  styleUrl: './comapny-overview-component.css',
})
export class ComapnyOverviewComponent {
  readonly BriefcaseIcon = Briefcase;
  readonly UsersIcon = Users;
  readonly BarChartIcon = BarChart2;
  readonly TrendingUpIcon = TrendingUp;
  readonly PlusIcon = Plus;
  readonly FileTextIcon = FileText;
  readonly AwardIcon = Award;

  readonly activeJobTargets = 12;
  readonly resumesAnalyzed = 348;
  readonly averageMatchRate = 76;

  readonly topCandidates: TopCandidate[] = [
    {
      name: 'Alex Johnson',
      appliedFor: 'Senior Angular Developer',
      matchScore: 92,
      matchBadgeClass: 'badge-success',
      date: 'Today',
    },
    {
      name: 'Maria Rodriguez',
      appliedFor: 'Frontend Tech Lead',
      matchScore: 84,
      matchBadgeClass: 'badge-success',
      date: 'Yesterday',
    },
    {
      name: 'Liam Chen',
      appliedFor: 'UI Engineer',
      matchScore: 73,
      matchBadgeClass: 'badge-warning',
      date: '2 days ago',
    },
    {
      name: 'Emily Carter',
      appliedFor: 'Product Designer',
      matchScore: 68,
      matchBadgeClass: 'badge-warning',
      date: '3 days ago',
    },
  ];
}

