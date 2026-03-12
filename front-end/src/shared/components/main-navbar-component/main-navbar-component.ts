import { Component } from '@angular/core';
import {
  LucideAngularModule,
  Menu,
  LayoutDashboard,
  FileText,
  Target,
  Upload,
  Sparkles,
  Sun,
  Moon,
  User,
  Settings,
  LogOut
} from 'lucide-angular';

@Component({
  selector: 'app-main-navbar-component',
    imports: [
        LucideAngularModule
    ],
  templateUrl: './main-navbar-component.html',
  styleUrl: './main-navbar-component.css',
})
export class MainNavbarComponent {
  readonly MenuIcon = Menu;
  readonly LayoutDashboardIcon = LayoutDashboard;
  readonly FileTextIcon = FileText;
  readonly TargetIcon = Target;
  readonly UploadIcon = Upload;
  readonly SparklesIcon = Sparkles;
  readonly SunIcon = Sun;
  readonly MoonIcon = Moon;
  readonly UserIcon = User;
  readonly SettingsIcon = Settings;
  readonly LogOutIcon = LogOut;
}
