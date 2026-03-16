import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {rxMethod} from '@ngrx/signals/rxjs-interop';
import {inject} from '@angular/core';
import {BulkRankingService} from '../service/bulk-ranking/bulk-ranking-service';
import {exhaustMap, tap} from 'rxjs';
import {tapResponse} from '@ngrx/operators';
import {toast} from 'ngx-sonner';
import {HttpErrorResponse} from '@angular/common/http';

export type RankedCandidate = {
  resumeId : string,
  candidateName : string ,
  rank : number,
  matchScore : number ,
  reason : string ,
  missingSkills : string[]
}

export type BulkRankingState = {
  results : RankedCandidate[],
  isAnalyzing : boolean,
  error : string | null
}

export const initialState: BulkRankingState = {
  results: [],
  isAnalyzing: false,
  error: null
};

export const BulkrankigStore = signalStore(
  {providedIn : 'root'},
  withState(initialState),

  withMethods((store)=>{
    const bulkRankService = inject(BulkRankingService)

    return {
      bulkRank : rxMethod<{jobId: string , resumeIds : string[]}>((data$)=>{
        return data$.pipe(
          tap(()=> patchState(store , {isAnalyzing : true, error : null})),
          exhaustMap((data$)=>{
            return bulkRankService.startBulkAnalyze(data$).pipe(
              tapResponse({
                next: (response) => {
                  console.log(response);
                  toast.success("the bulk ranking works");
                  patchState(
                    store ,
                    {
                      isAnalyzing: false ,
                      results : response
                    }
                  )
                },
                error: (error: HttpErrorResponse) => {
                  console.log(error);
                  toast.error("error , please try again later");
                  patchState(
                    store ,
                    {
                      isAnalyzing : false ,
                      error : error.message
                    }
                  )
                }
              })
            );
          })
        )
      })
    }
    }
  )

)
