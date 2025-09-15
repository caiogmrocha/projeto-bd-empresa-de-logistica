import { useMemo } from "react"
import { useInfiniteQuery, type UseInfiniteQueryResult } from "@tanstack/react-query"
import { getCategories, type Category } from "@/api/categories"
import type { PageResponse } from "@/api/pagination"

export type UseCategoriesResult = Omit<UseInfiniteQueryResult<PageResponse<Category>>, 'data'> & { data: Category[] }

export function useCategoriesQuery(options?: { limit?: number; search?: string; refetchInterval?: number }): UseCategoriesResult {
  const limit = options?.limit ?? 50
  const search = options?.search ?? ''

  const query = useInfiniteQuery<PageResponse<Category>>({
    queryKey: ["categories", limit, search],
    initialPageParam: 1, // 1-based
    queryFn: ({ pageParam }) => getCategories({ page: pageParam as number, limit, search }),
    getNextPageParam: (lastPage) => {
      if (lastPage.last) return undefined
      return lastPage.number + 2 // Spring returns 0-based; convert to 1-based next
    },
    refetchInterval: options?.refetchInterval ?? 5 * 60 * 1000,
    staleTime: 60 * 1000,
  })

  const data = useMemo(() => query.data?.pages.flatMap((p) => p.content) ?? [], [query.data])

  // Expose a familiar shape where data is already flattened as Category[]
  return { ...(query as any), data }
}
