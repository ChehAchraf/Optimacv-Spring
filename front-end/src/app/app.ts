import {Component, inject, OnInit, signal} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {NgxSonnerToaster } from 'ngx-sonner';
import {ToastComponent} from '../shared/components/toast-component/toast-component';
import {NgxSpinner, NgxSpinnerComponent, NgxSpinnerService} from 'ngx-spinner';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ToastComponent, NgxSonnerToaster, ToastComponent, ToastComponent, ToastComponent, NgxSpinnerComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  protected readonly title = signal('front-end');
  private spinner = inject(NgxSpinnerService)



}
