export type ActionPlanItem = {
  title: string;
  description: string;
};

export type FullAnalysis = {
  score: number;
  verdict: string;
  matchingSkills: string[];
  missingKeywords: string[];
  actionPlan: ActionPlanItem[];
};

export type MockAnalysis = {
  id: string;
  resumeName?: string;
  jobTitle: string;
  analyzedAt: string;
  fullAnalysis: FullAnalysis;
};

