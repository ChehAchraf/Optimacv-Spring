import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {IAnalyseRequest, IAnalysisHistory} from '../model/analyse.model';
import {inject} from '@angular/core';
import {AnalyseService} from '../service/analyse/analyse-service';
import {rxMethod} from '@ngrx/signals/rxjs-interop';
import {exhaustMap, switchMap, tap, timeout} from 'rxjs';
import {tapResponse} from '@ngrx/operators';
import {toast} from 'ngx-sonner';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

const initialState = {
  analyses: [] as IAnalysisHistory[],
  isLoading: false,
  totalElements: 0,
  currentPage: 0,
  totalPages: 0,
  error: null as string | null,
  searchQuery: '',
};

export const AnalysisStore = signalStore(
  {providedIn : 'root'},

  withState(initialState),

  withMethods((store)=>{
    const analysisService = inject(AnalyseService)
    const router = inject(Router)
    return {
      startAnalysis : rxMethod<IAnalyseRequest>((request$)=>{
        return request$.pipe(

          exhaustMap((request)=>{
            const analysisId = toast.loading("Our AI is analysing you're resume, please wait...")
            return analysisService.startAnalysis(request).pipe(
              timeout(60000),
              tapResponse({
                next : (response)=>{
                  toast.success("the analysis is done, you can now check it!",{id:analysisId})

                  patchState(store,(state)=>({
                    analyses : [response,...state.analyses]
                  }))
                  router.navigate(['/dashboard/history', response.id]);
                },
                error : (error : HttpErrorResponse)=>{
                  toast.error("error" , {id:analysisId})
                  console.log(error)
                }
              })
            )
          })
        )
      }),
      deleteAnalysis: rxMethod<string>((id$) => {
        return id$.pipe(
          exhaustMap((id) => {
            const deleteId = toast.loading("Deleting analysis...");
            return analysisService.deleteAnalysis(id).pipe(
              tapResponse({
                next: () => {
                  toast.success("Analysis deleted successfully!", { id: deleteId });
                  patchState(store, (state) => ({
                    analyses: state.analyses.filter((a) => a.id !== id),
                    totalElements: state.totalElements - 1
                  }));
                },
                error: () => {
                  toast.error("Failed to delete analysis", { id: deleteId });
                }
              })
            );
          })
        );
      }),
      loadHistory: rxMethod<number>((page$) => {
        return page$.pipe(
          tap(() => patchState(store, { isLoading: true, error: null })),
          switchMap((page) => {
            return analysisService.getHistory(page, 10, store.searchQuery()).pipe(
              tapResponse({
                next: (response) => {
                  let mappedAnalyses = response.content.map((content)=>{
                    let extractedScore = 0;

                    try{
                      let object = JSON.parse(content.feedback)
                      extractedScore = object.score || object.matchScore || 0;
                      console.log(extractedScore)
                    }catch (e: any){
                      console.log(e)
                      throw new Error("error")

                    }
                    return {
                      ...content,
                      parsedScore: extractedScore
                    };
                  })
                  patchState(store, {
                    analyses: mappedAnalyses,
                    totalElements: response.totalElements,
                    currentPage: response.number,
                    totalPages: response.totalPages,
                    isLoading: false,
                  });
                },
                error: (err) => {
                  patchState(store, { error: 'there must be an error. try again later', isLoading: false });
                },
              })
            );
          })
        );
      }),
      getAnalysisById : rxMethod<{ analysisId: string }>((request$)=>{
        return request$.pipe(
          switchMap((request)=>{
            return analysisService.getanalysis(request.analysisId).pipe(
              tapResponse({
                next : (response)=>{
                  let parsedData: any = null;
                  let extractedScore = 0;

                  try {
                    parsedData = JSON.parse(response.feedback);
                    extractedScore = parsedData.score || parsedData.matchScore || 0;
                  } catch (e: any) {
                    console.error("JSON Parsing error:", e);
                  }

                  const mappedAnalysis = {
                    ...response,
                    parsedScore: extractedScore,
                    fullAnalysis: parsedData
                  };

                  patchState(store, (state) => {
                    const exists = state.analyses.some((a) => a.id === mappedAnalysis.id);
                    return {
                      analyses: exists
                        ? state.analyses.map((a) => a.id === mappedAnalysis.id ? mappedAnalysis : a)
                        : [...state.analyses, mappedAnalysis],
                      isLoading: false
                    };
                  });
                },
                error : (error : HttpErrorResponse)=>{

                }
              })
            )
          })
        )
      })
    }
  }),
  withMethods((store) => ({
    updateSearchQuery: rxMethod<string>((query$) => {
      return query$.pipe(
        tap((query) => {
          patchState(store, { searchQuery: query, currentPage: 0 });
          store.loadHistory(0);
        })
      );
    })
  }))
)
