import { Component, output, OnDestroy } from '@angular/core';
import { RouterLink } from '@angular/router';
import {
  LucideAngularModule,
  Menu,
  User,
  Settings,
  LogOut,
  ChevronDown,
} from 'lucide-angular';

@Component({
  selector: 'app-topbar-component',
  standalone: true,
  imports: [LucideAngularModule, RouterLink],
  templateUrl: './topbar-component.html',
  styleUrl: './topbar-component.css',
})
export class TopbarComponent implements OnDestroy {
  /** Emitted when the mobile menu (hamburger) button is clicked. */
  menuClick = output<void>();

  readonly MenuIcon = Menu;
  readonly UserIcon = User;
  readonly SettingsIcon = Settings;
  readonly LogOutIcon = LogOut;
  readonly ChevronDownIcon = ChevronDown;

  isDropdownOpen = false;

  private documentClickHandler = (): void => this.closeDropdown();

  toggleDropdown(event: Event): void {
    event.stopPropagation();
    this.isDropdownOpen = !this.isDropdownOpen;
    if (this.isDropdownOpen) {
      setTimeout(() => document.addEventListener('click', this.documentClickHandler));
    } else {
      document.removeEventListener('click', this.documentClickHandler);
    }
  }

  closeDropdown(): void {
    if (this.isDropdownOpen) {
      this.isDropdownOpen = false;
      document.removeEventListener('click', this.documentClickHandler);
    }
  }

  ngOnDestroy(): void {
    document.removeEventListener('click', this.documentClickHandler);
  }

  onMenuClick(): void {
    this.menuClick.emit();
  }
}
