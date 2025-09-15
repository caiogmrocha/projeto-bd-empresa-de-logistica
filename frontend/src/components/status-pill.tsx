import * as React from "react"
import { ProductStatus } from "@/api/products"

export type StatusPillProps = {
  status: ProductStatus | string
  className?: string
}

function labelFor(status: string) {
  switch (status) {
    case ProductStatus.TESTED:
      return "Testado"
    case ProductStatus.RETURNED:
      return "Devolvido"
    default:
      return status
  }
}

function classesFor(status: string) {
  // Base styles for pill
  const base = "inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium"
  switch (status) {
    case ProductStatus.TESTED:
      return `${base} bg-emerald-100 text-emerald-800 dark:bg-emerald-900/30 dark:text-emerald-300`
    case ProductStatus.RETURNED:
      return `${base} bg-amber-100 text-amber-800 dark:bg-amber-900/30 dark:text-amber-300`
    default:
      return `${base} bg-zinc-100 text-zinc-800 dark:bg-zinc-800 dark:text-zinc-200`
  }
}

export const StatusPill: React.FC<StatusPillProps> = ({ status, className }) => {
  const s = String(status)
  return (
    <span className={`${classesFor(s)} ${className ?? ""}`.trim()}>
      {labelFor(s)}
    </span>
  )
}
