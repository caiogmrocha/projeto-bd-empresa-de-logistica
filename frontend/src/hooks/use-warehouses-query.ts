import { useQuery } from "@tanstack/react-query"
import { getWarehouses, type Warehouse } from "@/api/warehouses"

export function useWarehousesQuery(options?: { refetchInterval?: number }) {
  return useQuery<Warehouse[]>({
    queryKey: ["warehouses"],
    queryFn: getWarehouses,
    refetchInterval: options?.refetchInterval ?? 2 * 60 * 1000, // 2 minutes
    staleTime: 30 * 1000, // 30 seconds
  })
}

