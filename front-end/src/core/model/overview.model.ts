export interface IOverview {
  totalTargets: number
  analysesDone: number
  averageScore: number
  recentTargets: IRecentTarget[]
}

export interface IRecentTarget {
  id: string
  title: string
  company: string
  createdAt: string
}
