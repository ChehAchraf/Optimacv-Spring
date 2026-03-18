import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideAngularModule, ListChecks } from 'lucide-angular';
import {ActionPlanItem} from '../../../../../../core/model/analysis.model';


@Component({
  selector: 'app-action-plan-card',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './action-plan-card-component.html',
})
export class ActionPlanCardComponent {
  actionPlan = input.required<ActionPlanItem[]>();

  readonly ChecklistIcon = ListChecks;
}

