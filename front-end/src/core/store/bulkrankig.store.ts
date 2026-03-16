import {signalStore, withMethods, withState} from '@ngrx/signals';
import {rxMethod} from '@ngrx/signals/rxjs-interop';

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

  withMethods(
    
  )

)
