import {patchState, signalStore, withComputed, withMethods, withState} from '@ngrx/signals';
import {IJobRequest, IJobResponse} from '../model/job.model';
import {rxMethod} from '@ngrx/signals/rxjs-interop';
import {computed, inject} from '@angular/core';
import {JobService} from '../service/job/job-service';
import {distinctUntilChanged, exhaustMap, switchMap, tap} from 'rxjs';
import {tapResponse} from '@ngrx/operators';
import {HttpErrorResponse} from '@angular/common/http';
import {toast} from 'ngx-sonner';

export type JobState = {
  Jobs : IJobResponse[],
  isLoading : boolean ,
  error : string | null,
  currentPage: number,
  totalPages: number,
  searchQuery : string,
  totalElements: number
}

const initialState : JobState = {
  Jobs : [],
  isLoading : false,
  error : null,
  currentPage: 1,
  totalPages: 0,
  searchQuery : '',
  totalElements: 0
}


export const JobStore = signalStore(
  {providedIn : 'root'},
  withState(initialState),

  withMethods((store)=>{
    const jobService = inject(JobService)
    return {

      updateSearchQuery(query : string){
        patchState(store , {searchQuery : query, currentPage: 1})
        this.getMyJobs({ page: 1, size: 5 })
      },

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

      getMyJobs : rxMethod<{page: number, size: number}>((data$)=>{
        return data$.pipe(
          tap(()=>patchState(store, {isLoading:true,error:null})),

          switchMap(({page, size})=>{
            const query = store.searchQuery();
            return jobService.loadMyJobs(page - 1, size, query).pipe(
              tapResponse({
                next : (response) => {
                  patchState(store,{
                    Jobs : response.content,
                    currentPage: response.pageable.pageNumber + 1,
                    totalPages: response.totalPages,
                    totalElements: response.totalElements,
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
      }),


      deleteMyJob: rxMethod<string>((jobId$) => {
        return jobId$.pipe(
          distinctUntilChanged(),
          tap(() => patchState(store, { isLoading: true, error: null })),
          exhaustMap((jobId) => {
            return jobService.deleteMyJob(jobId).pipe(
              tapResponse({
                next: () => {
                  console.log("Job deleted!");
                  toast.success("The job target has been deleted!");
                  patchState(store, (state) => ({
                    isLoading: false,
                    Jobs: state.Jobs.filter((job) => job.id.toString() !== jobId)
                  }));
                },
                error: (error: HttpErrorResponse) => {
                  console.error(error.message);
                  toast.error("Error deleting job, please try again.");
                  patchState(store, { isLoading: false, error: error.message });
                }
              })
            );
          })
        );
      })

    }
  }),

  withComputed((store)=>({
    jobTotal : computed(()=>store.Jobs().length)
  }))
)
