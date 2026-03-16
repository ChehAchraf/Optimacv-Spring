import {Component, inject} from '@angular/core';
import {ToasterService} from '../../../core/service/toast/toaster-service';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-toast-component',
  imports: [CommonModule],
  templateUrl: './toast-component.html',
  styleUrl: './toast-component.css',
})
export class ToastComponent {
  protected toast = inject(ToasterService)
}
