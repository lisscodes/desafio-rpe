import axios from 'axios';
import { Invoice } from '../models/Invoice';

const API_URL = 'http://localhost:8080/faturas';

export const faturaService = {
  async listarPorCliente(clienteId: string): Promise<Invoice[]> {
    const response = await axios.get<Invoice[]>(`${API_URL}/${clienteId}`);
    return response.data;
  },

  async registrarPagamento(faturaId: number): Promise<Invoice> {
    const response = await axios.put<Invoice>(`${API_URL}/${faturaId}/pagamento`);
    return response.data;
  },

  async listarAtrasadas(): Promise<Invoice[]> {
    const response = await axios.get<Invoice[]>(`${API_URL}/atrasadas`);
    return response.data;
  }
};
