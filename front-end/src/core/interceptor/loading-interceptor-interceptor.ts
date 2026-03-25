import { HttpInterceptorFn } from '@angular/common/http';
import {inject} from '@angular/core';
import {NgxSpinnerService} from 'ngx-spinner';
import {finalize} from 'rxjs';

let activeRequests = 0;
export const loadingInterceptorInterceptor: HttpInterceptorFn = (req, next) => {
  const spinner = inject(NgxSpinnerService)

  if (activeRequests === 0) {
    spinner.show();
  }

  // 3. زيد فـ العداد مع كل ريكويست جديدة
  activeRequests++;

  return next(req).pipe(
    finalize(() => {
      // 4. فاش كتسالي الريكويست (نجاح ولا إيرور)، نقص العداد
      activeRequests--;

      // 5. طفي السبينر غييييير إيلا سالاو ݣاع الطلبات (العداد رجع لـ 0)
      if (activeRequests === 0) {
        spinner.hide();
      }
    })
  );

};
