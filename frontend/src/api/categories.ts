import type { PageResponse } from '@/api/pagination'

export interface Category { id: number; name: string }

export type CategorySortKey = 'id' | 'name' | 'createdAt' | 'updatedAt'
export type SortOrder = 'asc' | 'desc'

export interface GetCategoriesParams {
  page?: number // 1-based
  limit?: number
  search?: string
  sortBy?: CategorySortKey
  order?: SortOrder
}

const API_BASE_URL = (typeof import.meta !== 'undefined' && (import.meta as any).env?.VITE_API_URL) || process.env.VITE_API_URL || process.env.API_URL || ''

export async function getCategories(params: GetCategoriesParams = {}): Promise<PageResponse<Category>> {
  const page = params.page ?? 1
  const size = params.limit ?? 10
  const search = params.search ?? ''
  const sortBy = params.sortBy
  const order: SortOrder | undefined = params.order

  const q = new URLSearchParams()
  // Spring Data pageable is 0-based
  q.set('page', String(Math.max(0, page - 1)))
  q.set('size', String(size))
  if (search) q.set('search', search)
  if (sortBy && order) q.set('sort', `${sortBy},${order}`)

  const base = API_BASE_URL?.replace(/\/$/, '') || ''
  const url = `${base}/api/categories?${q.toString()}`

  const res = await fetch(url)
  if (!res.ok) {
    throw new Error(`Failed to fetch categories: ${res.status} ${res.statusText}`)
  }
  return (await res.json()) as PageResponse<Category>
}
