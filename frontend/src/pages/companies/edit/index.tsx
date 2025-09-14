import { useEffect } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useParams, useNavigate } from "react-router";
import { useForm, useFieldArray } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";
import { PlusCircle, XCircle } from "lucide-react";

import { getCompany, updateCompany, type Company, type CompanyUpdate } from "@/api/companies";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Label } from "@/components/ui/label";

const AddressFormSchema = z.object({
  street: z.string().min(1, "Rua é obrigatória."),
  number: z.string().min(1, "Número é obrigatório."),
  city: z.string().min(1, "Cidade é obrigatória."),
  state: z.string().min(1, "Estado é obrigatório."),
  zipCode: z.string().min(1, "CEP é obrigatório."),
  country: z.string().min(1, "País é obrigatório."),
});

const CompanyFormSchema = z.object({
  tradeName: z.string().min(1, "Nome fantasia é obrigatório.").max(100),
  legalName: z.string().min(1, "Razão social é obrigatória.").max(100),
  phones: z.array(z.object({
    value: z.string().min(1, "Telefone não pode ser vazio."),
  })),
  emails: z.array(z.object({
    value: z.string().email("Email inválido."),
  })),
  address: AddressFormSchema,
});

type CompanyFormValues = z.infer<typeof CompanyFormSchema>;

export const EditCompanyPage: React.FC = () => {
  const { companyId = "" } = useParams<{ companyId: string }>();
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const companyQuery = useQuery<Company>({
    queryKey: ["company", companyId],
    queryFn: () => getCompany(companyId),
    enabled: !!companyId,
  });

  const form = useForm<CompanyFormValues>({
    resolver: zodResolver(CompanyFormSchema),
    defaultValues: {
      tradeName: "",
      legalName: "",
      phones: [],
      emails: [],
      address: { street: "", number: "", city: "", state: "", zipCode: "", country: "" },
    },
  });

  const { fields: phoneFields, append: appendPhone, remove: removePhone } = useFieldArray({ control: form.control, name: "phones" });
  const { fields: emailFields, append: appendEmail, remove: removeEmail } = useFieldArray({ control: form.control, name: "emails" });

  useEffect(() => {
    if (companyQuery.data) {
      const c = companyQuery.data;
      form.reset({
        tradeName: c.tradeName,
        legalName: c.legalName,
        phones: c.phones ? c.phones.map(phone => ({ value: phone })) : [],
        emails: c.emails ? c.emails.map(email => ({ value: email })) : [],
        address: c.address,
      });
    }
  }, [companyQuery.data, form]);

  const mutation = useMutation({
    mutationFn: (values: CompanyUpdate) => updateCompany(companyId, values),
    onSuccess: () => {
      toast.success("Empresa atualizada com sucesso");
      queryClient.invalidateQueries({ queryKey: ["companies"] });
      queryClient.invalidateQueries({ queryKey: ["company", companyId] });
      navigate("/companies");
    },
    onError: (err: Error) => toast.error(err.message),
  });

  const onSubmit = (values: CompanyFormValues) => {
    const payload = {
      ...values,
      phones: values.phones.map(phone => phone.value),
      emails: values.emails.map(email => email.value),
    };
    mutation.mutate(payload);
  };

  if (companyQuery.isLoading) return <div className="p-6">Carregando empresa...</div>;
  if (companyQuery.isError) return <div className="p-6 text-red-500">Erro ao carregar empresa.</div>;

  return (
    <div className="mx-auto w-full max-w-2xl p-6">
      <Card>
        <CardHeader>
          <CardTitle>Editar Empresa</CardTitle>
          <CardDescription>Atualize os dados abaixo. O CNPJ não pode ser alterado.</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-2 mb-6">
            <Label htmlFor="cnpj">CNPJ</Label>
            <Input id="cnpj" value={companyQuery.data?.cnpj ?? ''} disabled />
          </div>

          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <FormField
                control={form.control}
                name="tradeName"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Nome Fantasia</FormLabel>
                    <FormControl>
                      <Input placeholder="Nome da empresa" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="legalName"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Razão Social</FormLabel>
                    <FormControl>
                      <Input placeholder="Razão social da empresa" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <div className="space-y-3">
                <FormLabel>Telefones</FormLabel>
                {phoneFields.map((field, index) => (
                  <FormField
                    key={field.id}
                    control={form.control}
                    name={`phones.${index}.value`}
                    render={({ field: formField }) => (
                      <FormItem>
                        <div className="flex items-center gap-2">
                          <FormControl>
                            <Input {...formField} placeholder="Telefone" />
                          </FormControl>
                          <Button type="button" variant="ghost" size="icon" className="shrink-0" onClick={() => removePhone(index)}>
                            <XCircle className="h-5 w-5 text-red-500" />
                          </Button>
                        </div>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                ))}
                {/* CORREÇÃO APLICADA AQUI */}
                <Button type="button" variant="outline" size="sm" onClick={() => appendPhone({ value: "" })}>
                  <span className="flex items-center gap-2">
                    <PlusCircle className="h-4 w-4" />
                    Adicionar Telefone
                  </span>
                </Button>
              </div>

              <div className="space-y-3">
                <FormLabel>Emails</FormLabel>
                {emailFields.map((field, index) => (
                  <FormField
                    key={field.id}
                    control={form.control}
                    name={`emails.${index}.value`}
                    render={({ field: formField }) => (
                      <FormItem>
                        <div className="flex items-center gap-2">
                          <FormControl>
                            <Input {...formField} placeholder="Email" />
                          </FormControl>
                          <Button type="button" variant="ghost" size="icon" className="shrink-0" onClick={() => removeEmail(index)}>
                            <XCircle className="h-5 w-5 text-red-500" />
                          </Button>
                        </div>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                ))}
                {/* CORREÇÃO APLICADA AQUI */}
                <Button type="button" variant="outline" size="sm" onClick={() => appendEmail({ value: "" })}>
                  <span className="flex items-center gap-2">
                    <PlusCircle className="h-4 w-4" />
                    Adicionar Email
                  </span>
                </Button>
              </div>

              <Card>
                <CardHeader>
                  <CardTitle>Endereço</CardTitle>
                </CardHeader>
                <CardContent className="grid gap-4 md-grid-cols-2">
                  <FormField
                    control={form.control}
                    name="address.street"
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
                    name="address.number"
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
                    name="address.city"
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
                    name="address.state"
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
                    name="address.zipCode"
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
                  <FormField
                    control={form.control}
                    name="address.country"
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
                </CardContent>
              </Card>

              <div className="flex justify-between mt-4">
                <Button type="button" variant="outline" onClick={() => navigate("/companies")}> Voltar </Button>
                <Button type="submit" disabled={mutation.isPending}>
                  {mutation.isPending ? "Salvando..." : "Salvar alterações"}
                </Button>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
};