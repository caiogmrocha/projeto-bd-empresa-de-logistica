import { useEffect } from "react"
import { useQuery, useMutation } from "@tanstack/react-query"
import { useParams } from "react-router"
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
  name: z.string().min(1, "Nome é obrigatório"),
  country: z.string().min(1, "País é obrigatório"),
  state: z.string().min(1, "Estado é obrigatório"),
  city: z.string().min(1, "Cidade é obrigatória"),
  street: z.string().min(1, "Rua é obrigatória"),
  number: z.string().min(1, "Número é obrigatório"),
  zipCode: z.string().min(1, "CEP é obrigatório"),
})

type WarehouseFormValues = z.infer<typeof WarehouseFormSchema>

export const EditWarehousePage: React.FC = () => {
  const { warehouseId = "" } = useParams<{ warehouseId: string }>()
  const warehouseQuery = useQuery<Warehouse>({
    queryKey: ["warehouse", warehouseId],
    queryFn: () => getWarehouse(warehouseId),
    enabled: !!warehouseId,
  })

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
      country: w.country,
      state: w.state,
      city: w.city,
      street: w.street,
      number: w.number,
      zipCode: w.zipCode,
    })
  }, [form, warehouseQuery.data])

  const mutation = useMutation({
    mutationFn: (values: WarehouseFormValues) => updateWarehouse(warehouseId, values),
    onSuccess: () => {
      toast.success("Warehouse updated successfully")
    },
    onError: (err: Error) => {
      toast.error(err.message)
    }
  })

  function onSubmit(values: WarehouseFormValues) {
    mutation.mutate(values)
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