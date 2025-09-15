import { useSearchParams } from "react-router"

export type PaginationSearchState = {
  page: number
  limit: number
  search: string
}

export type UsePaginationOptions = {
  paramKeys?: Partial<Record<keyof PaginationSearchState, string>>
  defaults?: Partial<PaginationSearchState>
}

// Generic hook to manage URL-based pagination/search state across pages
export function usePaginationSearchState(options: UsePaginationOptions = {}) {
  const { paramKeys, defaults } = options
  const pageKey = paramKeys?.page ?? "page"
  const limitKey = paramKeys?.limit ?? "limit"
  const searchKey = paramKeys?.search ?? "search"

  const [params, setParams] = useSearchParams()
  const page = parseInt(params.get(pageKey) || String(defaults?.page ?? 1), 10)
  const limit = parseInt(params.get(limitKey) || String(defaults?.limit ?? 10), 10)
  const search = params.get(searchKey) ?? String(defaults?.search ?? "")

  const setState = (next: Partial<PaginationSearchState>) => {
    const newParams = new URLSearchParams(params)
    if (next.page !== undefined) newParams.set(pageKey, String(next.page))
    if (next.limit !== undefined) newParams.set(limitKey, String(next.limit))
    if (next.search !== undefined) {
      if (next.search) newParams.set(searchKey, next.search)
      else newParams.delete(searchKey)
    }
    setParams(newParams, { replace: true })
  }

  return { page, limit, search, setState }
}

