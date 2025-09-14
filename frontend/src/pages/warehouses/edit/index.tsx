import { useEffect } from "react"
import { useQuery, useMutation } from "@tanstack/react-query"
import { useParams, useNavigate } from "react-router"
import { z } from "zod"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"

import { getWarehouse, updateWarehouse, type Warehouse } from "@/api/warehouses"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form"
import { toast } from "sonner"

const WarehouseFormSchema = z.object({
  name: z.string().min(1, "Nome é obrigatório").max(100, "Nome deve ter no máximo 100 caracteres"),
  country: z.string().min(1, "País é obrigatório").max(50, "País deve ter no máximo 50 caracteres"),
  state: z.string().min(1, "Estado é obrigatório").max(50, "Estado deve ter no máximo 50 caracteres"),
  city: z.string().min(1, "Cidade é obrigatória").max(50, "Cidade deve ter no máximo 50 caracteres"),
  street: z.string().min(1, "Rua é obrigatória").max(100, "Rua deve ter no máximo 100 caracteres"),
  number: z.string().min(1, "Número é obrigatório").max(10, "Número deve ter no máximo 10 dígitos"),
  zipCode: z.string().min(1, "CEP é obrigatório").max(8, "CEP deve ter no máximo 8 dígitos"),
})

type WarehouseFormValues = z.infer<typeof WarehouseFormSchema>

export const EditWarehousePage: React.FC = () => {
  const { warehouseId = "" } = useParams<{ warehouseId: string }>()
  const warehouseQuery = useQuery<Warehouse>({
    queryKey: ["warehouse", warehouseId],
    queryFn: () => getWarehouse(warehouseId),
    enabled: !!warehouseId,
  })
  const navigate = useNavigate()

  const form = useForm<WarehouseFormValues>({
    resolver: zodResolver(WarehouseFormSchema),
    defaultValues: {
      name: "",
      country: "",
      state: "",
      city: "",
      street: "",
      number: "",
      zipCode: "",
    },
    mode: "onSubmit",
  })

  // Populate form when warehouse data arrives
  useEffect(() => {
    const w = warehouseQuery.data
    if (!w) return
    form.reset({
      name: w.name,
      country: w.address.country,
      state: w.address.state,
      city: w.address.city,
      street: w.address.street,
      number: w.address.number,
      zipCode: w.address.zipCode,
    })
  }, [form, warehouseQuery.data])

  const mutation = useMutation({
  mutationFn: (values: WarehouseFormValues) =>
    updateWarehouse(warehouseId, {
      name: values.name,
      address: {
        country: values.country,
        state: values.state,
        city: values.city,
        street: values.street,
        number: values.number,
        zipCode: values.zipCode,
      },
    }),
  onSuccess: () => {
    toast.success("Warehouse updated successfully")
  },
  onError: (err: Error) => {
    toast.error(err.message)
  }
})

  function onSubmit(values: WarehouseFormValues) {
    mutation.mutate(values)
    navigate("/warehouses")
  }

  if (warehouseQuery.isLoading) {
    return <div className="p-6 text-sm text-zinc-500">Carregando armazém...</div>
  }

  if (warehouseQuery.isError) {
    return <div className="p-6 text-sm text-red-500">Erro ao carregar armazém.</div>
  }

  return (
    <div className="mx-auto w-full max-w-5xl p-6">
      <Card>
        <CardHeader>
          <CardTitle>Editar armazém</CardTitle>
          <CardDescription>Atualize os detalhes do armazém e salve.</CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <div className="grid gap-4 md:grid-cols-2">
                <FormField
                  control={form.control}
                  name="name"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Nome</FormLabel>
                      <FormControl>
                        <Input placeholder="Nome do armazém" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="country"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>País</FormLabel>
                      <FormControl>
                        <Input placeholder="País" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="state"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Estado</FormLabel>
                      <FormControl>
                        <Input placeholder="Estado" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="city"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Cidade</FormLabel>
                      <FormControl>
                        <Input placeholder="Cidade" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="street"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Rua</FormLabel>
                      <FormControl>
                        <Input placeholder="Rua" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="number"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Número</FormLabel>
                      <FormControl>
                        <Input placeholder="Número" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="zipCode"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>CEP</FormLabel>
                      <FormControl>
                        <Input placeholder="CEP" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
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