import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SideBarComponent } from './components/side-bar-component/side-bar-component';
import { TopbarComponent } from './components/topbar-component/topbar-component';

@Component({
  selector: 'app-dashboard-component',
  standalone: true,
  imports: [SideBarComponent, TopbarComponent, RouterOutlet],
  templateUrl: './dashboard-component.html',
  styleUrl: './dashboard-component.css',
})
export class DashboardComponent {
  /** Controls mobile sidebar visibility. Desktop sidebar is always visible. */
  sidebarOpen = signal(false);

  onSidebarClose(): void {
    this.sidebarOpen.set(false);
  }

  onMenuClick(): void {
    this.sidebarOpen.update((open) => !open);
  }
}
