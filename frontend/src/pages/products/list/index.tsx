import * as React from "react"
import { Link } from "react-router"
import { useQuery } from "@tanstack/react-query"
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
import { getProducts, type ProductsPage, type Product } from "@/api/products"
import { usePaginationSearchState } from "@/hooks/use-pagination-search-state"
import { useDebounced } from "@/hooks/use-debounced"
import { StatusPill } from "@/components/status-pill"

function pickProductName(p: Product): string {
  // Preferir pt-BR, depois en, senão o primeiro disponível
  if (p.names["pt-BR"]) return p.names["pt-BR"]
  if (p.names["en"]) return p.names["en"]
  const first = Object.values(p.names)[0]
  return first || "(sem nome)"
}

export function ProductsListPage() {
  const { page, limit, search, setState } = usePaginationSearchState()
  const [searchInput, setSearchInput] = React.useState(search)
  const debouncedSearch = useDebounced(searchInput, 400)

  React.useEffect(() => {
    // Mantém o input sincronizado quando a URL muda externamente
    setSearchInput(search)
  }, [search])

  const query = useQuery<ProductsPage>({
    queryKey: ["products", page, limit, debouncedSearch],
    queryFn: () => getProducts({ page, limit, search: debouncedSearch }),
  })

  const items = query.data?.items ?? []
  const total = query.data?.total ?? 0
  const totalPages = Math.max(1, Math.ceil(total / limit))

  return (
    <div className="w-full">
      <div className="flex items-center gap-2 py-4">
        <Input
          placeholder="Buscar produtos..."
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === 'Enter') setState({ page: 1, search: (e.currentTarget as HTMLInputElement).value })
          }}
          className="max-w-sm"
        />
        <Button variant="secondary" onClick={() => setState({ page: 1, search: searchInput })}>Buscar</Button>
        <div className="ml-auto flex items-center gap-2">
          <Button variant="outline" onClick={() => setState({ page: Math.max(1, page - 1) })} disabled={page <= 1}>Anterior</Button>
          <span className="text-sm text-muted-foreground">Página {page} / {totalPages}</span>
          <Button variant="outline" onClick={() => setState({ page: Math.min(totalPages, page + 1) })} disabled={page >= totalPages}>Próxima</Button>
        </div>
      </div>

      <div className="overflow-hidden rounded-md border">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead className="w-[80px]">ID</TableHead>
              <TableHead>Nome</TableHead>
              <TableHead>Status</TableHead>
              <TableHead className="text-right">Preço mín.</TableHead>
              <TableHead className="text-right">Estoque</TableHead>
              <TableHead className="text-right">Ações</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {items.length ? (
              items.map((p) => (
                <TableRow key={p.id}>
                  <TableCell>{p.id}</TableCell>
                  <TableCell>{pickProductName(p)}</TableCell>
                  <TableCell><StatusPill status={p.status} /></TableCell>
                  <TableCell className="text-right">{p.minimumSalePrice?.toLocaleString(undefined, { style: 'currency', currency: 'BRL' })}</TableCell>
                  <TableCell className="text-right">{p.stock}</TableCell>
                  <TableCell className="text-right space-x-2">
                    <Button asChild size="sm" variant="outline">
                      <Link to={`/products/${p.id}/edit`}>Editar</Link>
                    </Button>
                    <Button asChild size="sm" variant="destructive">
                      <Link to={`/products/${p.id}/delete`}>Excluir</Link>
                    </Button>
                  </TableCell>
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={6} className="h-24 text-center">
                  {query.isLoading ? 'Carregando...' : 'Nenhum resultado.'}
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>

      <div className="flex items-center justify-between py-4 text-sm text-muted-foreground">
        <div>Total: {total}</div>
        <div>Página {page} de {totalPages}</div>
      </div>
    </div>
  )
}
