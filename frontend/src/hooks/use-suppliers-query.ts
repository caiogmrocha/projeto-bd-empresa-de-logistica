import { useQuery } from "@tanstack/react-query"
import { getSuppliers, type Supplier } from "@/api/suppliers"

export function useSuppliersQuery(options?: { refetchInterval?: number }) {
  return useQuery<Supplier[]>({
    queryKey: ["suppliers"],
    queryFn: getSuppliers,
    refetchInterval: options?.refetchInterval ?? 2 * 60 * 1000, // 2 minutes
    staleTime: 30 * 1000, // 30 seconds
  })
}

