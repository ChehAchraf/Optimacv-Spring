import {Component, inject, input, output} from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import {
  LucideAngularModule,
  LayoutDashboard,
  Users,
  History,
  Settings,
  X,
  Sparkles, BriefcaseIcon,
} from 'lucide-angular';
import {AuthStore} from '../../../../core/store/auth.store';

@Component({
  selector: 'app-side-bar-component',
  standalone: true,
  imports: [LucideAngularModule, RouterLink, RouterLinkActive],
  templateUrl: './side-bar-component.html',
  styleUrl: './side-bar-component.css',
})
export class SideBarComponent {
  isOpen = input<boolean>(false);
  protected readonly authStore = inject(AuthStore)
  close = output<void>();

  readonly LayoutDashboardIcon = LayoutDashboard;
  readonly UsersIcon = Users;
  readonly HistoryIcon = History;
  readonly SettingsIcon = Settings;
  readonly XIcon = X;
  readonly SparklesIcon = Sparkles;

  onClose(): void {
    this.close.emit();
  }

  protected readonly BriefcaseIcon = BriefcaseIcon;
}
