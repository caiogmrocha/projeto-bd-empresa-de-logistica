import * as React from "react"
import {
  type ColumnDef,
  type SortingState,
  type ColumnFiltersState,
  type VisibilityState,
  flexRender,
  getCoreRowModel,
  getSortedRowModel,
  getFilteredRowModel,
  useReactTable,
} from "@tanstack/react-table"
import { ArrowUpDown, ChevronDown } from "lucide-react"

import { Button } from "@/components/ui/button"
import { Checkbox } from "@/components/ui/checkbox"
import {
  DropdownMenu,
  DropdownMenuTrigger,
  DropdownMenuContent,
  DropdownMenuCheckboxItem,
} from "@/components/ui/dropdown-menu"
import { Input } from "@/components/ui/input"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Link } from "react-router"
import { getWarehouses, type Warehouse } from "@/api/warehouses"

const columns: ColumnDef<Warehouse>[] = [
  {
    id: "select",
    header: ({ table }) => (
      <Checkbox
        checked={table.getIsAllPageRowsSelected()}
        onCheckedChange={(value) => table.toggleAllPageRowsSelected(!!value)}
        aria-label="Select all"
      />
    ),
    cell: ({ row }) => (
      <Checkbox
        checked={row.getIsSelected()}
        onCheckedChange={(value) => row.toggleSelected(!!value)}
        aria-label="Select row"
      />
    ),
    enableSorting: false,
    enableHiding: false,
  },
  {
    accessorKey: "name",
    header: ({ column }) => (
      <Button
        variant="ghost"
        onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
      >
        Nome <ArrowUpDown />
      </Button>
    ),
    cell: ({ row }) => <div>{row.getValue("name")}</div>,
  },
  {
    accessorFn: (row) => row.address.country,
    id: "country",
    header: ({ column }) => (
      <Button
        variant="ghost"
        onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
      >
        País <ArrowUpDown />
      </Button>
    ),
    cell: ({ row }) => <div>{row.getValue("country")}</div>,
  },
  {
    accessorFn: (row) => row.address.state,
    id: "state",
    header: ({ column }) => (
      <Button
        variant="ghost"
        onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
      >
        Estado <ArrowUpDown />
      </Button>
    ),
    cell: ({ row }) => <div>{row.getValue("state")}</div>,
  },
  {
    accessorFn: (row) => row.address.city,
    id: "city",
    header: ({ column }) => (
      <Button
        variant="ghost"
        onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
      >
        Cidade <ArrowUpDown />
      </Button>
    ),
    cell: ({ row }) => <div>{row.getValue("city")}</div>,
  },
  {
    id: "actions",
    header: "Opções",
    cell: ({ row }) => {
      const warehouse = row.original
      return (
        <div className="flex gap-2">
          <Button asChild size="sm" variant="outline">
            <Link to={`/warehouses/${warehouse.id}/edit`}>Editar</Link>
          </Button>
          <Button asChild size="sm" variant="destructive">
            <Link to={`/warehouses/${warehouse.id}/delete`}>Excluir</Link>
          </Button>
        </div>
      )
    },
    enableHiding: false,
  },
]

export function WarehousesListPage() {
  const [data, setData] = React.useState<Warehouse[]>([])
  const [loading, setLoading] = React.useState(true)
  const [error, setError] = React.useState<string | null>(null)
  const [sorting, setSorting] = React.useState<SortingState>([])
  const [columnFilters, setColumnFilters] = React.useState<ColumnFiltersState>([])
  const [columnVisibility, setColumnVisibility] = React.useState<VisibilityState>({})
  const [rowSelection, setRowSelection] = React.useState({})
  const [page, setPage] = React.useState(0)
  const [limit] = React.useState(10)
  const [total, setTotal] = React.useState(0)
  const [searchInput, setSearchInput] = React.useState("")
  const [search, setSearch] = React.useState("")

  // Debounce para busca (500ms)
  React.useEffect(() => {
    const handler = setTimeout(() => {
      setSearch(searchInput)
      setPage(0) 
    }, 500)

    return () => clearTimeout(handler)
  }, [searchInput])

  const fetchData = React.useCallback(() => {
    setLoading(true)
    getWarehouses({ page, limit, search })
      .then((result) => {
        setData(result.content)
        setTotal(result.total)
        setLoading(false)
      })
      .catch((err) => {
        setError(err.message)
        setData([])
        setLoading(false)
      })
  }, [page, limit, search])

  React.useEffect(() => {
    fetchData()
  }, [fetchData])

  const table = useReactTable({
    data,
    columns,
    manualPagination: true,
    pageCount: Math.ceil(total / limit),
    onSortingChange: setSorting,
    onColumnFiltersChange: setColumnFilters,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    onColumnVisibilityChange: setColumnVisibility,
    onRowSelectionChange: setRowSelection,
    state: {
      sorting,
      columnFilters,
      columnVisibility,
      rowSelection,
      pagination: { pageIndex: page, pageSize: limit },
    },
    onPaginationChange: (updater) => {
      const newPage =
        typeof updater === "function"
          ? updater({ pageIndex: page, pageSize: limit }).pageIndex
          : updater.pageIndex
      setPage(newPage)
    },
  })

  return (
    <div className="w-full">
      <div className="flex items-center py-4">
        <Input
          placeholder="Filtrar por nome..."
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
          className="max-w-sm"
        />
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="outline" className="ml-auto">
              Colunas <ChevronDown />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            {table
              .getAllColumns()
              .filter((column) => column.getCanHide())
              .map((column) => (
                <DropdownMenuCheckboxItem
                  key={column.id}
                  className="capitalize"
                  checked={column.getIsVisible()}
                  onCheckedChange={(value) => column.toggleVisibility(!!value)}
                >
                  {column.id}
                </DropdownMenuCheckboxItem>
              ))}
          </DropdownMenuContent>
        </DropdownMenu>
      </div>

      <div className="overflow-hidden rounded-md border">
        <Table>
          <TableHeader>
            {table.getHeaderGroups().map((headerGroup) => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map((header) => (
                  <TableHead key={header.id}>
                    {header.isPlaceholder
                      ? null
                      : flexRender(header.column.columnDef.header, header.getContext())}
                  </TableHead>
                ))}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {table.getRowModel().rows.length ? (
              table.getRowModel().rows.map((row) => (
                <TableRow key={row.id} data-state={row.getIsSelected() && "selected"}>
                  {row.getVisibleCells().map((cell) => (
                    <TableCell key={cell.id}>
                      {flexRender(cell.column.columnDef.cell, cell.getContext())}
                    </TableCell>
                  ))}
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={columns.length} className="h-24 text-center">
                  Nenhum resultado.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>

      <div className="flex items-center justify-end space-x-2 py-4">
        <div className="text-muted-foreground flex-1 text-sm">
          {table.getFilteredSelectedRowModel().rows.length} of{" "}
          {table.getFilteredRowModel().rows.length} row(s) selected.
        </div>
        <div className="space-x-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => table.previousPage()}
            disabled={page === 0}
          >
            Anterior
          </Button>
          <Button
            variant="outline"
            size="sm"
            onClick={() => table.nextPage()}
            disabled={page >= Math.ceil(total / limit) - 1}
          >
            Próximo
          </Button>
        </div>
      </div>
    </div>
  )
}
