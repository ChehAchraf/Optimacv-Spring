import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {IResumeRequest, IResumeResponse} from '../model/resume.model';
import {rxMethod} from '@ngrx/signals/rxjs-interop';
import {exhaustMap, switchMap, tap} from 'rxjs';
import {inject} from '@angular/core';
import {ResumeService} from '../service/resume/resume-service';
import {tapResponse} from '@ngrx/operators';
import {HttpErrorResponse} from '@angular/common/http';
import {toast} from 'ngx-sonner';

export type ResumeState = {
  files : IResumeResponse[] ,
  isLoading : boolean,
  error : string | null,
  selectedJob : string | null,
  selectedResume : string | null
}

const initialState : ResumeState ={
  files : [],
  isLoading : false,
  error : null,
  selectedJob : null,
  selectedResume : null
}

export const resumeStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store)=>{
    const resumeService = inject(ResumeService)
    return {
      uploadResume : rxMethod<IResumeRequest>((request$)=>{
        return request$.pipe(
          tap(()=>patchState(store, {isLoading:true,error:null})),

          exhaustMap((request)=>{
            const toastId = toast.loading("you're Resume is uploading, please wait...")
            return resumeService.uploadResume(request).pipe(
              tapResponse({
                next : (response)=>{
                  toast.success("you're resume has been uploaded successfully", {id:toastId})
                  patchState(store,{
                    files : [response,...store.files()],
                  })

                },
                error : (error : HttpErrorResponse)=>{
                  toast.error("there might be an error, please try again later!", {id:toastId})
                  console.log(error.message)
                }
              })
            )
          })
        )
      }),

      getMyResumes : rxMethod<void>((response$)=>{
        return response$.pipe(
          switchMap((response)=>{
            const resumesId = toast.loading("Resumes are loading...")
            return resumeService.getMyResumes().pipe(
              tapResponse({
                next : (response) =>{
                  toast.success("done!",{id:resumesId})
                  patchState(store,{
                    files : response
                  })
                },
                error : (error : HttpErrorResponse)=>{
                  toast.error("there must be an error, please try again later", {duration:3000,id:resumesId})
                }
              })
            )
          })
        )
      }),
      setSelectdJob : (id : string)=>{
        patchState(store , {selectedJob : id})
      },

      setSelectdResume: (id: string) => {
        patchState(store, { selectedResume: id });
      },
    }
  })
)
