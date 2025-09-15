import { useQuery } from "@tanstack/react-query"
import { getLanguages, type Language } from "@/api/languages"

export function useLanguagesQuery(options?: { refetchInterval?: number }) {
  return useQuery<Language[]>({
    queryKey: ["languages"],
    queryFn: getLanguages,
    refetchInterval: options?.refetchInterval ?? 5 * 60 * 1000, // 5 minutes
    staleTime: 60 * 1000, // 1 minute
  })
}

