import axios from 'axios';
import { Fatura } from '../models/Invoice';

const API_URL = 'http://localhost:8080/faturas';

export const faturaService = {
  async listarPorCliente(clienteId: string): Promise<Fatura[]> {
    const response = await axios.get<Fatura[]>(`${API_URL}/${clienteId}`);
    return response.data;
  },

  async registrarPagamento(faturaId: number): Promise<Fatura> {
    const response = await axios.put<Fatura>(`${API_URL}/${faturaId}/pagamento`);
    return response.data;
  },

  async listarAtrasadas(): Promise<Fatura[]> {
    const response = await axios.get<Fatura[]>(`${API_URL}/atrasadas`);
    return response.data;
  }
};
