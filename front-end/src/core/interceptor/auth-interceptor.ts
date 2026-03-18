import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {catchError, throwError} from 'rxjs';
import {toast} from 'ngx-sonner';
import {inject} from '@angular/core';
import {Router} from '@angular/router';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const token = localStorage.getItem('token');
  const router  = inject(Router);

  if(token){
    const newReq = req.clone({
      setHeaders : {
        Authorization: `Bearer ${token}`
      }
    })

    return next(newReq).pipe(
      catchError((error : HttpErrorResponse)=>{
        if (error.status === 401 || error.status === 403){
          toast.info("please log in first")
          localStorage.removeItem('token')
          router.navigate(['/login']);
        }
        return throwError(()=>error)
      })
    )
  }

  return next(req);
};
