import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const token = localStorage.getItem('token');

  if(token){
    const newReq = req.clone({
      setHeaders : {
        Authorization: `Bearer ${token}`
      }
    })

    return next(newReq);
  }

  return next(req);
};
