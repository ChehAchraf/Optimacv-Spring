import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { inject } from '@angular/core';
import { CompanyService } from '../service/company/company.service';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { switchMap, tap, timer, exhaustMap, takeWhile } from 'rxjs';
import { tapResponse } from '@ngrx/operators';
import { toast } from 'ngx-sonner';
import { HttpErrorResponse } from '@angular/common/http';

export interface CandidateResponseDTO {
  id: string;
  originalFileName: string;
  matchScore: number | null;
  aiFeedback: string | null;
  uploadedAt: string;
}

export interface JobTargetResponseDTO {
  id: string;
  title: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

type CompanyState = {
  isUploading: boolean;
  uploadSuccessMsg: string | null;
  candidates: CandidateResponseDTO[];
  jobs: JobTargetResponseDTO[];
  currentPage: number;
  pageSize: number;
  totalPages: number;
  totalElements: number;
}

const initialState: CompanyState = {
  isUploading: false,
  uploadSuccessMsg: null,
  candidates: [],
  jobs: [],
  currentPage: 0,
  pageSize: 10,
  totalPages: 0,
  totalElements: 0
}

export const CompanyStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store) => {
    const companyService = inject(CompanyService);

    return {
      uploadCandidates: rxMethod<{ jobId: string, files: File[] }>((request$) => {
        return request$.pipe(
          tap(() => {
            patchState(store, { isUploading: true, uploadSuccessMsg: null });
          }),
          exhaustMap((request) => {
            const toastId = toast.loading("Uploading candidates for AI analysis...");
            return companyService.bulkUploadResumes(request.jobId, request.files).pipe(
              tapResponse({
                next: () => {
                  toast.success("Bulk upload successful! AI is processing in the background.", { id: toastId });
                  patchState(store, {
                    isUploading: false,
                    uploadSuccessMsg: "Background processing started. Check back soon for results."
                  });
                },
                error: (error: HttpErrorResponse) => {
                  console.error('Bulk upload failed', error);
                  toast.error("Failed to upload candidates. Please try again.", { id: toastId });
                  patchState(store, { isUploading: false });
                }
              })
            );
          })
        );
      }),

      pollCandidates: rxMethod<string>((jobId$) => {
        return jobId$.pipe(
          switchMap((jobId) => {
            if (!jobId) return [];
            return timer(0, 5000).pipe(
              switchMap(() => companyService.getCandidates(jobId, store.currentPage(), store.pageSize()).pipe(
                tapResponse({
                  next: (response: PageResponse<CandidateResponseDTO>) => {
                    patchState(store, {
                      candidates: response.content || [],
                      totalPages: response.totalPages || 0,
                      totalElements: response.totalElements || 0
                    });
                  },
                  error: (error: HttpErrorResponse) => {
                    console.error("Failed to poll candidates", error);
                  }
                })
              )),
              takeWhile((response) =>
                (response.content || []).some((c: CandidateResponseDTO) => c.matchScore === null),
                true // CRITICAL: Emits the final response right before completing
              )
            );
          })
        );
      }),

      changePage(page: number) {
        patchState(store, { currentPage: page });
      },

      deleteCandidate: rxMethod<{ candidateId: string, jobId: string }>((request$) => {
        return request$.pipe(
          exhaustMap((req) => {
            const toastId = toast.loading("Deleting candidate...");
            return companyService.deleteCandidate(req.candidateId).pipe(
              switchMap(() => {
                toast.success("Candidate deleted successfully", { id: toastId });
                return companyService.getCandidates(req.jobId, store.currentPage(), store.pageSize()).pipe(
                  tapResponse({
                    next: (response: PageResponse<CandidateResponseDTO>) => {
                      patchState(store, {
                        candidates: response.content || [],
                        totalPages: response.totalPages || 0,
                        totalElements: response.totalElements || 0
                      });
                    },
                    error: (error: HttpErrorResponse) => {
                      console.error("Failed to refresh candidates", error);
                    }
                  })
                );
              }),
              tapResponse({
                next: () => { },
                error: (error: HttpErrorResponse) => {
                  console.error("Failed to delete candidate", error);
                  toast.error("Failed to delete candidate.", { id: toastId });
                }
              })
            );
          })
        );
      }),

      loadCompanyJobs: rxMethod<void>((trigger$) => {
        return trigger$.pipe(
          switchMap(() => companyService.getCompanyJobs().pipe(
            tapResponse({
              next: (response) => {
                console.log("from load comapny jobs", response)
                patchState(store, { jobs: response.content || [] });
              },
              error: (error: HttpErrorResponse) => {
                console.error("Failed to load jobs", error);
                toast.error("Failed to load your job targets.");
              }
            })
          ))
        );
      })
    };
  })
);
