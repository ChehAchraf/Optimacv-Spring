# Analysis Module Refactoring: Detail Endpoint Fix, History Endpoint, and DTO Enrichment

The [CvAnalysisResult](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/entities/CvAnalysisResult.java#9-35) entity is missing `userId` and `jobId` fields, making it impossible to filter analyses by the authenticated user or display human-readable job/resume names. The detail endpoint fetches by `resumeId` instead of `analysisId`, which is incorrect in a one-to-many (Resume → Analyses) model. We also need a paginated history endpoint for the frontend data table.

## User Review Required

> [!IMPORTANT]
> Adding `userId` and `jobId` columns to the `cv_analysis_results` table is a **schema change**. Existing rows in the DB will have `NULL` for these new columns. You may need to either:
> - Truncate the table in dev, or
> - Run a one-time migration to backfill these values.

> [!WARNING]
> The existing repository queries ([countAnalysesByUserId](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/repository/CvAnalysisResultRepository.java#17-25), [findAverageScoreByUserId](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/repository/CvAnalysisResultRepository.java#26-34)) use cross-module JPQL that references the [Resume](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/resume/entites/Resume.java#23-59) entity directly. This plan refactors them to query the new `userId` column on [CvAnalysisResult](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/entities/CvAnalysisResult.java#9-35) instead, which is cleaner and avoids the cross-module dependency — **but will return 0 for old rows** where `userId` is `NULL`.

---

## Proposed Changes

### Entity Layer

#### [MODIFY] [CvAnalysisResult.java](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/entities/CvAnalysisResult.java)
- Add `userId` (`UUID`, nullable — for backward compat) column
- Add `jobId` (`UUID`, nullable) column

---

### Internal Module APIs (Resume & Job)

#### [MODIFY] [ResumeApi.java](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/resume/api/ResumeApi.java)
- Add `String getResumeFileName(UUID resumeId)` method

#### [MODIFY] [ResumeApiImpl.java](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/resume/service/ResumeApiImpl.java)
- Implement `getResumeFileName()` — delegates to [ResumeService](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/resume/service/ResumeService.java#13-27)

#### [MODIFY] [ResumeService.java](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/resume/service/ResumeService.java)
- Add `String getResumeFileName(UUID resumeId)` method signature

#### [MODIFY] [ResumeServiceImpl.java](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/resume/service/ResumeServiceImpl.java)
- Implement `getResumeFileName()` — fetch resume by ID, return `fileName`

#### [MODIFY] [JobApi.java](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/job/api/JobApi.java)
- Add `String getJobTitle(UUID jobId)` method

#### [MODIFY] [JobApiImpl.java](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/job/service/JobApiImpl.java)
- Implement `getJobTitle()` — delegates to [JobTargetService](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/job/service/JobTargetService.java#12-32)

#### [MODIFY] [JobTargetService.java](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/job/service/JobTargetService.java)
- Add `String getJobTitle(UUID jobId)` method signature

#### [MODIFY] [JobTargetServiceImpl.java](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/job/service/JobTargetServiceImpl.java)
- Implement `getJobTitle()` — fetch job by ID, return `title`

---

### Repository

#### [MODIFY] [CvAnalysisResultRepository.java](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/repository/CvAnalysisResultRepository.java)
- Add `Optional<CvAnalysisResult> findByIdAndUserId(UUID id, UUID userId)`
- Add `Page<CvAnalysisResult> findAllByUserIdOrderByAnalyzedAtDesc(UUID userId, Pageable pageable)`
- Simplify [countAnalysesByUserId](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/repository/CvAnalysisResultRepository.java#17-25) → `long countByUserId(UUID userId)` (uses new `userId` column)
- Simplify [findAverageScoreByUserId](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/repository/CvAnalysisResultRepository.java#26-34) removal or keep as-is (currently returns `0.0` anyway)

---

### DTOs & Mapper

#### [NEW] [CvAnalysisHistoryDTO.java](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/dto/CvAnalysisHistoryDTO.java)
- Record with: `UUID analysisId`, `LocalDateTime analyzedAt`, `String resumeFileName`, `String jobTitle`, `String feedback`

#### [MODIFY] [CvAnalysisMapper.java](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/mapper/CvAnalysisMapper.java)
- Add `toHistoryDTO(CvAnalysisResult, String resumeFileName, String jobTitle)` default method

---

### Service

#### [MODIFY] [CvAnalysisService.java](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/service/CvAnalysisService.java)
- Modify [startAnalysis()](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/service/CvAnalysisService.java#188-204) to set `userId` and `jobId` on the entity before saving
- Modify [onCvUploaded()](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/service/CvAnalysisService.java#35-63) to accept `userId` if available (from published event), or leave `null`
- Replace [getAnalysisByResumeId(UUID)](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/service/CvAnalysisService.java#129-133) with `getAnalysisById(UUID analysisId, UUID userId)` — fetches by PK + userId ownership check
- Add `Page<CvAnalysisHistoryDTO> getAnalysisHistory(UUID userId, Pageable pageable)` — queries by `userId`, enriches each result using `ResumeApi.getResumeFileName()` and `JobApi.getJobTitle()`

#### [MODIFY] [AnalysisModuleApiImpl.java](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/service/AnalysisModuleApiImpl.java)
- Update [getTotalAnalysesByUser()](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/api/AnalysisModuleApi.java#6-7) to use the new simplified repository query

---

### Controller

#### [MODIFY] [CvAnalysisController.java](file:///c:/Users/shaor/Desktop/optimacv-spring/back-end/src/main/java/com/valhala/optimacvspring/analysis/controller/CvAnalysisController.java)
- Change `GET /{resumeId}` → `GET /{analysisId}` with `@AuthenticationPrincipal`
- Add `GET /history` accepting `Pageable` + `@AuthenticationPrincipal`

---

## Verification Plan

### Automated Tests
- Run `mvn compile -f c:\Users\shaor\Desktop\optimacv-spring\back-end\pom.xml` to confirm everything compiles with no errors.

### Manual Verification
- After running the application, test the new endpoints via Swagger UI or Postman:
  1. `POST /api/v1/analysis/start` — verify analysis is created with `userId` and `jobId` persisted
  2. `GET /api/v1/analysis/{analysisId}` — verify it returns the correct analysis for the authenticated user
  3. `GET /api/v1/analysis/history?page=0&size=10` — verify paginated results with enriched DTOs
