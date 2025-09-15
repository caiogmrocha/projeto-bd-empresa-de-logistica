import { z, type ZodErrorMap } from "zod"

// Zod error messages in Brazilian Portuguese
const ptBrErrorMap: ZodErrorMap = (issue) => {
  switch (issue.code) {
    case "invalid_type": {
      if (issue.received === "undefined") return { message: "Campo obrigatório" }
      return { message: `Tipo inválido: esperado ${issue.expected}, recebido ${issue.received}` }
    }
    case "unrecognized_keys":
      return { message: `Chaves não reconhecidas: ${issue.keys?.join(", ")}` }
    case "invalid_union":
      return { message: "Valor não corresponde a nenhuma das opções válidas" }
    case "invalid_format": {
      const v = issue.validation
      if (v === "email") return { message: "E-mail inválido" }
      if (v === "url") return { message: "URL inválida" }
      if (v === "uuid") return { message: "UUID inválido" }
      if (v === "regex") return { message: "Formato inválido" }
      if (v === "cuid") return { message: "CUID inválido" }
      return { message: "Formato inválido" }
    }
    case "too_small": {
      const min = issue.minimum
      const inclusive = issue.inclusive
      switch (issue.type) {
        case "string":
          return { message: inclusive ? `Mínimo de ${min} caracteres` : `Deve ser maior que ${min} caracteres` }
        case "number":
          return { message: inclusive ? `Deve ser maior ou igual a ${min}` : `Deve ser maior que ${min}` }
        case "array":
          return { message: inclusive ? `Selecione pelo menos ${min} itens` : `Selecione mais de ${min} itens` }
        case "date":
          return { message: inclusive ? `Data deve ser em ou após ${new Date(Number(min)).toLocaleDateString()}` : `Data deve ser após ${new Date(Number(min)).toLocaleDateString()}` }
        default:
          return { message: "Valor muito pequeno" }
      }
    }
    case "too_big": {
      const max = issue.maximum
      const inclusive = issue.inclusive
      switch (issue.type) {
        case "string":
          return { message: inclusive ? `Máximo de ${max} caracteres` : `Deve ser menor que ${max} caracteres` }
        case "number":
          return { message: inclusive ? `Deve ser menor ou igual a ${max}` : `Deve ser menor que ${max}` }
        case "array":
          return { message: inclusive ? `Selecione no máximo ${max} itens` : `Selecione menos de ${max} itens` }
        case "date":
          return { message: inclusive ? `Data deve ser em ou antes de ${new Date(Number(max)).toLocaleDateString()}` : `Data deve ser antes de ${new Date(Number(max)).toLocaleDateString()}` }
        default:
          return { message: "Valor muito grande" }
      }
    }
    case "not_multiple_of":
      return { message: `Deve ser múltiplo de ${issue.multipleOf}` }
    case "invalid_key":
      return { message: "Chave inválida" }
    case "invalid_element":
      return { message: "Elemento inválido" }
    case "invalid_value":
      return { message: "Valor inválido" }
    case "custom":
      return { message: issue.message ?? "Valor inválido" }
    default:
      return { message: "Valor inválido" }
  }
}

z.config({
  localeError: ptBrErrorMap
})

export {} // side-effect module
