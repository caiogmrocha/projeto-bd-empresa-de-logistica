import * as React from "react"
import { X } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Checkbox } from "@/components/ui/checkbox"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"

export type MultiSelectOption = {
  label: string
  value: string
}

export interface MultiSelectProps {
  options: MultiSelectOption[]
  value: string[]
  onChange: (next: string[]) => void
  placeholder?: string
  className?: string
  showClearAll?: boolean
}

export const MultiSelect: React.FC<MultiSelectProps> = ({
  options,
  value,
  onChange,
  placeholder = "Selecionar...",
  className,
  showClearAll = true,
}) => {
  const [open, setOpen] = React.useState(false)
  const [query, setQuery] = React.useState("")

  const selectedSet = React.useMemo(() => new Set(value), [value])

  const filtered = React.useMemo(() => {
    const q = query.trim().toLowerCase()
    if (!q) return options
    return options.filter((o) => o.label.toLowerCase().includes(q))
  }, [options, query])

  const toggle = (v: string) => {
    const next = new Set(selectedSet)
    if (next.has(v)) next.delete(v)
    else next.add(v)
    onChange(Array.from(next))
  }

  const remove = (v: string) => {
    if (!selectedSet.has(v)) return
    const next = Array.from(selectedSet).filter((x) => x !== v)
    onChange(next)
  }

  const clearAll = () => onChange([])

  const buttonLabel = value.length
    ? `${value.length} selecionado${value.length > 1 ? 's' : ''}`
    : placeholder

  return (
    <div className={className}>
      {/* Selected badges */}
      {value.length > 0 && (
        <div className="mb-2 flex flex-wrap gap-2">
          {value.map((v) => {
            const opt = options.find((o) => o.value === v)
            const label = opt?.label ?? v
            return (
              <span key={v} className="inline-flex items-center gap-1 rounded-full bg-zinc-100 px-2.5 py-0.5 text-xs text-zinc-800 dark:bg-zinc-800 dark:text-zinc-100">
                {label}
                <button
                  type="button"
                  className="inline-flex rounded-full p-0.5 hover:bg-zinc-200 dark:hover:bg-zinc-700"
                  onClick={() => remove(v)}
                  aria-label={`Remover ${label}`}
                >
                  <X className="h-3.5 w-3.5" />
                </button>
              </span>
            )
          })}
          {showClearAll && (
            <button type="button" className="text-xs text-blue-600 hover:underline" onClick={clearAll}>
              Limpar tudo
            </button>
          )}
        </div>
      )}

      <Popover open={open} onOpenChange={setOpen}>
        <PopoverTrigger asChild>
          <Button variant="outline" className="w-full justify-between">
            <span className="truncate">{buttonLabel}</span>
            <span className="ml-2 text-xs text-muted-foreground">(multi)</span>
          </Button>
        </PopoverTrigger>
        <PopoverContent align="start" className="w-[min(420px,90vw)] p-2">
          <div className="mb-2">
            <Input
              placeholder="Buscar..."
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              autoFocus
            />
          </div>
          <div className="max-h-64 overflow-auto pr-1">
            {filtered.length ? (
              <ul className="space-y-1">
                {filtered.map((o) => (
                  <li key={o.value}>
                    <label className="flex cursor-pointer items-center gap-2 rounded px-2 py-1 hover:bg-zinc-50 dark:hover:bg-zinc-900">
                      <Checkbox
                        checked={selectedSet.has(o.value)}
                        onCheckedChange={() => toggle(o.value)}
                      />
                      <span className="text-sm">{o.label}</span>
                    </label>
                  </li>
                ))}
              </ul>
            ) : (
              <div className="px-2 py-4 text-sm text-muted-foreground">Sem resultados</div>
            )}
          </div>
        </PopoverContent>
      </Popover>
    </div>
  )
}
