import { Component } from '@angular/core';
import { Credito } from './models/credito.model';
import { CreditoService } from './services/credito.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  creditos: Credito[] = [];
  loading = false;
  error = '';
  tipoBusca = 'nfse';
  termoBusca = '';

  constructor(private creditoService: CreditoService) {}

  buscar() {
    if (!this.termoBusca.trim()) {
      this.error = 'Digite um número para buscar';
      return;
    }

    this.loading = true;
    this.error = '';
    this.creditos = [];

    if (this.tipoBusca === 'nfse') {
      this.creditoService.buscarPorNumeroNfse(this.termoBusca)
        .subscribe({
          next: (creditos) => {
            this.creditos = creditos;
            this.loading = false;
          },
          error: (err) => {
            this.error = err.error?.message || 'Erro ao buscar créditos';
            this.loading = false;
          }
        });
    } else {
      this.creditoService.buscarPorNumeroCredito(this.termoBusca)
        .subscribe({
          next: (credito) => {
            
            this.creditos = new Array(credito);
            console.log(this.creditos);
            this.loading = false;
          },
          error: (err) => {
            this.error = err.error?.message || 'Erro ao buscar crédito';
            this.loading = false;
          }
        });
    }
  }

  limpar() {
    this.creditos = [];
    this.error = '';
    this.termoBusca = '';
  }
}