import { useCallback, useEffect, useMemo, useState } from "react"
import { useQuery, useMutation } from "@tanstack/react-query"
import { useParams } from "react-router"
import { z } from "zod"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"

import { getProduct, updateProduct, type ProductDetails, ProductStatus, type UpdateProductRequest } from "@/api/products"
import { type Warehouse } from "@/api/warehouses"
import { type Supplier } from "@/api/suppliers"
import { useLanguagesQuery } from "@/hooks/use-languages-query"
import { useWarehousesQuery } from "@/hooks/use-warehouses-query"
import { useSuppliersQuery } from "@/hooks/use-suppliers-query"

import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { Calendar } from "@/components/ui/calendar"
import { Checkbox } from "@/components/ui/checkbox"
import { toast } from "sonner"

const ProductFormSchema = z.object({
  names: z.record(z.string(), z.string().min(1).max(255)),
  descriptions: z.record(z.string(), z.string().min(1).max(255)),
  warrantyDate: z.date(),
  status: z.enum(ProductStatus),
  minimumSalePrice: z.number().min(0).optional(),
  stock: z.number().min(0).optional(),
  categoriesIds: z.array(z.number().int().positive()).optional(),
  warehouseId: z.number().int().positive(),
  supplierId: z.number().int().positive(),
})

type ProductFormValues = z.infer<typeof ProductFormSchema>

