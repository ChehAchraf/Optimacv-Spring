import {patchState, signalStore, withComputed, withMethods, withState} from '@ngrx/signals';
import {IJobRequest, IJobResponse} from '../model/job.model';
import {rxMethod} from '@ngrx/signals/rxjs-interop';
import {computed, inject} from '@angular/core';
import {JobService} from '../service/job/job-service';
import {exhaustMap, switchMap, tap} from 'rxjs';
import {tapResponse} from '@ngrx/operators';
import {HttpErrorResponse} from '@angular/common/http';
import {toast} from 'ngx-sonner';

export type JobState = {
  Jobs : IJobResponse[],
  isLoading : boolean ,
  error : string | null
}

const initialState : JobState = {
  Jobs : [],
  isLoading : false,
  error : null
}


export const JobStore = signalStore(
  {providedIn : 'root'},
  withState(initialState),

  withMethods((store)=>{
    const jobService = inject(JobService)
    return {
      createJob : rxMethod<IJobRequest>((data$) => {
        return data$.pipe(
          tap(()=> patchState(store , {isLoading : true , error : null})),
          exhaustMap((data)=>{
            return jobService.createJob(data).pipe(
              tapResponse({
                next : (response)=>{
                  console.log(response)
                  toast.success("the job has been created successfully")
                  patchState(store, (state)=>({
                    isLoading : false,
                    Jobs : [response, ...state.Jobs]
                  }))
                },
                error : (error :HttpErrorResponse)=>{
                  console.log(error.message)
                  toast.error("there must be an error, please try again later")
                }
              })
            );
          })
        )
      }),

      getMyJobs : rxMethod<void>((data$)=>{
        return data$.pipe(
          tap(()=>patchState(store, {isLoading:true,error:null})),

          switchMap((data)=>{
            return jobService.loadMyJobs().pipe(
              tapResponse({
                next : (response) => {
                  patchState(store,{
                    Jobs : response,
                    isLoading:false,
                  })
                },
                error : (error : HttpErrorResponse)=>{
                  patchState(store,{
                    isLoading : false,
                    error : error.message
                  })
                  toast.error("there must be an error")
                }
              })
            )
          })
        )
      })
    }
  }),

  withComputed((store)=>({
    jobTotal : computed(()=>store.Jobs().length)
  }))
)
