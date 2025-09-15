import { useQuery } from "@tanstack/react-query"
import { getWarehouses } from "@/api/warehouses"

export function useWarehousesQuery(options?: { refetchInterval?: number; limit?: number; search?: string }) {
  const limit = options?.limit ?? 100
  const search = options?.search ?? ''

  return useQuery({
    queryKey: ["warehouses", limit, search],
    // Compatível com retorno paginado (PageResponse<Warehouse> ou shape similar)
    queryFn: async () => {
      const page = await getWarehouses({ page: 0, limit, search })
      return page
    },
    refetchInterval: options?.refetchInterval ?? 2 * 60 * 1000, // 2 minutes
    staleTime: 30 * 1000, // 30 seconds
  })
}
