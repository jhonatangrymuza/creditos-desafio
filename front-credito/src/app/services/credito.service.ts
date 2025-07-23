import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Credito } from '../models/credito.model';

@Injectable({
    providedIn: 'root'
})
export class CreditoService {
  private baseUrl = 'http://localhost:8080/api/creditos';

  constructor(private http: HttpClient) {}

  buscarPorNumeroNfse(numeroNfse: string): Observable<Credito[]> {
    return this.http.get<Credito[]>(`${this.baseUrl}/${numeroNfse}`);
  }

  buscarPorNumeroCredito(numeroCredito: string): Observable<Credito> {
    return this.http.get<Credito>(`${this.baseUrl}/credito/${numeroCredito}`);
  }
}