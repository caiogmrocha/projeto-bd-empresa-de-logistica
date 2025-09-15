import * as React from "react"
import { Link } from "react-router"
import { ArrowUpDown } from "lucide-react"
import { useQuery } from "@tanstack/react-query"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { getCompanies, type CompaniesPage, type Company } from "@/api/companies"
import { usePaginationSearchState } from "@/hooks/use-pagination-search-state"
import { useDebounced } from "@/hooks/use-debounced"

export function CompaniesListPage() {
  const { page, limit, search, setState } = usePaginationSearchState({paramKeys: {search: 'tradeName'}});
  const [searchInput, setSearchInput] = React.useState(search);
  const [direction, setDirection] = React.useState<"asc" | "desc">("asc");
  const debouncedSearch = useDebounced(searchInput, 400);

  React.useEffect(() => {
    setSearchInput(search);
  }, [search]);

  const query = useQuery<CompaniesPage>({
    queryKey: ["companies", page, limit, debouncedSearch, direction],
    queryFn: () =>
      getCompanies({
        page: page - 1,
        size: limit,
        tradeName: debouncedSearch,
        sortBy: "tradeName",
        direction,
      }),
  });

  const items = query.data?.content ?? [];
  const total = query.data?.totalElements ?? 0;
  const totalPages = Math.max(1, Math.ceil(total / limit));

  return (
    <div className="w-full">
      <div className="flex items-center gap-2 py-4">
        <Input
          placeholder="Buscar empresas..."
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === "Enter") setState({ page: 1, search: e.currentTarget.value });
          }}
          className="max-w-sm"
        />
        <Button
          variant="secondary"
          onClick={() => setState({ page: 1, search: searchInput })}
        >
          Buscar
        </Button>
        <Button asChild variant="outline" className="ml-auto">
          <Link to="/companies/create">Nova empresa</Link>
        </Button>
      </div>

      <div className="overflow-hidden rounded-md border">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>ID</TableHead>
              <TableHead>
                <Button
                  variant="ghost"
                  onClick={() => setDirection(direction === "asc" ? "desc" : "asc")}
                >
                  Nome Fantasia
                  <ArrowUpDown className="ml-2 h-4 w-4" />
                </Button>
              </TableHead>
              <TableHead>Razão Social</TableHead>
              <TableHead>CNPJ</TableHead>
              <TableHead className="text-right">Ações</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {items.length ? (
              items.map((company: Company) => (
                <TableRow key={company.id}>
                  <TableCell>{company.id}</TableCell>
                  <TableCell>{company.tradeName}</TableCell>
                  <TableCell>{company.legalName}</TableCell>
                  <TableCell>{company.cnpj}</TableCell>
                  <TableCell className="text-right space-x-2">
                    <Button asChild size="sm" variant="secondary">
                      <Link to={`/companies/${company.id}`}>Detalhes</Link>
                    </Button>
                    <Button asChild size="sm" variant="outline">
                      <Link to={`/companies/${company.id}/edit`}>Editar</Link>
                    </Button>
                    <Button asChild size="sm" variant="destructive">
                      <Link to={`/companies/${company.id}/delete`}>Excluir</Link>
                    </Button>
                  </TableCell>
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={5} className="h-24 text-center">
                  {query.isLoading ? "Carregando..." : "Nenhum resultado."}
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>

      <div className="flex items-center justify-between py-4 text-sm text-muted-foreground">
        <div>Total: {total}</div>
        <div className="flex items-center gap-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => setState({ page: Math.max(1, page - 1) })}
            disabled={page <= 1}
          >
            Anterior
          </Button>
          <span>
            Página {page} de {totalPages}
          </span>
          <Button
            variant="outline"
            size="sm"
            onClick={() => setState({ page: Math.min(totalPages, page + 1) })}
            disabled={page >= totalPages}
          >
            Próxima
          </Button>
        </div>
      </div>
    </div>
  )
}