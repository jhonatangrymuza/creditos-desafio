export interface Credito {
  numeroCredito: string;
  numeroNfse: string;
  dataConstituicao: Date;
  valorIssqn: number;
  tipoCredito: string;
  simplesNacional: string;
  aliquota: number;
  valorFaturado: number;
  valorDeducao: number;
  baseCalculo: number;
}