export const ProductEditPage: React.FC = () => {
  const { productId = "" } = useParams<{ productId: string }>()

  const { data: languages = [] } = useLanguagesQuery()
  const { data: warehouses = [] } = useWarehousesQuery()
  const { data: suppliers = [] } = useSuppliersQuery()

  const [selectedLangs, setSelectedLangs] = useState<string[]>([])
  const [supplierType, setSupplierType] = useState<'PF' | 'PJ'>('PJ')
  const filteredSuppliers = useMemo(() => suppliers.filter(s => supplierType === 'PF' ? s.type === 'natural_person' : s.type === 'legal_entity'), [suppliers, supplierType])

const productQuery = useQuery<ProductDetails>({
    queryKey: ["product", productId],
    queryFn: () => getProduct(productId),
    enabled: !!productId,
  })

  const defaultValues = useMemo<Partial<ProductFormValues>>(() => {
    const names: Record<string, string> = {}
    const descriptions: Record<string, string> = {}
    languages.forEach((l) => {
      names[l.isoCode] = ""
      descriptions[l.isoCode] = ""
    })
    return {
      names,
      descriptions,
      warrantyDate: new Date(),
      status: ProductStatus.TESTED,
      minimumSalePrice: 0,
      stock: 0,
      categoriesIds: [],
      warehouseId: warehouses[0]?.id ?? 1,
      supplierId: suppliers[0]?.id ?? 1,
    }
  }, [languages, warehouses, suppliers])

  const form = useForm<ProductFormValues>({
    resolver: zodResolver(ProductFormSchema),
    defaultValues,
    mode: "onSubmit",
  })

  // Populate form when product arrives
  useEffect(() => {
    const p = productQuery.data
    if (!p) return

    // Initialize selected languages from product data
    const langsFromProduct = Object.keys(p.names ?? {})
    setSelectedLangs(langsFromProduct.length ? langsFromProduct : (languages[0] ? [languages[0].isoCode] : []))

    // Infer supplier type from product supplier
    const current = suppliers.find(s => s.id === p.supplierId)
    if (current) setSupplierType(current.type === 'natural_person' ? 'PF' : 'PJ')

    // Apply values
    form.reset({
      names: { ...defaultValues.names, ...p.names },
      descriptions: { ...defaultValues.descriptions, ...p.descriptions },
      warrantyDate: new Date(p.warrantyDate),
      status: p.status,
      minimumSalePrice: p.minimumSalePrice,
      stock: p.stock,
      categoriesIds: p.categoriesIds,
      warehouseId: p.warehouseId,
      supplierId: p.supplierId,
    })
  }, [form, productQuery.data, languages, warehouses, suppliers, defaultValues.names, defaultValues.descriptions])

  const mutation = useMutation({
    mutationFn: (values: UpdateProductRequest) => updateProduct(productId, values),
    onSuccess: () => {
      toast.success("Produto atualizado com sucesso")
    },
    onError: (err: Error) => {
      toast.error(err.message)
    }
  })

  const onSubmit = useCallback(async (values: ProductFormValues) => {
    if (selectedLangs.length === 0) {
      toast.error("Selecione ao menos um idioma")
      return
    }

    const names: Record<string, string> = {}
    const descriptions: Record<string, string> = {}
    selectedLangs.forEach((iso) => {
      names[iso] = values.names?.[iso] ?? ""
      descriptions[iso] = values.descriptions?.[iso] ?? ""
    })

    const payload: UpdateProductRequest = {
      names,
      descriptions,
      warrantyDate: values.warrantyDate,
      status: values.status,
      minimumSalePrice: Number(values.minimumSalePrice ?? 0),
      stock: Number(values.stock ?? 0),
      categoriesIds: values.categoriesIds ?? [],
      warehouseId: Number(values.warehouseId),
      supplierId: Number(values.supplierId),
    }

    await mutation.mutateAsync(payload)
  }, [mutation, selectedLangs])

  if (productQuery.isLoading) {
    return <div className="p-6 text-sm text-zinc-500">Carregando produto...</div>
  }

  if (productQuery.isError) {
    return <div className="p-6 text-sm text-red-500">Erro ao carregar produto.</div>
  }

  return (
    <div className="mx-auto w-full max-w-5xl p-6">
      <Card>
        <CardHeader>
          <CardTitle>Editar Produto</CardTitle>
          <CardDescription>Atualize os detalhes do produto e salve.</CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              {/* Language multi-select */}
              <div className="space-y-2">
                <div className="text-sm font-medium">Idiomas do produto</div>
                <div className="grid gap-2 sm:grid-cols-2 md:grid-cols-3">
                  {languages.map((lang) => {
                    const checked = selectedLangs.includes(lang.isoCode)
                    return (
                      <label key={lang.isoCode} className="flex items-center gap-2 text-sm">
                        <Checkbox
                          checked={checked}
                          onCheckedChange={(v) => {
                            setSelectedLangs((prev) => {
                              const isChecked = v === true
                              if (isChecked) {
                                return prev.includes(lang.isoCode) ? prev : [...prev, lang.isoCode]
                              }
                              if (prev.length <= 1) {
                                toast.error("Selecione ao menos um idioma")
                                return prev
                              }
                              return prev.filter((c) => c !== lang.isoCode)
                            })
                          }}
                        />
                        <span>{lang.name} ({lang.isoCode})</span>
                      </label>
                    )
                  })}
                </div>
                <p className="text-xs text-muted-foreground">Marque os idiomas que deseja editar. Apenas estes campos serão exibidos.</p>
              </div>

              {/* Names for selected languages */}
              <div className="space-y-3">
                <div className="text-sm font-medium">Nomes por idioma</div>
                {selectedLangs.map((iso) => (
                  <FormField
                    key={`name-${iso}`}
                    control={form.control}
                    name={`names.${iso}` as const}
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Nome ({iso})</FormLabel>
                        <FormControl>
                          <Input placeholder={`Nome (${iso})`} {...field} />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                ))}
              </div>

              {/* Descriptions for selected languages */}
              <div className="space-y-3">
                <div className="text-sm font-medium">Descrições por idioma</div>
                {selectedLangs.map((iso) => (
                  <FormField
                    key={`desc-${iso}`}
                    control={form.control}
                    name={`descriptions.${iso}` as const}
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Descrição ({iso})</FormLabel>
                        <FormControl>
                          <Input placeholder={`Descrição (${iso})`} {...field} />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                ))}
              </div>

              <div className="grid gap-4 md:grid-cols-2">
                {/* Warranty Date - Date Picker */}
                <FormField
                  control={form.control}
                  name="warrantyDate"
                  render={({ field }) => (
                    <FormItem className="flex flex-col">
                      <FormLabel>Data de Garantia</FormLabel>
                      <Popover>
                        <PopoverTrigger asChild>
                          <Button
                            variant="outline"
                            className="justify-start text-left font-normal"
                            type="button"
                          >
                            {field.value ? field.value.toLocaleDateString() : "Selecionar data"}
                          </Button>
                        </PopoverTrigger>
                        <PopoverContent className="w-auto p-0" align="start">
                          <Calendar
                            mode="single"
                            selected={field.value}
                            onSelect={(date) => date && field.onChange(date)}
                            autoFocus
                          />
                        </PopoverContent>
                      </Popover>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* Status */}
                <FormField
                  control={form.control}
                  name="status"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Status</FormLabel>
                      <Select onValueChange={field.onChange} value={field.value}>
                        <FormControl>
                          <SelectTrigger className="w-full">
                            <SelectValue placeholder="Selecione o status" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          {Object.values(ProductStatus).map((s) => (
                            <SelectItem key={s} value={s}>{s}</SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* Minimum Sale Price */}
                <FormField
                  control={form.control}
                  name="minimumSalePrice"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Preço mínimo de venda</FormLabel>
                      <FormControl>
                        <Input type="number" step="0.01" {...field} onChange={(e) => field.onChange(parseFloat(e.target.value || "0"))} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* Stock */}
                <FormField
                  control={form.control}
                  name="stock"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Estoque</FormLabel>
                      <FormControl>
                        <Input type="number" {...field} onChange={(e) => field.onChange(parseInt(e.target.value || "0", 10))} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* Warehouse */}
                <FormField
                  control={form.control}
                  name="warehouseId"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Armazém</FormLabel>
                      <Select onValueChange={(v) => field.onChange(parseInt(v, 10))} value={String(field.value ?? "")}>
                        <FormControl>
                          <SelectTrigger className="w-full">
                            <SelectValue placeholder="Selecione o armazém" />
                          </SelectTrigger>
                        </FormControl>
                        <SelectContent>
                          {warehouses.map((w: Warehouse) => (
                            <SelectItem key={w.id} value={String(w.id)}>{w.name}</SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <div className="grid gap-4 grid-cols-2">
                  {/* Supplier Type */}
                  <FormItem>
                    <FormLabel>Tipo do fornecedor</FormLabel>
                    <Select value={supplierType} onValueChange={(v) => setSupplierType(v as 'PF' | 'PJ')}>
                      <FormControl>
                        <SelectTrigger className="w-full">
                          <SelectValue placeholder="Selecione o tipo" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectItem value="PF">PF (Pessoa Física)</SelectItem>
                        <SelectItem value="PJ">PJ (Pessoa Jurídica)</SelectItem>
                      </SelectContent>
                    </Select>
                  </FormItem>

                  {/* Supplier */}
                  <FormField
                    control={form.control}
                    name="supplierId"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Fornecedor</FormLabel>
                        <Select onValueChange={(v) => field.onChange(parseInt(v, 10))} value={String(field.value ?? "")}>
                          <FormControl>
                            <SelectTrigger className="w-full">
                              <SelectValue placeholder="Selecione o fornecedor" />
                            </SelectTrigger>
                          </FormControl>
                          <SelectContent>
                            {filteredSuppliers.map((s: Supplier) => (
                              <SelectItem key={s.id} value={String(s.id)}>
                                {s.name} {s.type === 'natural_person' ? '(PF)' : '(PJ)'}
                              </SelectItem>
                            ))}
                          </SelectContent>
                        </Select>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                </div>
              </div>

              <div className="flex items-center gap-3">
                <Button type="submit" disabled={mutation.isPending}>
                  {mutation.isPending ? "Salvando..." : "Salvar alterações"}
                </Button>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  )
}
