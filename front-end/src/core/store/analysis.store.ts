import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {IAnalyseRequest, IAnalysisHistory} from '../model/analyse.model';
import {inject} from '@angular/core';
import {AnalyseService} from '../service/analyse/analyse-service';
import {rxMethod} from '@ngrx/signals/rxjs-interop';
import {exhaustMap, switchMap, tap} from 'rxjs';
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
              tapResponse({
                next : (response)=>{
                  toast.success("the analysis is done, you can now check it!",{id:analysisId})

                  patchState(store,(state)=>({
                    analyses : [response,...state.analyses]
                  }))
                  router.navigate(['/analysis', response.analysisId]);
                },
                error : (error)=>{
                  toast.error("error" , {id:analysisId})
                }
              })
            )
          })
        )
      }),
      loadHistory: rxMethod<number>((page$) => {
        return page$.pipe(
          tap(() => patchState(store, { isLoading: true, error: null })),
          exhaustMap((page) => {
            const historyId = toast.loading("data is loading...")
            return analysisService.getHistory(page, 10).pipe(
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
                  toast.success("done!" , {id:historyId})
                },
                error: (err) => {
                  patchState(store, { error: 'there must be an error. try again later', isLoading: false });
                  toast.error("there must be an error, please comeback later", {id:historyId})
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
  })
)
