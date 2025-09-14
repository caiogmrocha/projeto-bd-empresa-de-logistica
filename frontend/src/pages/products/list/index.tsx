import * as React from "react"
import { Link } from "react-router"
import { useInfiniteQuery, useMutation, useQueryClient } from "@tanstack/react-query"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { getProducts, deleteProduct, type Product, type GetProductsParams } from "@/api/products"
import type { PageResponse } from "@/api/pagination"
import { usePaginationSearchState } from "@/hooks/use-pagination-search-state"
import { useDebounced } from "@/hooks/use-debounced"
import { StatusPill } from "@/components/status-pill"
import { toast } from "sonner"

export function ProductsListPage() {
  const { limit, search, setState } = usePaginationSearchState()
  const queryClient = useQueryClient()
  const [searchInput, setSearchInput] = React.useState(search)
  const debouncedSearch = useDebounced(searchInput, 400)

  // Sorting state (local)
  const [sortBy, setSortBy] = React.useState<NonNullable<GetProductsParams['sortBy']>>('id')
  const [order, setOrder] = React.useState<NonNullable<GetProductsParams['order']>>('asc')

  React.useEffect(() => {
    // Mantém o input sincronizado quando a URL muda externamente
    setSearchInput(search)
  }, [search])

  const query = useInfiniteQuery<PageResponse<Product>>({
    queryKey: ["products", limit, debouncedSearch, sortBy, order],
    initialPageParam: 1, // 1-based
    queryFn: ({ pageParam }) => getProducts({ page: pageParam as number, limit, search: debouncedSearch, sortBy, order }),
    getNextPageParam: (lastPage) => {
      // Spring returns 0-based 'number' and 'last' flag
      if (lastPage.last) return undefined
      return lastPage.number + 2 // convert to 1-based next page
    },
  })

  const rows = query.data?.pages.flatMap((p) => p.content) ?? []

  const deleteMutation = useMutation({
    mutationFn: (id: number | string) => deleteProduct(id),
    onSuccess: (_res, idArg) => {
      // Remove o item deletado de todos os caches de produtos (todas as variações de queryKey)
      const queries = queryClient.getQueriesData<PageResponse<Product>>({ queryKey: ["products"] })
      for (const [key, data] of queries) {
        if (!data) continue
        const newPages = data.pages.map((pg) => ({
          ...pg,
          content: pg.content.filter((item) => item.id !== Number(idArg)),
          numberOfElements: pg.content.filter((item) => item.id !== Number(idArg)).length,
          totalElements: Math.max(0, (pg.totalElements ?? 0) - 1),
        }))
        queryClient.setQueryData(key, { ...data, pages: newPages })
      }
      toast.success(`Produto ${idArg} excluído com sucesso`)
    },
    onError: (err: Error) => {
      toast.error(err.message)
    },
  })

  const handleDelete = React.useCallback(async (id: number) => {
    const ok = window.confirm(`Deseja realmente excluir o produto ${id}?`)
    if (!ok) return
    await deleteMutation.mutateAsync(id)
  }, [deleteMutation])
  const firstPage = query.data?.pages[0]
  const total = firstPage?.totalElements ?? 0

  const toggleSort = (key: NonNullable<GetProductsParams['sortBy']>) => {
    if (sortBy === key) {
      setOrder(order === 'asc' ? 'desc' : 'asc')
    } else {
      setSortBy(key)
      setOrder('asc')
    }
  }

  const displayName = React.useCallback((product: Product) => {
    const n = product.names || {}
    const order = ['pt-BR', 'pt', 'en', ...Object.keys(n)]
    for (const key of order) if (n[key]) return n[key]
    return '(sem nome)'
  }, [])

  return (
    <div className="w-full">
      <div className="flex items-center gap-2 py-4">
        <Input
          placeholder="Buscar produtos (id, status, preço)..."
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === 'Enter') setState({ page: 1, search: (e.currentTarget as HTMLInputElement).value })
          }}
          className="max-w-sm"
        />
        <Button variant="secondary" onClick={() => setState({ page: 1, search: searchInput })}>Buscar</Button>
        <div className="ml-auto flex items-center gap-2 text-sm text-muted-foreground">
          <span>Carregados: {rows.length} / {total}</span>
        </div>
      </div>

      <div className="overflow-hidden rounded-md border">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead className="w-[80px]">
                <button className="inline-flex items-center gap-1" onClick={() => toggleSort('id')}>
                  ID {sortBy === 'id' ? (order === 'asc' ? '▲' : '▼') : ''}
                </button>
              </TableHead>
              <TableHead>Nome</TableHead>
              <TableHead>
                <button className="inline-flex items-center gap-1" onClick={() => toggleSort('status')}>
                  Status {sortBy === 'status' ? (order === 'asc' ? '▲' : '▼') : ''}
                </button>
              </TableHead>
              <TableHead className="text-right">
                <button className="inline-flex items-center gap-1" onClick={() => toggleSort('minimumSalePrice')}>
                  Preço mín. {sortBy === 'minimumSalePrice' ? (order === 'asc' ? '▲' : '▼') : ''}
                </button>
              </TableHead>
              <TableHead className="text-right">
                <button className="inline-flex items-center gap-1" onClick={() => toggleSort('createdAt')}>
                  Criado em {sortBy === 'createdAt' ? (order === 'asc' ? '▲' : '▼') : ''}
                </button>
              </TableHead>
              <TableHead className="text-right">Ações</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {rows.length ? (
              rows.map((p) => (
                <TableRow key={p.id}>
                  <TableCell>{p.id}</TableCell>
                  <TableCell className="truncate max-w-[240px]" title={displayName(p)}>{displayName(p)}</TableCell>
                  <TableCell><StatusPill status={p.status} /></TableCell>
                  <TableCell className="text-right">{typeof p.minimumSalePrice === 'number' ? p.minimumSalePrice.toLocaleString(undefined, { style: 'currency', currency: 'BRL' }) : String(p.minimumSalePrice)}</TableCell>
                  <TableCell className="text-right">{new Date(p.createdAt).toLocaleString()}</TableCell>
                  <TableCell className="text-right space-x-2">
                    <Button asChild size="sm" variant="outline">
                      <Link to={`/products/${p.id}/edit`}>Editar</Link>
                    </Button>
                    <Button size="sm" variant="destructive" onClick={() => handleDelete(p.id)} disabled={deleteMutation.isPending}>
                      {deleteMutation.isPending ? 'Excluindo...' : 'Excluir'}
                    </Button>
                  </TableCell>
                </TableRow>
              ))) : (
              <TableRow>
                <TableCell colSpan={5} className="h-24 text-center">
                  {query.isLoading ? 'Carregando...' : 'Nenhum resultado.'}
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>

      <div className="flex items-center justify-between py-4 text-sm text-muted-foreground">
        <div>Total: {total}</div>
        <div>
          <Button
            variant="outline"
            onClick={() => query.fetchNextPage()}
            disabled={!query.hasNextPage || query.isFetchingNextPage}
          >
            {query.isFetchingNextPage ? 'Carregando...' : (query.hasNextPage ? 'Carregar mais' : 'Não há mais resultados')}
          </Button>
        </div>
      </div>
    </div>
  )
}
