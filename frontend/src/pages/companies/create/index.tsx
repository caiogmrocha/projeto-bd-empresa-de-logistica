import { useCallback } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useForm, useFieldArray } from "react-hook-form";
import type { SubmitHandler } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useNavigate } from "react-router";
import { toast } from "sonner";
import { PlusCircle, XCircle } from "lucide-react";

import { createCompany } from "@/api/companies";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";

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
  cnpj: z.string().min(14, "CNPJ deve ter 14 caracteres.").max(14, "CNPJ deve ter 14 caracteres."),
  phones: z.array(z.object({
    value: z.string().min(1, "Telefone não pode ser vazio."),
  })),
  emails: z.array(z.object({
    value: z.string().email("Email inválido."),
  })),
  address: AddressFormSchema,
});

type CompanyFormValues = z.infer<typeof CompanyFormSchema>;

export const CompanyCreatePage = () => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const methods = useForm<CompanyFormValues>({
    resolver: zodResolver(CompanyFormSchema),
    defaultValues: {
      tradeName: "",
      legalName: "",
      cnpj: "",
      phones: [],
      emails: [],
      address: {
        street: "",
        number: "",
        city: "",
        state: "",
        zipCode: "",
        country: ""
      },
    },
    mode: "onSubmit",
  });

  const phoneFields = useFieldArray({
    control: methods.control,
    name: "phones"
  });

  const emailFields = useFieldArray({
    control: methods.control,
    name: "emails"
  });

  const createCompanyMutation = useMutation({
    mutationFn: createCompany,
    onSuccess: () => {
      toast.success("Empresa criada com sucesso");
      queryClient.invalidateQueries({ queryKey: ["companies"] });
      navigate("/companies");
    },
    onError: (err: Error) => {
      toast.error(err.message);
    },
  });

  const onSubmit: SubmitHandler<CompanyFormValues> = useCallback(async (values) => {
    const payload = {
      ...values,
      phones: values.phones.map(phone => phone.value),
      emails: values.emails.map(email => email.value),
    };
    await createCompanyMutation.mutateAsync(payload);
  }, [createCompanyMutation]);

  return (
    <div className="mx-auto w-full max-w-2xl p-6">
      <Card>
        <CardHeader>
          <CardTitle>Cadastrar Empresa</CardTitle>
          <CardDescription>Preencha os dados abaixo.</CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...methods}>
            <form onSubmit={methods.handleSubmit(onSubmit)} className="space-y-6">
              <FormField
                control={methods.control}
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
                control={methods.control}
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
              <FormField
                control={methods.control}
                name="cnpj"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>CNPJ</FormLabel>
                    <FormControl>
                      <Input placeholder="CNPJ" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <div className="space-y-3">
                <FormLabel>Telefones</FormLabel>
                {phoneFields.fields.map((field, index) => (
                  <FormField
                    key={field.id}
                    control={methods.control}
                    name={`phones.${index}.value`}
                    render={({ field: formField }) => (
                      <FormItem>
                        <div className="flex items-center gap-2">
                          <FormControl>
                            <Input {...formField} placeholder="Telefone" />
                          </FormControl>
                          <Button type="button" variant="ghost" size="icon" className="shrink-0" onClick={() => phoneFields.remove(index)}>
                            <XCircle className="h-5 w-5 text-red-500" />
                          </Button>
                        </div>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                ))}
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  onClick={() => phoneFields.append({ value: "" })}
                >
                  <span className="flex items-center gap-2">
                    <PlusCircle className="h-4 w-4" />
                    Adicionar Telefone
                  </span>
                </Button>
              </div>

              <div className="space-y-3">
                <FormLabel>Emails</FormLabel>
                {emailFields.fields.map((field, index) => (
                  <FormField
                    key={field.id}
                    control={methods.control}
                    name={`emails.${index}.value`}
                    render={({ field: formField }) => (
                      <FormItem>
                        <div className="flex items-center gap-2">
                          <FormControl>
                            <Input {...formField} placeholder="Email" />
                          </FormControl>
                          <Button type="button" variant="ghost" size="icon" className="shrink-0" onClick={() => emailFields.remove(index)}>
                            <XCircle className="h-5 w-5 text-red-500" />
                          </Button>
                        </div>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                ))}
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  onClick={() => emailFields.append({ value: "" })}
                >
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
                <CardContent className="grid gap-4 md:grid-cols-2">
                  <FormField
                    control={methods.control}
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
                    control={methods.control}
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
                    control={methods.control}
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
                    control={methods.control}
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
                    control={methods.control}
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
                    control={methods.control}
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
                <Button type="submit" disabled={createCompanyMutation.isPending}>
                  {createCompanyMutation.isPending ? "Criando..." : "Criar empresa"}
                </Button>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
};