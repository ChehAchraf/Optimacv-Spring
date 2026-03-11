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
  selector: 'app-navbar-component',
  imports: [LucideAngularModule],
  standalone: true,
  templateUrl: './navbar-component.html',
  styleUrl: './navbar-component.css',
})
export class NavbarComponent {
  
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
