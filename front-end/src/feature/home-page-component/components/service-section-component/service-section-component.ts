import { Component } from '@angular/core';
import {
  LucideAngularModule,
  Bot,
  Target,
  Users,
  FileCheck,
} from 'lucide-angular';

@Component({
  selector: 'app-service-section-component',
  standalone: true,
  imports: [LucideAngularModule],
  templateUrl: './service-section-component.html',
  styleUrl: './service-section-component.css',
})
export class ServiceSectionComponent {
  readonly BotIcon = Bot;
  readonly TargetIcon = Target;
  readonly UsersIcon = Users;
  readonly FileCheck2Icon = FileCheck;
}
