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
  selectedResume : string | null,
  currentPage: number,
  totalPages: number,
  searchQuery : string,
  totalElements: number
}

const initialState : ResumeState ={
  files : [],
  isLoading : false,
  error : null,
  selectedJob : null,
  selectedResume : null,
  currentPage: 1,
  totalPages: 0,
  searchQuery : '',
  totalElements: 0
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

      updateSearchQuery(query : string){
        patchState(store , {searchQuery : query, currentPage: 1})
        this.getMyResumes({ page: 1, size: 10 })
      },

      getMyResumes : rxMethod<{page: number, size: number}>((request$)=>{
        return request$.pipe(
          switchMap(({page, size})=>{
            const query = store.searchQuery();
            const resumesId = toast.loading("Resumes are loading...")
            return resumeService.getMyResumes(page - 1, size, query).pipe(
              tapResponse({
                next : (response) =>{
                  toast.success("done!",{id:resumesId})
                  patchState(store,{
                    files : response.content,
                    currentPage: response.pageable.pageNumber + 1,
                    totalPages: response.totalPages,
                    totalElements: response.totalElements
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
