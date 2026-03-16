import {Component, inject} from '@angular/core';
import {AuthStore} from '../../../../core/store/auth.store';
import {ComapnyOverviewComponent} from './comapny-overview-component/comapny-overview-component';
import {UserOverviewComponent} from './user-overview-component/user-overview-component';

@Component({
  selector: 'app-overview-page',
  imports: [
    ComapnyOverviewComponent,
    UserOverviewComponent

  ],
  templateUrl: './overview-page.html',
  styleUrl: './overview-page.css',
})
export class OverviewPage {
  protected authStore = inject(AuthStore)
}
