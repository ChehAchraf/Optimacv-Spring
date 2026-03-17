import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {IOverview} from '../model/overview.model';
import {rxMethod} from '@ngrx/signals/rxjs-interop';
import {switchMap, tap} from 'rxjs';
import {inject} from '@angular/core';
import {OverviewService} from '../service/overview/overview-service';
import {tapResponse} from '@ngrx/operators';
import {HttpErrorResponse} from '@angular/common/http';
import {toast} from 'ngx-sonner';

export type OverviewState = {
  overview : IOverview | null,
  isLoading : boolean,
  error : string | null
}

const initialState : OverviewState = {
  overview : null,
  isLoading : false,
  error : null
}

export const overviewStore = signalStore(

  {providedIn: "root"},

  withState(initialState),

  withMethods((store)=>{
    const overviewService = inject(OverviewService)
    return {
      getMyOverview : rxMethod<void>((response$)=>{
        return response$.pipe(
          tap(()=> patchState(store , {isLoading : true} ) ),
          switchMap((response)=>{
            return overviewService.getOverView().pipe(
              tapResponse({
                next : (response)=>{
                  patchState(
                    store,
                    {
                      overview : response,
                      isLoading : false
                    }
                  )
                  toast.loading("data is loading...",{duration : 700})
                },
                error : (error : HttpErrorResponse)=>{
                    patchState(
                      store,
                      {
                        isLoading : false,
                        error : error.message
                      }
                    )
                  toast.error("Error fetching the statistiques, please try again later!")
                }
              })
            )
          })
        )
      })
    }
  })

)
