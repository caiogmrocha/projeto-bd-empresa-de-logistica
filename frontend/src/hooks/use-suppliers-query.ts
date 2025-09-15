import { useQuery } from "@tanstack/react-query"
import { getSuppliers, type Supplier } from "@/api/suppliers"

export function useSuppliersQuery(options?: { refetchInterval?: number }) {
  return useQuery<Supplier[]>({
    queryKey: ["suppliers"],
    // Query function must match React Query's signature; we ignore the context and just fetch.
    queryFn: async () => {
      const page = await getSuppliers({ page: 0, size: 100, sortBy: "id", direction: "asc" })
      return page.content
    },
    refetchInterval: options?.refetchInterval ?? 2 * 60 * 1000, // 2 minutes
    staleTime: 30 * 1000, // 30 seconds
  })
}
