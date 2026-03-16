import {Injectable, signal} from '@angular/core';

export type ToastType = 'success' | 'error' | 'warning' | 'info';

@Injectable({
  providedIn: 'root',
})
export class ToasterService {

  toastState = signal<{message : string, type: ToastType, show : boolean}>({
    message : '',
    type : "success",
    show: false
  })

  show(message : string , type : ToastType){
    this.toastState.set({message,type, show:true})

    setTimeout(()=>{
      this.hide()
    }, 4000)
  }

  hide(){
    this.toastState.update(state => ({...state,show:false}))
  }

}
